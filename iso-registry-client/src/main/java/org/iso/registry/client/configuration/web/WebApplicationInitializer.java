package org.iso.registry.client.configuration.web;

import javax.servlet.Filter;

import org.iso.registry.client.configuration.security.SecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import de.geoinfoffm.registry.client.web.AbstractWebApplicationInitializer;
import de.geoinfoffm.registry.persistence.PersistenceConfiguration;

/**
 * Initializer for the Spring web application.
 * 
 * @author Florian Esser
 *
 */
@Configuration
public class WebApplicationInitializer extends AbstractWebApplicationInitializer 
{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { SecurityConfiguration.class, PersistenceConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { IsoClientConfiguration.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
