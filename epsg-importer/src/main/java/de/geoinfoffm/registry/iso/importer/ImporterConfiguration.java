package de.geoinfoffm.registry.iso.importer;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import de.geoinfoffm.registry.core.CoreConfiguration;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

@Configuration
@PropertySources({
	@PropertySource("classpath:hibernate.configuration.xml"),
	@PropertySource("classpath:hibernate.properties.xml")
})
public class ImporterConfiguration
{
	@Bean
	@Autowired
	public HibernateConfiguration hibernateConfiguration(final Environment env) {
		return new HibernateConfiguration() {
			
			public DriverManagerDataSource dataSource() {
				DriverManagerDataSource dataSource = new DriverManagerDataSource();
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
