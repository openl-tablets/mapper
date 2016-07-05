package org.openl.rules.mapping.to;

import org.dozer.BaseMappingParamsAwareCustomConverter;
import org.openl.rules.mapping.MappingParameters;
import org.dozer.MappingParentObjects;

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
