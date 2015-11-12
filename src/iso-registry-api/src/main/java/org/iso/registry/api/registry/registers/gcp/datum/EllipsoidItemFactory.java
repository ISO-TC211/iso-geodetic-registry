package org.iso.registry.api.registry.registers.gcp.datum;

import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.datum.EllipsoidItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory("Ellipsoid")
public class EllipsoidItemFactory extends RegisterItemFactoryImpl<EllipsoidItem, EllipsoidItemProposalDTO> implements
		RegisterItemFactory<EllipsoidItem, EllipsoidItemProposalDTO>
{
	@Autowired
	private EllipsoidItemRepository ellipsoidRepository;
	
	@Autowired
	private UnitOfMeasureItemRepository uomRepository;

	@Override
	public EllipsoidItem createRegisterItem(EllipsoidItemProposalDTO proposal) {
		if (proposal.getReferencedItemUuid() != null) {
			return ellipsoidRepository.findOne(proposal.getReferencedItemUuid());
		}
		
		EllipsoidItem result = super.createRegisterItem(proposal);
//		proposal.setAdditionalValues(result, entityManager);
		
		return result;
	}

}
