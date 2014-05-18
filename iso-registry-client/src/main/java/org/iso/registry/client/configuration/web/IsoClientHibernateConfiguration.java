package org.iso.registry.client.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

@Configuration
public class IsoClientHibernateConfiguration
{
	@Bean
	public HibernateConfiguration hibernateConfiguration() {
		return new HibernateConfiguration();
	}
}
