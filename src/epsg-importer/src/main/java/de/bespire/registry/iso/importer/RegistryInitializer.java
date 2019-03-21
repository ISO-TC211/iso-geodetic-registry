package de.bespire.registry.iso.importer;

import java.util.Arrays;

import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.RegistersChangedEvent;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserGroup;
import de.geoinfoffm.registry.core.model.RegistryUserGroupRepository;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.persistence.ItemClassRepository;

@Component
public class RegistryInitializer implements ApplicationEventPublisherAware
{
public static enum Mode {
	PRODUCTION,
	DEMO
}

	private static final Logger logger = LoggerFactory.getLogger(RegistryInitializer.class);
	
	@Autowired
	private RegistryUserGroupRepository groupRepository;
	
	@Autowired
	private SubmittingOrganizationRepository suborgRepository;

	@Autowired
	private RegistryUserRepository userRepository;

	@Autowired
	private RegistryUserService userService;
	
	@Autowired
	private OrganizationService orgService;

	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RegisterService registerService;
	
	private ApplicationEventPublisher eventPublisher;
	
	@Transactional
	public void initializeRegistry(Mode mode) {
		logger.info("Initializing registry...");
		
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Authentication authentication = new PreAuthenticatedAuthenticationToken("SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			RegistryUserGroup adminGroup = groupRepository.findByName("ROLE_ADMIN");  
			if (adminGroup == null) {
				logger.info("> Creating ROLE_ADMIN...");
				adminGroup = new RegistryUserGroup("ROLE_ADMIN");
				groupRepository.save(adminGroup);
				logger.info("done");
			}

			Organization isotc211 = createOrganization("ISO/TC 211", "TC211");
			RegistryUser admin = createUser("ISO Registry Administrator", "a", "admin@example.org", isotc211, adminGroup);

			String registerName = "Geodetic Codes & Parameters";
			RE_Register r = registerService.findByName(registerName); 
			if (r == null) {
				logger.info("> Creating register...");
				r = registerService.createRegister(
						registerName,
						isotc211, isotc211, isotc211,
						roleService, 
						RE_Register.class,
						new ParameterizedRunnable<RE_Register>() {
							public void run(RE_Register parameter) {
							}
						}
				);

				eventPublisher.publishEvent(new RegistersChangedEvent(this));

				logger.info(">>> '{}' (owner = {}; manager = {})", new Object[] { r.getName(), r.getOwner().getName(), r.getManager().getName() });
			}

			Role submitterRole = registerService.getSubmitterRole(r);
			isotc211.assignRole(submitterRole);

			Role managerRole = registerService.getManagerRole(r);
			Role ownerRole = registerService.getOwnerRole(r);
			Role controlBodyRole = registerService.getControlBodyRole(r);
			Role pocRole = orgService.getPointOfContactRole(isotc211);

			authentication = new PreAuthenticatedAuthenticationToken("SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_CONTROLBODY_" + r.getUuid().toString())));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			switch (mode) {
				case PRODUCTION:
					orgService.delegate(admin, submitterRole, isotc211);
					orgService.delegate(admin, controlBodyRole, isotc211);
					orgService.delegate(admin, managerRole, isotc211);

					Organization nrcan = createOrganization("Natural Resources Canada", "NRCan");
					nrcan.assignRole(submitterRole);
					nrcan.assignRole(managerRole);
					nrcan.assignRole(controlBodyRole);

					Organization ngs = createOrganization("National Geodetic Survey, NOS, NOAA", "NGS");
					ngs.assignRole(submitterRole);

					Organization ign = createOrganization("Institut Géographique National", "NGS");
					ign.assignRole(submitterRole);

					Organization nga = createOrganization("National Geospatial Intelligence Agency", "NGA");
					nga.assignRole(submitterRole);

					Organization drdlr = createOrganization("Rural Development and Land Reform South Africa", "DRDLR");
					drdlr.assignRole(submitterRole);

					Organization ga = createOrganization("Geoscience Australia", "GA");
					ga.assignRole(submitterRole);

					Organization linz	= createOrganization("Land Information New Zealand", "LINZ");
					linz.assignRole(submitterRole);
					break;
				case DEMO:
					RegistryUser submitter = createUser("ISO Registry Submitter", "s", "submitter@example.org", isotc211);
					orgService.delegate(submitter, submitterRole, isotc211);

					RegistryUser regman = createUser("ISO Register Manager", "r", "regman@example.org", isotc211);
					orgService.delegate(regman, managerRole, isotc211);

					RegistryUser owner = createUser("ISO Register Owner", "o", "owner@example.org", isotc211);
					orgService.delegate(owner, ownerRole, isotc211);

					RegistryUser cb = createUser("ISO Register Control Body", "c", "controlbody@example.org", isotc211);
					orgService.delegate(cb, controlBodyRole, isotc211);

					RegistryUser poc = createUser("ISO/TC 211 Point of Contact", "p", "poc@example.org", isotc211);
					orgService.delegate(poc, pocRole, isotc211);
					break;
			}
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
		}
		finally {
			SecurityContextHolder.getContext().setAuthentication(currentAuth);
		}
	}

	protected Organization createOrganization(String name, String shortName) throws UnauthorizedException {
		CI_ResponsibleParty respExample = new CI_ResponsibleParty("John Doe", null, null, CI_RoleCode.USER);
		RE_SubmittingOrganization orgExample = new RE_SubmittingOrganization(name, respExample);
		orgExample = suborgRepository.save(orgExample);

		RE_SubmittingOrganization_PropertyType pt = new RE_SubmittingOrganization_PropertyType();
		pt.setUuidref(orgExample.getUuid().toString());
		
		CreateOrganizationRequest cor = new CreateOrganizationRequest();
		cor.setName(name);
		cor.setShortName(shortName);
		cor.setSubmittingOrganization(pt);
		return orgService.createOrganization(cor);
	}

	protected RegistryUser createUser(String name, String password, String mail, Organization organization, Role... roles)
			throws UserRegistrationException, UnauthorizedException {
		
		RegistryUser existingUser = userRepository.findByEmailAddressIgnoreCase(mail);
		
		if (existingUser != null) {
			return existingUser;
		}

		CreateRegistryUserRequest req = new CreateRegistryUserRequest();
		req.setName(name);
		req.setPassword(password);
		req.setOrganizationUuid(organization.getUuid().toString());
		req.setEmailAddress(mail);
		req.setPreferredLanguage("en");
		req.setActive(true);
		for (Role role : roles) {
			req.getRole().add(role.getName());
		}
		
		logger.info(mail);

		existingUser = userService.registerUser(req);
		
		Role membershipRole = orgService.getMembershipRole(organization);
		orgService.delegate(existingUser, membershipRole, organization);
		
		return existingUser;
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
			logger.info("> Adding item class '{}' to register '{}'...\n", name, r.getName());
			ic = new RE_ItemClass();
			ic.setName(name);
			r.getContainedItemClasses().add(ic);
			ic = itemClassRepository.save(ic);
		}
		
		return ic;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

}

