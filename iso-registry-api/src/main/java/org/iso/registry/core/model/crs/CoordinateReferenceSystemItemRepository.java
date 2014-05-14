package org.iso.registry.core.model.crs;

import org.iso.registry.core.model.cs.CoordinateSystemItem;

import de.geoinfoffm.registry.core.EntityRepository;

public interface CoordinateReferenceSystemItemRepository extends EntityRepository<CoordinateReferenceSystemItem>
{
	CoordinateReferenceSystemItem findByCode(Integer code);
}
