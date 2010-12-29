package org.dozer.fieldmap;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dozer.classmap.ClassMap;
import org.dozer.propertydescriptor.DozerPropertyDescriptor;
import org.dozer.propertydescriptor.PropertyDescriptorFactory;
import org.dozer.util.MappingUtils;

/**
 * The custom extension of excluded field map which used by engine to support
 * multi-source field map exclusions.
 */
public class MultiFieldsExcludeFieldMap extends ExcludeFieldMap {

    private List<DozerField> src;
    private List<DozerField> dest;

    public MultiFieldsExcludeFieldMap(ClassMap classMap) {
        super(classMap);
    }

    public List<DozerField> getSrc() {
        return src;
    }

    public void setSrc(List<DozerField> srcField) {
        this.src = srcField;
    }

    public List<DozerField> getDest() {
        return dest;
    }

    public void setDest(List<DozerField> dest) {
        this.dest = dest;
    }

    @Override
    public String getDateFormat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HintContainer getDestDeepIndexHintContainer() {
        throw new UnsupportedOperationException();
    }

    public List<DozerField> getDestCopy() {
        return FieldMapUtils.getCopy(dest);
    }

    @Override
    public String getDestFieldCreateMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDestFieldDefaultValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDestFieldIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDestFieldKey() {
        return FieldMapUtils.getFieldKey(dest);
    }

    @Override
    public String getDestFieldMapGetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDestFieldMapSetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDestFieldName() {
        return FieldMapUtils.getFieldName(dest);
    }

    @Override
    public String getDestFieldTheGetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDestFieldTheSetMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDestFieldType() {
        throw new UnsupportedOperationException();
    }

    public Class<?> getSrcFieldType(Class<?> runtimeSrcClass) {
        return getSrcPropertyDescriptor(runtimeSrcClass).getPropertyType();
    }

    @Override
    public Class<?> getDestFieldType(Class<?> runtimeDestClass) {
        return getSrcPropertyDescriptor(runtimeDestClass).getPropertyType();
    }

    @Override
    public Method getDestFieldWriteMethod(Class<?> runtimeDestClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HintContainer getDestHintContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getDestHintType(Class<?> runtimeSrcClass) {
        throw new UnsupportedOperationException();
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
    protected DozerPropertyDescriptor getDestPropertyDescriptor(Class<?> runtimeDestClass) {
        DozerPropertyDescriptor result = getDestPropertyDescriptorMap().get(runtimeDestClass);
        if (result == null) {
            DozerPropertyDescriptor descriptor = PropertyDescriptorFactory.getPropertyDescriptor(runtimeDestClass,
                getClassMap(), getDest());
            getDestPropertyDescriptorMap().putIfAbsent(runtimeDestClass, descriptor);
            result = descriptor;
        }
        return result;
    }

    @Override
    public Object getDestValue(Object runtimeDestObj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getGenericType(Class<?> runtimeDestClass) {
        throw new UnsupportedOperationException();
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
    public String getSrcFieldName() {
        // TODO Auto-generated method stub
        return super.getSrcFieldName();
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
    public Object getSrcFieldValue(Object runtimeSrcObj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public HintContainer getSrcHintContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDestFieldAccessible() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDestFieldIndexed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDestFieldRequired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDestMapEmptyString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDestMapNull() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean isDestSelfReferencing() {
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
    public void setDestDeepIndexHintContainer(HintContainer destDeepIndexHintHint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDestField(DozerField destField) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDestHintContainer(HintContainer destHint) {
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
            "destination field", dest).append("type", getType()).append("customConverter", getCustomConverter())
            .append("relationshipType", getRelationshipType()).append("removeOrphans", isRemoveOrphans()).append(
                "mapId", getMapId()).append("copyByReference", isCopyByReference()).append("copyByReferenceOveridden",
                isCopyByReferenceOveridden()).append("mapCondition", getMapCondition()).append("mapConditionId",
                getMapConditionId()).toString();
    }

    @Override
    public void validate() {
        if (src == null) {
            MappingUtils.throwMappingException("src field must be specified");
        }
        if (dest == null) {
            MappingUtils.throwMappingException("dest field must be specified");
        }
    }

    @Override
    public void writeDestValue(Object runtimeDestObj, Object destFieldValue) {
        throw new UnsupportedOperationException();
    }

}
