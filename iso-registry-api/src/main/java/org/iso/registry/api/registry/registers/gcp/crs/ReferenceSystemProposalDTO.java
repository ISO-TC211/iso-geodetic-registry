package org.iso.registry.api.registry.registers.gcp.crs;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public abstract class ReferenceSystemProposalDTO extends IdentifiedItemProposalDTO
{
	private AreaItemProposalDTO domainOfValidity;

	public ReferenceSystemProposalDTO() {
		super();
	}
	
	public ReferenceSystemProposalDTO(ReferenceSystemItem item) {
		super(item);
	}

	public ReferenceSystemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public ReferenceSystemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public ReferenceSystemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}
	
//	@Override
//	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
//		super.setAdditionalValues(registerItem, entityManager);
//		
//		if (registerItem instanceof ReferenceSystemItem) {
//			ReferenceSystemItem item = (ReferenceSystemItem)registerItem;
//
//		}
//	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof ReferenceSystemItem) {
			ReferenceSystemItem item = (ReferenceSystemItem)registerItem;

			this.setDomainOfValidity(new AreaItemProposalDTO(item.getDomainOfValidity()));
		}
	}

	public AreaItemProposalDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(AreaItemProposalDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

}
