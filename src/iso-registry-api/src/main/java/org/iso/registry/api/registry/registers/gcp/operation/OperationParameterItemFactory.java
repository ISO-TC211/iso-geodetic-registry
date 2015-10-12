package org.iso.registry.api.registry.registers.gcp.operation;

import org.iso.registry.core.model.operation.OperationParameterItem;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory("OperationParameter")
public class OperationParameterItemFactory <I extends OperationParameterItem, D extends OperationParameterItemProposalDTO> 
extends RegisterItemFactoryImpl<I, D>
implements RegisterItemFactory<I, D>
{
	@Override
	public I createRegisterItem(D proposal) {
		return super.createRegisterItem(proposal);
	}
}
