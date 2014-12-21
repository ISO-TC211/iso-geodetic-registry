/**
 * 
 */
package org.iso.registry.client.controller;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.iso.registry.client.controller.registry.ProposalNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.client.web.ClientConfiguration;
import de.geoinfoffm.registry.client.web.OrganizationFormBean;
import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.client.web.SignupFormBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalFactory;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.security.RegistryPermission;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.AppealRepository;
import de.geoinfoffm.registry.persistence.ProposalRepository;
import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

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
	private RegistryUserService userService;

	@Autowired
	private ProposalManagementInformationRepository pmiRepository;

	@Autowired
	private OrganizationService orgService;

	@Autowired
	private SubmittingOrganizationRepository suborgRepository;

	@Autowired
	private ProposalFactory proposalFactory;
	
	@Autowired
	private RegistrySecurity security;

	@Autowired
	private AdministrationController adminController;

	
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

		List<Proposal> proposals = proposalRepository.findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse();
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

		List<Proposal> proposals = proposalRepository.findByDateSubmittedIsNotNullAndGroupIsNullAndIsConcludedIsFalse();
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

	@RequestMapping(value = "/submitter", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String submitterOverview(final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.SUBMITTER_ROLE_PREFIX);
		
		List<RegisterItemViewBean> proposalViewBeans = new ArrayList<RegisterItemViewBean>();

		RE_SubmittingOrganization sponsor = suborgRepository.findAll().get(0);
		List<Proposal> proposals = proposalRepository.findBySponsorAndGroupIsNullAndIsConcludedIsFalse(sponsor);
		for (Proposal proposal : proposals) {
			if (!security.may(RegistryPermission.READ, proposal)) {
				continue;
			}

			Appeal appeal = appealRepository.findByAppealedProposal(proposal);
			
			if (appeal != null && !appeal.isDecided()) {
				proposalViewBeans.add(new RegisterItemViewBean(appeal));
			}
			else {
				if (!proposal.isFinal()) {
					proposalViewBeans.add(new RegisterItemViewBean(proposal));
				}
			}
		}

		model.addAttribute("proposals", proposalViewBeans);

		return "mgmt/submitter";
	}

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

	@RequestMapping(value = "/poc", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String pointOfContactOverview(@ModelAttribute("organization") final Organization organization, final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(POINTOFCONTACT_ROLE_PREFIX);

		model.addAttribute("isPoC", "true");

		RegistryUser currentUser = security.getCurrentUser();
		return adminController.viewOrganization(currentUser.getOrganization().getUuid().toString(), organization, model);
	}

	@Transactional
	@RequestMapping(value = "/poc", method = RequestMethod.PUT)
	public String updateOrganization(WebRequest request, 
								   @ModelAttribute("organization") final OrganizationFormBean organization, 
								   final BindingResult bindingResult, final Model model) throws UnauthorizedException {

		security.assertHasRoleWith(POINTOFCONTACT_ROLE_PREFIX);

		model.addAttribute("isPoC", "true");

		RegistryUser currentUser = security.getCurrentUser();
		UUID orgUuid = currentUser.getOrganization().getUuid();
		adminController.updateOrganization(request, orgUuid, organization, bindingResult, model);
		
		// TODO Flash attribute
		
		return "redirect:/management/poc";
	}
	
	@RequestMapping(value = "/poc/addUser", method = RequestMethod.GET)
	@Transactional
	public String pointOfContactNewUser(WebRequest request,
										@ModelAttribute("user") SignupFormBean userData,
									    final Model model) throws UnauthorizedException {

		request.removeAttribute("signupData", WebRequest.SCOPE_SESSION);

		security.assertHasRoleWith(POINTOFCONTACT_ROLE_PREFIX);

		model.addAttribute("isPoC", "true");

		RegistryUser currentUser = security.getCurrentUser();

		userData.setOrganizationUuid(currentUser.getOrganization().getUuid().toString());

		List<Organization> orgs = orgService.findAll();
		model.addAttribute("organizations", orgs);
		
		model.addAttribute("isOrganizationFixed", "true");
		
		return "signup";
	}


	@RequestMapping(value = "/poc/addUser", method = RequestMethod.POST)
	@Transactional
	public String createUser(WebRequest request, @Valid @ModelAttribute("user") final SignupFormBean userData,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes)
			throws Exception {

		if (bindingResult.hasErrors()) {
			List<Organization> orgs = orgService.findAll();
			model.addAttribute("organizations", orgs);
			return "signup";
		}

		if (StringUtils.isEmpty(userData.getPreferredLanguage())) {
			userData.setPreferredLanguage("en");
		}

		model.addAttribute("isPoC", "true");

		RegistryUser user;

		CreateRegistryUserRequest registryUser = userData.toRegistrationDTO();
		
		boolean sendConfirmationMails = ClientConfiguration.isSendConfirmationMails();
		
		user = userService.registerUser(registryUser);

		if (sendConfirmationMails) {
			redirectAttributes.addFlashAttribute("signedUp", user.getEmailAddress().toString());
		}
		else {
			redirectAttributes.addFlashAttribute("signedUpNoConfirmation", user.getEmailAddress().toString());
		}

		return "redirect:/management/poc";
	}

	@RequestMapping(value = "/poc/delegation", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity<Void> pointOfContactDenyDelegationRequest(@RequestParam("userUuid") UUID userUuid, 
																	@RequestParam("organizationUuid") UUID orgUuid,
																	@RequestParam("roleName") String roleName,
																	final Model model) throws UnauthorizedException {

		security.denyDelegationRequest(userUuid, orgUuid, roleName);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
