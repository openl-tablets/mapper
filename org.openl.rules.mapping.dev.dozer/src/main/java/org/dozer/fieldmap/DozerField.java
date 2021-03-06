/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dozer.fieldmap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dozer.util.DozerConstants;

/**
 * Only intended for internal use.
 *
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * @author dmitry.buzdin
 */
public class DozerField implements Cloneable {

    private String type;
    private String name;
    private String dateFormat;
    private String theGetMethod;
    private String theSetMethod;
    private String key;
    private String mapSetMethod;
    private String mapGetMethod;
    private Boolean accessible = Boolean.FALSE;
    private String createMethod;
    private boolean indexed;
    private String index;
    private boolean required;
    private String defaultValue;
    private HintContainer hintContainer;
    private HintContainer deepIndexHintContainer;

    public DozerField(String name, String type) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getTheGetMethod() {
        return theGetMethod;
    }

    public void setTheGetMethod(String theGetMethod) {
        this.theGetMethod = theGetMethod;
    }

    public String getTheSetMethod() {
        return theSetMethod;
    }

    public void setTheSetMethod(String theSetMethod) {
        this.theSetMethod = theSetMethod;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMapGetMethod() {
        return mapGetMethod;
    }

    public void setMapGetMethod(String mapGetMethod) {
        this.mapGetMethod = mapGetMethod;
    }

    public String getMapSetMethod() {
        return mapSetMethod;
    }

    public void setMapSetMethod(String mapSetMethod) {
        this.mapSetMethod = mapSetMethod;
    }

    public Boolean isAccessible() {
        if (accessible != null) {
            return accessible;
        }
        return Boolean.FALSE;
    }

    public void setAccessible(Boolean isAccessible) {
        this.accessible = isAccessible;
    }

    public String getCreateMethod() {
        return createMethod;
    }

    public void setCreateMethod(String createMethod) {
        this.createMethod = createMethod;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean isIndexed) {
        this.indexed = isIndexed;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public HintContainer getHintContainer() {
        return hintContainer;
    }

    public void setHintContainer(HintContainer hintContainer) {
        this.hintContainer = hintContainer;
    }

    public HintContainer getDeepIndexHintContainer() {
        return deepIndexHintContainer;
    }

    public void setDeepIndexHintContainer(HintContainer deepIndexHintContainer) {
        this.deepIndexHintContainer = deepIndexHintContainer;
    }

    public boolean isCustomGetterSetterField() {
        return getTheGetMethod() != null || getTheSetMethod() != null;
    }

    public boolean isMapTypeCustomGetterSetterField() {
        return getMapGetMethod() != null || getMapSetMethod() != null;
    }

    public boolean isSelfReferenced() {
        return getName().equals(DozerConstants.SELF_KEYWORD);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public DozerField copyOf() {
        DozerField copy = new DozerField(name, type);
        copy.setDateFormat(dateFormat);
        copy.setTheGetMethod(theGetMethod);
        copy.setTheSetMethod(theSetMethod);
        copy.setKey(key);
        copy.setMapSetMethod(mapSetMethod);
        copy.setMapGetMethod(mapGetMethod);
        copy.setAccessible(accessible);
        copy.setCreateMethod(createMethod);
        copy.setIndexed(indexed);
        copy.setIndex(index);
        return copy;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}