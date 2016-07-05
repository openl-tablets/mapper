package org.dozer.factory;

import org.dozer.MappingParameters;

/**
 * @author Dmitry Buzdin
 */
public interface BeanCreationStrategy {

    boolean isApplicable(BeanCreationDirective directive);

    Object create(MappingParameters params, BeanCreationDirective directive);
}
