package org.iso.registry.core.model.operation;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;

@Repository
public interface OperationParameterItemRepository extends EntityRepository<OperationParameterItem>
{
	OperationParameterItem findByCode(Integer code);
}
