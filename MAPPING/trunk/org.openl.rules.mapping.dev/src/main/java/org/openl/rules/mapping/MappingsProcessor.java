package org.openl.rules.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.Mapping;

/**
 * Internal class which processes mapping definitions and creates internal
 * mapping model. Current implementation uses OpenL Rules project as mapping
 * definitions source. Not intended for direct use by Application code.
 */
class MappingsProcessor {

    /**
     * Class definition of the rules project.
     */
    private Class<?> instanceClass;

    /**
     * The instance object of the rules project.
     */
    private Object instance;

    /**
     * Creates new instance of mappings processor using OpenL Rules project
     * instance information.
     * 
     * @param instanceClass class definition
     * @param instance instance object
     */
    public MappingsProcessor(Class<?> instanceClass, Object instance) {
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
     * @return collection of bean mappings
     */
    private Collection<BeanMap> processMappings(Collection<Mapping> mappings) {

        Map<String, BeanMap> beanMappings = new HashMap<String, BeanMap>();

        for (Mapping mapping : mappings) {
            Class<?> classA = mapping.getClassA();
            Class<?> classB = mapping.getClassB();

//            BeanMap beanMapping = findBeanMapping(beanMappings, classA, classB);
//            BeanMap reverseBeanMapping = findBeanMapping(beanMappings, classB, classA);
//
//            if (beanMapping == null) {
//                beanMapping = new BeanMap();
//                beanMapping.setClassA(classA);
//                beanMapping.setClassB(classB);
//                beanMappings.put(new ClassPair(classA, classB), beanMapping);
//
//                reverseBeanMapping = new Bean2BeanMappingDescriptor();
//                reverseBeanMapping.setClassA(classB);
//                reverseBeanMapping.setClassB(classA);
//
//                beanMappings.put(new ClassPair(classB, classA), reverseBeanMapping);
//            }
//
//            beanMapping.getFieldMappings().add(createFieldMapping(mapping));
//
//            if (!mapping.isOneWay()) {
//                reverseBeanMapping.getFieldMappings().add(createFieldMapping(reverseFieldMapping(mapping)));
//            }
            
            
        }
        
        return null;
    }
}