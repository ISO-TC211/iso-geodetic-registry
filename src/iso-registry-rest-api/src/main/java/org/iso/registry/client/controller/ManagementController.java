/**
 * 
 */
package org.iso.registry.client.controller;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.CONTROLBODY_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.MANAGER_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.POINTOFCONTACT_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.SUBMITTER_ROLE_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.iso.registry.client.controller.registry.ProposalNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.bespire.LoggerFactory;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.ProposalListItem;
import de.geoinfoffm.registry.api.ProposalListItemImpl;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.ProposalTaskManager;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.client.web.ClientConfiguration;
import de.geoinfoffm.registry.client.web.DatatableParameters;
import de.geoinfoffm.registry.client.web.DatatablesResult;
import de.geoinfoffm.registry.client.web.OrganizationFormBean;
import de.geoinfoffm.registry.client.web.signup.SignupFormBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.AppealDisposition;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequestRepository;
import de.geoinfoffm.registry.core.model.ProposalFactory;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.core.model.RegisterRelatedRole;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.core.security.RegistryUserUtils;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;
import de.geoinfoffm.registry.persistence.AppealRepository;

/**
 * @author Florian.Esser
 *
 */
@Controller
@RequestMapping("/management")
public class ManagementController
{
	private static final Logger logger = LoggerFactory.make();
	
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
	private ProposalService proposalService;
	
	@Autowired
	private RegistrySecurity security;

	@Autowired
	private AdministrationController adminController;

	@Autowired
	private ProposalWorkflowManager workflowManager;

	@Autowired
	private ProposalChangeRequestRepository pcrRepository;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ProposalTaskManager proposalTaskManager;

	@RequestMapping(value = "/owner", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String ownerOverview(final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.OWNER_ROLE_PREFIX);

		return "mgmt/owner";
	}

	@RequestMapping(value = "/owner/appeals", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody DatatablesResult getOwnerAppeals(@RequestParam Map<String, String> parameters) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.OWNER_ROLE_PREFIX);

		DatatableParameters dtParameters = new DatatableParameters(parameters);
		Pageable pageable = dtParameters.createPageable();

		Page<Appeal> appeals;
		if (!StringUtils.isEmpty(dtParameters.sSearch)) {
			appeals = appealRepository.findByDisposition(AppealDisposition.PENDING, "%" + dtParameters.sSearch + "%", pageable);
		}
		else {
			appeals = appealRepository.findByDisposition(AppealDisposition.PENDING, pageable);
		}

		Locale locale = LocaleContextHolder.getLocale();

		List<ProposalListItem> appealListItems = new ArrayList<ProposalListItem>();
		for (Appeal appeal : appeals) {
			if (appeal.isPending()) {
				appealListItems.add(new ProposalListItemImpl(appeal, messageSource, locale, workflowManager, pcrRepository));
			}
		}

		DatatablesResult result = new DatatablesResult(appeals.getTotalElements(), appeals.getTotalElements(), dtParameters.sEcho, appealListItems);
		return result;
	}

	@RequestMapping(value = "/controlbody", method = RequestMethod.GET)
	@Transactional
	public String controlBodyOverview(final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.CONTROLBODY_ROLE_PREFIX);

		return "mgmt/controlbody";
	}

	@RequestMapping(value = "/controlbody/proposals", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody DatatablesResult getControlBodyProposals(@RequestParam Map<String, String> parameters) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.CONTROLBODY_ROLE_PREFIX);

		DatatableParameters dtParameters = new DatatableParameters(parameters);
		Pageable pageable = dtParameters.createPageable();

		List<RE_Register> cbRegisters = new ArrayList<>();
		for (Role role : security.getCurrentUser().getRoles()) {
			if (role.getName().startsWith(CONTROLBODY_ROLE_PREFIX)) {
				if (role instanceof RegisterRelatedRole) {
					cbRegisters.add(((RegisterRelatedRole)role).getRegister());
				}
				else {
					throw new RuntimeException(String.format("Role type '%s' not yet implemented", role.getClass().getCanonicalName()));
				}
			}
		}
		
		List<ProposalGroup> groups = security.findGroupsByTargetRegisters(cbRegisters, ProposalWorkflowManager.STATUS_IN_APPROVAL_PROCESS);
		
		List<Proposal> proposals;
		if (!StringUtils.isEmpty(dtParameters.sSearch)) {
			proposals = proposalRepository.findByStatusAndParentIsNull(ProposalWorkflowManager.STATUS_IN_APPROVAL_PROCESS, "%" + dtParameters.sSearch + "%");
		}
		else {
			proposals = proposalRepository.findByStatusAndParentIsNull(ProposalWorkflowManager.STATUS_IN_APPROVAL_PROCESS);
		}
		logger.trace("getControlBodyProposals() : Found {} candidates", proposals.size());

		Locale locale = LocaleContextHolder.getLocale();
		
		List<UUID> uuids = new ArrayList<>();
		List<ProposalListItem> proposalViewBeans = new ArrayList<>();

		for (Proposal proposal : proposals) {
			boolean isControlBody = false;
			if (proposal instanceof Supersession) {
				isControlBody = security.isControlBody(proposal.getUuid());
			}
			else if (proposal instanceof ProposalGroup) {
				isControlBody = groups.contains(proposal);
			}
			else {
				isControlBody = security.isControlBody(proposal.getUuid()); 
			}
			if (isControlBody) {
				uuids.add(proposal.getUuid());
				logger.trace("getControlBodyProposals() : User is CB for proposal {} ({})", proposal.getTitle(), proposal.getUuid().toString());
			}
			else {
				logger.trace("getControlBodyProposals() : User is not CB for proposal {} ({})", proposal.getTitle(), proposal.getUuid().toString());				
			}
		}
		
		Page<Proposal> filteredProposals;
		DatatablesResult result;
		if (!uuids.isEmpty()) {
			filteredProposals = proposalRepository.findByUuids(uuids, pageable);
			for (Proposal proposal : filteredProposals) {
				ProposalListItem rvb = new ProposalListItemImpl(proposal, messageSource, locale, workflowManager, pcrRepository);

				if (proposalTaskManager.hasActiveTask(proposal)) {
					rvb.overrideProposalStatus(ProposalWorkflowManager.STATUS_PROCESSING);
				}
				
				proposalViewBeans.add(rvb);
			}

			result = new DatatablesResult(filteredProposals.getTotalElements(), filteredProposals.getTotalElements(), dtParameters.sEcho, proposalViewBeans);
		}
		else {
			result = new DatatablesResult(0, 0, dtParameters.sEcho, new ArrayList<>());
		}
		
		return result;
	}

	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	@Transactional
	public String managerOverview(final Model model) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.MANAGER_ROLE_PREFIX);

		return "mgmt/manager";
	}

	@RequestMapping(value = "/manager/proposals", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public @ResponseBody DatatablesResult getManagerProposals(@RequestParam Map<String, String> parameters) throws UnauthorizedException {
		security.assertHasRoleWith(MANAGER_ROLE_PREFIX);

		DatatableParameters dtParameters = new DatatableParameters(parameters);
		Pageable pageable = dtParameters.createPageable();

		Page<Proposal> proposals;
		if (parameters.containsKey("concluded") && "true".equals(parameters.get("concluded"))) {
			if (!StringUtils.isEmpty(dtParameters.sSearch)) {
				proposals = proposalRepository.findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsTrue("%" + dtParameters.sSearch + "%", pageable);
			}
			else {
				proposals = proposalRepository.findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsTrue(pageable);
			}
		}
		else {
			if (!StringUtils.isEmpty(dtParameters.sSearch)) {
				proposals = proposalRepository.findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsFalse("%" + dtParameters.sSearch + "%", pageable);
			}
			else {
				proposals = proposalRepository.findByDateSubmittedIsNotNullAndParentIsNullAndIsConcludedIsFalse(pageable);
			}
		}

		List<UUID> uuids = new ArrayList<>();
		for (Proposal proposal : proposals) {
			if (security.hasEntityRelatedRoleForAny(MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters())) {
				uuids.add(proposal.getUuid());
			}
		}
		
		List<ProposalListItem> proposalViewBeans = new ArrayList<>();
		if (!uuids.isEmpty()) {
			proposals = proposalRepository.findByUuids(uuids, pageable);
		}
	
		Locale locale = LocaleContextHolder.getLocale();

		for (Proposal proposal : proposals) {
			ProposalListItem rvb = new ProposalListItemImpl(proposal, messageSource, locale, workflowManager, pcrRepository);
			proposalViewBeans.add(rvb);
		}
		DatatablesResult result = new DatatablesResult(proposals.getTotalElements(), proposals.getTotalElements(), dtParameters.sEcho, proposalViewBeans);

		return result;
	}

	@RequestMapping(value = "/submitter", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String submitterOverview(final Model model) throws UnauthorizedException {
		security.assertIsSubmitter();
		
		return "mgmt/submitter";
	}

	@RequestMapping(value = "/submitter/creategroup", method = RequestMethod.POST)
	public ResponseEntity<String> createProposalGroup(@RequestParam("groupName") String groupName) throws InvalidProposalException {
		RegistryUser user = security.getCurrentUser();
		ProposalGroup group = proposalService.createProposalGroup(groupName, new ArrayList<Proposal>(), user.getOrganization().getSubmittingOrganization());

		return new ResponseEntity<>(group.getUuid().toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/submitter/proposals", method = RequestMethod.GET)
	public @ResponseBody DatatablesResult getSubmitterProposals(@RequestParam Map<String, String> parameters) throws UnauthorizedException {
		security.isSubmitter();

		DatatableParameters dtParameters = new DatatableParameters(parameters);
		Pageable pageable = dtParameters.createPageable();

		Page<Proposal> proposals;
		if (!StringUtils.isEmpty(dtParameters.sSearch)) {
			proposals = proposalRepository.findBySponsorAndParentIsNullAndIsConcludedIsFalse(RegistryUserUtils.getUserSponsor(userRepository), "%" + dtParameters.sSearch + "%", pageable);
		}
		else {
			proposals = proposalRepository.findBySponsorAndParentIsNullAndIsConcludedIsFalse(RegistryUserUtils.getUserSponsor(userRepository), pageable);
		}

		Locale locale = LocaleContextHolder.getLocale();
		List<ProposalListItem> proposalViewBeans = new ArrayList<>();

		for (Proposal proposal : proposals) {
			ProposalListItem rvb = new ProposalListItemImpl(proposal, messageSource, locale, workflowManager, pcrRepository);
			rvb.setSubmitter(security.hasEntityRelatedRoleForAll(SUBMITTER_ROLE_PREFIX, proposal.getAffectedRegisters()));
			proposalViewBeans.add(rvb);
		}
		DatatablesResult result = new DatatablesResult(proposals.getTotalElements(), proposals.getTotalElements(), dtParameters.sEcho, proposalViewBeans);

		return result;
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
		
		if (proposal.hasParent()) {
			throw new IllegalOperationException("Cannot review proposal has a parent proposal");
		}
		
		RegisterItemViewBean pvb = RegisterItemViewBean.forProposal(proposal, workflowManager);
		model.addAttribute("proposal", pvb);
		
		return "mgmt/review";
	}

	@RequestMapping(value = "/submitter/proposals/groupcandidates", method = RequestMethod.GET)
	public @ResponseBody DatatablesResult getGroupCandidateProposals(@RequestParam Map<String, String> parameters) throws UnauthorizedException {
		security.assertHasRoleWith(RegistrySecurity.SUBMITTER_ROLE_PREFIX);

		String sEcho = parameters.get("sEcho");
		String iDisplayStart = parameters.get("iDisplayStart");
		String iDisplayLength = parameters.get("iDisplayLength");
		String iColumns = parameters.get("iColumns");
		String iSortingsCols = parameters.get("iSortingCols");
		String sSearch = parameters.get("sSearch");
		
		Map<Integer, String> columns = new HashMap<Integer, String>();
		if (iColumns != null) {
			int columnCount = Integer.parseInt(iColumns);
			for (int i = 0; i < columnCount; i++) {
				columns.put(i, parameters.get("mDataProp_" + Integer.toString(i)));
			}
		}

		Map<String, String> sortingColumns = new HashMap<String, String>();
		if (iSortingsCols != null) {
			int sortingCols = Integer.parseInt(iSortingsCols);
			for (int i = 0; i < sortingCols; i++) {
				String sortingCol = parameters.get("iSortCol_" + Integer.toString(i));
				String direction = parameters.get("sSortDir_" + Integer.toString(i));
				sortingColumns.put(columns.get(Integer.parseInt(sortingCol)), direction);
			}
		}
		
		Pageable pageable;
		if (iDisplayStart != null && iDisplayLength != null) {
			int startAt = Integer.parseInt(iDisplayStart);
			int length = Integer.parseInt(iDisplayLength);
			int pageNo = startAt / length;
			
			Sort sort;
			if (!sortingColumns.isEmpty()) {
				List<Order> orders = new ArrayList<Order>();
				for (String property : sortingColumns.keySet()) {
					String sortCol = property;
					if (property.equals("proposalStatus")) {
						sortCol = "status";
					}
					Order order = new Order(Direction.fromString(sortingColumns.get(property).toString().toUpperCase()), sortCol);
					orders.add(order);
				}
				sort = new Sort(orders);
			}
			else {
				sort = new Sort(new Order("status"));
			}
			
			pageable = new PageRequest(pageNo, length, sort);
		}
		else {
			pageable = new PageRequest(0, 10);
		}
		
		List<Class<?>> excludedClasses = new ArrayList<>();
		excludedClasses.add(ProposalGroup.class);
		
		Page<Proposal> proposals;
		if (!StringUtils.isEmpty(sSearch)) {
			proposals = proposalRepository.findBySponsorAndParentIsNullAndDateSubmittedIsNullAndTypeNotIn(RegistryUserUtils.getUserSponsor(userRepository), excludedClasses, "%" + sSearch + "%", pageable);
		}
		else {
			proposals = proposalRepository.findBySponsorAndParentIsNullAndDateSubmittedIsNullAndTypeNotIn(RegistryUserUtils.getUserSponsor(userRepository), excludedClasses, pageable);
		}

		Locale locale = LocaleContextHolder.getLocale();
		
		List<ProposalListItem> proposalViewBeans = new ArrayList<>();

		for (Proposal proposal : proposals) {
			ProposalListItem rvb = new ProposalListItemImpl(proposal, messageSource, locale, workflowManager, pcrRepository);
			proposalViewBeans.add(rvb);
		}

		DatatablesResult result = new DatatablesResult(proposals.getTotalElements(), proposals.getTotalElements(), sEcho, proposalViewBeans);
		
		return result;
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
