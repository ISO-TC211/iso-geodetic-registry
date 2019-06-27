package org.iso.registry.client.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.iso.registry.api.initialization.IsoRegistryInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UpdateUserException;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.client.web.AbstractController;
import de.geoinfoffm.registry.client.web.ClientConfiguration;
import de.geoinfoffm.registry.client.web.OrganizationFormBean;
import de.geoinfoffm.registry.client.web.RegistryUserFormBean;
import de.geoinfoffm.registry.client.web.signup.SignupFormBean;
import de.geoinfoffm.registry.core.RegistryInitializer;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserGroup;
import de.geoinfoffm.registry.core.model.RegistryUserGroupRepository;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ItemClassRepository;

/**
 * Root controller.
 * 
 * @author Florian Esser
 * 
 */
@Controller
@ComponentScan(basePackages = { "org.iso.registry", "de.bespire.registry", "de.geoinfoffm.registry" })
public class SiteController extends AbstractController
{
	private static final Logger logger = LoggerFactory.getLogger(SiteController.class);

	@Autowired
	private RegistryUserGroupRepository groupRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private RegistryUserService userService;
	
	@Autowired
	private OrganizationService orgService;

	@Autowired
	private OrganizationRepository orgRepository;

	@Autowired
	private SubmittingOrganizationRepository suborgRepository;

	@Autowired
	private RegisterService registerService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RegisterItemService itemService;
	
	@Autowired
	private ProposalService proposalService;

	@Autowired
	private ViewResolver viewResolver;
	
	@Autowired
	private RegistrySecurity security;

	@Autowired
	private MutableAclService mutableAclService;
	
	@Autowired
	private DelegationRepository delegationRepository;
	
	@Autowired
	private RegistryInitializer initializer;

	private static boolean isInitializing = false;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof SignupFormBean) {
//			binder.setValidator(new SignupValidator(userService));
			binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		}
		if (binder.getTarget() instanceof RegistryUserFormBean) {
			binder.setValidator(new AdministrationValidator(userService));
		}
	}

	/**
	 * Handling method for root requests.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homeHandler() {
		return "home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginHandler() {
		return "login";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDeniedHandler() {
		return "403";
	}

	@RequestMapping(value = "/terms", method = RequestMethod.GET)
	public String termsHandler() {
		return "terms";
	}

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public String initialize(HttpServletRequest request) throws InvalidProposalException, UserRegistrationException, UnauthorizedException {
		return "init";
	}

	@RequestMapping(value = "/init/status", method = RequestMethod.GET)
	public ResponseEntity<String> initStatus(HttpServletRequest request) {
		switch (initializer.status()) {
			case IsoRegistryInitializer.STATUS_NOT_INITIALIZING:
				return new ResponseEntity<String>("Not initializing.", HttpStatus.NO_CONTENT);
			case IsoRegistryInitializer.STATUS_INITIALIZING:
				return new ResponseEntity<String>(initializer.log(), HttpStatus.PARTIAL_CONTENT);			
			case IsoRegistryInitializer.STATUS_DONE:
				return new ResponseEntity<String>(initializer.log(), HttpStatus.OK);
			default:
				return new ResponseEntity<String>("Unknown status: " + initializer.status(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/init/start", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<String> initializeStart(HttpServletRequest request) throws Exception {
		initializer.initializeRegistry();
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/testacl", method = RequestMethod.GET)
	@Transactional
	public void testAcl() {

	}
	
	@RequestMapping(value = "/acls", method = RequestMethod.GET)
	@Transactional
	public String acls() throws UnauthorizedException {
		security.assertHasRole(RegistrySecurity.ADMIN_ROLE);
		
		return "acls";
	}

	/**
	 * Displays the application error view.
	 * 
	 * May be used e.g. by unsuccessful AJAX calls to display the server-side exception to the user
	 *  
	 * @param model View model (injected)
	 * @param details POST parameter "errorDetails" to hold the server-side error message
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/error", method = RequestMethod.POST) 
	public String handleError(final Model model, @RequestParam("errorDetails") String details) throws UnsupportedEncodingException {
		// Log the error
		if (!StringUtils.isEmpty(details)) {
			logger.error(details);
			try {
				details = URLDecoder.decode(details, "UTF-8"); 
			}
			catch (Throwable t) {
				details = "Please check the server log for details.";
			}
			
			model.addAttribute("errorDetails", details);
		}
		else {
			model.addAttribute("errorDetails", "No error details available");			
		}
		
		return "error";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String registerUser(WebRequest request, @ModelAttribute("user") final SignupFormBean userData,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {
		
		if (!ClientConfiguration.isSignupEnabled()) {
			return "redirect:/";
		}

		request.removeAttribute("signupData", WebRequest.SCOPE_SESSION);

		List<Organization> orgs = orgRepository.findAll();
		model.addAttribute("organizations", orgs);

		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@Transactional
	public String createUser(WebRequest request, @Valid @ModelAttribute("user") final SignupFormBean userData,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes)
			throws Exception {

		if (!ClientConfiguration.isSignupEnabled()) {
			return "redirect:/";
		}

		if (bindingResult.hasErrors()) {
			List<Organization> orgs = orgRepository.findAll();
			model.addAttribute("organizations", orgs);
			return "signup";
		}

		if (StringUtils.isEmpty(userData.getPreferredLanguage())) {
			userData.setPreferredLanguage("en");
		}
		
		if (userData.isOrganizationNotListed()) {
			request.setAttribute("signupData", userData, WebRequest.SCOPE_SESSION);
			return "redirect:/signup/2";
		}
		
		RegistryUser user;

		CreateRegistryUserRequest registryUser = userData.toRegistrationDTO();
		
		user = this.registerUser(registryUser, redirectAttributes);

		return "redirect:/";
	}

	@RequestMapping(value = "/signup/2", method = RequestMethod.GET)
	public String registerUserOrganizationDetails(WebRequest request, @ModelAttribute("organization") final OrganizationFormBean organization,
			final Model model, final RedirectAttributes redirectAttributes) throws Exception {
	
		if (!ClientConfiguration.isSignupEnabled()) {
			return "redirect:/";
		}

		SignupFormBean signupData = (SignupFormBean)request.getAttribute("signupData", WebRequest.SCOPE_SESSION);
		if (signupData == null) {
			return "redirect:/signup";
		}
		
		model.addAttribute("isNew", "true");
		model.addAttribute("isSignup", "true");
		return "admin/organization";
	}

	@RequestMapping(value = "/signup/2", method = RequestMethod.POST)
	@Transactional
	public String createUserWithNewOrganization(WebRequest request, @Valid @ModelAttribute("organization") final OrganizationFormBean organizationData,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes)
			throws Exception {

		if (!ClientConfiguration.isSignupEnabled()) {
			return "redirect:/";
		}

		SignupFormBean userData = (SignupFormBean)request.getAttribute("signupData", WebRequest.SCOPE_SESSION);
		if (userData == null) {
			return "redirect:/signup";
		}

		RegistryUser user;

		CreateRegistryUserRequest registryUser = userData.toRegistrationDTO();
		CreateOrganizationRequest organization = organizationData.toRegistrationDTO();

		user = this.registerUser(registryUser, organization, redirectAttributes);

		return "redirect:/";
	}

	protected RegistryUser registerUser(CreateRegistryUserRequest userData, CreateOrganizationRequest organizationData, final RedirectAttributes redirectAttributes) 
			throws UserRegistrationException, UnauthorizedException, InvalidProposalException {
		
		if (!ClientConfiguration.isSignupEnabled()) {
			throw new UserRegistrationException("User registration is disabled");
		}

		RegistryUser user;
		boolean sendConfirmationMails = ClientConfiguration.isSendConfirmationMails();
		if (sendConfirmationMails) {
			userService.activateConfirmationMails();
		}
		
		try {
			if (organizationData == null) {
				user = userService.registerUser(userData);
			}
			else {
				user = userService.registerUser(userData, organizationData);				
			}
		}
		finally {
			userService.deactivateConfirmationMails();
		}

		if (sendConfirmationMails) {
			redirectAttributes.addFlashAttribute("signedUp", user.getEmailAddress().toString());
		}
		else {
			redirectAttributes.addFlashAttribute("signedUpNoConfirmation", user.getEmailAddress().toString());
		}
		
		return user;
	}

	protected RegistryUser registerUser(CreateRegistryUserRequest userData, final RedirectAttributes redirectAttributes)
			throws UserRegistrationException, UnauthorizedException, InvalidProposalException {
		
		return this.registerUser(userData, null, redirectAttributes);
	}

	@RequestMapping(value = "/signup/confirmation", method = RequestMethod.GET)
	@Transactional
	public String confirmMailAddress(WebRequest request,
								   @RequestParam("token") UUID token,
								   @RequestParam("mail") String emailAddress,
								   final Model model, 
								   final RedirectAttributes redirectAttributes) throws Exception {
		
		boolean success = userService.confirmUser(emailAddress, token);
		
		if (success) {
			redirectAttributes.addFlashAttribute("accountConfirmed", emailAddress);
		}
		else {
			redirectAttributes.addFlashAttribute("accountConfirmationFailed", emailAddress);
		}
		
		return "redirect:/";
	}

	@RequestMapping(value = "/myprofile", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewUserProfile(WebRequest request, HttpServletRequest servletRequest,
								@ModelAttribute("user") final RegistryUserFormBean userData,
							    final BindingResult bindingResult,
							    final Model model) throws Exception {
		if (!security.isLoggedIn()) {
			return "redirect:/";
		}		
		
		RegistryUser user = security.getCurrentUser();
		user = userService.findOne(user.getUuid());
		
		List<String> groups = new ArrayList<String>();
		List<String> roles = new ArrayList<String>();
		for (Role role : user.getRoles()) {
			if (role instanceof RegistryUserGroup) {
				groups.add(role.getName());
			}
			else {
				roles.add(role.getName());
			}
		}
		model.addAttribute("groups", groups);
		model.addAttribute("roles", roles);
		
		List<String> organizationRoles = new ArrayList<String>();
		for (Role role : user.getOrganization().getRoles()) {
			organizationRoles.add(role.getName());
		}
		model.addAttribute("orgRoles", organizationRoles);
		
		model.addAttribute("delegations", delegationRepository.findByActor(user));

		userData.initializeFromUser(user);
		
		model.addAttribute("user", userData);
		model.addAttribute("userObj", user);
		
		return "myprofile";
	}

	@RequestMapping(value = "/myprofile", method = RequestMethod.PUT) 
	public String updateUserProfile(WebRequest request,
					       		  @Valid @ModelAttribute("user") final RegistryUserFormBean userData,
					       		  final BindingResult bindingResult,
								  final RedirectAttributes redirectAttributes,
					       		  final Model model) throws Exception {
		
		if (!security.isLoggedIn()) {
			return "redirect:/";
		}
		
		RegistryUser user = security.getCurrentUser();
		user = userService.findOne(user.getUuid());
		
		if (bindingResult.hasErrors()) {
			List<String> groups = new ArrayList<String>();
			List<String> roles = new ArrayList<String>();
			for (Role role : user.getRoles()) {
				if (role instanceof RegistryUserGroup) {
					groups.add(role.getName());
				}
				else {
					roles.add(role.getName());
				}
			}
			model.addAttribute("groups", groups);
			model.addAttribute("roles", roles);
			
			List<String> organizationRoles = new ArrayList<String>();
			for (Role role : user.getOrganization().getRoles()) {
				organizationRoles.add(role.getName());
			}
			model.addAttribute("orgRoles", organizationRoles);
			
			model.addAttribute("delegations", delegationRepository.findByActor(user));
			
			RegistryUserFormBean userBean = new RegistryUserFormBean(user);
			
			model.addAttribute("user", userBean);
			model.addAttribute("userObj", user);
			
			return "myprofile";
		}
		
		user = userService.updateUser(userData.toUpdateDTO(user.getUuid()));
		if (userData.getOrganizationUuid() != null && !userData.getOrganizationUuid().equals(user.getOrganization().getUuid())) {
			Organization organization = orgRepository.findOne(userData.getOrganizationUuid());
			if (organization == null) {
				throw new UpdateUserException(String.format("User references non-existent organization %s", userData.getOrganizationUuid()));
			}
			user.setOrganization(organization);
			user = userRepository.save(user);
		}
		
		Iterable<Organization> orgs = orgService.findAll();
		model.addAttribute("organizations", orgs);
		
		redirectAttributes.addFlashAttribute("userProfileUpdated", user);
		
		return "redirect:/";
	}

	@RequestMapping(value = "/myprofile/delegation", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> requestDelegation (@RequestParam("userUuid") UUID userUuid, 
												   @RequestParam("organizationUuid") UUID orgUuid,
												   @RequestParam("roleName") String roleName,
												   final Model model) throws UnauthorizedException {

		Delegation delegation = security.requestDelegation(userUuid, orgUuid, roleName);
		
		if (delegation == null) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
