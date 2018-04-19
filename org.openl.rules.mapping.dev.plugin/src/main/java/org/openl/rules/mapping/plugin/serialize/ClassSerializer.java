package org.openl.rules.mapping.plugin.serialize;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.util.CollectionUtils;
import org.dozer.util.ReflectionUtils;

/**
 * Serializes {@link Class} objects into internal domain model.
 * 
 * Intended for internal use.
 */
public class ClassSerializer {

    private static final Log LOG = LogFactory.getLog(ClassSerializer.class); 
   
    private static final String[] ignoredProperties = new String [] {"class"};
    
    public static BeanEntry serialize(Class<?> clazz, boolean quietReflectionErrors) {
        if (clazz == null) {
            return null;
        }

        BeanEntry bean = new BeanEntry();
        bean.setName(clazz.getName());
        bean.setExtendedType(getSuperclass(clazz));

        PropertyDescriptor[] propDescriptors = null;
        try {
            propDescriptors = ReflectionUtils.getPropertyDescriptors(clazz);
        } catch (Throwable e) {
            if (!quietReflectionErrors) {
                LOG.error(String.format("An error has occured while loading properties of class '%s'", clazz.getName()), e);
            }
        }
        
        if (propDescriptors == null) {
            return bean;
        }
        
        List<FieldEntry> fields = new ArrayList<FieldEntry>(propDescriptors.length);

        for (PropertyDescriptor propDescriptor : propDescriptors) {
            if (!isIgnoredProperty(propDescriptor.getName())) {
                try {
                    Class<?> propertyType = propDescriptor.getPropertyType();
                    
                    if (propertyType != null) {
                        FieldEntry field = new FieldEntry();
                        field.setName(propDescriptor.getName());
                        field.setType(propertyType);

                        boolean isCollection = CollectionUtils.isCollection(propertyType);
                        boolean isArray = CollectionUtils.isArray(propertyType);
                        if (isCollection || isArray) {
                            field.setCollectionType(isArray ? CollectionType.ARRAY : CollectionType.COLLECTION);
                            field.setCollectionItemType(ReflectionUtils.getComponentType(propertyType,
                                propDescriptor,
                                Object.class));
                        }

                        fields.add(field);
                    }
                } catch (Throwable e) {
                    if (!quietReflectionErrors) {
                        LOG.error(String.format("An error has occurred while processing property '%s' of class %s", propDescriptor.getName(), clazz.getName()), e);
                    }
                }
            }
        }

        bean.setFields(fields);

        return bean;
    }

    public static List<BeanEntry> serialize(Collection<Class<?>> classes, boolean quietReflectionErrors) {
        List<BeanEntry> beans = new ArrayList<BeanEntry>(classes.size());

        for (Class<?> clazz : classes) {
            beans.add(serialize(clazz, quietReflectionErrors));
        }

        return beans;
    }
    
    private static Class<?> getSuperclass(Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        if (Object.class == superclass) {
            return null;
        }
        
        return superclass;
    }
    
    private static boolean isIgnoredProperty(String propName) {
        for (String ignoredProperty : ignoredProperties) {
            if (ignoredProperty.equals(propName)) {
                return true;
            }
        }
        
        return false;
    }
}
