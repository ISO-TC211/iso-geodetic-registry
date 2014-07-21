package org.iso.registry.client.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.AreaItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.GeodeticCoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemAxisItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.DatumItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.EllipsoidItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.PrimeMeridianItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.AxisDTO;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.datum.GeodeticDatumItem;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.iso.registry.core.model.iso19103.MeasureType;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UpdateUserException;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.client.web.AbstractController;
import de.geoinfoffm.registry.client.web.ClientConfiguration;
import de.geoinfoffm.registry.client.web.OrganizationFormBean;
import de.geoinfoffm.registry.client.web.RegistryUserFormBean;
import de.geoinfoffm.registry.client.web.SignupFormBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.RegistersChangedEvent;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserGroup;
import de.geoinfoffm.registry.core.model.RegistryUserGroupRepository;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.soap.CreateOrganizationRequest;
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

	private StringBuilder initLog;
	
	private static boolean isInitializing = false;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof SignupFormBean) {
			binder.setValidator(new SignupValidator(userService));
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

		List<Organization> orgs = orgRepository.findAll();
		model.addAttribute("organizations", orgs);

		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@Transactional
	public String createUser(WebRequest request, @Valid @ModelAttribute("user") final SignupFormBean userData,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes)
			throws Exception {

		if (bindingResult.hasErrors()) {
			List<Organization> orgs = orgRepository.findAll();
			model.addAttribute("organizations", orgs);
			return "signup";
		}

		if (StringUtils.isEmpty(userData.getPreferredLanguage())) {
			userData.setPreferredLanguage("de");
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

			Organization isotc211 = createOrganization("ISO/TC 211");
			
			RegistryUser regman = createUser("ISO Register Manager", "m", "regman@example.org", isotc211);
			RegistryUser admin = createUser("ISO Registry Administrator", "a", "admin@example.org", isotc211, adminGroup);

			initLog.append("\n");
			
			String registerName = "Geodetic Codes & Parameters";
			RE_Register r = registerService.findByName(registerName); 
			if (r == null) {
				initLog.append("> Creating register...\n");
				r = registerService.createRegister(
						registerName,
						regman, regman, regman,
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

//			Role submitterRole = registerService.getSubmitterRole(r);
//			initLog.append(">>> Adding submitter ");
//			initLog.append(admin.getEmailAddress());
//			xxx.assignRole(submitterRole);
//			initLog.append("\n");
//			initLog.append("\n");
			
			/*
			 * WGS 84 consists of:
			 * 
			 * - Area 1262 (World)
			 * - Coordinate System 6422 (Ellipsoidal 2D CS, lat/lon, N/E, degree)
			 *   - Axis 106 (north, abbr. "Lat") 
			 *     - UoM 9122
			 *   - Axis 107 (east, abbr. "Long")
			 *     - UoM 9122
			 * - Datum 6326 (Geodetic)
			 *   - Ellipsoid 7030
			 *     - Axis UoM 9001 (length: metre)
			 *   - Prime Meridian 8901
			 *     - Longitude UoM 9102
			 *   - Area 1262
			 *   
			 */

			/**
			 * Areas
			 */
			RE_ItemClass icArea = this.addItemClass("Area", r);
			final RE_RegisterItem worldArea = this.registerItem(r, icArea, "World", BigInteger.valueOf(1262L), isotc211, AreaItemProposalDTO.class,
					new ParameterizedRunnable<AreaItemProposalDTO>() {
						@Override
						public void run(AreaItemProposalDTO parameter) {
							parameter.setCode(1262);
							parameter.setSouthBoundLatitude(-90.0);
							parameter.setNorthBoundLatitude(+90.0);
							parameter.setWestBoundLongitude(-180.0);
							parameter.setEastBoundLongitude(+180.0);
						}
					});
			this.registerItem(r, icArea, "Germany - west of 7.5°E", BigInteger.valueOf(1624L), isotc211, AreaItemProposalDTO.class,
					new ParameterizedRunnable<AreaItemProposalDTO>() {
						@Override
						public void run(AreaItemProposalDTO parameter) {
							parameter.setCode(1624);
							parameter.setSouthBoundLatitude(49.1);
							parameter.setNorthBoundLatitude(53.75);
							parameter.setWestBoundLongitude(5.87);
							parameter.setEastBoundLongitude(7.5);
						}
					});
			
			/**
			 * Units of Measurement
			 */
			RE_ItemClass icUoM = this.addItemClass("UnitOfMeasure", r);
			final RE_RegisterItem uom9122 = this.registerItem(r, icUoM,
					"degree (supplier to define representation)",
					BigInteger.valueOf(9122L), 
					isotc211,
					UnitOfMeasureItemProposalDTO.class,
					new ParameterizedRunnable<UnitOfMeasureItemProposalDTO>() {
						@Override
						public void run(UnitOfMeasureItemProposalDTO parameter) {
							parameter.setMeasureType(MeasureType.ANGLE);
							parameter.setSymbol("");
						}
					});
			final RE_RegisterItem uom9102 = this.registerItem(r, icUoM,
					"degree",
					BigInteger.valueOf(9102L), 
					isotc211,
					UnitOfMeasureItemProposalDTO.class,
					new ParameterizedRunnable<UnitOfMeasureItemProposalDTO>() {
						@Override
						public void run(UnitOfMeasureItemProposalDTO parameter) {
							parameter.setMeasureType(MeasureType.ANGLE);
							parameter.setSymbol("");
						}
					});
			final RE_RegisterItem uom9001 = this.registerItem(r, icUoM,
					"metre",
					BigInteger.valueOf(9001L), 
					isotc211,
					UnitOfMeasureItemProposalDTO.class,
					new ParameterizedRunnable<UnitOfMeasureItemProposalDTO>() {
						@Override
						public void run(UnitOfMeasureItemProposalDTO parameter) {
							parameter.setMeasureType(MeasureType.LENGTH);
							parameter.setSymbol("m");
						}
					});
					
			/**
			 * Coordinate System Axes
			 */
			RE_ItemClass icAxis = this.addItemClass("CoordinateSystemAxis", r);
			final RE_RegisterItem axisLat = this.registerItem(r, icAxis,
					"Geodetic latitude",
					BigInteger.valueOf(10106L), 
					isotc211,
					CoordinateSystemAxisItemProposalDTO.class,
					new ParameterizedRunnable<CoordinateSystemAxisItemProposalDTO>() {
						@Override
						public void run(CoordinateSystemAxisItemProposalDTO parameter) {
							parameter.setCode(106);
							parameter.setAxisAbbreviation("Lat");
							parameter.setAxisDirection(CS_AxisDirection.NORTH);
							parameter.setAxisUnit(new UnitOfMeasureItemProposalDTO((UnitOfMeasureItem)uom9122));
						}
					});
			final RE_RegisterItem axisLon = this.registerItem(r, icAxis,
					"Geodetic longitude",
					BigInteger.valueOf(10107L), 
					isotc211,
					CoordinateSystemAxisItemProposalDTO.class,
					new ParameterizedRunnable<CoordinateSystemAxisItemProposalDTO>() {
						@Override
						public void run(CoordinateSystemAxisItemProposalDTO parameter) {
							parameter.setCode(107);
							parameter.setAxisAbbreviation("Lon");
							parameter.setAxisDirection(CS_AxisDirection.EAST);
							parameter.setAxisUnit(new UnitOfMeasureItemProposalDTO((UnitOfMeasureItem)uom9122));
						}
					});
			
			/**
			 * Coordinate System
			 */
			RE_ItemClass icEllipsoidalCs = this.addItemClass("EllipsoidalCS", r);
			final RE_RegisterItem cs6422 = this.registerItem(r, icEllipsoidalCs, 
					"Ellipsoidal 2D CS. Axes: latitude, longitude. Orientations: north, east. UoM: degree",
					BigInteger.valueOf(6422L), 
					isotc211,
					CoordinateSystemItemProposalDTO.class,
					new ParameterizedRunnable<CoordinateSystemItemProposalDTO>() {
						@Override
						public void run(CoordinateSystemItemProposalDTO p) {
							p.setCode(6422);
							p.setRemarks("Coordinates referenced to this CS are in degrees. Any degree representation "
									+ "(e.g. DMSH, decimal, etc.) may be used but that used must be declared for the "
									+ "user by the supplier of data. Used in geographic 2D coordinate reference systems.");
							p.addAxis(new AxisDTO((CoordinateSystemAxisItem)axisLat));
							p.addAxis(new AxisDTO((CoordinateSystemAxisItem)axisLon));
						}
					});

			/** 
			 * Ellipsoid
			 */
			RE_ItemClass icEllipsoid = this.addItemClass("Ellipsoid", r);
			final RE_RegisterItem el7030 = this.registerItem(r, icEllipsoid, 
					"WGS 84", 
					BigInteger.valueOf(7030L), 
					isotc211,
					EllipsoidItemProposalDTO.class,
					new ParameterizedRunnable<EllipsoidItemProposalDTO>() {
						@Override
						public void run(EllipsoidItemProposalDTO p) {
							p.setCode(7030);
							p.setRemarks("Inverse flattening derived from four defining parameters (semi-major axis; C20 = -484.16685*10e-6; "
									+ "earth's angular velocity w = 7292115e11 rad/sec; gravitational constant GM = 3986005e8 m*m*m/s/s).");
							p.setSemiMajorAxis(6378137.0);
							p.setSemiMajorAxisUom(new UnitOfMeasureItemProposalDTO((UnitOfMeasureItem)uom9001));
							p.setInverseFlattening(298.257223563);
							p.setSphere(false);
						}
					});
			
			/**
			 * Prime Meridian
			 */
			RE_ItemClass icPrimeMeridian = this.addItemClass("PrimeMeridian", r);
			final RE_RegisterItem pm8901 = this.registerItem(r, icPrimeMeridian, 
					"Greenwich", 
					BigInteger.valueOf(8901L), 
					isotc211,
					PrimeMeridianItemProposalDTO.class,
					new ParameterizedRunnable<PrimeMeridianItemProposalDTO>() {
						@Override
						public void run(PrimeMeridianItemProposalDTO p) {
							p.setCode(8901);
							p.setGreenwichLongitude(0.0);
							p.setGreenwichLongitudeUom(new UnitOfMeasureItemProposalDTO((UnitOfMeasureItem)uom9102));
						}
					});
			
			/**
			 * Geodetic Datum
			 */
			RE_ItemClass icGeodeticDatum = this.addItemClass("GeodeticDatum", r);
			final RE_RegisterItem dt6326 = this.registerItem(r, icGeodeticDatum, 
					"World Geodetic System 1984", 
					BigInteger.valueOf(6326L), 
					isotc211,
					DatumItemProposalDTO.class,
					new ParameterizedRunnable<DatumItemProposalDTO>() {
						@Override
						public void run(DatumItemProposalDTO p) {
							p.setCode(6326);
							p.setEllipsoid(new EllipsoidItemProposalDTO((EllipsoidItem)el7030));
							p.setPrimeMeridian(new PrimeMeridianItemProposalDTO((PrimeMeridianItem)pm8901));
						}
					});
			
			/**
			 * Coordinate Reference System
			 */
			RE_ItemClass icGeodeticCrs = this.addItemClass("GeodeticCRS", r);
			this.registerItem(r, icGeodeticCrs, "WGS 84", BigInteger.valueOf(4326L), isotc211, GeodeticCoordinateReferenceSystemItemProposalDTO.class,
					new ParameterizedRunnable<GeodeticCoordinateReferenceSystemItemProposalDTO>() {
						@Override
						public void run(GeodeticCoordinateReferenceSystemItemProposalDTO p) {
							p.setCode(4326);
//							p.setDomainOfValidity(new AreaItemProposalDTO((AreaItem)worldArea));
							p.setCoordinateSystem(new CoordinateSystemItemProposalDTO((CoordinateSystemItem)cs6422));
							p.setDatum(new DatumItemProposalDTO((GeodeticDatumItem)dt6326));
							
//							parameter.setCode(4326);
//							parameter.setArea(new AreaItemProposalDTO(worldArea.getUuid()));
//							parameter.setScope("Horizontal component of 3D system. Used by the GPS satellite navigation "
//									+ "system and for NATO military geodetic surveying.");
//							parameter.setType(CoordinateSystemType.GEOGRAPHIC_2D);
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

	private Organization createOrganization(String name) throws UnauthorizedException {
		CI_ResponsibleParty respExample = new CI_ResponsibleParty("John Doe", null, null, CI_RoleCode.USER);
		RE_SubmittingOrganization orgExample = new RE_SubmittingOrganization(name, respExample);
		orgExample = suborgRepository.save(orgExample);

		RE_SubmittingOrganization_PropertyType pt = new RE_SubmittingOrganization_PropertyType();
		pt.setUuidref(orgExample.getUuid().toString());
		
		CreateOrganizationRequest cor = new CreateOrganizationRequest();
		cor.setName(name);
		cor.setSubmittingOrganization(pt);
		return orgService.createOrganization(cor);
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

	public <P extends RegisterItemProposalDTO> RE_RegisterItem registerItem(RE_Register register, RE_ItemClass itemClass, String name, BigInteger itemIdentifier, Organization sponsor, Class<P> dtoClass, ParameterizedRunnable<P> paramSetter) throws InvalidProposalException, InstantiationException, IllegalAccessException {
		P proposal;
		proposal = BeanUtils.instantiateClass(dtoClass);
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getSubmittingOrganization().getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setName(name);
		proposal.setDefinition("Definition");
		proposal.setJustification("Justification");
		
		paramSetter.run(proposal);

		initLog.append(String.format("> Adding item '%s' of class '%s' to register '%s'...", proposal.getName(), itemClass.getName(), register.getName()));
		
		Addition ai = proposalService.createAdditionProposal(proposal);
		proposalService.submitProposal(ai);
		
		String decisionEvent = "Decision event";
		acceptProposal(ai, decisionEvent, itemIdentifier);
		
		initLog.append("done.\n");
		
		return ai.getItem();
	}

	protected void acceptProposal(Addition ai, String decisionEvent, BigInteger itemIdentifier) throws InvalidProposalException {
		try {
			if (itemIdentifier != null) {
				ai.getItem().setItemIdentifier(itemIdentifier);
				itemService.saveRegisterItem(ai.getItem());
			}
			proposalService.reviewProposal(ai);
			proposalService.acceptProposal(ai, decisionEvent);
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

	protected RegistryUser createUser(String name, String password, String mail, Organization organization, Role... roles)
			throws UserRegistrationException, UnauthorizedException {
		
		RegistryUser existingUser = userRepository.findByEmailAddress(mail);
		
		if (existingUser != null) {
			return existingUser;
		}

		CreateRegistryUserRequest req = new CreateRegistryUserRequest();
		req.setName(name);
		req.setPassword(password);
		req.setOrganizationUuid(organization.getUuid().toString());
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
