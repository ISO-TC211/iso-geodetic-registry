package org.iso.registry.api.registry.registers.gcp.operation;

import org.iso.registry.core.model.operation.OperationParameterItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.soap.Addition_Type;

public class OperationParameterItemProposalDTO extends GeneralOperationParameterItemProposalDTO
{

	public OperationParameterItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public OperationParameterItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public OperationParameterItemProposalDTO(OperationParameterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public OperationParameterItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public OperationParameterItemProposalDTO(Proposal proposal, ProposalDtoFactory factory) {
		super(proposal, factory);
	}

	public OperationParameterItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}

}
