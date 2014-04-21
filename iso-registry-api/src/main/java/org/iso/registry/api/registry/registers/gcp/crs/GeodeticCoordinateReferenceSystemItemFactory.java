package org.iso.registry.api.registry.registers.gcp.crs;

import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.GeodeticDatumItemProposalDTO;
import org.iso.registry.core.model.crs.GeodeticCoordinateReferenceSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItemRepository;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.iso.registry.core.model.datum.GeodeticDatumItem;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemFactory;
import de.geoinfoffm.registry.core.model.ItemFactory;

@ItemFactory({ "GeodeticCRS" })
public class GeodeticCoordinateReferenceSystemItemFactory 
extends CoordinateReferenceSystemItemFactory<GeodeticCoordinateReferenceSystemItem, GeodeticCoordinateReferenceSystemItemProposalDTO>
implements RegisterItemFactory<GeodeticCoordinateReferenceSystemItem, GeodeticCoordinateReferenceSystemItemProposalDTO>
{
	@Autowired
	private CoordinateSystemItemRepository csRepository;
	
	@Autowired
	private DatumItemRepository datumRepository;

	@Override
	public GeodeticCoordinateReferenceSystemItem createRegisterItem(GeodeticCoordinateReferenceSystemItemProposalDTO proposal) {
		GeodeticCoordinateReferenceSystemItem result = super.createRegisterItem(proposal);
		
		CoordinateSystemItemProposalDTO csDto = proposal.getCoordinateSystem();
		CoordinateSystemItem cs;
		if (csDto.getReferencedItemUuid() != null) {
			cs = csRepository.findOne(csDto.getReferencedItemUuid());
		}
		else {
			throw new RuntimeException("Not yet implemented");
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
		}
		result.setCoordinateSystem(cs);

		GeodeticDatumItemProposalDTO datumDto = proposal.getDatum();
		GeodeticDatumItem datum;
		if (datumDto.getReferencedItemUuid() != null) {
			DatumItem tempDatum = datumRepository.findOne(datumDto.getReferencedItemUuid());
			if (!(tempDatum instanceof GeodeticDatumItem)) {
				throw new RuntimeException(String.format("Illegal datum provided for Geodetic CRS: expected GeodeticDatum, was %s", tempDatum.getClass().getCanonicalName()));
			}
			datum = (GeodeticDatumItem)tempDatum;
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
		result.setDatum(datum);

		return result;
	}
	
}
