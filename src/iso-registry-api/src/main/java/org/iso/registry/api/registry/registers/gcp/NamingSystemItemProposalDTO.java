package org.iso.registry.api.registry.registers.gcp;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.core.model.IdentifiedItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.soap.Addition_Type;

public class NamingSystemItemProposalDTO extends IdentifiedItemProposalDTO
{

	public NamingSystemItemProposalDTO() {
	}

	public NamingSystemItemProposalDTO(IdentifiedItem item) {
		super(item);
	}

	public NamingSystemItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public NamingSystemItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public NamingSystemItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

}
