package org.iso.registry.api.registry;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.ProposalNote;
import org.iso.registry.core.model.ProposalNoteRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.api.ProposalServiceImpl;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;

public class IsoProposalServiceImpl extends ProposalServiceImpl implements IsoProposalService
{
	public static final String STATUS_RETURNED_BY_MANAGER = "RETURNED_BY_MANAGER";
	public static final String STATUS_RETURNED_BY_CONTROLBODY = "RETURNED_BY_CONTROLBODY";
	
	private static final Logger logger = LoggerFactory.make();
	
	@Autowired
	private ProposalNoteRepository noteRepository;
	
	@Autowired
	private ProposalManagementInformationRepository pmiRepository;
	
	@Autowired
	private RegisterItemRepository itemRepository;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	private ProposalWorkflowManager workflowManager;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	public IsoProposalServiceImpl(ProposalRepository repository) {
		super(repository);
	}

	@Override
	@Transactional
	public Proposal acceptProposal(Proposal proposal, String controlBodyDecisionEvent) throws InvalidProposalException,
			IllegalOperationException, UnauthorizedException {
		
		if (proposal instanceof Addition) {
			// assign final identifier to IdentifiedItems
			Addition addition = (Addition)proposal;
			assignItemIdentifier(addition);
		}
		else if (proposal instanceof ProposalGroup) {
			ProposalGroup group = (ProposalGroup)proposal;
			for (Proposal subproposal : group.getProposals()) {
				if (subproposal instanceof Addition) {
					Addition addition = (Addition)subproposal;
					assignItemIdentifier(addition);
					for (Proposal dependentProposal : subproposal.getDependentProposals()) {
						if (dependentProposal instanceof Addition) {
							assignItemIdentifier((Addition)dependentProposal);
						}
					}
				}
			}
		}
		
		return super.acceptProposal(proposal, controlBodyDecisionEvent);
	}

	private void assignItemIdentifier(Addition addition) {
		RE_RegisterItem proposedItem = addition.getItem();
		if (proposedItem instanceof IdentifiedItem) {
			Integer identifier = this.findNextAvailableIdentifier();
			((IdentifiedItem)proposedItem).setIdentifier(identifier);
			proposedItem.setItemIdentifier(BigInteger.valueOf(identifier.longValue()));
			logger.debug(">>> Assigned identifier {} to item '{}'", identifier, proposedItem.getName());
		}
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

		if (workflowManager.isUnderReview(proposal)) {
			// Returned by register manager
			security.hasEntityRelatedRoleForAll(RegistrySecurity.MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());
			proposal.setStatus(STATUS_RETURNED_BY_MANAGER);
			
		}
		else if (workflowManager.isPending(proposal)) {
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
	
	@Override
	public Integer findNextAvailableIdentifier() {
		String jpql = "SELECT MAX(i.identifier) FROM IdentifiedItem i";
		Integer maxCode = (Integer)entityManager.createQuery(jpql).getResultList().get(0);
		return (maxCode == null || maxCode < 1) ? 1 : maxCode + 1;
	}
}
