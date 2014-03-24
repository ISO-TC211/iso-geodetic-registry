package org.iso.registry.api.initialization;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.iso.registry.api.AreaItemProposalDTO;
import org.iso.registry.api.CoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.core.model.CoordinateSystemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ViewResolver;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.RegistersChangedEvent;
import de.geoinfoffm.registry.core.RegistryInitializer;
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

public class IsoRegistryInitializer implements RegistryInitializer, ApplicationEventPublisherAware
{
	private static final Logger logger = LoggerFactory.getLogger(IsoRegistryInitializer.class);
	
	@Autowired private RegistryUserGroupRepository groupRepository;
	@Autowired private ItemClassRepository itemClassRepository;
	@Autowired private RegistryUserRepository userRepository;
	@Autowired private RegistryUserService userService;
	@Autowired private SubmittingOrganizationRepository suborgRepository;
	@Autowired private RegisterService registerService;
	@Autowired private RoleService roleService;
	@Autowired private RegisterItemService itemService;
	@Autowired private ViewResolver viewResolver;
	@Autowired private RegistrySecurity security;
	@Autowired private MutableAclService mutableAclService;

	private ApplicationEventPublisher eventPublisher;
	
	private AtomicInteger progress;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	@Override
	@Transactional
	public synchronized void initializeRegistry() {
		logger.info("Initializing registry...");

		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		try { 
//			Authentication authentication = PreAuthenticatedAuthenticationToken("SYSTEM", "N/A",
//					Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			Authentication authentication = new RunAsUserToken("SYSTEM", "SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")), currentAuth.getClass());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			RegistryUserGroup adminGroup = groupRepository.findByName("ROLE_ADMIN");
			if (adminGroup == null) {
				logger.info("> Creating ROLE_ADMIN...");
				adminGroup = new RegistryUserGroup("ROLE_ADMIN");
				groupRepository.save(adminGroup);
			}

			CI_ResponsibleParty respExample = new CI_ResponsibleParty("Johne Doe", null, null, CI_RoleCode.USER);
			RE_SubmittingOrganization orgExample = new RE_SubmittingOrganization("EXAMPLE", respExample);
			suborgRepository.save(orgExample);

			RegistryUser rt = createUser("René Thiele", "r", "rene.thiele@geoinfoffm.de", adminGroup);
			RegistryUser ex = createUser("John Submitter", "s", "submitter@example.org");

			String registerName = "EPSG Geodetic Parameter Data Set";
			RE_Register r = registerService.findByName(registerName);
			if (r == null) {
				logger.info("> Creating register...");
				r = registerService.createRegister(registerName, rt, rt, rt, roleService, RE_Register.class,
						new ParameterizedRunnable<RE_Register>() {
							@Override
							public void run(RE_Register parameter) {
							}
						});

				eventPublisher.publishEvent(new RegistersChangedEvent(this));

				logger.info(">>> {} (owner = {}; manager = {})", new Object[] { r.getName(), r.getOwner().getName(), r.getManager().getName() });
			}

			Role submitterRole = registerService.getSubmitterRole(r);
			logger.info(">>> Adding submitter {}", ex.getEmailAddress());

			RE_ItemClass icCrs = this.addItemClass("CoordinateReferenceSystem", r);
			RE_ItemClass icArea = this.addItemClass("Area", r);

			final RE_RegisterItem worldArea = this.registerItem(r, icArea, "World", AreaItemProposalDTO.class,
					new ParameterizedRunnable<AreaItemProposalDTO>() {
						@Override
						public void run(AreaItemProposalDTO parameter) {
							parameter.setCode(1262);
						}
					});
			this.registerItem(r, icArea, "Germany - west of 7.5°E", AreaItemProposalDTO.class,
					new ParameterizedRunnable<AreaItemProposalDTO>() {
						@Override
						public void run(AreaItemProposalDTO parameter) {
							parameter.setCode(1624);
						}
					});

			this.registerItem(r, icCrs, "WGS 84", CoordinateReferenceSystemItemProposalDTO.class,
					new ParameterizedRunnable<CoordinateReferenceSystemItemProposalDTO>() {
						@Override
						public void run(CoordinateReferenceSystemItemProposalDTO parameter) {
							parameter.setCode(4326);
							parameter.setAreaUuid(worldArea.getUuid());
							parameter
									.setScope("Horizontal component of 3D system. Used by the GPS satellite navigation system and for NATO military geodetic surveying.");
							parameter.setType(CoordinateSystemType.GEOGRAPHIC_2D);
						}
					});

			logger.info("Initialization complete.");
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
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
			logger.info("> Adding item class to register '{}'...", r.getName());
			ic = new RE_ItemClass();
			ic.setName(name);
			r.addContainedItemClass(ic);
			ic = itemClassRepository.save(ic);

			logger.info(">>> {}", ic.getName());
		}

		return ic;
	}

	public <P extends RegisterItemProposalDTO> RE_RegisterItem registerItem(RE_Register register,
			RE_ItemClass itemClass, String name, Class<P> dtoClass, ParameterizedRunnable<P> paramSetter)
			throws InvalidProposalException, InstantiationException, IllegalAccessException {
		P proposal;
		proposal = dtoClass.newInstance();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(suborgRepository.findAll().get(0).getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setName(name);
		proposal.setDefinition("Definition");
		proposal.setJustification("Justification");

		paramSetter.run(proposal);

		logger.info("> Adding item {}' of class {}' to register '{}'...", new Object[] { proposal.getName(), itemClass.getName(), register.getName() });

		Addition ai = itemService.proposeAddition(proposal);
		String decisionEvent = "Decision event";
		acceptProposal(ai, decisionEvent);

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

	@Override
	@Transactional
	public synchronized void loadExampleData() throws InvalidProposalException, UserRegistrationException,
			UnauthorizedException {
		logger.info("Loading example data...");

		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Authentication authentication = new RunAsUserToken("SYSTEM", "SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")), currentAuth.getClass());
//			Authentication authentication = new PreAuthenticatedAuthenticationToken("SYSTEM", "N/A",
//					Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			logger.info("Finished loading example data.");
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
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

		logger.info(">>>  {}", mail);

		return userService.registerUser(req);
	}
	

}
