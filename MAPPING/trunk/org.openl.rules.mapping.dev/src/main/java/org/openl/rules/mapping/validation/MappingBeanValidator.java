package org.openl.rules.mapping.validation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.dozer.MappingParameters;
import org.dozer.util.MappingUtils;
import org.openl.rules.mapping.Mapping;
import org.openl.rules.mapping.OpenLReflectionUtils;
import org.openl.rules.mapping.TypeResolver;
import org.openl.rules.mapping.loader.MappingDefinitionUtils;
import org.openl.types.IOpenClass;
import org.openl.types.IOpenMethod;
import org.openl.validation.ValidationStatus;

public class MappingBeanValidator extends OpenLDataBeanValidator<Mapping> {

    @Override
    public BeanValidationResult validateBean(Mapping beanToValidate, IOpenClass openClass) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();
        Mapping bean = MappingDefinitionUtils.normalizeMapping(beanToValidate);

        Set<ConstraintViolation> fieldPathViolations = validateFieldPaths(bean);
        violations.addAll(fieldPathViolations);
        
        if (fieldPathViolations.isEmpty()) {
            violations.addAll(validateConverterMethods(bean, openClass));
            violations.addAll(validateConditionMethods(bean, openClass));
        }

        if (violations.isEmpty()) {
            return new BeanValidationResult(ValidationStatus.SUCCESS, bean.getClass());
        }

        BeanValidationResult result = new BeanValidationResult(ValidationStatus.FAIL, bean.getClass());
        result.addAllConstraintViolation(violations);

        return result;
    }

    private Set<ConstraintViolation> validateConverterMethods(Mapping bean, IOpenClass openClass) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        Class<?> fieldAType = getFieldAType(bean);
        Class<?> fieldBType = getFieldBType(bean);

        violations.addAll(validateConvertMethod("convertMethodAB", bean.getConvertMethodAB(), openClass, fieldAType,
            fieldBType));
        violations.addAll(validateConvertMethod("convertMethodBA", bean.getConvertMethodBA(), openClass, fieldBType,
            fieldAType));

        return violations;
    }

    private Set<ConstraintViolation> validateConditionMethods(Mapping bean, IOpenClass openClass) {
        Set<ConstraintViolation> violations = new HashSet<ConstraintViolation>();

        Class<?> fieldAType = getFieldAType(bean);
        Class<?> fieldBType = getFieldBType(bean);

        violations.addAll(validateConditionMethod("conditionAB", bean.getConditionAB(), openClass, fieldAType,
            fieldBType));
        violations.addAll(validateConditionMethod("conditionBA", bean.getConditionBA(), openClass, fieldBType,
            fieldAType));

        return violations;
    }

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

            FieldPathHierarchyElement[] hierarchy = ValidationUtils.getFieldHierarchy(bean.getClassA(), fieldA[0],
                fieldTypeHint);
            FieldPathHierarchyElement lastElement = hierarchy[hierarchy.length - 1];
            fieldAType = lastElement.getType();
            
            if (StringUtils.isNotBlank(lastElement.getIndex()) && MappingUtils.isSupportedCollection(lastElement.getType())) {
                fieldAType = MappingUtils.getSupportedCollectionEntryType(lastElement.getType());
            }
        }

        return fieldAType;
    }

    private Class<?> getFieldBType(Mapping bean) {
        String fieldB = bean.getFieldB();
        Class<?> fieldBType = null;
        Class<?>[] fieldBTypeHint = bean.getFieldBHint();

        FieldPathHierarchyElement[] hierarchy = ValidationUtils.getFieldHierarchy(bean.getClassB(), fieldB, fieldBTypeHint);
        FieldPathHierarchyElement lastElement = hierarchy[hierarchy.length - 1];
        fieldBType = lastElement.getType();
        
        if (StringUtils.isNotBlank(lastElement.getIndex()) && MappingUtils.isSupportedCollection(lastElement.getType())) {
            fieldBType = MappingUtils.getSupportedCollectionEntryType(lastElement.getType());
        }

        return fieldBType;
    }

    // TODO: remove code duplication
    private Set<ConstraintViolation> validateConvertMethod(String propertyName, String convertMethod,
        IOpenClass openClass, Class<?> fieldA, Class<?> fieldB) {

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

        if (convertMethodClass != null) {
            Method simpleMethod = findMethod(convertMethodClass, convertMethodName, new Class<?>[] { fieldA, fieldB });
            Method extendedMethod = findMethod(convertMethodClass, convertMethodName, new Class<?>[] {
                    MappingParameters.class, fieldA, fieldB });

            if (simpleMethod == null && extendedMethod == null) {
                violations.add(createPropertyViolation(propertyName, convertMethod, String.format(
                    "Convert method '%1$s(%2$s, %3$s)' or '%1$s(%4$s, %2$s, %3$s)' cannot be found in class '%5$s'",
                    convertMethod, fieldA.getName(), fieldB.getName(), MappingParameters.class.getName(),
                    convertMethodClass.getName())));
            }

            if (simpleMethod != null) {
                Class<?> returnType = simpleMethod.getReturnType();
                if (!OpenLReflectionUtils.isAssignableFrom(fieldB, returnType)) {
                    violations.add(createPropertyViolation(propertyName, convertMethod, String.format(
                        "Destination field of type '%s' cannot be assigned from value of '%s' type", fieldB.getName(),
                        returnType)));
                }
            }

            if (extendedMethod != null) {
                Class<?> returnType = extendedMethod.getReturnType();
                if (!OpenLReflectionUtils.isAssignableFrom(fieldB, returnType)) {
                    violations.add(createPropertyViolation(propertyName, convertMethod, String.format(
                        "Destination field of type '%s' cannot be assigned from value of '%s' type", fieldB.getName(),
                        returnType)));
                }
            }
        } else {
            IOpenMethod simpleMethod = findMethod(openClass, convertMethodName, new Class<?>[] { fieldA, fieldB });
            IOpenMethod extendedMethod = findMethod(openClass, convertMethodName, new Class<?>[] {
                    MappingParameters.class, fieldA, fieldB });

            if (simpleMethod == null && extendedMethod == null) {
                violations.add(createPropertyViolation(propertyName, convertMethod, String.format(
                    "Convert method '%1$s(%2$s, %3$s)' or '%1$s(%4$s, %2$s, %3$s)' cannot be found in class '%5$s'",
                    convertMethod, fieldA.getName(), fieldB.getName(), MappingParameters.class.getName(),
                    openClass.getName())));
            }

            if (simpleMethod != null) {
                IOpenClass returnType = simpleMethod.getType();
                if (returnType == null || !OpenLReflectionUtils.isAssignableFrom(fieldB, returnType.getInstanceClass())) {
                    violations.add(createPropertyViolation(propertyName, convertMethod, String.format(
                        "Destination field of type '%s' cannot be assigned from value of '%s' type", fieldB.getName(),
                        returnType.getInstanceClass().getName())));
                }
            }

            if (extendedMethod != null) {
                IOpenClass returnType = simpleMethod.getType();
                if (returnType == null || !OpenLReflectionUtils.isAssignableFrom(fieldB, returnType.getInstanceClass())) {
                    violations.add(createPropertyViolation(propertyName, convertMethod, String.format(
                        "Destination field of type '%s' cannot be assigned from value of '%s' type", fieldB.getName(),
                        returnType.getInstanceClass().getName())));
                }
            }
        }

        return violations;
    }

    private Set<ConstraintViolation> validateConditionMethod(String propertyName, String conditionMethod,
        IOpenClass openClass, Class<?> fieldA, Class<?> fieldB) {

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

        if (conditionMethodClass != null) {
            Method simpleMethod = findMethod(conditionMethodClass, conditionMethodName,
                new Class<?>[] { fieldA, fieldB });
            Method extendedMethod = findMethod(conditionMethodClass, conditionMethodName, new Class<?>[] {
                    MappingParameters.class, fieldA, fieldB });

            if (simpleMethod == null && extendedMethod == null) {
                violations.add(createPropertyViolation(propertyName, conditionMethod, String.format(
                    "Convert method '%1$s(%2$s, %3$s)' or '%1$s(%4$s, %2$s, %3$s)' cannot be found in class '%5$s'",
                    conditionMethod, fieldA.getName(), fieldB.getName(), MappingParameters.class.getName(),
                    conditionMethodClass.getName())));
            }

            if (simpleMethod != null) {
                Class<?> returnType = simpleMethod.getReturnType();
                if (boolean.class != returnType && !Boolean.class.equals(returnType)) {
                    violations.add(createPropertyViolation(propertyName, conditionMethod,
                        "Condition method have to return value of boolean type"));
                }
            }

            if (extendedMethod != null) {
                Class<?> returnType = extendedMethod.getReturnType();
                if (boolean.class != returnType && !Boolean.class.equals(returnType)) {
                    violations.add(createPropertyViolation(propertyName, conditionMethod,
                        "Condition method have to return value of boolean type"));
                }
            }
        } else {
            IOpenMethod simpleMethod = findMethod(openClass, conditionMethodName, new Class<?>[] { fieldA, fieldB });
            IOpenMethod extendedMethod = findMethod(openClass, conditionMethodName, new Class<?>[] {
                    MappingParameters.class, fieldA, fieldB });

            if (simpleMethod == null && extendedMethod == null) {
                violations.add(createPropertyViolation(propertyName, conditionMethod, String.format(
                    "Convert method '%1$s(%2$s, %3$s)' or '%1$s(%4$s, %2$s, %3$s)' cannot be found in class '%5$s'",
                    conditionMethod, fieldA.getName(), fieldB.getName(), MappingParameters.class.getName(),
                    openClass.getName())));
            }

            if (simpleMethod != null) {
                IOpenClass returnType = simpleMethod.getType();
                if (boolean.class != returnType.getInstanceClass() && !Boolean.class.equals(returnType
                    .getInstanceClass())) {
                    violations.add(createPropertyViolation(propertyName, conditionMethod,
                        "Condition method have to return value of boolean type"));

                }
            }

            if (extendedMethod != null) {
                IOpenClass returnType = simpleMethod.getType();
                if (boolean.class != returnType.getInstanceClass() && !Boolean.class.equals(returnType
                    .getInstanceClass())) {
                    violations.add(createPropertyViolation(propertyName, conditionMethod,
                        "Condition method have to return value of boolean type"));

                }
            }
        }

        return violations;

    }

    private Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        return ValidationUtils.findMatchingAccessibleMethod(clazz, methodName, paramTypes);
    }

    private IOpenMethod findMethod(IOpenClass clazz, String methodName, Class<?>[] paramTypes) {
        return ValidationUtils.findMatchingAccessibleMethod(clazz, methodName, paramTypes);
    }

    // TODO: remove code duplication
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
                        message = String.format("Exception occurred determining field hierarchy for Class --> %s, Field --> %s", classAType == null ? null : classAType.getName() , field);
                    }
                    
                    violations.add(createPropertyViolation("fieldA", field, message));
                }

                if (hierarchy != null) {
                    List<FieldPathHierarchyElement> hierarchyElementList = Arrays.asList(hierarchy);

                    @SuppressWarnings("unchecked")
                    Collection<FieldPathHierarchyElement> invalidElements = CollectionUtils.select(
                        hierarchyElementList, new Predicate() {
                            public boolean evaluate(Object arg) {
                                FieldPathHierarchyElement e = (FieldPathHierarchyElement) arg;
                                String index = e.getIndex();

                                return StringUtils.isNotBlank(index) && ValidationUtils.isSimpleCollectionIndex(index) && ValidationUtils
                                    .getCollectionIndex(index) == -1;
                            }
                        });

                    if (!invalidElements.isEmpty()) {
                        for (FieldPathHierarchyElement e : invalidElements) {
                            violations.add(createPropertyViolation("fieldA", field,
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
                message = String.format("Exception occurred determining field hierarchy for Class --> %s, Field --> %s", classBType == null ? null : classBType.getName() , fieldB);
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
                    violations.add(createPropertyViolation("fieldB", fieldB,
                        String.format("Index value '%s' cannot be used for destination field", e.getIndex())));
                }
            }
        }

        return violations;
    }

    private PropertyConstraintViolation createPropertyViolation(String propertyName, Object invalidValue,
        String violationMessage) {
        PropertyConstraintViolation violation = new PropertyConstraintViolation();
        violation.setInvalidValue(invalidValue);
        violation.setMessage(violationMessage);
        violation.setPropertyName(propertyName);

        return violation;
    }

}
