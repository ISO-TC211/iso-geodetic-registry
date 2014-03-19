package org.iso.registry.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.EntityBackendFactoryBean;

/**
 * Spring configuration for the GDI-DE Registry API.
 * 
 * @author Florian Esser
 *
 */
@Configuration
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
}
