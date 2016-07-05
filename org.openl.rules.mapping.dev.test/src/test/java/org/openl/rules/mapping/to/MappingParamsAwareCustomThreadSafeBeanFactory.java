package org.openl.rules.mapping.to;

import org.openl.rules.mapping.MappingParameters;

public class MappingParamsAwareCustomThreadSafeBeanFactory extends MappingParamsAwareCustomBeanFactory {

    @Override
    public void setMappingParams(MappingParameters params) {
        super.setMappingParams(params);
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
