package org.iso.registry.api.registry;

import org.iso.registry.core.model.ProposalNote;
import org.iso.registry.core.model.ProposalNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.ProposalServiceImpl;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ProposalRepository;

//@Service
public class IsoProposalServiceImpl extends ProposalServiceImpl implements IsoProposalService
{
	public static final String STATUS_RETURNED_BY_MANAGER = "RETURNED_BY_MANAGER";
	public static final String STATUS_RETURNED_BY_CONTROLBODY = "RETURNED_BY_CONTROLBODY";
	
	@Autowired
	private ProposalNoteRepository noteRepository;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	public IsoProposalServiceImpl(ProposalRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	public Proposal returnProposal(Proposal proposal, String noteToSubmitter) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		if (proposal == null) {
			throw new InvalidProposalException("Cannot return null proposal.");
		}

		if (proposal.isUnderReview()) {
			// Returned by register manager
			security.hasEntityRelatedRoleForAll(RegistrySecurity.MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());
			proposal.setStatus(STATUS_RETURNED_BY_MANAGER);
			
		}
		else if (proposal.isPending()) {
			// Returned by control body
			security.hasEntityRelatedRoleForAll(RegistrySecurity.CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());
			proposal.setStatus(STATUS_RETURNED_BY_CONTROLBODY);
		}
		else {
			throw new IllegalOperationException("Cannot return proposal: wrong status");
		}

		RegistryUser author = security.getCurrentUser();

		proposal.setDateSubmitted(null);
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi.setDateProposed(null);
		}
		this.createNote(proposal, author, noteToSubmitter);

		proposal = repository().save(proposal);
		return proposal;
	}

	@Override
	public ProposalNote createNote(Proposal proposal, RegistryUser author, String note) {
		ProposalNote result = new ProposalNote(proposal, author, note);
		
		return noteRepository.save(result);
	}
	
	
}
