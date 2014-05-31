package org.iso.registry.core.model.operation;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface GeneralOperationParameterItemRepository extends EntityRepository<GeneralOperationParameterItem>
{
	GeneralOperationParameterItem findByCode(Integer code);
}
