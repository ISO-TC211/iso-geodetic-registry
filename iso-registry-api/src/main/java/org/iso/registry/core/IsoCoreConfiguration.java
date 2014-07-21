package org.iso.registry.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.geoinfoffm.registry.api.RegistrySecurityImpl;
import de.geoinfoffm.registry.core.security.RegistrySecurity;

/**
 * Spring configuration for the GDI-DE Registry Core.
 * 
 * @author Florian Esser
 *
 */
@ComponentScan
@Configuration
public class IsoCoreConfiguration
{
	@Bean
	public RegistrySecurity registrySecurity() {
		return new RegistrySecurityImpl();
	}
}