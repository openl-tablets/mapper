package org.dozer.fieldmap;

import org.dozer.classmap.ClassMap;
import org.dozer.classmap.MappingDirection;
import org.dozer.propertydescriptor.DozerPropertyDescriptor;
import org.dozer.propertydescriptor.PropertyDescriptorFactory;
import org.dozer.util.MappingUtils;

public class EmptySourceFieldMap extends FieldMap {

    public EmptySourceFieldMap(ClassMap classMap) {
        super(classMap);
    }

    @Override
    public void validate() {
        if (getDestField() == null) {
            MappingUtils.throwMappingException("dest field must be specified");
        }
    }

    @Override
    public DozerPropertyDescriptor getSrcPropertyDescriptor(Class<?> runtimeSrcClass) {
        DozerPropertyDescriptor result = getSrcPropertyDescriptorMap().get(runtimeSrcClass);
        if (result == null) {
            DozerPropertyDescriptor descriptor = PropertyDescriptorFactory.getPropertyDescriptor(runtimeSrcClass,
                getClassMap(),
                getSrcField(),
                getDestField());
            getSrcPropertyDescriptorMap().putIfAbsent(runtimeSrcClass, descriptor);
            result = descriptor;
        }
        return result;
    }

    @Override
    public String getSrcFieldName() {
        return null;
    }

    @Override
    public MappingDirection getType() {
        return MappingDirection.ONE_WAY;
    }

}
