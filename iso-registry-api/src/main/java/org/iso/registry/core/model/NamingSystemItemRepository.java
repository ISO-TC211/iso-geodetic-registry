package org.iso.registry.core.model;

import de.geoinfoffm.registry.core.EntityRepository;

public interface NamingSystemItemRepository extends EntityRepository<NamingSystemItem>
{
	NamingSystemItem findByCode(Integer code);
}
