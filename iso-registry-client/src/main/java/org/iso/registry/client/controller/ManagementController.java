/**
 * 
 */
package org.iso.registry.client.controller;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.MANAGER_ROLE_PREFIX;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.iso.registry.client.controller.registry.ProposalNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalFactory;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.AppealRepository;
import de.geoinfoffm.registry.persistence.ProposalRepository;

/**
 * @author Florian.Esser
 *
 */
@Controller
@RequestMapping("/management")
public class ManagementController
{
	@Autowired
	private AppealRepository appealRepository;

	@Autowired
	private ProposalRepository proposalRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;

	@Autowired
	private ProposalManagementInformationRepository pmiRepository;
	
	@Autowired
	private ProposalFactory proposalFactory;
	
	@Autowired
	private RegistrySecurity security;
	
	@RequestMapping(value = "/owner", method = RequestMethod.GET)
	@Transactional
	public String ownerOverview(final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.OWNER_ROLE_PREFIX);
		
		List<RegisterItemViewBean> appealedItemViewBeans = new ArrayList<RegisterItemViewBean>();

		List<Appeal> appeals = appealRepository.findAll();
		for (Appeal appeal : appeals) {
			if (appeal.isPending()) {
				appealedItemViewBeans.add(new RegisterItemViewBean(appeal));
			}
		}

		model.addAttribute("appealedItems", appealedItemViewBeans);

		return "mgmt/owner";
	}

	@RequestMapping(value = "/controlbody", method = RequestMethod.GET)
	@Transactional
	public String controlBodyOverview(final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.CONTROLBODY_ROLE_PREFIX);

		List<RegisterItemViewBean> proposalViewBeans = new ArrayList<RegisterItemViewBean>();

		List<Proposal> proposals = proposalRepository.findByGroupIsNull();
		for (Proposal proposal : proposals) {
			if (proposal.isPending() && proposal.isReviewed()) {
				proposalViewBeans.add(new RegisterItemViewBean(proposal));
			}
		}
//		List<RE_ProposalManagementInformation> proposals = pmiRepository.findByStatusAndDateProposedIsNotNull(RE_DecisionStatus.PENDING);	
//		for (RE_ProposalManagementInformation proposal : proposals) {
//			proposalViewBeans.add(new RegisterItemViewBean(proposalFactory.createProposal(proposal)));
//		}

		model.addAttribute("proposals", proposalViewBeans);

		return "mgmt/controlbody";
	}

	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	@Transactional
	public String managerOverview(final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.MANAGER_ROLE_PREFIX);

		List<RegisterItemViewBean> proposalViewBeans = new ArrayList<RegisterItemViewBean>();

		List<Proposal> proposals = proposalRepository.findByGroupIsNull();
		for (Proposal proposal : proposals) {
			if (!security.hasEntityRelatedRoleForAll(MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters())) {
				continue;
			}

//			if (!proposal.isReviewed() || proposal.isPending()) {
			if (!proposal.isFinal()) {
				proposalViewBeans.add(new RegisterItemViewBean(proposal));
			}
		}

		model.addAttribute("proposals", proposalViewBeans);

		return "mgmt/manager";
	}

//	@RequestMapping(value = "/submitter", method = RequestMethod.GET)
//	@Transactional(readOnly = true)
//	public String submitterOverview(final Model model) throws UnauthorizedException {
//		security.assertHasRoleWith(RegistrySecurity.SUBMITTER_ROLE_PREFIX);
//		
//		List<RegisterItemViewBean> proposalViewBeans = new ArrayList<RegisterItemViewBean>();
//		
//		List<Proposal> proposals = proposalRepository.findBySponsorAndGroupIsNull(RegistryUserUtils.getUserSponsor(userRepository));
//		for (Proposal proposal : proposals) {
////			if (!security.hasEntityRelatedRoleForAll(SUBMITTER_ROLE_PREFIX, proposal.getAffectedRegisters())) {
//			if (!security.may(READ, proposal)) {
//				continue;
//			}
//
//			Appeal appeal = appealRepository.findByAppealedProposal(proposal);
//			
//			if (appeal != null && !appeal.isDecided()) {
//				proposalViewBeans.add(new RegisterItemViewBean(appeal));
//			}
//			else {
//				if (!proposal.isFinal()) {
//					proposalViewBeans.add(new RegisterItemViewBean(proposal));
//				}
//			}
//		}
//
//		model.addAttribute("proposals", proposalViewBeans);
//
//		return "mgmt/submitter";
//	}

	@RequestMapping(value = "/review/{uuid}", method = RequestMethod.GET)
	@Transactional
	public String reviewProposal(@PathVariable("uuid") UUID uuid, final Model model) throws ProposalNotFoundException, IllegalOperationException, UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.MANAGER_ROLE_PREFIX);

//		RE_ProposalManagementInformation pmi = pmiRepository.findOne(uuid);
//
//		RegisterItemViewBean pvb = new RegisterItemViewBean(proposalFactory.createProposal(pmi));
		Proposal proposal = proposalRepository.findOne(uuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(uuid);
		}
		
		if (proposal.hasGroup()) {
			throw new IllegalOperationException("Cannot review proposal that is part of a proposal group");
		}
		
		RegisterItemViewBean pvb = new RegisterItemViewBean(proposal);
		model.addAttribute("proposal", pvb);
		
		return "mgmt/review";
	}

}
