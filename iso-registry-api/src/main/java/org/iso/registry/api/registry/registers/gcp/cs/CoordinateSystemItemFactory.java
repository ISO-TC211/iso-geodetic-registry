package org.iso.registry.api.registry.registers.gcp.cs;

import javax.management.RuntimeErrorException;

import org.iso.registry.core.model.cs.CoordinateSystemAxisItemRepository;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory(value = "EllipsoidalCS")
public class CoordinateSystemItemFactory 
extends RegisterItemFactoryImpl<CoordinateSystemItem, CoordinateSystemItemProposalDTO> 
implements RegisterItemFactory<CoordinateSystemItem, CoordinateSystemItemProposalDTO>
{
	@Autowired
	private CoordinateSystemItemRepository csRepository;
	
	@Autowired
	private CoordinateSystemAxisItemRepository axisRepository;

	@Override
	public CoordinateSystemItem createRegisterItem(CoordinateSystemItemProposalDTO proposal) {
		if (proposal.getReferencedItemUuid() != null) {
			return csRepository.findOne(proposal.getItemUuid());
		}
		else {
			CoordinateSystemItem result = super.createRegisterItem(proposal);
			proposal.setAdditionalValues(result, entityManager);
//			result.setCode(proposal.getCode());
//
//			for (CoordinateSystemAxisProposalDTO axis : proposal.getAxes()) {
//				if (axis.getReferencedItemUuid() != null) {
//					result.addAxis(axisRepository.findOne(axis.getReferencedItemUuid()));
//				}
//				else {
//					throw new RuntimeException("Not yet implemented");
//				}
//			}
			
			return result;
		}
	}

}
