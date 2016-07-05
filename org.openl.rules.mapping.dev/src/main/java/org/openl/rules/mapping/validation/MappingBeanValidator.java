package org.openl.rules.mapping.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.openl.rules.mapping.MappingParameters;
import org.dozer.util.MappingUtils;
import org.openl.rules.mapping.Mapping;
import org.openl.rules.mapping.OpenLReflectionUtils;
import org.openl.rules.mapping.TypeResolver;
import org.openl.rules.mapping.loader.MappingDefinitionUtils;
import org.openl.rules.mapping.validation.utils.ClassMetaInfo;
import org.openl.rules.mapping.validation.utils.MethodMetaInfo;
import org.openl.rules.mapping.validation.utils.ValidationUtils;
import org.openl.types.IOpenClass;
import org.openl.validation.ValidationStatus;

/**
 * Validates {@link Mapping} bean.
 * 
 * Current validator starts his work after OpenL project is compiled without
 * errors and data tables are already loaded but not processed by mapping
 * processor. It's means that we should make several operations to get required
 * data for validation process manually, for example, get field types.
 */
public class MappingBeanValidator extends OpenLDataBeanValidator<Mapping> {

    @Override
    public BeanValidationResult validateBean(Mapping beanToValidate, IOpenClass openClass) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        // Normalize data to validate. It's required step because OpenL engine
        // loads data with some specific.
        Mapping bean = MappingDefinitionUtils.normalizeMapping(beanToValidate);

        // Validate field paths.
        Set<ConstraintViolation> fieldPathViolations = validateFieldPaths(bean);
        violations.addAll(fieldPathViolations);

        // Check that we doesn't have invalid field paths. If we have invalid
        // field path we can't to get field type consequently we should skip
        // convert, condition methods validation step.
        if (fieldPathViolations.isEmpty()) {
            violations.addAll(validateConvertMethods(bean, openClass));
            violations.addAll(validateConditionMethods(bean, openClass));
            violations.addAll(validateDiscriminatorMethods(bean, openClass));
        }

        // If we doesn't have violations we can make a decision that validation
        // is passed successfully.
        if (violations.isEmpty()) {
            return new BeanValidationResult(ValidationStatus.SUCCESS, bean.getClass());
        }

        // Create a validation fail result.
        BeanValidationResult result = new BeanValidationResult(ValidationStatus.FAIL, bean.getClass());
        result.addAllConstraintViolation(violations);

        return result;
    }

    /**
     * Validates convert methods if they are defined.
     * 
     * @param bean field mapping definition
     * @param openClass openl class
     * @return set of validation constraints
     */
    private Set<ConstraintViolation> validateConvertMethods(Mapping bean, IOpenClass openClass) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        Class<?> fieldAType = getFieldAType(bean);
        Class<?> fieldBType = getFieldBType(bean);

        violations.addAll(validateConvertMethod("convertMethodAB", bean.getConvertMethodAB(), openClass, fieldAType, fieldBType));
        violations.addAll(validateConvertMethod("convertMethodBA", bean.getConvertMethodBA(), openClass, fieldBType, fieldAType));

        return violations;
    }

    private Set<ConstraintViolation> validateConditionMethods(Mapping bean, IOpenClass openClass) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        Class<?> fieldAType = getFieldAType(bean);
        Class<?> fieldBType = getFieldBType(bean);

        violations.addAll(validateConditionMethod("conditionAB", bean.getConditionAB(), openClass, fieldAType, fieldBType));
        violations.addAll(validateConditionMethod("conditionBA", bean.getConditionBA(), openClass, fieldBType, fieldAType));

        return violations;
    }
    
    private Set<ConstraintViolation> validateDiscriminatorMethods(Mapping bean, IOpenClass openClass) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        Class<?> fieldAType = getFieldAType(bean);
        Class<?> fieldBType = getFieldBType(bean);

        Class<?> fieldAEntryTypeHint = null;
        if (bean.getFieldAType() != null && bean.getFieldAType().length == 1) {
            fieldAEntryTypeHint = bean.getFieldAType()[0];
        }

        Class<?> fieldBEntryTypeHint = bean.getFieldBType();
        
        violations.addAll(validateDiscriminatorMethod("fieldBDiscriminator", bean.getFieldBDiscriminator(), openClass, fieldAType, fieldBType, fieldAEntryTypeHint, fieldBEntryTypeHint));
        violations.addAll(validateDiscriminatorMethod("fieldADiscriminator", bean.getFieldADiscriminator(), openClass, fieldBType, fieldAType, fieldBEntryTypeHint, fieldAEntryTypeHint));

        return violations;
    }

    /**
     * Gets field A type.
     * 
     * @param bean field mapping definition
     * @return field type
     */
    private Class<?> getFieldAType(Mapping bean) {
        String[] fieldA = bean.getFieldA();
        Class<?> fieldAType = null;

        if (fieldA == null) {
            fieldAType = Object.class;
        } else if (fieldA.length > 1) {
            fieldAType = Object[].class;
        } else {
            Class<?>[] fieldTypeHint = null;
            if (bean.getFieldAHint() != null) {
                fieldTypeHint = bean.getFieldAHint()[0];
            }

            FieldPathHierarchyElement[] hierarchy = ValidationUtils.getFieldHierarchy(bean.getClassA(),
                fieldA[0],
                fieldTypeHint);
            FieldPathHierarchyElement lastElement = hierarchy[hierarchy.length - 1];
            fieldAType = lastElement.getType();

            if (StringUtils.isNotBlank(lastElement.getIndex()) && MappingUtils.isSupportedCollection(lastElement.getType())) {
                Class<?> entryTypeHint = bean.getFieldAType() != null ? bean.getFieldAType()[0] : null;
                fieldAType = ValidationUtils.getSupportedCollectionEntryType(lastElement.getType(), entryTypeHint);
            }
        }

        return fieldAType;
    }

    /**
     * Gets field B type.
     * 
     * @param bean field mapping definition
     * @return field type
     */
    private Class<?> getFieldBType(Mapping bean) {
        String fieldB = bean.getFieldB();
        Class<?> fieldBType = null;
        Class<?>[] fieldBTypeHint = bean.getFieldBHint();

        FieldPathHierarchyElement[] hierarchy = ValidationUtils.getFieldHierarchy(bean.getClassB(),
            fieldB,
            fieldBTypeHint);
        FieldPathHierarchyElement lastElement = hierarchy[hierarchy.length - 1];
        fieldBType = lastElement.getType();

        if (StringUtils.isNotBlank(lastElement.getIndex()) && MappingUtils.isSupportedCollection(lastElement.getType())) {
            Class<?> entryTypeHint = bean.getFieldBType();
            fieldBType = ValidationUtils.getSupportedCollectionEntryType(lastElement.getType(), entryTypeHint);
        }

        return fieldBType;
    }

    private Set<ConstraintViolation> validateConvertMethod(String propertyName,
            String convertMethod,
            IOpenClass openClass,
            Class<?> fieldA,
            Class<?> fieldB) {

        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        if (StringUtils.isBlank(convertMethod)) {
            return violations;
        }

        String convertMethodClassName = MappingDefinitionUtils.getTypeName(convertMethod);
        String convertMethodName = MappingDefinitionUtils.getMethodName(convertMethod);
        Class<?> convertMethodClass = null;

        TypeResolver typeResolver = OpenLReflectionUtils.getTypeResolver(openClass);

        if (StringUtils.isNotBlank(convertMethodClassName)) {
            convertMethodClass = typeResolver.findClass(convertMethodClassName);
        }

        String className = null;
        MethodMetaInfo simpleMethod = null;
        MethodMetaInfo extendedMethod = null;

        if (convertMethodClass != null) {
            simpleMethod = ValidationUtils.findMethod(convertMethodClass, convertMethodName, new Class<?>[] { fieldA, fieldB });
            extendedMethod = ValidationUtils.findMethod(convertMethodClass, convertMethodName, new Class<?>[] { MappingParameters.class, fieldA, fieldB });
            className = convertMethodClass.getName();
        } else {
            simpleMethod = ValidationUtils.findMethod(openClass, convertMethodName, new Class<?>[] { fieldA, fieldB });
            extendedMethod = ValidationUtils.findMethod(openClass, convertMethodName, new Class<?>[] { MappingParameters.class, fieldA, fieldB });
            className = openClass.getName();
        }

        if (simpleMethod == null && extendedMethod == null) {
            violations.add(createPropertyViolation(propertyName,
                convertMethod,
                String.format("Convert method '%1$s(%2$s, %3$s)' or '%1$s(%4$s, %2$s, %3$s)' cannot be found in class '%5$s'",
                    convertMethod, fieldA.getName(), fieldB.getName(), MappingParameters.class.getName(), className)));
        }

        if (simpleMethod != null) {
            ClassMetaInfo returnType = simpleMethod.getReturnType();
            if (returnType == null || !OpenLReflectionUtils.isAssignableFrom(fieldB, returnType.getInstanceClass())) {
                violations.add(createPropertyViolation(propertyName,
                    convertMethod,
                    String.format("Destination field of type '%s' cannot be assigned from value of '%s' type",
                        fieldB.getName(), returnType.getName())));
            }
        }

        if (extendedMethod != null) {
            ClassMetaInfo returnType = extendedMethod.getReturnType();
            if (returnType == null || !OpenLReflectionUtils.isAssignableFrom(fieldB, returnType.getInstanceClass())) {
                violations.add(createPropertyViolation(propertyName,
                    convertMethod,
                    String.format("Destination field of type '%s' cannot be assigned from value of '%s' type",
                        fieldB.getName(), returnType.getName())));
            }
        }

        return violations;
    }

    private Set<ConstraintViolation> validateConditionMethod(String propertyName,
            String conditionMethod,
            IOpenClass openClass,
            Class<?> fieldA,
            Class<?> fieldB) {

        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        if (StringUtils.isBlank(conditionMethod)) {
            return violations;
        }

        String conditionMethodClassName = MappingDefinitionUtils.getTypeName(conditionMethod);
        String conditionMethodName = MappingDefinitionUtils.getMethodName(conditionMethod);
        Class<?> conditionMethodClass = null;

        TypeResolver typeResolver = OpenLReflectionUtils.getTypeResolver(openClass);

        if (StringUtils.isNotBlank(conditionMethodClassName)) {
            conditionMethodClass = typeResolver.findClass(conditionMethodClassName);
        }

        String className = null;
        MethodMetaInfo simpleMethod = null;
        MethodMetaInfo extendedMethod = null;

        if (conditionMethodClass != null) {
            simpleMethod = ValidationUtils.findMethod(conditionMethodClass, conditionMethodName, new Class<?>[] { fieldA, fieldB });
            extendedMethod = ValidationUtils.findMethod(conditionMethodClass, conditionMethodName, new Class<?>[] { MappingParameters.class, fieldA, fieldB });
            className = conditionMethodClass.getName();
        } else {
            simpleMethod = ValidationUtils.findMethod(openClass, conditionMethodName, new Class<?>[] { fieldA, fieldB });
            extendedMethod = ValidationUtils.findMethod(openClass, conditionMethodName, new Class<?>[] { MappingParameters.class, fieldA, fieldB });
            className = openClass.getName();
        }

        if (simpleMethod == null && extendedMethod == null) {
            violations.add(createPropertyViolation(propertyName,
                conditionMethod,
                String.format("Convert method '%1$s(%2$s, %3$s)' or '%1$s(%4$s, %2$s, %3$s)' cannot be found in class '%5$s'",
                    conditionMethod, fieldA.getName(), fieldB.getName(), MappingParameters.class.getName(), className)));
        }

        if (simpleMethod != null) {
            ClassMetaInfo returnType = simpleMethod.getReturnType();
            if (boolean.class != returnType.getInstanceClass() && !Boolean.class.equals(returnType.getInstanceClass())) {
                violations.add(createPropertyViolation(propertyName, conditionMethod, "Condition method have to return value of boolean type"));

            }
        }

        if (extendedMethod != null) {
            ClassMetaInfo returnType = extendedMethod.getReturnType();
            if (boolean.class != returnType.getInstanceClass() && !Boolean.class.equals(returnType.getInstanceClass())) {
                violations.add(createPropertyViolation(propertyName, conditionMethod, "Condition method have to return value of boolean type"));

            }
        }
        
        return violations;
    }

    private Set<ConstraintViolation> validateDiscriminatorMethod(String propertyName,
            String methodName,
            IOpenClass openClass,
            Class<?> fieldA,
            Class<?> fieldB,
            Class<?> fieldAEntryTypeHint,
            Class<?> fieldBEntryTypeHint) {

        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        if (StringUtils.isBlank(methodName)) {
            return violations;
        }

        String discriminatorMethodClassName = MappingDefinitionUtils.getTypeName(methodName);
        String discriminatorMethodName = MappingDefinitionUtils.getMethodName(methodName);
        Class<?> discriminatorMethodClass = null;

        TypeResolver typeResolver = OpenLReflectionUtils.getTypeResolver(openClass);

        if (StringUtils.isNotBlank(discriminatorMethodClassName)) {
            discriminatorMethodClass = typeResolver.findClass(discriminatorMethodClassName);
        }

        Class<?> fieldAType = ValidationUtils.getCollectionFieldType(fieldA, fieldAEntryTypeHint);
        Class<?> fieldBType = ValidationUtils.getCollectionFieldType(fieldB, fieldBEntryTypeHint);
        Class<?> fieldAEntryType = ValidationUtils.getSupportedCollectionEntryType(fieldAType, fieldAEntryTypeHint);

        String className = null;
        MethodMetaInfo simpleMethod = null;
        MethodMetaInfo extendedMethod = null;

        if (discriminatorMethodClass != null) {
            simpleMethod = ValidationUtils.findMethod(discriminatorMethodClass, discriminatorMethodName, new Class<?>[] { fieldAEntryType, fieldBType });
            extendedMethod = ValidationUtils.findMethod(discriminatorMethodClass, discriminatorMethodName, new Class<?>[] { MappingParameters.class, fieldAType, fieldBType });
            className = discriminatorMethodClass.getName();
        } else {
            simpleMethod = ValidationUtils.findMethod(openClass, discriminatorMethodName, new Class<?>[] { fieldAType, fieldBType });
            extendedMethod = ValidationUtils.findMethod(openClass, discriminatorMethodName, new Class<?>[] { MappingParameters.class, fieldAEntryType, fieldBType });
            className = openClass.getName();
        }


        // Check field types
        if (!ValidationUtils.isSupportedCollection(fieldB)) {
            violations.add(createPropertyViolation(propertyName,
                fieldB.getName(),
                String.format("Target field '%s' should be an array or a collection", fieldB.getName())));
        } else {
            
            if (simpleMethod == null && extendedMethod == null) {
                violations.add(createPropertyViolation(propertyName,
                    methodName,
                    String.format("Discriminator method '%1$s(%2$s, %3$s)' or '%1$s(%4$s, %2$s, %3$s)' cannot be found in class '%5$s'",
                        methodName,
                        fieldAEntryType.getName(),
                        fieldBType.getName(),
                        MappingParameters.class.getName(),
                        className)));
            } else {
                Class<?> entryType = ValidationUtils.getSupportedCollectionEntryType(fieldB, fieldBEntryTypeHint);
                ClassMetaInfo returnType = null;

                // Check return type
                if (simpleMethod != null) {
                    returnType = simpleMethod.getReturnType();
                }
                if (extendedMethod != null) {
                    returnType = extendedMethod.getReturnType();
                }
                if (returnType == null || !OpenLReflectionUtils.isAssignableFrom(entryType,
                    returnType.getInstanceClass())) {
                    String returnTypeName = returnType == null ? null : returnType.getName();
                    violations.add(createPropertyViolation(propertyName,
                        methodName,
                        String.format("Destination collection item of type '%s' cannot be assigned from value of '%s' type",
                            entryType.getName(),
                            returnTypeName)));
                }
            }
        }

        return violations;
    }
 
    private Set<ConstraintViolation> validateFieldPaths(Mapping bean) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        Class<?> classAType = bean.getClassA();
        String[] fieldA = bean.getFieldA();

        if (fieldA != null) {
            for (int i = 0; i < fieldA.length; i++) {
                String field = fieldA[i];
                Class<?>[] fieldTypeHint = null;

                if (bean.getFieldAHint() != null) {
                    fieldTypeHint = bean.getFieldAHint()[i];
                }

                FieldPathHierarchyElement[] hierarchy = null;
                try {
                    hierarchy = ValidationUtils.getFieldHierarchy(classAType, field, fieldTypeHint);
                } catch (Exception ex) {
                    String message = ex.getMessage();
                    if (StringUtils.isBlank(message)) {
                        message = String.format("Exception occurred determining field hierarchy for Class --> %s, Field --> %s",
                            classAType == null ? null : classAType.getName(),
                            field);
                    }

                    violations.add(createPropertyViolation("fieldA", field, message));
                }

                if (hierarchy != null) {
                    List<FieldPathHierarchyElement> hierarchyElementList = Arrays.asList(hierarchy);

                    @SuppressWarnings("unchecked")
                    Collection<FieldPathHierarchyElement> invalidElements = CollectionUtils.select(hierarchyElementList,
                        new Predicate() {
                            public boolean evaluate(Object arg) {
                                FieldPathHierarchyElement e = (FieldPathHierarchyElement) arg;
                                String index = e.getIndex();

                                return StringUtils.isNotBlank(index) && ValidationUtils.isSimpleCollectionIndex(index) && ValidationUtils.getCollectionIndex(index) == -1;
                            }
                        });

                    if (!invalidElements.isEmpty()) {
                        for (FieldPathHierarchyElement e : invalidElements) {
                            violations.add(createPropertyViolation("fieldA",
                                field,
                                String.format("Index value '%s' cannot be used for source field", e.getIndex())));
                        }
                    }
                }
            }
        }

        Class<?> classBType = bean.getClassB();
        String fieldB = bean.getFieldB();
        Class<?>[] fieldBTypeHint = bean.getFieldBHint();

        FieldPathHierarchyElement[] hierarchy = null;
        try {
            hierarchy = ValidationUtils.getFieldHierarchy(classBType, fieldB, fieldBTypeHint);
        } catch (Exception ex) {
            String message = ex.getMessage();
            if (StringUtils.isBlank(message)) {
                message = String.format("Exception occurred determining field hierarchy for Class --> %s, Field --> %s",
                    classBType == null ? null : classBType.getName(),
                    fieldB);
            }

            violations.add(createPropertyViolation("fieldB", fieldB, message));
        }

        if (hierarchy != null) {
            List<FieldPathHierarchyElement> hierarchyElementList = Arrays.asList(hierarchy);

            @SuppressWarnings("unchecked")
            Collection<FieldPathHierarchyElement> invalidElements = CollectionUtils.select(hierarchyElementList,
                new Predicate() {
                    public boolean evaluate(Object arg) {
                        FieldPathHierarchyElement e = (FieldPathHierarchyElement) arg;
                        String index = e.getIndex();

                        return StringUtils.isNotBlank(index) && !ValidationUtils.isSimpleCollectionIndex(index);
                    }
                });

            if (!invalidElements.isEmpty()) {
                for (FieldPathHierarchyElement e : invalidElements) {
                    violations.add(createPropertyViolation("fieldB",
                        fieldB,
                        String.format("Index value '%s' cannot be used for destination field", e.getIndex())));
                }
            }
        }

        return violations;
    }

    private PropertyConstraintViolation createPropertyViolation(String propertyName,
            Object invalidValue,
            String violationMessage) {
        PropertyConstraintViolation violation = new PropertyConstraintViolation();
        violation.setInvalidValue(invalidValue);
        violation.setMessage(violationMessage);
        violation.setPropertyName(propertyName);

        return violation;
    }

}
