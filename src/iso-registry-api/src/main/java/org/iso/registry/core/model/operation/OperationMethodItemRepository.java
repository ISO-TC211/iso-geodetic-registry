package org.iso.registry.core.model.operation;

import java.util.List;

import org.iso.registry.core.model.IdentifiedItemRepository;

import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;

public interface OperationMethodItemRepository extends IdentifiedItemRepository<OperationMethodItem>
{
	List<OperationMethodItem> findByIdentifierAndStatus(Integer identifier, RE_ItemStatus status);
}
