package de.bespire.registry.iso.importer;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

@Configuration
@ComponentScan(basePackages = { "de.geoinfoffm.registry", "org.iso.registry" })
@PropertySources({
	@PropertySource("classpath:hibernate.connection.xml"),
	@PropertySource("classpath:hibernate.properties.xml")
})
public class ImporterConfiguration
{
	private static final Logger logger = LoggerFactory.getLogger(ImporterConfiguration.class);
	
	@Bean
	@Autowired
	public HibernateConfiguration hibernateConfiguration(final Environment env) {
		return new HibernateConfiguration() {
			
			public DriverManagerDataSource dataSource() {
				DriverManagerDataSource dataSource = new DriverManagerDataSource();
				
				logger.info(env.getProperty("hibernate.connection.url"));
				logger.info(env.getProperty("hibernate.connection.username"));
				
				dataSource.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
				dataSource.setUrl(env.getProperty("hibernate.connection.url"));
				dataSource.setUsername(env.getProperty("hibernate.connection.username"));
				dataSource.setPassword(env.getProperty("hibernate.connection.password"));
				
				return dataSource;
			}
			
			public Properties additionalParameters() {
				Properties hibernateProperties = new Properties();

				hibernateProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
				hibernateProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

				return hibernateProperties;
			}
		};
	}
}
