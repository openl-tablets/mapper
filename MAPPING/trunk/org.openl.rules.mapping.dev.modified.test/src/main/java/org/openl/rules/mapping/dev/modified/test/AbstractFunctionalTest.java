package org.openl.rules.mapping.dev.modified.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.util.DozerConstants;
import org.junit.Before;

public abstract class AbstractFunctionalTest {

    protected Mapper mapper;

    @Before
    public void setUp() throws Exception {
        System.setProperty("log4j.debug", "true");
        System.setProperty(DozerConstants.DEBUG_SYS_PROP, "true");
        mapper = new DozerBeanMapper();
    }

    protected Mapper getMapper(String... mappingFiles) {
        List<String> list = new ArrayList<String>();
        
        if (mappingFiles != null) {
            list.addAll(Arrays.asList(mappingFiles));
        }
     
        Mapper result = new DozerBeanMapper();
        ((DozerBeanMapper) result).setMappingFiles(list);
     
        return result;
    }

}
