package org.iso.registry.core.model.operation;

import java.util.List;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;

public interface OperationMethodItemRepository extends EntityRepository<OperationMethodItem>
{
	OperationMethodItem findByCode(Integer code);
	List<OperationMethodItem> findByCodeAndStatus(Integer code, RE_ItemStatus status);
}
