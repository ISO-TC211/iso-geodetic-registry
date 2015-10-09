package org.iso.registry.api.registry.registers.gcp.crs;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.api.soap.Addition_Type;

public abstract class ReferenceSystemItemProposalDTO extends IdentifiedItemProposalDTO
{
	private ExtentDTO domainOfValidity;

	public ReferenceSystemItemProposalDTO() {
		super();
		this.domainOfValidity = new ExtentDTO();
	}
	
	public ReferenceSystemItemProposalDTO(ReferenceSystemItem item) {
		super(item);
		this.domainOfValidity = new ExtentDTO(item.getDomainOfValidity());
	}

	public ReferenceSystemItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public ReferenceSystemItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public ReferenceSystemItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
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

			this.setDomainOfValidity(new ExtentDTO(item.getDomainOfValidity()));
		}
	}

	public ExtentDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

}
