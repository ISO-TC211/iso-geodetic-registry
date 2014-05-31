package org.iso.registry.api.registry.registers.gcp.operation;

import org.iso.registry.core.model.operation.CoordinateOperationItem;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory({
	"Conversion", "Transformation", "ConcatenatedOperation" 
})
public class CoordinateOperationItemFactory <I extends CoordinateOperationItem, D extends CoordinateOperationItemProposalDTO> 
extends RegisterItemFactoryImpl<I, D>
implements RegisterItemFactory<I, D>
{
	@Override
	public I createRegisterItem(D proposal) {
		return super.createRegisterItem(proposal);
	}
}
