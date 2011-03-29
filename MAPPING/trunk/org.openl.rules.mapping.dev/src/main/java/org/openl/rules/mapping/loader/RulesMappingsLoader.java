package org.openl.rules.mapping.loader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.FieldMappingCondition;
import org.openl.rules.mapping.ClassMappingConfiguration;
import org.openl.rules.mapping.Converter;
import org.openl.rules.mapping.GlobalConfiguration;
import org.openl.rules.mapping.Mapping;
import org.openl.rules.mapping.TypeResolver;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.BeanMapConfiguration;
import org.openl.rules.mapping.definition.ConditionDescriptor;
import org.openl.rules.mapping.definition.Configuration;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.definition.FieldMap;
import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.loader.condition.ConditionFactory;
import org.openl.rules.mapping.loader.converter.ConverterFactory;
import org.openl.rules.mapping.loader.converter.ConverterIdFactory;

/**
 * Intended for internal use only.
 * 
 * Loads mapping declarations and converts them into internal model which used
 * by mapper.
 */
public class RulesMappingsLoader {

    private Class<?> instanceClass;
    private Object instance;
    private TypeResolver typeResolver;

    /**
     * Internal cache of default converters.
     */
    private Map<String, ConverterDescriptor> customConvertersMap = new HashMap<String, ConverterDescriptor>();

    public RulesMappingsLoader(Class<?> instanceClass, Object instance, TypeResolver typeResolver) {
        this.instanceClass = instanceClass;
        this.instance = instance;
        this.typeResolver = typeResolver;
    }

    /**
     * Loads mappings from source.
     * 
     * @return collection of loaded {@link BeanMap} objects
     */
    public Collection<BeanMap> loadMappings(Map<String, BeanMapConfiguration> mappingConfigurations,
        Configuration globalConfiguration) {
        List<Mapping> mappings = findDeclarations(instanceClass, instance, Mapping.class);

        return processMappings(mappings, mappingConfigurations, globalConfiguration);
    }

    /**
     * Loads global configuration.
     * 
     * @return global configuration
     */
    public Configuration loadConfiguration() {
        List<GlobalConfiguration> globalConfigurations = findDeclarations(instanceClass, instance,
            GlobalConfiguration.class);

        return processConfiguration(globalConfigurations);
    }

    public Map<String, BeanMapConfiguration> loadBeanMapConfiguraitons(Configuration globalConfiguration) {
        List<ClassMappingConfiguration> configs = findDeclarations(instanceClass, instance,
            ClassMappingConfiguration.class);

        return processClassConfigurations(configs, globalConfiguration);
    }

    private Map<String, BeanMapConfiguration> processClassConfigurations(List<ClassMappingConfiguration> configs,
        Configuration globalConfiguration) {

        Map<String, BeanMapConfiguration> beanMapConfigs = new HashMap<String, BeanMapConfiguration>();

        for (ClassMappingConfiguration classConfiguration : configs) {
            Class<?> srcClass = classConfiguration.getClassA();
            Class<?> destClass = classConfiguration.getClassB();

            BeanMapConfiguration beanMapConfiguration = createBeanMapConfiguration(srcClass, destClass,
                globalConfiguration);
            beanMapConfiguration.setDateFormat(classConfiguration.getDateFormat());
            beanMapConfiguration.setMapEmptyStrings(classConfiguration.getMapEmptyStrings());
            beanMapConfiguration.setMapNulls(classConfiguration.getMapNulls());
            beanMapConfiguration.setRequiredFields(classConfiguration.getRequiredFields());
            beanMapConfiguration.setRequiredFields(classConfiguration.getRequiredFields());
            beanMapConfiguration.setTrimStrings(classConfiguration.getTrimStrings());
            beanMapConfiguration.setWildcard(classConfiguration.getWildcard());
            beanMapConfiguration.setClassABeanFactory(classConfiguration.getClassABeanFactory());
            beanMapConfiguration.setClassBBeanFactory(classConfiguration.getClassBBeanFactory());

            // If user defined class mapping configuration earlier we override
            // that configuration with new one.
            // 
            String key = BeanMapKeyFactory.createKey(srcClass, destClass);
            beanMapConfigs.put(key, beanMapConfiguration);
        }

        return beanMapConfigs;
    }

    private BeanMapConfiguration createBeanMapConfiguration(Class<?> srcClass, Class<?> destClass,
        Configuration globalConfiguration) {
        BeanMapConfiguration beanMapConfiguration = new BeanMapConfiguration();
        beanMapConfiguration.setGlobalConfiguration(globalConfiguration);
        beanMapConfiguration.setClassA(srcClass);
        beanMapConfiguration.setClassB(destClass);

        return beanMapConfiguration;
    }

    /**
     * Loads defined default converters from source.
     * 
     * @return collection of loaded {@link ConverterDescriptor} objects
     */
    public Collection<ConverterDescriptor> loadDefaultConverters() {
        List<Converter> defaultConverters = findDeclarations(instanceClass, instance, Converter.class);

        return processDefaultConverters(defaultConverters);
    }

    /**
     * Finds mapping definitions in specified OpenL Rules project.
     * 
     * @param instanceClass class definition
     * @param instance instance object
     * @param declarationType declaration type to find
     * @return list of mapping definitions
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> findDeclarations(Class<?> instanceClass, Object instance, Class<T> declarationType) {

        List<T> declarations = new ArrayList<T>();

        // We use instance class definition to obtain list of declarations.
        //
        Collection<Method> methods = findDeclarationMethods(instanceClass, declarationType);

        for (Method method : methods) {
            T[] declarationsArray;

            try {
                // Invoke method to obtain mappings.
                //
                declarationsArray = (T[]) method.invoke(instance, new Object[0]);
            } catch (Exception e) {
                throw new RulesMappingException("Cannot load declarations", e);
            }

            // Add loaded mapping to result collection.
            //
            declarations.addAll(Arrays.asList(declarationsArray));
        }

        return declarations;
    }

    /**
     * Finds mapping declarations using class definition of the OpenL Rules
     * project. Current method implementation uses assumption that methods which
     * provide definitions returns array of declarations and doesn't have
     * parameters.
     * 
     * @param instanceClass class definition
     * @param declarationType declaration type
     * @return list of methods what returns mapping definitions
     */
    private Collection<Method> findDeclarationMethods(Class<?> instanceClass, final Class<?> declarationType) {

        Method[] methods = instanceClass.getMethods();

        Predicate predicate = new Predicate() {
            public boolean evaluate(Object arg0) {
                Method method = (Method) arg0;
                return method.getReturnType().isArray() && method.getReturnType().getComponentType() == declarationType;
            }
        };

        Collection<Method> declarations = new ArrayList<Method>();
        CollectionUtils.select(Arrays.asList(methods), predicate, declarations);

        return declarations;
    }

    /**
     * Reads mapping definitions and creates internal mapping model.
     * 
     * @param mappings mappings definitions
     * @param mappingConfigurations configurations
     * @param globalConfiguration global configuration
     * @return collection of bean mappings
     */
    private Collection<BeanMap> processMappings(Collection<Mapping> mappings,
        Map<String, BeanMapConfiguration> mappingConfigurations, Configuration globalConfiguration) {

        Map<String, BeanMap> beanMappings = getPreDefinedBeanMappingsByConfiguration(mappingConfigurations);

        for (Mapping mapping : mappings) {
            normalizeMapping(mapping);

            Class<?> classA = mapping.getClassA();
            Class<?> classB = mapping.getClassB();
            // Find appropriate bean map for current field map.
            //
            String key = BeanMapKeyFactory.createKey(classA, classB);
            BeanMap beanMapping = beanMappings.get(key);
            // If bean map is not exists create new one
            //
            if (beanMapping == null) {
                beanMapping = createBeanMap(classA, classB);
                // If user didn't define bean map configuration create new one
                // with reference to a global configuration.
                // 
                BeanMapConfiguration beanMappingConfiguration = createBeanMapConfiguration(classA, classB,
                    globalConfiguration);

                beanMapping.setConfiguration(beanMappingConfiguration);
                beanMappings.put(key, beanMapping);
            }

            beanMapping.getFieldMappings().add(createFieldMap(mapping, beanMapping));

            // TODO: review existing code. Can we use Dozer's mapping processor
            // instead of using this code?
            //
            if (!(mapping.getOneWay() != null && mapping.getOneWay())) {
                // If field mapping is bi-directional find reverse bean map
                //
                String reverseMappingKey = BeanMapKeyFactory.createKey(classB, classA);
                BeanMap reverseBeanMapping = beanMappings.get(reverseMappingKey);
                // If bean map is not exists create new one
                //
                if (reverseBeanMapping == null) {
                    reverseBeanMapping = createBeanMap(classB, classA);
                    reverseBeanMapping.setConfiguration(beanMapping.getConfiguration());

                    beanMappings.put(reverseMappingKey, reverseBeanMapping);
                }

                // Create reverse mapping
                //
                Mapping reverseMapping = reverseMapping(mapping);
                reverseBeanMapping.getFieldMappings().add(createFieldMap(reverseMapping, reverseBeanMapping));
            }
        }

        return beanMappings.values();
    }

    private Map<String, BeanMap> getPreDefinedBeanMappingsByConfiguration(
        Map<String, BeanMapConfiguration> mappingConfigurations) {
        Map<String, BeanMap> beanMappings = new HashMap<String, BeanMap>();

        for (Map.Entry<String, BeanMapConfiguration> entry : mappingConfigurations.entrySet()) {
            String key = entry.getKey();
            BeanMapConfiguration configuration = entry.getValue();
            BeanMap beanMap = createBeanMap(configuration.getClassA(), configuration.getClassB());
            beanMap.setConfiguration(configuration);

            beanMappings.put(key, beanMap);
        }

        return beanMappings;
    }

    /**
     * Loads global configuration
     * 
     * @param globalConfigurations global configuration definitions.
     * @return
     */
    private Configuration processConfiguration(List<GlobalConfiguration> globalConfigurations) {
        // Global configuration must be only one. If user defined several
        // configuration we should inform him that global configuration can be
        // only one.
        // 
        // TODO: in future a global configuration can be defined for each module
        // (module scope global configurations). In case of this we have to add
        // processor which will load configurations properly.
        //
        if (globalConfigurations.size() > 1) {
            throw new RulesMappingException("Found more than 1 global configuration definition");
        }

        Configuration configuration = new Configuration();
        // in current implementation we iterate thru definitions and fill
        // configuration instance with
        // definition values.
        // 
        for (GlobalConfiguration globalConfiguration : globalConfigurations) {
            configuration.setDateFormat(globalConfiguration.getDateFormat());
            configuration.setMapEmptyStrings(globalConfiguration.getMapEmptyStrings());
            configuration.setMapNulls(globalConfiguration.getMapNulls());
            configuration.setRequiredFields(globalConfiguration.getRequiredFields());
            configuration.setTrimStrings(globalConfiguration.getTrimStrings());
            configuration.setWildcard(globalConfiguration.getWildcard());
        }

        return configuration;
    }

    /**
     * Helper method that makes several actions with loaded mappings to remove
     * OpenL data loading specific.
     * 
     * @param mapping mapping object
     */
    private void normalizeMapping(Mapping mapping) {
        Class<?>[][] fieldAHint = getAHint(mapping.getFieldAHint(), mapping.getFieldA());

        if (fieldAHint != null) {
            for (int i = 0; i < fieldAHint.length; i++) {
                Class<?>[] element = fieldAHint[i];
                if (isEmpty(element)) {
                    fieldAHint[i] = null;
                }
            }
        }

        if (mapping.getFieldA() != null) {
            fieldAHint = resizeHintIfRequired(fieldAHint, mapping.getFieldA().length);
        }

        mapping.setFieldAHint(fieldAHint);

        Class<?>[] fieldBHint = mapping.getFieldBHint();
        if (isEmpty(fieldBHint)) {
            mapping.setFieldBHint(null);
        }

        Class<?>[] fieldAType = mapping.getFieldAType();
        if (isEmpty(fieldAType)) {
            mapping.setFieldAType(null);
        }
    }

    private Class<?>[][] resizeHintIfRequired(Class<?>[][] existedHint, int size) {
        if (existedHint == null || size < 1) {
            return null;
        }

        if (existedHint.length != size) {
            Class<?>[][] newHint = new Class<?>[size][];
            System.arraycopy(existedHint, 0, newHint, 0, existedHint.length);

            return newHint;
        }

        return existedHint;
    }

    private boolean isEmpty(Class<?>[] array) {
        if (array == null || array.length == 0) {
            return true;
        }

        for (Class<?> element : array) {
            if (element != null) {
                return false;
            }
        }

        return true;
    }

    private Class<?>[][] getAHint(Class<?>[][] fieldAHint, String[] field) {

        if (field == null || field.length == 0 || fieldAHint == null) {
            return null;
        }

        if (field.length > 1) {
            return fieldAHint;
        }

        Class<?>[][] hint = new Class<?>[1][fieldAHint.length];

        for (int i = 0; i < fieldAHint.length; i++) {
            if (fieldAHint[i] != null && fieldAHint[i].length > 0) {
                hint[0][i] = fieldAHint[i][0];
            } else {
                hint[0][i] = null;
            }

        }

        return hint;
    }

    private Collection<ConverterDescriptor> processDefaultConverters(List<Converter> defaultConverters) {
        List<ConverterDescriptor> descriptors = new ArrayList<ConverterDescriptor>();

        for (Converter converter : defaultConverters) {
            String id = ConverterIdFactory.createConverterId(converter);
            ConverterDescriptor customConverter = null;

            if (customConvertersMap.containsKey(id)) {
                customConverter = customConvertersMap.get(id);
            } else {
                customConverter = createConverterDescriptor(id, converter.getConvertMethod(), converter.getClassA(),
                    converter.getClassB());
            }

            descriptors.add(customConverter);
        }

        return descriptors;
    }

    /**
     * Creates new {@link BeanMap} instance using classes info.
     * 
     * @param classA source class
     * @param classB destination class
     * @return {@link BeanMap} instance
     */
    private BeanMap createBeanMap(Class<?> classA, Class<?> classB) {
        BeanMap beanMapping = new BeanMap();
        beanMapping.setSrcClass(classA);
        beanMapping.setDestClass(classB);

        return beanMapping;
    }

    /**
     * Reverses {@link Mapping} object.
     * 
     * @param mapping prime mapping
     * @return revered mapping
     */
    private Mapping reverseMapping(Mapping mapping) {
        if (mapping == null) {
            throw new RulesMappingException("An empty field mapping is found");
        }

        String[] fieldA = mapping.getFieldA();
        if (fieldA == null || fieldA.length == 0) {
            throw new RulesMappingException("Empty source mapping should be one way");
        }

        if (fieldA.length > 1) {
            throw new RulesMappingException("Multi source mapping should be one way");
        }

        Mapping reverseMapping = new Mapping();
        reverseMapping.setClassA(mapping.getClassB());
        reverseMapping.setClassB(mapping.getClassA());
        reverseMapping.setFieldA(new String[] { mapping.getFieldB() });
        reverseMapping.setFieldB(fieldA[0]);
        reverseMapping.setFieldACreateMethod(mapping.getFieldBCreateMethod());
        reverseMapping.setFieldBCreateMethod(mapping.getFieldACreateMethod());
        reverseMapping.setFieldADefaultValue(mapping.getFieldBDefaultValue());
        reverseMapping.setFieldBDefaultValue(mapping.getFieldADefaultValue());
        reverseMapping.setFieldARequired(mapping.getFieldBRequired());
        reverseMapping.setFieldBRequired(mapping.getFieldARequired());
        reverseMapping.setMapNulls(mapping.getMapNulls());
        reverseMapping.setOneWay(mapping.getOneWay());
        reverseMapping.setConvertMethodAB(mapping.getConvertMethodBA());
        reverseMapping.setConvertMethodBA(mapping.getConvertMethodAB());
        reverseMapping.setConditionAB(mapping.getConditionBA());
        reverseMapping.setConditionBA(mapping.getConditionAB());
        reverseMapping.setConvertMethodABId(mapping.getConvertMethodBAId());
        reverseMapping.setConvertMethodBAId(mapping.getConvertMethodABId());
        reverseMapping.setConditionABId(mapping.getConditionBAId());
        reverseMapping.setConditionBAId(mapping.getConditionABId());

        
        if (mapping.getFieldBType() != null) {
            reverseMapping.setFieldAType(new Class<?>[] { mapping.getFieldBType() });
        }
        if (mapping.getFieldAType() != null && mapping.getFieldAType()[0] != null) {
            reverseMapping.setFieldBType(mapping.getFieldAType()[0]);
        }
        if (mapping.getFieldBHint() != null) {
            reverseMapping.setFieldAHint(new Class<?>[][] { mapping.getFieldBHint() });
        }
        if (mapping.getFieldAHint() != null && mapping.getFieldAHint()[0] != null) {
            reverseMapping.setFieldBHint(mapping.getFieldAHint()[0]);
        }
        if (mapping.getFieldADateFormat() != null && mapping.getFieldADateFormat()[0] != null) {
            reverseMapping.setFieldBDateFormat(mapping.getFieldADateFormat()[0]);
        }
        if (mapping.getFieldBDateFormat() != null) {
            reverseMapping.setFieldADateFormat(new String[] { mapping.getFieldBDateFormat() });
        }

        return reverseMapping;
    }

    /**
     * Creates a single field mapping descriptor using field mapping definition.
     * 
     * @param mapping mapping definition
     * @return field mapping descriptor
     */
    private FieldMap createFieldMap(Mapping mapping, BeanMap beanMap) {
        FieldMap fieldMapping = new FieldMap();
        fieldMapping.setBeanMap(beanMap);
        fieldMapping.setSrc(mapping.getFieldA());
        fieldMapping.setDest(mapping.getFieldB());
        fieldMapping.setMapNulls(mapping.getMapNulls());
        fieldMapping.setMapEmptyStrings(mapping.getMapEmptyStrings());
        fieldMapping.setRequired(mapping.getFieldBRequired());
        fieldMapping.setDefaultValue(mapping.getFieldBDefaultValue());
        fieldMapping.setSrcHint(mapping.getFieldAHint());
        fieldMapping.setDestHint(mapping.getFieldBHint());
        fieldMapping.setSrcType(mapping.getFieldAType());
        fieldMapping.setDestType(mapping.getFieldBType());
        fieldMapping.setTrimStrings(mapping.getTrimStrings());
        fieldMapping.setSrcDateFormat(mapping.getFieldADateFormat());
        fieldMapping.setDestDateFormat(mapping.getFieldBDateFormat());

        // Update type name because if user defined create method with class
        // name without package prefix we should use type resolver to load
        // appropriate class.
        if (StringUtils.isNotEmpty(mapping.getFieldBCreateMethod()) && getTypeName(mapping.getFieldBCreateMethod()) != null) {
            String typeName = getTypeName(mapping.getFieldBCreateMethod());
            Class<?> clazz = typeResolver.findClass(typeName);
            if (clazz == null) {
                throw new RulesMappingException(String.format("Type '%s' not found", typeName));
            }

            fieldMapping.setCreateMethod(clazz.getName() + "." + getMethodName(mapping.getFieldBCreateMethod()));
        } else {
            fieldMapping.setCreateMethod(mapping.getFieldBCreateMethod());
        }

        if (!StringUtils.isBlank(mapping.getConvertMethodABId())) {
            // create converter descriptor for current field mapping.
            ConverterDescriptor converterDescriptor = createConverterDescriptor(mapping.getConvertMethodABId(), null,
                null, null);
            fieldMapping.setConverter(converterDescriptor);
        } else if (!StringUtils.isBlank(mapping.getConvertMethodAB())) {
            // create converter descriptor for current field mapping.
            String converterId = MappingIdFactory.createMappingId(mapping);
            ConverterDescriptor converterDescriptor = createConverterDescriptor(converterId, mapping
                .getConvertMethodAB(), mapping.getClassA(), mapping.getClassB());
            fieldMapping.setConverter(converterDescriptor);
        }

        if (!StringUtils.isBlank(mapping.getConditionABId())) {
            // create converter descriptor for current field mapping.
            ConditionDescriptor conditionDescriptor = createConditionDescriptor(mapping.getConditionABId(), null);
            fieldMapping.setCondition(conditionDescriptor);
        } else if (!StringUtils.isBlank(mapping.getConditionAB())) {
            String conditionId = MappingIdFactory.createMappingId(mapping);
            ConditionDescriptor conditionDescriptor = createConditionDescriptor(conditionId, mapping.getConditionAB());
            fieldMapping.setCondition(conditionDescriptor);
        }

        return fieldMapping;
    }

    private ConverterDescriptor createConverterDescriptor(String converterId, String convertMethod, Class<?> srcType,
        Class<?> destType) {
        // At this moment we don't know real types of fields and cannot cache
        // converter instances. To reduce count of converters we are using
        // proxy objects which invokes appropriate convert method at runtime
        CustomConverter converter = null;

        // Check that user defined convert method using method name.
        // If convert method is not defined we are does't try to resolve convert
        // method and using only method ID value to create descriptor.
        //
        if (StringUtils.isNotEmpty(convertMethod)) {
            String typeName = getTypeName(convertMethod);

            if (typeName != null) {
                Class<?> convertClass = typeResolver.findClass(typeName);
                if (convertClass == null) {
                    throw new RulesMappingException(String.format("Type '%s' not found", typeName));
                }

                converter = ConverterFactory.createConverter(getMethodName(convertMethod), convertClass, null);
            } else {
                converter = ConverterFactory.createConverter(convertMethod, instanceClass, instance);
            }
        }

        return new ConverterDescriptor(converterId, converter, srcType, destType);
    }

    private ConditionDescriptor createConditionDescriptor(String conditionId, String conditionMethod) {
        // At this moment we don't know real types of fields and cannot cache
        // condition instances. To reduce count of condition methods we are
        // using proxies objects which invokes appropriate condition method at
        // runtime
        FieldMappingCondition condition = null;

        if (StringUtils.isNotEmpty(conditionMethod)) {
            String typeName = getTypeName(conditionMethod);

            if (typeName != null) {
                Class<?> conditionClass = typeResolver.findClass(typeName);
                if (conditionClass == null) {
                    throw new RulesMappingException(String.format("Type '%s' not found", typeName));
                }

                condition = ConditionFactory.createCondition(getMethodName(conditionMethod), conditionClass, instance);
            } else {
                condition = ConditionFactory.createCondition(conditionMethod, instanceClass, instance);
            }
        }

        return new ConditionDescriptor(conditionId, condition);
    }

    private String getMethodName(String methodName) {
        if (!methodName.contains(".")) {
            return methodName;
        }

        return methodName.substring(methodName.lastIndexOf('.') + 1);
    }

    private String getTypeName(String methodName) {
        if (!methodName.contains(".")) {
            return null;
        }

        return methodName.substring(0, methodName.lastIndexOf('.'));
    }

}
