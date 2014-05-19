package org.iso.registry.api.registry.registers.gcp.datum;

import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.iso.registry.core.model.datum.EllipsoidItemRepository;
import org.iso.registry.core.model.datum.PrimeMeridianItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory({
	"GeodeticDatum",
	"VerticalDatum",
	"EngineeringDatum"
})
public class DatumItemFactory
extends RegisterItemFactoryImpl<DatumItem, DatumItemProposalDTO>
implements RegisterItemFactory<DatumItem, DatumItemProposalDTO>
{
	@Autowired
	private DatumItemRepository datumRepository;
	
	@Autowired
	private AreaItemRepository areaRepository;
	
	@Autowired
	private EllipsoidItemRepository ellipsoidRepository;
	
	@Autowired
	private PrimeMeridianItemRepository primeMeridianRepository;
	
	@Override
	public DatumItem createRegisterItem(DatumItemProposalDTO proposal) {
		if (proposal.getReferencedItemUuid() != null) {
			return datumRepository.findOne(proposal.getItemUuid());
		}
		else {
			DatumItem result = super.createRegisterItem(proposal);
//			proposal.setAdditionalValues(result, entityManager);
			
			return result;
		}
	}
}
