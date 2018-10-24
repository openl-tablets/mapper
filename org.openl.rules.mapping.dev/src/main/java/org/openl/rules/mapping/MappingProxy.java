package org.openl.rules.mapping;

import org.dozer.MappingContext;
import org.dozer.MappingException;
import org.openl.rules.mapping.exception.RulesMappingException;

/**
 * Internal class which processes mapping definitions and creates internal mapping model. Current implementation uses
 * OpenL Rules project as mapping definitions source and Dozer bean mapping framework with some modifications as a
 * mapping tool. Not intended for direct use by Application code.
 */
class MappingProxy implements Mapper {

    private final org.dozer.Mapper beanMapper;

    MappingProxy(org.dozer.Mapper beanMapper) {
        this.beanMapper = beanMapper;
    }

    public void map(Object source, Object destination) {
        try {
            beanMapper.map(source, destination);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }

    public <T> T map(Object source, Class<T> destination) {
        try {
            return beanMapper.map(source, destination);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }

    public void map(Object source, Object destination, MappingContext context) {
        try {
            beanMapper.map(source, destination, context);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }

    public <T> T map(Object source, Class<T> destination, MappingContext context) {
        try {
            return beanMapper.map(source, destination, context);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }
}
