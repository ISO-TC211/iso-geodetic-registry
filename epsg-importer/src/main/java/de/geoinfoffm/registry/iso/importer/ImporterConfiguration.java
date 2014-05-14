package de.geoinfoffm.registry.iso.importer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

@Configuration
public class ImporterConfiguration
{
	@Bean
	public HibernateConfiguration hibernateConfiguration() {
		return new HibernateConfiguration();
	}
}
