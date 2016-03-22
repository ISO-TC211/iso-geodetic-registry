package org.iso.registry.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.geoinfoffm.registry.api.RegistrySecurityImpl;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.core.workflow.Iso19135ProposalWorkflowManager;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;

/**
 * Spring configuration for the GDI-DE Registry Core.
 * 
 * @author Florian Esser
 *
 */
@ComponentScan(basePackages = { "de.geoinfoffm.registry", "de.bespire.registry", "org.iso.registry" })
@Configuration
public class IsoCoreConfiguration
{
	@Bean
	public RegistrySecurity registrySecurity() {
		return new RegistrySecurityImpl();
	}
	
	@Bean
	public ProposalWorkflowManager proposalWorkflowManager() {
		return new Iso19135ProposalWorkflowManager();
	}
}