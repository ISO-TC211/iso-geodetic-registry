package org.iso.registry.core.model;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface AreaItemRepository extends EntityRepository<AreaItem>
{
//	Collection<AreaItem> findAllOrderByName();
}
