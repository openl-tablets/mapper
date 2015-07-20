package org.dozer.converters;

import org.dozer.CustomConverter;

public class InstanceCustomConverterDescription extends CustomConverterDescription {

    private CustomConverter instance;

    public CustomConverter getInstance() {
        return instance;
    }

    public void setInstance(CustomConverter instance) {
        this.instance = instance;
    }

}
