/**
 * 
 */
package org.iso.registry.client.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.EntityNotFoundException;
import de.geoinfoffm.registry.api.NoRevisionAtThisPointInTimeException;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.UpdateUserException;
import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.client.web.BasePathRedirectView;
import de.geoinfoffm.registry.client.web.NotFoundException;
import de.geoinfoffm.registry.client.web.OrganizationFormBean;
import de.geoinfoffm.registry.client.web.RegistryUserFormBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.NonExistentRevisionException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;

/**
 * Controller for the Administration views.
 * 
 * @author Florian Esser
 *
 */
@Controller
@RequestMapping("/admin")
public class AdministrationController
{	
	private static final Logger logger = LoggerFactory.getLogger(AdministrationController.class);

	@Autowired
	private RegistryUserService userService;

	@Autowired
	private RegistryUserRepository userRepository;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private DelegationRepository delegationRepository;

	@Autowired
	private RegisterItemService itemService;
	
	@Autowired
	private ProposalService proposalService;

	@Autowired
	private ProposalRepository proposalRepository;
	
	@Autowired
	private ViewResolver viewResolver;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RegistrySecurity security;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(new AdministrationValidator(userService, organizationService));
	}
	
	/**
	 * Fetches all {@link RegistryUser}s from the repository and puts them
	 * in the view model.
	 * 
	 * @param model Injected view model
	 * @return
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getAllUsers(Model model) {
		logger.debug("Request all users");

		List<RegistryUser> users = userService.findAll();
		
		model.addAttribute("users", users);
		
		return "admin/users";
	}
	
	/**
	 * Displays an empty view and allows for the creation of a new user.
	 * 
	 * @param userData The view-backing {@link RegistryUserPO} object
	 * @param model The view model
	 * @return The name of the view used
	 */
	@RequestMapping(value="/user/new", method = RequestMethod.GET)
	public String addNewUser(@ModelAttribute("user") RegistryUserFormBean userData, final Model model) {
		model.addAttribute("isNew", "true");
		List<Role> roles = roleRepository.findAll();
		model.addAttribute("roles", roles);
		Iterable<Organization> orgs = organizationService.findAll();
		model.addAttribute("organizations", orgs);
		
		return "admin/user";
	}

	/**
	 * Creates a new user.
	 * 
	 * @param user The view-backing {@link RegistryUserFormBean} object
	 * @param bindingResult The validation result
	 * @param model The view model
	 * @param redirectAttributes the {@link RedirectAttributes}
	 * @return The name of the view used
	 */
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/user/new")
	public String createRegistryUser(WebRequest request, @Valid @ModelAttribute("user") final RegistryUserFormBean userData, 
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) throws Exception {

		if (bindingResult.hasErrors()) {
			model.addAttribute("isNew", "true");
			List<Role> roles = roleRepository.findAll();
			model.addAttribute("roles", roles);
			Iterable<Organization> orgs = organizationService.findAll();
			model.addAttribute("organizations", orgs);
			return "admin/user";
		}
		
		CreateRegistryUserRequest registryUser = userData.toRegistrationDTO();
		
		Set<String> roles = new HashSet<String>();
		for (String paramName : request.getParameterMap().keySet()) {
			if (paramName.startsWith("ROLE_") && "on".equals(request.getParameter(paramName))) {
				// Rolle aktiviert
				roles.add(paramName);
			}
		}		
		registryUser.getRole().addAll(roles);
		
		RegistryUser user = userService.registerUser(registryUser);
		
		redirectAttributes.addFlashAttribute("createdUser", user.getUuid().toString());
		
		return "redirect:/admin/users";
	}

	/**
	 * Updates a user.
	 * 
	 * @param uuid ID of the user to display
	 * @param userData The view-backing {@link RegistryUserPO} object
	 * @param model The view model
	 * @return The name of the view used to render the user
	 * @throws UpdateUserException 
	 */
	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT) 
	public String updateUser(WebRequest request,
						   @PathVariable("id") UUID uuid, 
					       @Valid @ModelAttribute("user") final RegistryUserFormBean userData,
					       final BindingResult bindingResult,
					       final Model model) throws Exception {
		
		RegistryUser user = userService.findOne(uuid);
		
		if (user == null) {
			throw new NotFoundException(String.format("Cannot edit non-existent user %s", uuid));
		}
		
		if (bindingResult.hasErrors()) {
			List<Role> roles = roleRepository.findAll();
			model.addAttribute("roles", roles);
			Iterable<Organization> orgs = organizationService.findAll();
			model.addAttribute("organizations", orgs);
			return "admin/user";
		}
		
		for (String paramName : request.getParameterMap().keySet()) {
			if (paramName.startsWith("ROLE_") && "on".equals(request.getParameter(paramName))) {
				userData.addRole(paramName);
			}
		}
		
		user = userService.updateUser(userData.toUpdateDTO(uuid));
		
		return "redirect:/admin/users";
	}

	/**
	 * Finds and display details of a user.
	 * 
	 * @param uuid ID of the user to update
	 * @param user The view-backing {@link RegistryUserPO} object
	 * @param model The view model
	 * @return The name of the view used to render the user
	 * @throws IllegalOperationException 
	 */
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET) 
	public String viewUser(@PathVariable("id") String uuid, final Model model) throws IllegalOperationException {
		UUID id = UUID.fromString(uuid);
		RegistryUser registryUser = userService.findOne(id);
		
		if (registryUser == null) {
			throw new NotFoundException(String.format("Cannot edit non-existent user %s", uuid));
		}
		
		model.addAttribute("user", new RegistryUserFormBean(registryUser));
		Iterable<Organization> orgs = organizationService.findAll();
		model.addAttribute("organizations", orgs);
		List<Role> roles = roleRepository.findAll();
		model.addAttribute("roles", roles);
		
		return "admin/user";
	}

	/**
	 * Removes a user.
	 * 
	 * @param uuid ID of the user to remove.
	 * @throws UnauthorizedException 
	 */
	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public View deleteOrganization(@PathVariable("id") String uuid, final RedirectAttributes redirectAttributes) throws UnauthorizedException {
		try {
			UUID id = UUID.fromString(uuid);
			userService.delete(id);
			redirectAttributes.addFlashAttribute("deletedUser", "true");
		}
		catch (IllegalArgumentException e) {
			// Ignore
		}
		catch (NullPointerException e) {
			// Ignore
		}
		catch (EntityNotFoundException e) {
			throw new NotFoundException(String.format("Cannot remove non-existent user %s", uuid), e);
		}
		
		return new BasePathRedirectView("/admin/users");
	}
	
	/**
	 * Fetches all {@link Organization}s from the repository and puts them
	 * in the view model.
	 * 
	 * @param model Injected view model
	 * @param revision Backend revision
	 * @return
	 */
	@RequestMapping(value = "/organizations", method = RequestMethod.GET)
	public String getAllOrganizations(Model model, @RequestParam(value="rev", defaultValue="0") int revision,
			@RequestParam(value="revisionDate", defaultValue="") String revisionDate) {
		
		logger.debug("Request all organizations");
		
		Date revDate = null;
		if (revisionDate != null && !revisionDate.isEmpty()) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				revDate = df.parse(revisionDate);
			}
			catch (ParseException e) {
				// Ignore
			}
		}
		
		Calendar now = Calendar.getInstance();
		Iterable<Organization> orgs;
		if (revDate != null && !revDate.after(now.getTime())) {
			model.addAttribute("revisionDate", revisionDate);
			try {
				orgs = organizationService.findAll(revDate);
			}
			catch (NoRevisionAtThisPointInTimeException e) {
				orgs = organizationService.findAll();
				model.addAttribute("noRevisionAtGivenDate", "true");
			}
		}
		else if (revision <= 0) {
			orgs = organizationService.findAll();
		}
		else {
			try {
				orgs = organizationService.findAll(revision);
			}
			catch (NonExistentRevisionException e) {
				orgs = organizationService.findAll();
				model.addAttribute("noRevisionAtGivenDate", "true");				
			}
		}

		model.addAttribute("organizations", orgs);
		model.addAttribute("revision", revision);
		
		return "admin/organizations";
	}

	/**
	 * Handles {@link NotFoundException}s.
	 * 
	 * @return The error view
	 */
	@ExceptionHandler(NotFoundException.class)
	public String handleNotFoundException() {
		return "notfound";
	}
	
	/**
	 * Finds and display details of an organization.
	 * 
	 * @param uuid ID of the organization to display
	 * @param organization The view-backing {@link OrganizationPO} object
	 * @param model The view model
	 * @return The name of the view used to render the organization
	 * @throws UnauthorizedException 
	 */
	@RequestMapping(value = "/organization/{id}", method = RequestMethod.GET) 
	public String viewOrganization(@PathVariable("id") String uuid, @ModelAttribute("organization") final Organization organization, final Model model) throws UnauthorizedException {
		UUID id = UUID.fromString(uuid);
		Organization org = organizationService.findOne(id);
		
		if (org == null) {
			throw new NotFoundException(String.format("Cannot edit non-existent organization %s", uuid));
		}

		security.assertMayWrite(org);
		
		model.addAttribute("organization", new OrganizationFormBean(org));
		
		List<RegistryUser> users = userRepository.findByOrganization(org);
		model.addAttribute("users", users);
		
		List<Delegation> delegations = delegationRepository.findByDelegatingOrganization(org);
		model.addAttribute("delegations", delegations);
		
		return "admin/organization";
	}

	/**
	 * Displays an empty view and allows for the creation of a new organization.
	 * 
	 * @param organization The view-backing {@link OrganizationPO} object
	 * @param model The view model
	 * @return The name of the view used
	 */
	@RequestMapping(value="/organization/new", method = RequestMethod.GET)
	public String addNewOrganization(@ModelAttribute("organization") OrganizationFormBean organization, final Model model) {
		model.addAttribute("isNew", "true");
		return "admin/organization";
	}
	
	/**
	 * Updates an organization.
	 * 
	 * @param organization The view-backing {@link OrganizationPO} object
	 * @param bindingResult The validation result
	 * @param model The view model
	 * @return The name of the view used
	 * @throws UnauthorizedException 
	 */
	@Transactional
	@RequestMapping(value = "/organization/{id}", method = RequestMethod.PUT)
	public String updateOrganization(WebRequest request, 
								   @PathVariable("id") UUID id, 
								   @ModelAttribute("organization") final OrganizationFormBean organization, 
								   final BindingResult bindingResult, final Model model) throws UnauthorizedException {
		
		organization.setUuid(id);
		Organization org = organizationService.findOne(id);
		if (org == null) {
			throw new EntityNotFoundException(String.format("Organization %s does not exist", id));
		}

		security.assertMayWrite(org);

		List<Delegation> delegations = delegationRepository.findByDelegatingOrganization(org);

		// Organization role changes may only be performed by administrators (not by PoCs!) 
		if (security.isAdmin()) {
			for (String paramName : request.getParameterMap().keySet()) {
				if (paramName.startsWith("ROLE_") && "on".equals(request.getParameter(paramName))) {
					organization.addRole(paramName);
				}
			}
		}

		org = organizationService.updateOrganization(organization.toUpdateDTO(id));

		// Handle delegations separately to make sure that newly assigned roles are visible to
		// organizationService.delegate(...)
		for (String paramName : request.getParameterMap().keySet()) { 
			if (paramName.startsWith("DELEGATE_")) {
				String[] values = paramName.substring(9).split("_TO_");
				Role role = roleRepository.findByName(values[0]);
				RegistryUser user = userRepository.findOne(UUID.fromString(values[1]));

				Delegation delegation = organizationService.findDelegation(user, role, org);
				if ("on".equals(request.getParameter(paramName))) {
					if (delegation == null || !delegation.isApproved()) {
						delegation = organizationService.delegate(user, role, org);
					}
					
					delegations.remove(delegation);
				}
			}
		}
		
		for (Delegation delegation : delegations) {
			// Approved delegations left in the list were unchecked
			if (delegation.isApproved()) {
				organizationService.revokeDelegation(delegation);
			}
		}

		org = organizationService.updateOrganization(organization.toUpdateDTO(id));

		return "redirect:/admin/organizations";
	}

	/**
	 * Creates a new organization.
	 * 
	 * @param organization The view-backing {@link OrganizationPO} object
	 * @param bindingResult The validation result
	 * @param model The view model
	 * @param redirectAttributes the {@link RedirectAttributes}
	 * @return The name of the view used
	 * @throws UnauthorizedException 
	 */
	@RequestMapping(value = "/organization/new", method = RequestMethod.POST)
	public String createOrganization(@ModelAttribute("organization") final OrganizationFormBean organizationData, final BindingResult bindingResult, 
			final Model model, final RedirectAttributes redirectAttributes) throws UnauthorizedException {
		
		CreateOrganizationRequest organization = organizationData.toRegistrationDTO();

		Organization org = organizationService.createOrganization(organization);

		redirectAttributes.addFlashAttribute("createdOrganization", org.getUuid().toString());
		
		return "redirect:/admin/organizations";
	}

	/**
	 * Removes an organization.
	 * 
	 * @param uuid ID of the organization to remove.
	 * @throws UnauthorizedException 
	 */
	@RequestMapping(value = "/organization/{id}", method = RequestMethod.DELETE)
	public String removeRow(@PathVariable("id") String uuid, final RedirectAttributes redirectAttributes) throws UnauthorizedException {
		try {
			UUID id = UUID.fromString(uuid);
			organizationService.delete(id);
			redirectAttributes.addFlashAttribute("deletedOrganization", "true");
		}
		catch (IllegalArgumentException e) {
			// Ignore
		}
		catch (NullPointerException e) {
			// Ignore
		}
		catch (EntityNotFoundException e) {
			throw new NotFoundException(String.format("Cannot remove non-existent organization %s", uuid), e);
		}
		
		return "redirect:/admin/organizations";
	}	
	
	@RequestMapping(value = "/proposals", method = RequestMethod.GET)
	@Transactional
	public String viewProposals(final Model model) {
		List<RegisterItemViewBean> proposedItemViewBeans = new ArrayList<RegisterItemViewBean>();

		List<Proposal> proposals = proposalRepository.findAll();
		for (Proposal proposal : proposals) {
			if (proposal.hasGroup()) continue; // only show top-level proposals
			
			Appeal appeal = proposalService.findAppeal(proposal);
			if (appeal != null) {
				proposedItemViewBeans.add(new RegisterItemViewBean(appeal));
			}
			else {
				proposedItemViewBeans.add(new RegisterItemViewBean(proposal));
			}
		}
		
		model.addAttribute("proposals", proposedItemViewBeans);

		return "admin/proposals";
	}
}
