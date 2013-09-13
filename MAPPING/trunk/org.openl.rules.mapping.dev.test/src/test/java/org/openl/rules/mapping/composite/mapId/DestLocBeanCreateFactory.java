package org.openl.rules.mapping.composite.mapId;

import org.dozer.*;
import org.openl.rules.mapping.RulesBeanMapper;
import org.openl.rules.mapping.composite.mapId.destination.DestLocDTO;
import org.openl.rules.mapping.composite.mapId.source.SrcLocation;

/**
 * @author kkachanovskiy@exigenservices.com
 */
public class DestLocBeanCreateFactory implements BeanFactory, MappingParamsAware {
    MappingParameters params;

    public Object createBean(Object source, Class<?> sourceClass, String targetBeanId) {
        Object result = null;
/*
        if (source instanceof SrcLocation) {
            MappingContext context = (MappingContext) params.get("context");
            SrcLocation srcLocation = (SrcLocation) source;
            context.setMapId(srcLocation.getStateCode());

            result = new DestLocDTO();
        } else {
*/
            try {
                Class clazz = Class.forName(targetBeanId);
                result = clazz.newInstance();
            } catch (Exception e) {
                throw new MappingException("Failed to intantiate: " + e.getMessage());
            }
//        }

        return result;
    }

    public void setMappingParams(MappingParameters params) {
        this.params = params;
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
