package org.iso.registry.api.registry.registers.gcp.crs;

import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.ItemFactoryRegistry;
import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.ItemFactory;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.persistence.ItemClassRepository;

@ItemFactory({ "GeodeticCRS" })
public class CoordinateReferenceSystemItemFactory 
extends RegisterItemFactoryImpl<CoordinateReferenceSystemItem, CoordinateReferenceSystemItemProposalDTO> 
implements RegisterItemFactory<CoordinateReferenceSystemItem, CoordinateReferenceSystemItemProposalDTO>
{
	@Autowired
	private AreaItemRepository areaRepository;
	
	@Autowired
	private ItemClassRepository itemClassRepository;
	
//	@Autowired
//	private ItemFactoryRegistry itemFactories; 
	
	@Override
	public CoordinateReferenceSystemItem createRegisterItem(CoordinateReferenceSystemItemProposalDTO proposal) {
		CoordinateReferenceSystemItem result = super.createRegisterItem(proposal);

		RE_ItemClass itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());

		AreaItemProposalDTO areaDto = proposal.getDomainOfValidity();
		
		AreaItem area;
		if (areaDto.getReferencedItemUuid() != null) {
			area = areaRepository.findOne(areaDto.getReferencedItemUuid());
		}
		else {
//			RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO> registerItemFactory = 
//					(RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO>)itemFactories.getFactory(itemClass.getName());
//
//			area = (AreaItem)registerItemFactory.createRegisterItem(areaDto);
			area = null;
		}
		
		result.setDomainOfValidity(area);
		result.setScope(proposal.getScope());
		result.setRemarks(proposal.getRemarks());
		
		return result;
	}

}
