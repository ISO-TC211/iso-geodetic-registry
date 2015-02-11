package org.iso.registry.api;

import org.iso.registry.api.initialization.IsoRegistryInitializer;
import org.iso.registry.api.registry.IsoProposalService;
import org.iso.registry.api.registry.IsoProposalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.geoinfoffm.registry.api.ApiConfiguration;
import de.geoinfoffm.registry.api.ControlBodyDiscoveryStrategy;
import de.geoinfoffm.registry.api.ControlBodyDiscoveryStrategyImpl;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.OrganizationServiceImpl;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegistrySecurityImpl;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.RegistryUserServiceImpl;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.EntityBackendFactoryBean;

/**
 * Spring configuration for the GDI-DE Registry API.
 * 
 * @author Florian Esser
 *
 */
@Configuration
@Import(ApiConfiguration.class)
@ComponentScan(basePackages = { "de.geoinfoffm.registry", "org.iso.registry" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"de.geoinfoffm.registry", "org.iso.registry"}, 
					   repositoryFactoryBeanClass = EntityBackendFactoryBean.class)
public class IsoApiConfiguration 
{
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	@Bean
	public IsoProposalService isoProposalService(ProposalRepository repository) {
		return new IsoProposalServiceImpl(repository);
	}
	
	@Autowired
	@Bean
	public RegistrySecurity registrySecurity() {
		return new RegistrySecurityImpl();
	}
	
//	@Autowired
//	@Bean
//	public RegistryUserService registryUserService(RegistryUserRepository userRepository) {
//		return new RegistryUserServiceImpl(userRepository);
//	}

	@Autowired
	@Bean
	public OrganizationService organizationService(OrganizationRepository repository) {
		return new OrganizationServiceImpl(repository);
	}
	
	@Autowired 
	@Bean
	public ControlBodyDiscoveryStrategy controlBodyDiscoveryStrategy(RegisterService registerService, DelegationRepository delegationRepository) {
		return new ControlBodyDiscoveryStrategyImpl(registerService, delegationRepository);
	}
}
