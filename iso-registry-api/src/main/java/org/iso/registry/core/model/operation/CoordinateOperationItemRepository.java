package org.iso.registry.core.model.operation;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface CoordinateOperationItemRepository extends EntityRepository<CoordinateOperationItem>
{
	CoordinateOperationItem findByCode(Integer code);
}
