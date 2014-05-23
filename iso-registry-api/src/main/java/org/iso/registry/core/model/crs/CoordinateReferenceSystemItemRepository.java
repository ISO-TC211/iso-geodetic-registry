package org.iso.registry.core.model.crs;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface CoordinateReferenceSystemItemRepository extends EntityRepository<CoordinateReferenceSystemItem>
{
	CoordinateReferenceSystemItem findByCode(Integer code);
}
