package org.iso.registry.api.registry.registers.gcp.crs;

import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.GeodeticDatumItemProposalDTO;
import org.iso.registry.core.model.crs.GeodeticCoordinateReferenceSystemItem;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class GeodeticCoordinateReferenceSystemItemProposalDTO extends CoordinateReferenceSystemItemProposalDTO
{
	private CoordinateSystemItemProposalDTO coordinateSystem;
	private GeodeticDatumItemProposalDTO datum;

	public GeodeticCoordinateReferenceSystemItemProposalDTO() { }
	
	public GeodeticCoordinateReferenceSystemItemProposalDTO(GeodeticCoordinateReferenceSystemItem crs) {
		super(crs);
	}

	public GeodeticCoordinateReferenceSystemItemProposalDTO(CoordinateSystemItemProposalDTO cs, GeodeticDatumItemProposalDTO datum) {
		this.coordinateSystem = cs;
		this.datum = datum;
	}
	
	public GeodeticCoordinateReferenceSystemItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

//	@Override
//	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
//		super.setAdditionalValues(registerItem, entityManager);
//		
//		if (registerItem instanceof GeodeticCoordinateReferenceSystemItem) {
//			GeodeticCoordinateReferenceSystemItem item = (GeodeticCoordinateReferenceSystemItem)registerItem;
//
//			item.set
//		}
//	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof GeodeticCoordinateReferenceSystemItem) {
			GeodeticCoordinateReferenceSystemItem item = (GeodeticCoordinateReferenceSystemItem)registerItem;

			this.setCoordinateSystem(new CoordinateSystemItemProposalDTO(item.getCoordinateSystem()));
		}
	}

	public CoordinateSystemItemProposalDTO getCoordinateSystem() {
		return coordinateSystem;
	}

	public void setCoordinateSystem(CoordinateSystemItemProposalDTO coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}

	public GeodeticDatumItemProposalDTO getDatum() {
		return datum;
	}

	public void setDatum(GeodeticDatumItemProposalDTO datum) {
		this.datum = datum;
	}


}
