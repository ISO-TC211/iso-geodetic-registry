package de.geoinfoffm.registry.iso.importer;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.UserRegistrationException;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.UnauthorizedException;
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
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

@Component
public class RegistryInitializer
{
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
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RegisterService registerService;
	
	@Transactional
	public void initializeRegistry() {
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
			
			CI_ResponsibleParty respExample = new CI_ResponsibleParty("Johne Doe", null, null, CI_RoleCode.USER);
			RE_SubmittingOrganization orgExample = new RE_SubmittingOrganization("EXAMPLE", respExample);
			suborgRepository.save(orgExample);
			
			RegistryUser rt = createUser("René Thiele", "r", "rene.thiele@geoinfoffm.de", adminGroup);
			RegistryUser ex = createUser("John Submitter", "s", "submitter@example.org");

			String registerName = "Geodetic Parameter Data Set";
			RE_Register r = registerService.findByName(registerName); 
			if (r == null) {
				logger.info("> Creating register...");
				r = registerService.createRegister(
						registerName,
						rt, rt, rt,
						roleService, 
						RE_Register.class,
						new ParameterizedRunnable<RE_Register>() {
							public void run(RE_Register parameter) {
							}
						}
				);

//				eventPublisher().publishEvent(new RegistersChangedEvent(this));

				logger.info(">>> '{}' (owner = {}; manager = {})", new Object[] { r.getName(), r.getOwner().getName(), r.getManager().getName() });
			}

			Role submitterRole = registerService.getSubmitterRole(r);
			logger.info(">>> Adding submitter ");
			logger.info(ex.getEmailAddress());
			ex.assignRole(submitterRole);
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
		
		logger.info(mail);

		return userService.registerUser(req);
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
			r.addContainedItemClass(ic);
			ic = itemClassRepository.save(ic);
		}
		
		return ic;
	}

}

