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
package org.dozer.propertydescriptor;

import java.beans.PropertyDescriptor;

public class DeepHierarchyElement {
    private PropertyDescriptor propDescriptor;
    private String index;
    private Class<?> hintType;

    public DeepHierarchyElement(PropertyDescriptor propDescriptor, String index, Class<?> hintType) {
        this.propDescriptor = propDescriptor;
        this.index = index;
        this.hintType = hintType;
    }

    public String getIndex() {
        return index;
    }

    public PropertyDescriptor getPropDescriptor() {
        return propDescriptor;
    }

    public Class<?> getHintType() {
        return hintType;
    }

}