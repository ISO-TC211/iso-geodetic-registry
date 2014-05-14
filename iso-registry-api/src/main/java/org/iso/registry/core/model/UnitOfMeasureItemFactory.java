package org.iso.registry.core.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory("UnitOfMeasure")
public class UnitOfMeasureItemFactory 
extends RegisterItemFactoryImpl<UnitOfMeasureItem, UnitOfMeasureItemProposalDTO>
implements RegisterItemFactory<UnitOfMeasureItem, UnitOfMeasureItemProposalDTO>
{
	@Autowired
	private UnitOfMeasureItemRepository uomRepository;
	
	@Override
	public UnitOfMeasureItem createRegisterItem(UnitOfMeasureItemProposalDTO proposal) {
		if (proposal.getReferencedItemUuid() != null) {
			return uomRepository.findOne(proposal.getItemUuid());
		}
		else {
			UnitOfMeasureItem result = super.createRegisterItem(proposal);
			proposal.setAdditionalValues(result, entityManager);
			
			return result;
		}
	}

}
