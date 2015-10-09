package org.iso.registry.api.registry.registers.gcp.cs;

import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory("CoordinateSystemAxis")
public class CoordinateSystemAxisItemFactory
extends RegisterItemFactoryImpl<CoordinateSystemAxisItem, CoordinateSystemAxisItemProposalDTO> 
implements RegisterItemFactory<CoordinateSystemAxisItem, CoordinateSystemAxisItemProposalDTO>
{
	@Autowired
	private CoordinateSystemAxisItemRepository axisRepository;
	
	@Autowired
	private UnitOfMeasureItemRepository uomRepository;

	@Override
	public CoordinateSystemAxisItem createRegisterItem(CoordinateSystemAxisItemProposalDTO proposal) {
		if (proposal.getReferencedItemUuid() != null) {
			return axisRepository.findOne(proposal.getReferencedItemUuid());
		}
		
		CoordinateSystemAxisItem result = super.createRegisterItem(proposal);
//		proposal.setAdditionalValues(result, entityManager);
		
//		UnitOfMeasureItem uom = uomRepository.findOne(proposal.getAxisUnit().getReferencedItemUuid());
//		result.setAxisUnit(uom);
//		result.setAxisAbbreviation(proposal.getAxisAbbreviation());
		
		return result;
	}

	
//	public CoordinateSystemAxisItem createAxis(CoordinateSystemAxisProposalDTO proposal) {
//		if (proposal.getUuid() != null) {
//			return axisRepository.findOne(proposal.getUuid());
//		}
//		else {
//			final CoordinateSystemAxisItem result = super.createRegisterItem(proposal);
//
//			UnitOfMeasureItem uom = uomRepository.findOne(proposal.getAxisUnit().getItemUuid());
//			result.setAxisUnit(uom);
//			
//			result.setAxisAbbreviation(proposal.getAxisAbbreviation());
//			
////			CoordinateSystemAxisItem axisItem = new CoordinateSystemAxisItem(
////					proposal.getAxisAbbreviation(),
////					proposal.getAxisDirection(),
////					uom,
////					proposal.getMaximumValue(),
////					proposal.getMinimumValue(),
////					proposal.getRangeMeaning()
////			);
//			
//			return result;
//		}
//	}

}
