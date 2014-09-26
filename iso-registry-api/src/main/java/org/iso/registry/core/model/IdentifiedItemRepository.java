package org.iso.registry.core.model;

import org.springframework.data.repository.NoRepositoryBean;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;

@NoRepositoryBean
public interface IdentifiedItemRepository<I extends IdentifiedItem> extends EntityRepository<I>
{
	I findByIdentifier(Integer identifier);
	I findByNameAndStatus(String name, RE_ItemStatus status);
}
