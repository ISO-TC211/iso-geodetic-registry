package org.iso.registry.core.model.operation;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface OperationParameterItemRepository extends EntityRepository<OperationParameterItem>
{
	OperationParameterItem findByCode(Integer code);
}
