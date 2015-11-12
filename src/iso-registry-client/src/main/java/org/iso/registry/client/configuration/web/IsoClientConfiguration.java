package org.iso.registry.client.configuration.web;

import java.util.Locale;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import de.geoinfoffm.registry.client.web.AbstractWebMvcConfigurerAdapter;

/**
 * Spring configuration class for the GDI-DE Registry Client.
 * 
 * @author Florian Esser
 * 
 */
@ComponentScan(basePackages = { "org.iso.registry", "de.bespire.registry", "de.geoinfoffm.registry" })
@EnableWebMvc
@EnableTransactionManagement
@EnableAsync
@Configuration
@EnableSpringDataWebSupport
public class IsoClientConfiguration extends AbstractWebMvcConfigurerAdapter
{
	@Override
	protected Locale defaultLocale() {
		return StringUtils.parseLocaleString("en");
	}
}
