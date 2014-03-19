package org.iso.registry.client.configuration.web;

import java.util.List;
import java.util.Properties;

import org.iso.registry.client.RegistryClientViewResolver;
import org.iso.registry.client.configuration.ClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RegisterServiceImpl;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

/**
 * Spring configuration class for the GDI-DE Registry Client.
 * 
 * @author Florian Esser
 * 
 */
@ComponentScan(basePackages = { "org.iso.registry", "de.geoinfoffm.registry" })
@EnableWebMvc
@EnableTransactionManagement
@Configuration
public class IsoClientConfiguration extends WebMvcConfigurerAdapter
{
//	@Autowired
//	private RegisterService registerService;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new Converter<CharacterString, String>() {

			@Override
			public String convert(CharacterString cs) {
				return CharacterString.asString(cs);
			}
		});

		registry.addConverter(new Converter<String, CharacterString>() {

			@Override
			public CharacterString convert(String s) {
				return new CharacterString(s);
			}
		});
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(new PageRequest(1, 5));
		argumentResolvers.add(resolver);
	}

	@Bean
	public HibernateConfiguration hibernateConfiguration() {
		return new HibernateConfiguration();
	}

	@Bean(name = "localeResolver")
	public LocaleResolver localeResolver() {
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setCookieName("siteLanguage");
		cookieLocaleResolver.setCookiePath("/");
		cookieLocaleResolver.setDefaultLocale(StringUtils.parseLocaleString("en"));
		return cookieLocaleResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	public BeanNameUrlHandlerMapping beanNameUrlHandlerMapping() {
		return new BeanNameUrlHandlerMapping();
	}

	@Bean
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		// NB, selecting HTML5 as the template mode.
		resolver.setTemplateMode("HTML5");
		resolver.setCharacterEncoding("UTF-8");
		resolver.setCacheable(false);

		return resolver;

	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.addDialect(springSecurityDialect());
		return engine;
	}

	/**
	 * Creates the {@link ViewResolver} beans for the application.
	 */
	@Bean
	public ViewResolver viewResolver() {

		RegistryClientViewResolver viewResolver = new RegistryClientViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(1);
		viewResolver.setViewNames(new String[] { "*" });
		viewResolver.setCache(false);
		viewResolver.setCharacterEncoding("UTF-8");

		ClientConfiguration clientConfiguration = new ClientConfiguration();
		Properties properties = clientConfiguration.configuration();
		for (Object property : properties.keySet()) {
			if (property instanceof String) {
				viewResolver.addStaticVariable((String)property, properties.getProperty((String)property));
			}
		}

		return viewResolver;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * Creates the {@link MessageSource} beans for the application.
	 * 
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		//		messageSource.setBasenames("classpath:messages/messages", "classpath:messages/validation");
		//		messageSource.setBasename("WEB-INF/i18n/messages");
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setFallbackToSystemLocale(false);

		// if true, the key of the message will be displayed if the key is not
		// found, instead of throwing a NoSuchMessageException
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		// # -1 : never reload, 0 always reload
		messageSource.setCacheSeconds(0);
		return messageSource;
	}

	@Bean
	@Autowired
	public DomainClassConverter<?> domainClassConverter(FormattingConversionService conversionService) {
		return new DomainClassConverter<FormattingConversionService>(conversionService);
	}
	
	@Bean
	public RegistrySecurity registrySecurity() {
		return new RegistrySecurity();
	}

	@Autowired
	@Bean
	public RegisterService registerService(RegisterRepository registerRepository) {
		return new RegisterServiceImpl(registerRepository);
	}

}
