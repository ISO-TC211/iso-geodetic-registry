package org.iso.registry.api.registry.registers.gcp.crs;

import java.math.BigInteger;

import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public abstract class CoordinateReferenceSystemItemProposalDTO extends ReferenceSystemProposalDTO
{
//	private RS_Identifier identifier;
	private String scope;
	
	public CoordinateReferenceSystemItemProposalDTO() {
		super();
	}
	
	public CoordinateReferenceSystemItemProposalDTO(CoordinateReferenceSystemItem crs) {
		super(crs);
	}	

	public CoordinateReferenceSystemItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public CoordinateReferenceSystemItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public CoordinateReferenceSystemItemProposalDTO(Proposal proposal) {
		super(proposal);
	}
	
//	@Override
//	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
//		super.setAdditionalValues(registerItem, entityManager);
//		
//		if (registerItem instanceof CoordinateReferenceSystemItem) {
//			CoordinateReferenceSystemItem item = (CoordinateReferenceSystemItem)registerItem;
//
//			item.setsco
//			item.setAliases(aliases);
//		}
//	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof CoordinateReferenceSystemItem) {
			CoordinateReferenceSystemItem crsItem = (CoordinateReferenceSystemItem)item;

			this.setScope(crsItem.getScope());
		}
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

}
