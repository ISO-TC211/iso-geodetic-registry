package org.iso.registry.core.model;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;

@Repository
public interface UnitOfMeasureItemRepository extends EntityRepository<UnitOfMeasureItem>
{
	UnitOfMeasureItem findByCode(Integer code);
	UnitOfMeasureItem findByNameAndStatus(String name, RE_ItemStatus status);
}
