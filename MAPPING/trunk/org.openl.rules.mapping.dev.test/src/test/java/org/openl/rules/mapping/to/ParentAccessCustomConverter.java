package org.openl.rules.mapping.to;

import org.dozer.BaseMappingParamsAwareCustomConverter;
import org.dozer.MappingParameters;
import org.dozer.MappingParentObjects;

import java.util.List;
import java.util.Stack;

public class ParentAccessCustomConverter extends BaseMappingParamsAwareCustomConverter{
	@Override
	public Object convert(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
		Integer i = Integer.class.cast(sourceFieldValue);

		MappingParentObjects parentObjects = (MappingParentObjects) mappingParameters.get("PARENTOBJECTS");

		A a = null;
		for (Object o: parentObjects.getSourceParents()) {
			if (o instanceof A) {
				a = A.class.cast(o);
				
			}
		}
		return a.getAString();
	}
}
