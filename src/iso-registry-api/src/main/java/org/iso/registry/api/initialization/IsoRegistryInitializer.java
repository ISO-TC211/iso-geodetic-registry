package org.iso.registry.api.initialization;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.AbstractRegistryInitializer;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.api.soap.CreateOrganizationRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.RegistersChangedEvent;
import de.geoinfoffm.registry.core.RegistryInitializer;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
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
import de.geoinfoffm.registry.persistence.RegisterRepository;

@Component
public class IsoRegistryInitializer extends AbstractRegistryInitializer implements RegistryInitializer, ApplicationEventPublisherAware
{
	public static final String ISO_REGISTRY_CONTROLBODY_ORG_NAME = "Control Body for the ISO Geodetic Registry Network";
	
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
	private RegisterRepository registerRepository;
	
	@Autowired
	private RegisterService registerService;
	
	@Override
	@Transactional
	protected void initialize() {
		try {
			RegistryUserGroup adminGroup = groupRepository.findByName("ROLE_ADMIN");  
			if (adminGroup == null) {
				log("> Creating ROLE_ADMIN...");
				adminGroup = new RegistryUserGroup("ROLE_ADMIN");
				groupRepository.save(adminGroup);
				log("done");
			}
			
			Organization isotc211 = orgService.findByName(ISO_REGISTRY_CONTROLBODY_ORG_NAME);
			if (isotc211 == null) {
				isotc211 = createOrganization(ISO_REGISTRY_CONTROLBODY_ORG_NAME, "CB");
			}

			String adminPassword = RandomStringUtils.randomAlphanumeric(16);
			RegistryUser admin = createUser("ISO Geodetic Registry Administrator", adminPassword, "admin@example.org", isotc211, adminGroup);
			log(String.format(">>> Password of admin@example.org was set to %s", adminPassword));

			String registerName = "ISO Geodetic Register";
			RE_Register r = registerService.findByName(registerName); 
			if (r == null) {
				log("> Creating register...");
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

				eventPublisher().publishEvent(new RegistersChangedEvent(this));

				log(String.format(">>> '%s' (owner = %s; manager = %s)", r.getName(), r.getOwner().getName(), r.getManager().getName()));
			}

			Role submitterRole = registerService.getSubmitterRole(r);
			isotc211.assignRole(submitterRole);
			orgService.delegate(admin, submitterRole, isotc211);

			Role managerRole = registerService.getManagerRole(r);
			orgService.delegate(admin, managerRole, isotc211);

			Role ownerRole = registerService.getOwnerRole(r);
			orgService.delegate(admin, ownerRole, isotc211);

			Role controlBodyRole = registerService.getControlBodyRole(r);
			orgService.delegate(admin, controlBodyRole, isotc211);
			
			Role pocRole = orgService.getPointOfContactRole(isotc211);
			orgService.delegate(admin, pocRole, isotc211);

			addItemClass("CompoundCRS", r);
			addItemClass("EngineeringCRS", r);
			addItemClass("GeodeticCRS", r);
			addItemClass("ProjectedCRS", r);
			addItemClass("VerticalCRS", r);
			addItemClass("CartesianCS", r);
			addItemClass("EllipsoidalCS", r);
			addItemClass("SphericalCS", r);
			addItemClass("VerticalCS", r);
			addItemClass("EngineeringDatum", r);
			addItemClass("GeodeticDatum", r);
			addItemClass("VerticalDatum", r);
			addItemClass("ConcatenatedOperation", r);
			addItemClass("Conversion", r);
			addItemClass("Transformation", r);
			addItemClass("CoordinateSystemAxis", r);
			addItemClass("Ellipsoid", r);
			addItemClass("OperationMethod", r);
			addItemClass("OperationParameter", r);
			addItemClass("PrimeMeridian", r);
			addItemClass("UnitOfMeasure", r);
		}
		catch (Throwable t) {
			throw new RuntimeException(t.getMessage(), t);
		}
	}

	@Transactional
	@Override
	protected void loadExampleData() throws Exception {
		Organization isotc211 = orgService.findByName(ISO_REGISTRY_CONTROLBODY_ORG_NAME);
		RegistryUserGroup adminGroup = groupRepository.findByName("ROLE_ADMIN");
		
		RegistryUser submitter = createUser("ISO Geodetic Registry Submitter", "s", "submitter@example.org", isotc211);
		RegistryUser regman = createUser("ISO Register Manager", "r", "regman@example.org", isotc211);
		RegistryUser owner = createUser("ISO Register Owner", "o", "owner@example.org", isotc211);
		RegistryUser cb = createUser("ISO Register Control Body", "c", "controlbody@example.org", isotc211);
		RegistryUser poc = createUser("ISO TC/211 Point of Contact", "p", "poc@example.org", isotc211);

		String registerName = "ISO Geodetic Register";
		RE_Register r = registerService.findByName(registerName); 

		Role submitterRole = registerService.getSubmitterRole(r);
		isotc211.assignRole(submitterRole);
		orgService.delegate(submitter, submitterRole, isotc211);

		Role managerRole = registerService.getManagerRole(r);
		orgService.delegate(regman, managerRole, isotc211);

		Role ownerRole = registerService.getOwnerRole(r);
		orgService.delegate(owner, ownerRole, isotc211);

		Role controlBodyRole = registerService.getControlBodyRole(r);
		orgService.delegate(cb, controlBodyRole, isotc211);
		
		Role pocRole = orgService.getPointOfContactRole(isotc211);
		orgService.delegate(poc, pocRole, isotc211);
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
		
		log(mail);

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
			log(String.format("> Adding item class '%s' to register '%s'...\n", name, r.getName()));
			ic = new RE_ItemClass();
			ic.setName(name);
			r.getContainedItemClasses().add(ic);
			ic.getRegisters().add(r);
			ic = itemClassRepository.save(ic);
			r = registerRepository.save(r);
		}
		
		return ic;
	}

}
