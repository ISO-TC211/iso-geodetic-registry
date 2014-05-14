package org.iso.registry.core.model.crs;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface AreaItemRepository extends EntityRepository<AreaItem>
{
//	Collection<AreaItem> findAllOrderByName();
	AreaItem findByCode(Integer code);
}
