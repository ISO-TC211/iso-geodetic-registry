package org.iso.registry.core.model.operation;

import de.geoinfoffm.registry.core.EntityRepository;

public interface OperationMethodItemRepository extends EntityRepository<OperationMethodItem>
{
	OperationMethodItem findByCode(Integer code);
}
