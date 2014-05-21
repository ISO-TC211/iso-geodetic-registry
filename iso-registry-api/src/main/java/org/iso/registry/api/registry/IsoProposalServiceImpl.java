package org.iso.registry.api.registry;

import org.iso.registry.core.model.ProposalNote;
import org.iso.registry.core.model.ProposalNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.geoinfoffm.registry.api.ProposalServiceImpl;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ProposalRepository;

//@Service
public class IsoProposalServiceImpl extends ProposalServiceImpl implements IsoProposalService
{
	public static final String STATUS_REJECTED_BY_MANAGER = "REJECTED_BY_MANAGER";
	
	@Autowired
	private ProposalNoteRepository noteRepository;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	public IsoProposalServiceImpl(ProposalRepository repository) {
		super(repository);
	}

	@Override
	public Proposal rejectProposal(Proposal proposal, String controlBodyDecisionEvent) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		if (proposal == null) {
			throw new InvalidProposalException("Cannot reject null proposal.");
		}

		if (proposal.isUnderReview()) {
			// Rejection by register manager
			security.hasEntityRelatedRoleForAll(RegistrySecurity.MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());
			RegistryUser author = security.getCurrentUser();
			
			this.createNote(proposal, author, controlBodyDecisionEvent);
			proposal.setDateSubmitted(null);
			proposal.setStatus(STATUS_REJECTED_BY_MANAGER);
			
			proposal = repository().save(proposal);

			return proposal;
		}
		else {
			return super.rejectProposal(proposal, controlBodyDecisionEvent);
		}
	}

	@Override
	public ProposalNote createNote(Proposal proposal, RegistryUser author, String note) {
		ProposalNote result = new ProposalNote(proposal, author, note);
		
		return noteRepository.save(result);
	}
	
	
}
