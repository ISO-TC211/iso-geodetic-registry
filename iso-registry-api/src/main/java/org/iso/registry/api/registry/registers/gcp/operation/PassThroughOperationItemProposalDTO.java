package org.iso.registry.api.registry.registers.gcp.operation;

import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.operation.PassThroughOperationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class PassThroughOperationItemProposalDTO extends CoordinateOperationItemProposalDTO
{

	public PassThroughOperationItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public PassThroughOperationItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public PassThroughOperationItemProposalDTO(PassThroughOperationItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public PassThroughOperationItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public PassThroughOperationItemProposalDTO(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public PassThroughOperationItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}

}
