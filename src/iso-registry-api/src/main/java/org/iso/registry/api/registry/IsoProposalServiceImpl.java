package org.iso.registry.api.registry;

import static de.geoinfoffm.registry.core.workflow.Iso19135ProposalWorkflowManager.*;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.ProposalNote;
import org.iso.registry.core.model.ProposalNoteRepository;
import org.iso.registry.core.model.UnitOfMeasureItem;
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
			LinkedList<Addition> q = new LinkedList<>();
			ProposalGroup group = (ProposalGroup)proposal;
			for (Proposal subproposal : group.getProposals()) {
				if (subproposal instanceof Addition) {
					Addition addition = (Addition)subproposal;
					processGroupProposal(q, addition);
				}
			}
			
			Addition unity = null;
			LinkedList<Addition> si = new LinkedList<>();
			LinkedList<Addition> nonsi = new LinkedList<>();
			LinkedList<Addition> per = new LinkedList<>();
			LinkedList<Addition> uom = new LinkedList<>();
			LinkedList<Addition> pm = new LinkedList<>();
			LinkedList<Addition> ell = new LinkedList<>();
			LinkedList<Addition> axis = new LinkedList<>();
			LinkedList<Addition> cs = new LinkedList<>();
			LinkedList<Addition> param = new LinkedList<>();
			LinkedList<Addition> method = new LinkedList<>();
			LinkedList<Addition> conv = new LinkedList<>();
			LinkedList<Addition> datum = new LinkedList<>();
			LinkedList<Addition> crs = new LinkedList<>();
			LinkedList<Addition> tx = new LinkedList<>();
			LinkedList<Addition> other = new LinkedList<>();
			for (Addition addition : q) {
				if ("UnitOfMeasure".equals(addition.getItemClassName())) {
					UnitOfMeasureItem uomItem = (UnitOfMeasureItem)addition.getItem();
					switch (uomItem.getName()) {
					case "unity":
						unity = addition;
						break;
					case "metre":
					case "second":
					case "radian":
						si.add(addition);
						break;
					case "degree":
					case "degree (supplier to define representation)":
					case "arc-second":
					case "US survey foot":
						nonsi.add(addition);
						break;
					default:
						if (uomItem.getName().contains(" per ")) {
							per.add(addition);
						} 
						else { 
							uom.add(addition);
						}
					}
				}
				else if ("PrimeMeridian".equals(addition.getItemClassName())) {
					pm.add(addition);
				}
				else if ("Ellipsoid".equals(addition.getItemClassName())) {
					ell.add(addition);
				}
				else if ("CoordinateSystemAxis".equals(addition.getItemClassName())) {
					axis.add(addition);
				}
				else if (addition.getItemClassName().endsWith("CS")) {
					cs.add(addition);
				}
				else if ("OperationParameter".equals(addition.getItemClassName())) {
					param.add(addition);
				}
				else if ("OperationMethod".equals(addition.getItemClassName())) {
					method.add(addition);
				}
				else if ("Conversion".equals(addition.getItemClassName())) {
					conv.add(addition);
				}
				else if (addition.getItemClassName().endsWith("Datum")) {
					datum.add(addition);
				}
				else if (addition.getItemClassName().endsWith("CRS")) {
					crs.add(addition);
				}
				else if ("Transformation".equals(addition.getItemClassName())) {
					tx.add(addition);
				}
				else {
					logger.debug(MessageFormat.format("Unpriviledged item class: {0}", addition.getItemClassName()));
					other.add(addition);
				}
			}

			if (unity != null) {
				assignItemIdentifier(unity);
			}
			for (Addition a : si) {
				assignItemIdentifier(a);
			}
			for (Addition a : nonsi) {
				assignItemIdentifier(a);
			}
			for (Addition a : uom) {
				assignItemIdentifier(a);
			}
			for (Addition a : per) {
				assignItemIdentifier(a);
			}
			for (Addition a : pm) {
				assignItemIdentifier(a);
			}
			for (Addition a : ell) {
				assignItemIdentifier(a);
			}
			for (Addition a : axis) {
				assignItemIdentifier(a);
			}
			for (Addition a : cs) {
				assignItemIdentifier(a);
			}
			for (Addition a : param) {
				assignItemIdentifier(a);
			}
			for (Addition a : method) {
				assignItemIdentifier(a);
			}
			for (Addition a : conv) {
				assignItemIdentifier(a);
			}
			for (Addition a : datum) {
				assignItemIdentifier(a);
			}
			for (Addition a : crs) {
				assignItemIdentifier(a);
			}
			for (Addition a : tx) {
				assignItemIdentifier(a);
			}
			for (Addition a : other) {
				assignItemIdentifier(a);
			}
		}
		
		return super.acceptProposal(proposal, controlBodyDecisionEvent);
	}

	private void processGroupProposal(Queue<Addition> queue, Proposal proposal) { 
		for (Proposal dependentProposal : proposal.getDependentProposals()) {
			processGroupProposal(queue, dependentProposal);
		}
		if (proposal instanceof Addition) {
//			assignItemIdentifier((Addition)proposal);
			queue.add((Addition)proposal);
		}
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
