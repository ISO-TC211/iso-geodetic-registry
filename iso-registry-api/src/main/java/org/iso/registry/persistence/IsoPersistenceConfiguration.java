package org.iso.registry.persistence;

import java.util.Properties;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.geoinfoffm.registry.persistence.EntityBackendFactoryBean;

/**
 * Spring configuration class for the persistence layer.
 * 
 * @author Florian Esser
 * 
 */
@ComponentScan(basePackages = { "de.geoinfoffm.registry", "org.iso.registry" })
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "de.geoinfoffm.registry", "org.iso.registry" },
				       repositoryFactoryBeanClass = EntityBackendFactoryBean.class)
public class IsoPersistenceConfiguration
{
}
