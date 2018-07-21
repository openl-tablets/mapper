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
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.dozer.CollectionItemDiscriminator;
import org.dozer.CustomConverter;
import org.dozer.FieldMappingCondition;
import org.openl.rules.mapping.ClassMappingConfiguration;
import org.openl.rules.mapping.Converter;
import org.openl.rules.mapping.GlobalConfiguration;
import org.openl.rules.mapping.Mapping;
import org.openl.rules.mapping.TypeResolver;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.BeanMapConfiguration;
import org.openl.rules.mapping.definition.CollectionItemDiscriminatorDescriptor;
import org.openl.rules.mapping.definition.ConditionDescriptor;
import org.openl.rules.mapping.definition.Configuration;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.definition.FieldMap;
import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.loader.condition.ConditionFactory;
import org.openl.rules.mapping.loader.converter.ConverterFactory;
import org.openl.rules.mapping.loader.converter.ConverterIdFactory;
import org.openl.rules.mapping.loader.discriminator.CollectionItemDiscriminatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intended for internal use only.
 * 
 * Loads mapping declarations and converts them into internal model which used
 * by mapper.
 */
public class RulesMappingsLoader {
    
    private static final Logger LOG = LoggerFactory.getLogger(RulesMappingsLoader.class);
    
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
    @SuppressWarnings("unchecked")
    public Collection<BeanMap> loadMappings(Map<String, BeanMapConfiguration> mappingConfigurations,
        Configuration globalConfiguration) {
        List<Mapping> mappings = findDeclarations(instanceClass, instance, Mapping.class);
        
        if (LOG.isDebugEnabled()) {
            Collection<String> mappingNames = (Collection<String>) CollectionUtils.collect(mappings, new Transformer() {
                
                @Override
                public String transform(Object arg) {
                    Mapping mapping = (Mapping)arg;
                    return MappingIdFactory.createMappingId(mapping);
                }
            }); 
            
            LOG.debug("Found mapping declarations:\n" + StringUtils.join(mappingNames, ",\n"));
        }

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
            String key = BeanMapKeyFactory.createKey(srcClass, destClass);
            beanMapConfigs.put(key, beanMapConfiguration);
            
            String reverseKey = BeanMapKeyFactory.createKey(destClass, srcClass);
            
            // If class level configurations map doesn't have configuration for
            // reverse-way mapping we should create a new one else we consider
            // that user defines his own configuration.
            if (beanMapConfigs.get(reverseKey) == null) {
                beanMapConfigs.put(reverseKey, reverseBeanMapConfiguration(beanMapConfiguration));
            }
        }

        return beanMapConfigs;
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

        for (Mapping originalMapping : mappings) {
            Mapping mapping = MappingDefinitionUtils.normalizeMapping(originalMapping);

            Class<?> classA = mapping.getClassA();
            Class<?> classB = mapping.getClassB();
            // Find appropriate bean map for current field map.
            //
            String beanMapKey = BeanMapKeyFactory.createKey(classA, classB);
            BeanMap beanMapping = beanMappings.get(beanMapKey);
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
                beanMappings.put(beanMapKey, beanMapping);
            }

            String mappingId = MappingIdFactory.createMappingId(mapping);
            if (!beanMapping.hasFieldMap(mappingId)) {
                beanMapping.addFieldMap(mappingId, createFieldMap(mapping, beanMapping));
            }

            // If field mapping is bi-directional create reverse bean map
            //
            if (!(mapping.getOneWay() != null && mapping.getOneWay())) {
                String reverseMappingKey = BeanMapKeyFactory.createKey(classB, classA);
                BeanMap reverseBeanMapping = beanMappings.get(reverseMappingKey);
                // If bean map is not exists create new one
                //
                if (reverseBeanMapping == null) {
                    reverseBeanMapping = createBeanMap(classB, classA);
                    reverseBeanMapping.setConfiguration(reverseBeanMapConfiguration(beanMapping.getConfiguration()));

                    beanMappings.put(reverseMappingKey, reverseBeanMapping);
                }

                // Create reverse mapping
                Mapping reverseMapping = MappingDefinitionUtils.reverseMapping(mapping);
                String reverseMappingId = MappingIdFactory.createMappingId(reverseMapping);

                if (!reverseBeanMapping.hasFieldMap(reverseMappingId)) {
                    reverseBeanMapping.addFieldMap(reverseMappingId, createFieldMap(reverseMapping, reverseBeanMapping));
                }
            }
        }

        return beanMappings.values();
    }

    private BeanMapConfiguration createBeanMapConfiguration(Class<?> classA,
            Class<?> classB,
            Configuration globalConfiguration) {

        BeanMapConfiguration config = new BeanMapConfiguration();
        config.setClassA(classA);
        config.setClassB(classB);
        config.setGlobalConfiguration(globalConfiguration);
        
        return config;
    }
    
    private BeanMapConfiguration reverseBeanMapConfiguration(BeanMapConfiguration beanMapConfiguration) {
        BeanMapConfiguration reverseConfig = createBeanMapConfiguration(beanMapConfiguration.getClassB(),
            beanMapConfiguration.getClassA(),
            beanMapConfiguration.getGlobalConfiguration());
        
        reverseConfig.setClassABeanFactory(beanMapConfiguration.getClassBBeanFactory());
        reverseConfig.setClassBBeanFactory(beanMapConfiguration.getClassABeanFactory());
        reverseConfig.setDateFormat(beanMapConfiguration.getDateFormat());
        reverseConfig.setMapEmptyStrings(beanMapConfiguration.isMapEmptyStrings());
        reverseConfig.setMapNulls(beanMapConfiguration.isMapNulls());
        reverseConfig.setRequiredFields(beanMapConfiguration.isRequiredFields());
        reverseConfig.setTrimStrings(beanMapConfiguration.isTrimStrings());
        reverseConfig.setWildcard(beanMapConfiguration.isWildcard());
        
        return reverseConfig;
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
	        configuration.setBeanFactory(globalConfiguration.getBeanFactory());
        }

        return configuration;
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
        fieldMapping.setMapId(mapping.getMapId());

        // Update type name because if user defined create method with class
        // name without package prefix we should use type resolver to load
        // appropriate class.
        if (StringUtils.isNotEmpty(mapping.getFieldBCreateMethod()) && MappingDefinitionUtils.getTypeName(mapping.getFieldBCreateMethod()) != null) {
            String typeName = MappingDefinitionUtils.getTypeName(mapping.getFieldBCreateMethod());
            Class<?> clazz = getType(typeName);

            fieldMapping.setCreateMethod(clazz.getName() + "." + MappingDefinitionUtils.getMethodName(mapping.getFieldBCreateMethod()));
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
            ConverterDescriptor converterDescriptor = createConverterDescriptor(converterId,
                mapping.getConvertMethodAB(), mapping.getClassA(), mapping.getClassB());
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
        
        if (!StringUtils.isBlank(mapping.getFieldBDiscriminatorId())) {
            CollectionItemDiscriminatorDescriptor discriminatorDescriptor = createDiscriminatorDescriptor(mapping.getFieldBDiscriminatorId(), null);
            fieldMapping.setCollectionItemDiscriminator(discriminatorDescriptor);
        } else if (!StringUtils.isBlank(mapping.getFieldBDiscriminator())) {
            String discriminatorId = MappingIdFactory.createMappingId(mapping);
            CollectionItemDiscriminatorDescriptor discriminatorDescriptor = createDiscriminatorDescriptor(discriminatorId, mapping.getFieldBDiscriminator());
            fieldMapping.setCollectionItemDiscriminator(discriminatorDescriptor);
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
            String typeName = MappingDefinitionUtils.getTypeName(convertMethod);

            if (typeName != null) {
                Class<?> convertClass = getType(typeName);
                converter = ConverterFactory.createConverter(MappingDefinitionUtils.getMethodName(convertMethod), convertClass, null);
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
            String typeName = MappingDefinitionUtils.getTypeName(conditionMethod);

            if (typeName != null) {
                Class<?> conditionClass = getType(typeName);
                condition = ConditionFactory.createCondition(MappingDefinitionUtils.getMethodName(conditionMethod), conditionClass, instance);
            } else {
                condition = ConditionFactory.createCondition(conditionMethod, instanceClass, instance);
            }
        }

        return new ConditionDescriptor(conditionId, condition);
    }

    private CollectionItemDiscriminatorDescriptor createDiscriminatorDescriptor(String discriminatorId, String discriminatorMethod) {
        CollectionItemDiscriminator discriminator = null;

        if (StringUtils.isNotEmpty(discriminatorMethod)) {
            String typeName = MappingDefinitionUtils.getTypeName(discriminatorMethod);

            if (typeName != null) {
                Class<?> discriminatorClass = getType(typeName);
                discriminator = CollectionItemDiscriminatorFactory.createDiscriminator(MappingDefinitionUtils.getMethodName(discriminatorMethod), discriminatorClass, instance);
            } else {
                discriminator = CollectionItemDiscriminatorFactory.createDiscriminator(discriminatorMethod, instanceClass, instance);
            }
        }

        return new CollectionItemDiscriminatorDescriptor(discriminatorId, discriminator);
    }

    private Class<?> getType(String typeName) {
        Class<?> conditionClass = typeResolver.findClass(typeName);
        if (conditionClass == null) {
            throw new RulesMappingException(String.format("Type '%s' not found", typeName));
        }
        
        return conditionClass;
    }

}
