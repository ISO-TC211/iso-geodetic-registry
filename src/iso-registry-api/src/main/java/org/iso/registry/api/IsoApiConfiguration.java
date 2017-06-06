package org.iso.registry.api;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.iso.registry.api.registry.IsoProposalServiceImpl;
import org.iso.registry.persistence.IsoExcelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.bespire.registry.io.excel.configuration.ExcelConfiguration;
import de.geoinfoffm.registry.api.ApiConfiguration;
import de.geoinfoffm.registry.api.RoleDiscoveryStrategyImpl;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.OrganizationServiceImpl;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RoleDiscoveryStrategy;
import de.geoinfoffm.registry.api.forum.ProposalDiscussionService;
import de.geoinfoffm.registry.api.forum.ProposalDiscussionServiceImpl;
import de.geoinfoffm.registry.core.forum.ProposalDiscussionRepository;
import de.geoinfoffm.registry.core.model.DelegationRepository;
import de.geoinfoffm.registry.core.model.OrganizationRepository;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.persistence.EntityBackendFactoryBean;

/**
 * Spring configuration for the GDI-DE Registry API.
 * 
 * @author Florian Esser
 *
 */
@Configuration
@Import(ApiConfiguration.class)
@ComponentScan(basePackages = { "de.geoinfoffm.registry", "de.bespire.registry", "org.iso.registry" })
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"de.geoinfoffm.registry", "de.bespire.registry", "org.iso.registry" }, 
					   repositoryFactoryBeanClass = EntityBackendFactoryBean.class)
public class IsoApiConfiguration 
{
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	@Bean
	public ProposalService proposalService(ProposalRepository repository) {
		return new IsoProposalServiceImpl(repository);
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
	public RoleDiscoveryStrategy roleDiscoveryStrategy(RegisterService registerService, DelegationRepository delegationRepository) {
		return new RoleDiscoveryStrategyImpl(registerService, delegationRepository);
	}

	@Autowired
	@Bean
	public ProposalDiscussionService proposalDiscussionService(ProposalDiscussionRepository repository) {
		return new ProposalDiscussionServiceImpl(repository);
	}
	
	@Bean
	public AsyncTaskExecutor asyncTaskExecutor() {
		SimpleAsyncTaskExecutor result = new SimpleAsyncTaskExecutor();
		
		return new DelegatingSecurityContextAsyncTaskExecutor(result);
	}
	
	@Bean
	public ExcelConfiguration excelConfiguration() throws IOException, JAXBException, URISyntaxException {
		return IsoExcelConfiguration.reload();
	}
}
