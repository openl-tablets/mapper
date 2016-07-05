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

package org.dozer.loader.api;

import org.dozer.loader.DozerBuilder;
import org.dozer.util.ReflectionUtils;

/**
 * @author Dmitry Buzdin
 */
public class FieldDefinition {

    private String value;

    private Boolean accessible;
    private String createMethod;
    private String key;
    private String mapGetMethod;
    private String mapSetMethod;
    private String getMethod;
    private String setMethod;
    private boolean required;
    private String defaultValue;
    private String dateFormat;

    private String hint;
    private String deepHint;
    private boolean iterate;

    public FieldDefinition(String value) {
        this.value = value;
    }

    public void build(DozerBuilder.FieldDefinitionBuilder builder) {
        builder.accessible(this.accessible);
        builder.createMethod(this.createMethod);

        builder.key(this.key);
        builder.mapGetMethod(this.mapGetMethod);
        builder.mapSetMethod(this.mapSetMethod);

        builder.theGetMethod(this.getMethod);
        builder.theSetMethod(this.setMethod);

        builder.required(this.required);
        builder.defaultValue(this.defaultValue);

        builder.hint(this.hint);
        builder.deepHint(this.deepHint);
        builder.dateFormat(this.dateFormat);

        if (this.iterate) {
            builder.iterate();
        }
    }

    public FieldDefinition iterate() {
        this.iterate = true;
        return this;
    }

    public FieldDefinition accessible() {
        return accessible(true);
    }

    public FieldDefinition dateFormat(String value) {
        this.dateFormat = value;
        return this;
    }

    public FieldDefinition defaultValue(String value) {
        this.defaultValue = value;
        return this;
    }

    public FieldDefinition required(boolean value) {
        this.required = value;
        return this;
    }

    public FieldDefinition accessible(boolean value) {
        this.accessible = value;
        return this;
    }

    public FieldDefinition createMethod(String method) {
        this.createMethod = method;
        return this;
    }

    public FieldDefinition mapKey(String key) {
        this.key = key;
        return this;
    }

    public FieldDefinition mapMethods(String getMethod, String setMethod) {
        this.mapGetMethod = getMethod;
        this.mapSetMethod = setMethod;
        return this;
    }

    public FieldDefinition getMethod(String getMethod) {
        this.getMethod = getMethod;
        return this;
    }

    public FieldDefinition setMethod(String setMethod) {
        this.setMethod = setMethod;
        return this;
    }

    public FieldDefinition hint(final Class<?>... types) {
        this.hint = ReflectionUtils.mergeTypeNames(types);
        return this;
    }

    public FieldDefinition deepHint(final Class<?>... types) {
        this.deepHint = ReflectionUtils.mergeTypeNames(types);
        return this;
    }

    public FieldDefinition hint(String types) {
        this.hint = types;
        return this;
    }

    public FieldDefinition deepHint(String types) {
        this.deepHint = types;
        return this;
    }

    String resolve() {
        return value;
    }

}
