package org.dozer.fieldmap;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dozer.classmap.ClassMap;
import org.dozer.propertydescriptor.DozerPropertyDescriptor;
import org.dozer.propertydescriptor.PropertyDescriptorFactory;
import org.dozer.util.MappingUtils;

/**
 * Field map extension which allows to define n to 1 field mappings.
 * Restriction: 1) user have to provide custom converter which should convert
 * multiple values into one value; 2) this type of field map cannot be
 * bi-directional.
 */
public class MultiSourceFieldMap extends FieldMap {

    public MultiSourceFieldMap(ClassMap classMap) {
        super(classMap);
    }

    private List<DozerField> src;

    public List<DozerField> getSrc() {
        return src;
    }

    public void setSrc(List<DozerField> srcField) {
        this.src = srcField;
    }

    @Override
    public void validate() {
        if (src == null) {
            MappingUtils.throwMappingException("src field must be specified");
        }
        if (getDestField() == null) {
            MappingUtils.throwMappingException("dest field must be specified");
        }
    }

    @Override
    protected DozerPropertyDescriptor getSrcPropertyDescriptor(Class<?> runtimeSrcClass) {
        DozerPropertyDescriptor result = getSrcPropertyDescriptorMap().get(runtimeSrcClass);
        if (result == null) {
            DozerPropertyDescriptor descriptor = PropertyDescriptorFactory.getPropertyDescriptor(runtimeSrcClass,
                getClassMap(), getSrc());
            getSrcPropertyDescriptorMap().putIfAbsent(runtimeSrcClass, descriptor);
            result = descriptor;
        }
        return result;
    }

    @Override
    public String getSrcFieldName() {
        return FieldMapUtils.getFieldName(src);
    }

    @Override
    public String getDateFormat() {
        return FieldMapUtils.getDateFormat(getDestField(), getClassMap());
    }

    @Override
    public HintContainer getSrcDeepIndexHintContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected DozerField getSrcField() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DozerField getSrcFieldCopy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSrcFieldCreateMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSrcFieldIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSrcFieldKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSrcFieldMapGetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSrcFieldMapSetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSrcFieldTheGetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSrcFieldTheSetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSrcFieldType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HintContainer getSrcHintContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSrcFieldAccessible() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSrcFieldIndexed() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean isSrcSelfReferencing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSrcDeepIndexHintContainer(HintContainer srcDeepIndexHint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSrcField(DozerField sourceField) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSrcHintContainer(HintContainer sourceHint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("source field", src).append(
            "destination field", getDestField()).append("type", getType()).append("customConverter",
            getCustomConverter()).append("relationshipType", getRelationshipType()).append("removeOrphans",
            isRemoveOrphans()).append("mapId", getMapId()).append("copyByReference", isCopyByReference()).append(
            "copyByReferenceOveridden", isCopyByReferenceOveridden()).append("srcTypeHint", getSrcHintContainer())
            .append("destTypeHint", getDestHintContainer()).append("mapCondition", getMapCondition()).append(
                "mapConditionId", getMapConditionId()).toString();
    }

}
