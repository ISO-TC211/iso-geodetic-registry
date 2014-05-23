package org.iso.registry.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import de.geoinfoffm.registry.core.Repository;
import de.geoinfoffm.registry.persistence.EntityBackendFactoryBean;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfigurationImpl;

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
