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
import org.dozer.fieldmap.FieldMappingCondition;
import org.openl.rules.mapping.Mapping;
import org.openl.rules.mapping.RulesMappingException;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.BeanMapKeyFactory;
import org.openl.rules.mapping.definition.ConditionDescriptor;
import org.openl.rules.mapping.definition.ConditionFactory;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.definition.ConverterFactory;
import org.openl.rules.mapping.definition.FieldMap;
import org.openl.rules.mapping.definition.MappingIdFactory;

public class RulesMappingsLoader {
    
    private Class<?> instanceClass;
    private Object instance;
    
    public RulesMappingsLoader(Class<?> instanceClass, Object instance) {
        this.instanceClass = instanceClass;
        this.instance = instance;
    }

    public Collection<BeanMap> loadMappings() {
        List<Mapping> mappings = findMappings(instanceClass, instance);

        return processMappings(mappings);
    }

    /**
     * Finds mapping definitions in specified OpenL Rules project.
     * 
     * @param instanceClass class definition
     * @param instance instance object
     * @return list of mapping definitions
     */
    private List<Mapping> findMappings(Class<?> instanceClass, Object instance) {

        List<Mapping> mappings = new ArrayList<Mapping>();

        // We use instance class definition to obtain list of mapping
        // declarations.
        //
        Collection<Method> declarations = findMappingDeclarations(instanceClass);

        for (Method declaration : declarations) {
            Mapping[] mappingsArray;

            try {
                // Invoke method to obtain mappings.
                //
                mappingsArray = (Mapping[]) declaration.invoke(instance, new Object[0]);
            } catch (Exception e) {
                throw new RulesMappingException("Cannot load mappings", e);
            }

            // Add loaded mapping to result collection.
            //
            mappings.addAll(Arrays.asList(mappingsArray));
        }

        return mappings;
    }

    /**
     * Finds mapping declarations using class definition of the OpenL Rules
     * project. Current method implementation uses assumption that methods which
     * provide mapping definitions return {@link Mapping}[] type and doesn't
     * have parameters.
     * 
     * @param instanceClass class definition
     * @return list of methods what returns mapping definitions
     */
    private Collection<Method> findMappingDeclarations(Class<?> instanceClass) {

        Method[] methods = instanceClass.getMethods();

        Predicate predicate = new Predicate() {
            public boolean evaluate(Object arg0) {
                Method method = (Method) arg0;
                return method.getReturnType().isArray() && method.getReturnType().getComponentType() == Mapping.class;
            }
        };

        Collection<Method> mappingDeclarations = new ArrayList<Method>();
        CollectionUtils.select(Arrays.asList(methods), predicate, mappingDeclarations);

        return mappingDeclarations;
    }

    /**
     * Reads mapping definitions and creates internal mapping model.
     * 
     * @param mappings mappings definitions
     * @param instanceClass
     * @param instance
     * @return collection of bean mappings
     */
    private Collection<BeanMap> processMappings(Collection<Mapping> mappings) {

        Map<String, BeanMap> beanMappings = new HashMap<String, BeanMap>();

        for (Mapping mapping : mappings) {
            Class<?> classA = mapping.getClassA();
            Class<?> classB = mapping.getClassB();
            // Find appropriate bean map for current field map.
            //
            BeanMap beanMapping = findBeanMapping(beanMappings, classA, classB);
            // If bean map is not exists create new one
            //
            if (beanMapping == null) {
                beanMapping = createBeanMap(classA, classB);
                String key = BeanMapKeyFactory.createKey(classA, classB);
                beanMappings.put(key, beanMapping);
            }

            beanMapping.getFieldMappings().add(createFieldMap(beanMapping, mapping));

            if (!mapping.isOneWay()) {
                // If field mapping is bi-directional find reversed bean map
                //
                BeanMap reversedBeanMapping = findBeanMapping(beanMappings, classB, classA);
                // If bean map is not exists create new one
                //
                if (reversedBeanMapping == null) {
                    reversedBeanMapping = createBeanMap(classB, classA);
                    String key = BeanMapKeyFactory.createKey(classB, classA);
                    beanMappings.put(key, reversedBeanMapping);
                }

                // Create reversed mapping
                //
                Mapping reversedMapping = reverseMapping(mapping);
                reversedBeanMapping.getFieldMappings().add(
                    createFieldMap(reversedBeanMapping, reversedMapping));
            }
        }

        return beanMappings.values();
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
     * Finds appropriate bean mapping in given mappings map.
     * 
     * @param beanMappings mappings map
     * @param classA source class
     * @param classB destination class
     * @return {@link BeanMap} if mapping is found; <code>null</code> -
     *         otherwise
     */
    private BeanMap findBeanMapping(Map<String, BeanMap> beanMappings, Class<?> classA, Class<?> classB) {
        String key = BeanMapKeyFactory.createKey(classA, classB);

        if (beanMappings.containsKey(key)) {
            return beanMappings.get(key);
        }

        return null;
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

        Mapping reversedMapping = new Mapping();
        reversedMapping.setClassA(mapping.getClassB());
        reversedMapping.setClassB(mapping.getClassA());
        reversedMapping.setFieldA(new String[] { mapping.getFieldB() });
        reversedMapping.setFieldB(fieldA[0]);
        reversedMapping.setClassABeanFactory(mapping.getClassBBeanFactory());
        reversedMapping.setClassBBeanFactory(mapping.getClassABeanFactory());
        reversedMapping.setFieldACreateMethod(mapping.getFieldBCreateMethod());
        reversedMapping.setFieldBCreateMethod(mapping.getFieldACreateMethod());
        reversedMapping.setFieldADefaultValue(mapping.getFieldBDefaultValue());
        reversedMapping.setFieldBDefaultValue(mapping.getFieldADefaultValue());
        reversedMapping.setFieldARequired(mapping.isFieldBRequired());
        reversedMapping.setFieldBRequired(mapping.isFieldARequired());
        reversedMapping.setMapNulls(mapping.isMapNulls());
        reversedMapping.setOneWay(mapping.isOneWay());
        reversedMapping.setConvertMethodAB(mapping.getConvertMethodBA());
        reversedMapping.setConvertMethodBA(mapping.getConvertMethodAB());
        reversedMapping.setConditionAB(mapping.getConditionBA());
        reversedMapping.setConditionBA(mapping.getConditionAB());

        return reversedMapping;
    }

    /**
     * Creates a single field mapping descriptor using field mapping definition.
     * 
     * @param mapping mapping definition
     * @return field mapping descriptor
     */
    private FieldMap createFieldMap(BeanMap beanMap, Mapping mapping) {
        FieldMap fieldMapping = new FieldMap();
        fieldMapping.setSrc(mapping.getFieldA());
        fieldMapping.setDest(mapping.getFieldB());
        fieldMapping.setMapNulls(mapping.isMapNulls());
        fieldMapping.setRequired(mapping.isFieldBRequired());
        fieldMapping.setDefaultValue(mapping.getFieldBDefaultValue());
        fieldMapping.setCreateMethod(mapping.getFieldBCreateMethod());
        fieldMapping.setBeanMap(beanMap);

        if (!StringUtils.isBlank(mapping.getConvertMethodAB())) {
            String converterId = createConverterId(mapping);
            ConverterDescriptor converterDescriptor = createConverterDescriptor(converterId, mapping
                .getConvertMethodAB());
            fieldMapping.setConverter(converterDescriptor);
        }

        if (!StringUtils.isBlank(mapping.getConditionAB())) {
            String conditionId = createConditionId(mapping);
            ConditionDescriptor conditionDescriptor = createConditionDescriptor(conditionId, mapping.getConditionAB());
            fieldMapping.setCondition(conditionDescriptor);
        }

        return fieldMapping;
    }

    private String createConverterId(Mapping mapping) {
        return MappingIdFactory.createMappingId(mapping);
    }

    private ConverterDescriptor createConverterDescriptor(String converterId, String convertMethod) {
        CustomConverter converter = ConverterFactory.createConverter(convertMethod, instanceClass, instance);
        return new ConverterDescriptor(converterId, converter);
    }

    private String createConditionId(Mapping mapping) {
        return MappingIdFactory.createMappingId(mapping);
    }

    private ConditionDescriptor createConditionDescriptor(String conditionId, String conditionMethod) {
        FieldMappingCondition condition = ConditionFactory.createCondition(conditionMethod, instanceClass, instance);
        return new ConditionDescriptor(conditionId, condition);
    }

}
