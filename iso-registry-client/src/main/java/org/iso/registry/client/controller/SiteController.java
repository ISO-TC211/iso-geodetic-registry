package org.iso.registry.client.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.iso.registry.api.AreaItemProposalDTO;
import org.iso.registry.api.CoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.core.model.CoordinateSystemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.client.web.AbstractController;
import de.geoinfoffm.registry.client.web.SignupFormBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.RegistersChangedEvent;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserGroup;
import de.geoinfoffm.registry.core.model.RegistryUserGroupRepository;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

/**
 * Root controller.
 * 
 * @author Florian Esser
 * 
 */
@Controller
@ComponentScan(basePackages = { "org.iso.registry", "de.geoinfoffm.registry" })
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
	private SubmittingOrganizationRepository suborgRepository;

	@Autowired
	private RegisterService registerService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RegisterItemService itemService;

	@Autowired
	private ViewResolver viewResolver;
	
	@Autowired
	private RegistrySecurity security;

	@Autowired
	private MutableAclService mutableAclService;
	
	private StringBuilder initLog;
	
	private static boolean isInitializing = false;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
//		if (binder.getTarget() instanceof SignupFormBean) {
//			binder.setValidator(new SignupValidator(userService));
//		}
	}

	/**
	 * Handling method for root requests.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homeHandler() {
		return "home";
	}

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public String initialize(HttpServletRequest request) throws InvalidProposalException, UserRegistrationException, UnauthorizedException {
		return "init";
	}

	@RequestMapping(value = "/init/status", method = RequestMethod.GET)
	public ResponseEntity<String> initStatus(HttpServletRequest request) {
		if (initLog == null) {
			return new ResponseEntity<String>("Not initializing.", HttpStatus.NO_CONTENT);
		}
		else if (isInitializing) {
			return new ResponseEntity<String>(initLog.toString(), HttpStatus.PARTIAL_CONTENT);			
		}
		else {
			return new ResponseEntity<String>(initLog.toString(), HttpStatus.OK);						
		}
	}

	@RequestMapping(value = "/init/start", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<String> initializeStart(HttpServletRequest request) throws InvalidProposalException, UserRegistrationException, UnauthorizedException {
		isInitializing = true;
		initLog = new StringBuilder();
		
		initializeRegistry();
		loadExampleData();
		isInitializing = false;

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


	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String registerUser(WebRequest request, @ModelAttribute("user") final SignupFormBean userData,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) {

		request.removeAttribute("signupData", WebRequest.SCOPE_SESSION);
		
		return "signup";
	}

	@Transactional
	private synchronized void initializeRegistry() {
		initLog.append("Initializing registry...\n\n");

		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Authentication authentication = new PreAuthenticatedAuthenticationToken("SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			RegistryUserGroup adminGroup = groupRepository.findByName("ROLE_ADMIN");  
			if (adminGroup == null) {
				initLog.append("> Creating ROLE_ADMIN...");
				adminGroup = new RegistryUserGroup("ROLE_ADMIN");
				groupRepository.save(adminGroup);
				initLog.append("done\n");
			}
			
			CI_ResponsibleParty respExample = new CI_ResponsibleParty("Johne Doe", null, null, CI_RoleCode.USER);
			RE_SubmittingOrganization orgExample = new RE_SubmittingOrganization("EXAMPLE", respExample);
			suborgRepository.save(orgExample);
			
			RegistryUser rt = createUser("René Thiele", "r", "rene.thiele@geoinfoffm.de", adminGroup);
			RegistryUser ex = createUser("John Submitter", "s", "submitter@example.org");

			initLog.append("\n");
			
			String registerName = "EPSG Geodetic Parameter Data Set";
			RE_Register r = registerService.findByName(registerName); 
			if (r == null) {
				initLog.append("> Creating register...\n");
				r = registerService.createRegister(
						registerName,
						rt, rt, rt,
						roleService, 
						RE_Register.class,
						new ParameterizedRunnable<RE_Register>() {
							@Override
							public void run(RE_Register parameter) {
							}
						}
				);
				
				eventPublisher().publishEvent(new RegistersChangedEvent(this));

				initLog.append(String.format(">>> '%s'", r.getName()));
				initLog.append(String.format(" (owner = %s; manager = %s)\n", r.getOwner().getName(), r.getManager().getName()));
				
				initLog.append("\n");
			}

			Role submitterRole = registerService.getSubmitterRole(r);
			initLog.append(">>> Adding submitter ");
			initLog.append(ex.getEmailAddress());
			ex.assignRole(submitterRole);
			initLog.append("\n");
			initLog.append("\n");

			RE_ItemClass icCrs = this.addItemClass("CoordinateReferenceSystem", r);
			RE_ItemClass icArea = this.addItemClass("Area", r);

			final RE_RegisterItem worldArea = this.registerItem(r, icArea, "World", AreaItemProposalDTO.class, new ParameterizedRunnable<AreaItemProposalDTO>() {
				@Override
				public void run(AreaItemProposalDTO parameter) {
					parameter.setCode(1262);
				}
			});
			this.registerItem(r, icArea, "Germany - west of 7.5°E", AreaItemProposalDTO.class, new ParameterizedRunnable<AreaItemProposalDTO>() {
				@Override
				public void run(AreaItemProposalDTO parameter) {
					parameter.setCode(1624);
				}
			});

			this.registerItem(r, icCrs, "WGS 84", CoordinateReferenceSystemItemProposalDTO.class, new ParameterizedRunnable<CoordinateReferenceSystemItemProposalDTO>() {
				@Override
				public void run(CoordinateReferenceSystemItemProposalDTO parameter) {
					parameter.setCode(4326);
					parameter.setAreaUuid(worldArea.getUuid());
					parameter.setScope("Horizontal component of 3D system. Used by the GPS satellite navigation system and for NATO military geodetic surveying.");
					parameter.setType(CoordinateSystemType.GEOGRAPHIC_2D);
				}
			});

			initLog.append("Initialization complete.");
		}
		catch (Throwable t) {
			t.printStackTrace();
			initLog.append("\n\n");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			initLog.append(sw.toString());
		}
		finally {
			SecurityContextHolder.getContext().setAuthentication(currentAuth);
		}
	}
	
	private RE_ItemClass addItemClass(String name, RE_Register r) {
		RE_ItemClass ic = null;
		for (RE_ItemClass itemClass : r.getContainedItemClasses()) {
			if (itemClass.getName().equals(name)) {
				ic = itemClass;
				break;
			}
		}
		
		if (ic == null) {
			initLog.append(String.format("> Adding item class to register '%s'...\n", r.getName()));
			ic = new RE_ItemClass();
			ic.setName(name);
			r.addContainedItemClass(ic);
			ic = itemClassRepository.save(ic);

			initLog.append(String.format(">>> %s\n", ic.getName()));
			
			initLog.append("\n");
		}
		
		return ic;
	}

	public <P extends RegisterItemProposalDTO> RE_RegisterItem registerItem(RE_Register register, RE_ItemClass itemClass, String name, Class<P> dtoClass, ParameterizedRunnable<P> paramSetter) throws InvalidProposalException, InstantiationException, IllegalAccessException {
		P proposal;
		proposal = dtoClass.newInstance();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(suborgRepository.findAll().get(0).getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setName(name);
		proposal.setDefinition("Definition");
		proposal.setJustification("Justification");
		
		paramSetter.run(proposal);

		initLog.append(String.format("> Adding item '%s' of class '%s' to register '%s'...", proposal.getName(), itemClass.getName(), register.getName()));
		
		Addition ai = itemService.proposeAddition(proposal);
		String decisionEvent = "Decision event";
		acceptProposal(ai, decisionEvent);
		
		initLog.append("done.\n");
		
		return ai.getItem();
	}

	protected void acceptProposal(Addition ai, String decisionEvent) throws InvalidProposalException {
		try {
			itemService.reviewProposal(ai);
			itemService.acceptProposal(ai, decisionEvent);
		}
		catch (IllegalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Transactional
	public synchronized void loadExampleData() throws InvalidProposalException, UserRegistrationException, UnauthorizedException {
		initLog.append("\n");
		initLog.append("===========================================================\n");
		initLog.append("Loading example data...\n\n");
		
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Authentication authentication = new PreAuthenticatedAuthenticationToken("SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			SecurityContextHolder.getContext().setAuthentication(authentication);


			initLog.append("\n\n\n");
			initLog.append("Finished. Please click Start to continue.");
		}
		catch (Throwable t) {
			t.printStackTrace();
			initLog.append("\n\n");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			initLog.append(sw.toString());
		}
		finally {
			SecurityContextHolder.getContext().setAuthentication(currentAuth);
		}
	}

	protected RegistryUser createUser(String name, String password, String mail, Role... roles)
			throws UserRegistrationException, UnauthorizedException {
		
		RegistryUser existingUser = userRepository.findByEmailAddress(mail);
		
		if (existingUser != null) {
			return existingUser;
		}

		CreateRegistryUserRequest req = new CreateRegistryUserRequest();
		req.setName(name);
		req.setPassword(password);
		req.setEmailAddress(mail);
		req.setPreferredLanguage("de");
		req.setActive(true);
		for (Role role : roles) {
			req.getRole().add(role.getName());
		}
		
		initLog.append(">>> ");
		initLog.append(mail);
		initLog.append("\n");

		return userService.registerUser(req);
	}

	private static String leadingZero(int i) {
		if (i < 10) {
			return "0" + Integer.toString(i);
		}
		else {
			return Integer.toString(i);
		}
	}

}
