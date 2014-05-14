package org.iso.registry.api.registry.registers.gcp.datum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.datum.EllipsoidItemRepository;
import org.iso.registry.core.model.datum.GeodeticDatumItem;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.iso.registry.core.model.datum.PrimeMeridianItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.api.RegisterItemFactoryImpl;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory("GeodeticDatum")
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
			proposal.setAdditionalValues(result, entityManager);
			
//			result.setAnchorDefinition(proposal.getAnchorDefinition());
//			if (proposal.getDomainOfValidity() != null && proposal.getDomainOfValidity().getItemUuid() != null) {
//				AreaItem area = areaRepository.findOne(proposal.getDomainOfValidity().getItemUuid());
//				result.setDomainOfValidity(area);
//			}
//			result.setRealizationEpoch(proposal.getRealizationEpoch());
//
//			if (result instanceof GeodeticDatumItem) {
//				EllipsoidItem e = ellipsoidRepository.findOne(proposal.getEllipsoid().getReferencedItemUuid());
//				PrimeMeridianItem pm = primeMeridianRepository.findOne(proposal.getPrimeMeridian().getReferencedItemUuid());
//
//				((GeodeticDatumItem)result).setEllipsoid(e);
//				((GeodeticDatumItem)result).setPrimeMeridian(pm);
//			}
			
			return result;
		}
	}
}
