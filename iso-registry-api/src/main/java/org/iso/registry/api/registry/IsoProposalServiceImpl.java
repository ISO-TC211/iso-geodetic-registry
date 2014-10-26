package org.iso.registry.api.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.iso.registry.core.model.ProposalNote;
import org.iso.registry.core.model.ProposalNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.ProposalServiceImpl;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ProposalRepository;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;

public class IsoProposalServiceImpl extends ProposalServiceImpl implements IsoProposalService
{
	public static final String STATUS_RETURNED_BY_MANAGER = "RETURNED_BY_MANAGER";
	public static final String STATUS_RETURNED_BY_CONTROLBODY = "RETURNED_BY_CONTROLBODY";
	
	@Autowired
	private ProposalNoteRepository noteRepository;
	
	@Autowired
	private ProposalManagementInformationRepository pmiRepository;
	
	@Autowired
	private RegisterItemRepository itemRepository;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	public IsoProposalServiceImpl(ProposalRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	public void deleteProposal(Proposal proposal) throws IllegalOperationException, UnauthorizedException {
		if (proposal == null) {
			throw new IllegalArgumentException("Cannot return null proposal.");
		}

		if (proposal.isSubmitted()) {
			throw new IllegalOperationException("Cannot delete submitted proposals: use withdraw");			
		}
		
		List<RE_RegisterItem> itemsToDelete = new ArrayList<>();
		List<RE_ProposalManagementInformation> pmisToDelete = new ArrayList<>();
		
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			if (pmi.getItem() != null && RE_ItemStatus.NOT_VALID.equals(pmi.getItem().getStatus())) {
				RE_RegisterItem item = pmi.getItem();
				pmi.setItem(null);
				itemsToDelete.add(item);
			}

			pmisToDelete.add(pmi);
		}
		
		proposal.delete();
		repository().delete(proposal);
		
		for (RE_ProposalManagementInformation pmi : pmisToDelete) {
			pmiRepository.delete(pmi);
		}

		for (RE_RegisterItem item : itemsToDelete) {
			itemRepository.delete(item);
		}
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
		
		if (!StringUtils.isEmpty(noteToSubmitter)) {
			this.createNote(proposal, author, noteToSubmitter);
		}

		proposal = repository().save(proposal);
		return proposal;
	}

	@Override
	public ProposalNote createNote(Proposal proposal, RegistryUser author, String note) {
		ProposalNote result = new ProposalNote(proposal, author, note);
		
		return noteRepository.save(result);
	}
	
}
