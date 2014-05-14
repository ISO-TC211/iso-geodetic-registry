package org.iso.registry.core.model.datum;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;

@Repository
public interface DatumItemRepository extends EntityRepository<DatumItem>
{
	DatumItem findByCode(Integer code);
}
