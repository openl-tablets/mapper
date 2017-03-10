package org.openl.rules.mapping;

import java.util.Collection;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.openl.classloader.SimpleBundleClassLoader;
import org.openl.conf.IOpenLConfiguration;
import org.openl.conf.IUserContext;
import org.openl.conf.OpenLConfiguration;
import org.openl.conf.UserContext;
import org.openl.types.IOpenClass;
import org.openl.types.IOpenMethod;
import org.openl.types.java.OpenClassHelper;

public class OpenLReflectionUtils {

    private static final String DEFAULT_OPENL_CONFIGURATION_PREFIX = "org.openl.rules.java::";
    private static final String DEFAULT_USER_HOME = ".";

    private OpenLReflectionUtils() {
    }

    public static TypeResolver getTypeResolver(IOpenClass openClass) {
        if (openClass.getMetaInfo() != null && StringUtils.isNotBlank(openClass.getMetaInfo().getSourceUrl())) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            UserContext userContext = new UserContext(contextClassLoader, DEFAULT_USER_HOME);
            TypeResolver typeResolver = getTypeResolver(openClass.getMetaInfo().getSourceUrl(), userContext);
            if (typeResolver == null && contextClassLoader instanceof SimpleBundleClassLoader){
                SimpleBundleClassLoader simpleBundleClassLoader = (SimpleBundleClassLoader) contextClassLoader;
                userContext = new UserContext(simpleBundleClassLoader.getParent(), DEFAULT_USER_HOME);
                return getTypeResolver(openClass.getMetaInfo().getSourceUrl(), userContext);
            }
        }

        return null;
    }

    public static TypeResolver getTypeResolver(String sourceURL, IUserContext userContext) {
        String configName = DEFAULT_OPENL_CONFIGURATION_PREFIX + sourceURL;
        IOpenLConfiguration config = OpenLConfiguration.getInstance(configName, userContext);

        if (config != null) {
            return new RulesTypeResolver(config);
        }

        return null;
    }

    public static IOpenMethod findMatchingAccessibleMethod(IOpenClass clazz, String methodName, Class<?>[] parameterTypes) {

        IOpenClass[] openLParameterTypes = OpenClassHelper.getOpenClasses(clazz, parameterTypes);
        IOpenMethod method = clazz.getMatchingMethod(methodName, openLParameterTypes);

        if (method != null) {
            return method;
        }
        
        // search through all methods
        IOpenMethod bestMatch = null;
        Class<?>[] bestMatchParameterTypes = null;
        
        Collection<IOpenMethod> methods = clazz.getMethods();
        
        for (IOpenMethod m : methods) {
            if (m.getName().equals(methodName)) {
                // compare parameters
                IOpenClass[] openClasses = m.getSignature().getParameterTypes();
                Class<?>[] methodParameterTypes = OpenClassHelper.getInstanceClasses(openClasses);

                if (ClassUtils.isAssignable(parameterTypes, methodParameterTypes, true)) {
                    if (bestMatch == null || MemberUtils.compareParameterTypes(methodParameterTypes,
                        bestMatchParameterTypes, parameterTypes) < 0) {
                        bestMatch = m;
                        bestMatchParameterTypes = parameterTypes;
                    }
                }
            }
        }

        return bestMatch;
    }

    public static boolean isAssignableFrom(Class<?> classToCheck, Class<?> fromClass) {
        return ClassUtils.isAssignable(classToCheck, fromClass, true);
    }
    
    /*
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements.  See the NOTICE file distributed with
     * this work for additional information regarding copyright ownership.
     * The ASF licenses this file to You under the Apache License, Version 2.0
     * (the "License"); you may not use this file except in compliance with
     * the License.  You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    /**
     * Contains common code for working with Methods/Constructors, extracted and
     * refactored from <code>MethodUtils</code> when it was imported from Commons
     * BeanUtils.
     *
     * @author Apache Software Foundation
     * @author Steve Cohen
     * @author Matt Benson
     * @since 2.5
     * @version $Id: MemberUtils.java 1057013 2011-01-09 20:04:16Z niallp $
     */
    static class MemberUtils {

        /** Array of primitive number types ordered by "promotability" */
        private static final Class<?>[] ORDERED_PRIMITIVE_TYPES = { Byte.TYPE, Short.TYPE,
                Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE };

        /**
         * Compare the relative fitness of two sets of parameter types in terms of
         * matching a third set of runtime parameter types, such that a list ordered
         * by the results of the comparison would return the best match first
         * (least).
         *
         * @param left the "left" parameter set
         * @param right the "right" parameter set
         * @param actual the runtime parameter types to match against
         * <code>left</code>/<code>right</code>
         * @return int consistent with <code>compare</code> semantics
         */
        public static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
            float leftCost = getTotalTransformationCost(actual, left);
            float rightCost = getTotalTransformationCost(actual, right);
            return leftCost < rightCost ? -1 : rightCost < leftCost ? 1 : 0;
        }

        /**
         * Returns the sum of the object transformation cost for each class in the
         * source argument list.
         * @param srcArgs The source arguments
         * @param destArgs The destination arguments
         * @return The total transformation cost
         */
        private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
            float totalCost = 0.0f;
            for (int i = 0; i < srcArgs.length; i++) {
                Class<?> srcClass = srcArgs[i];
                Class<?> destClass = destArgs[i];
                totalCost += getObjectTransformationCost(srcClass, destClass);
            }
            return totalCost;
        }

        /**
         * Gets the number of steps required needed to turn the source class into
         * the destination class. This represents the number of steps in the object
         * hierarchy graph.
         * @param srcClass The source class
         * @param destClass The destination class
         * @return The cost of transforming an object
         */
        private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
            if (destClass.isPrimitive()) {
                return getPrimitivePromotionCost(srcClass, destClass);
            }
            float cost = 0.0f;
            while (srcClass != null && !destClass.equals(srcClass)) {
                if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
                    // slight penalty for interface match.
                    // we still want an exact match to override an interface match,
                    // but
                    // an interface match should override anything where we have to
                    // get a superclass.
                    cost += 0.25f;
                    break;
                }
                cost++;
                srcClass = srcClass.getSuperclass();
            }
            /*
             * If the destination class is null, we've travelled all the way up to
             * an Object match. We'll penalize this by adding 1.5 to the cost.
             */
            if (srcClass == null) {
                cost += 1.5f;
            }
            return cost;
        }

        /**
         * Get the number of steps required to promote a primitive number to another
         * type.
         * @param srcClass the (primitive) source class
         * @param destClass the (primitive) destination class
         * @return The cost of promoting the primitive
         */
        private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
            float cost = 0.0f;
            Class<?> cls = srcClass;
            if (!cls.isPrimitive()) {
                // slight unwrapping penalty
                cost += 0.1f;
                cls = ClassUtils.wrapperToPrimitive(cls);
            }
            for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
                if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                    cost += 0.1f;
                    if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                        cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                    }
                }
            }
            return cost;
        }

    }
 
}