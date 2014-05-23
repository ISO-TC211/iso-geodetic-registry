package org.iso.registry.api;

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
import de.geoinfoffm.registry.persistence.EntityBackendFactoryBean;
import de.geoinfoffm.registry.persistence.ProposalRepository;

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
}
