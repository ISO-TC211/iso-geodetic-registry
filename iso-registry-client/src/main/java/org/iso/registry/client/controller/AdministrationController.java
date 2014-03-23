/**
 * 
 */
package org.iso.registry.client.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.EntityNotFoundException;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.UpdateUserException;
import de.geoinfoffm.registry.client.web.BasePathRedirectView;
import de.geoinfoffm.registry.client.web.NotFoundException;
import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.client.web.RegistryUserFormBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.persistence.ProposalRepository;
import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

/**
 * Controller for the Administration views.
 * 
 * @author Florian Esser
 *
 */
@Controller
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
public class AdministrationController
{	
	private static final Logger logger = LoggerFactory.getLogger(AdministrationController.class);

	@Autowired
	private RegistryUserService userService;

	@Autowired
	private RegistryUserRepository userRepository;

	@Autowired
	private RegisterItemService itemService;

	@Autowired
	private ProposalRepository proposalRepository;
	
	@Autowired
	private ViewResolver viewResolver;
	
	@Autowired
	private RoleRepository roleRepository;

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
		
		return "/admin/users";
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
	
	@RequestMapping(value = "/proposals", method = RequestMethod.GET)
	@Transactional
	public String viewProposals(final Model model) {
		List<RegisterItemViewBean> proposedItemViewBeans = new ArrayList<RegisterItemViewBean>();

		List<Proposal> proposals = proposalRepository.findAll();
		for (Proposal proposal : proposals) {
			if (proposal.hasGroup()) continue; // only show top-level proposals
			
			Appeal appeal = itemService.findAppeal(proposal);
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
