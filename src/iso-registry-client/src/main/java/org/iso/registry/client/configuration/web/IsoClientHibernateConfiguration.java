package org.iso.registry.client.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfigurationImpl;

@Configuration
public class IsoClientHibernateConfiguration
{
	@Bean
	public HibernateConfiguration hibernateConfiguration() {
		return new HibernateConfigurationImpl();
	}
}
