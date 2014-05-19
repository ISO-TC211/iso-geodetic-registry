package org.iso.registry.api.registry.registers.gcp.crs;

import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;
import de.geoinfoffm.registry.persistence.ItemClassRepository;

@ItemFactory({
	"GeodeticCRS",
	"VerticalCRS",
	"EngineeringCRS",
	"CompoundCRS",
	"ProjectedCRS"
})
public class CoordinateReferenceSystemItemFactory<I extends CoordinateReferenceSystemItem, D extends CoordinateReferenceSystemItemProposalDTO>
extends RegisterItemFactoryImpl<I, D> 
implements RegisterItemFactory<I, D>
{
	@Autowired
	private AreaItemRepository areaRepository;
	
	@Autowired
	private ItemClassRepository itemClassRepository;
	
//	@Autowired
//	private ItemFactoryRegistry itemFactories; 
	
	@Override
	public I createRegisterItem(D proposal) {
		I result = super.createRegisterItem(proposal);
//		proposal.setAdditionalValues(result, entityManager);

//		RE_ItemClass itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());
//
//		AreaItemProposalDTO areaDto = proposal.getDomainOfValidity();
//		AreaItem area;
//		if (areaDto.getReferencedItemUuid() != null) {
//			area = areaRepository.findOne(areaDto.getReferencedItemUuid());
//		}
//		else {
//			ItemFactoryRegistry itemFactories = context.getBean(ItemFactoryRegistry.class);
//			RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO> registerItemFactory = 
//					(RegisterItemFactory<RE_RegisterItem, RegisterItemProposalDTO>)itemFactories.getFactory("Area");
//
//			RE_ItemClass icArea = itemClassRepository.findByName("Area");
//			areaDto.setItemClassUuid(icArea.getUuid());
//			areaDto.setTargetRegisterUuid(proposal.getTargetRegisterUuid());
//			
//			area = (AreaItem)registerItemFactory.createRegisterItem(areaDto);
//			areaDto.setAdditionalValues(area, entityManager);
//			
//			proposal.addDependentItem(area);
//		}
//		result.setDomainOfValidity(area);
//		
//		result.setScope(proposal.getScope());
//		result.setRemarks(proposal.getRemarks());
		
		return result;
	}

}
