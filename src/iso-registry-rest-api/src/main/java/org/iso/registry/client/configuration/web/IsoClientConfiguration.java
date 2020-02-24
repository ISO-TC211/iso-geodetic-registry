package org.iso.registry.client.configuration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.util.List;

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
//@Profile("web")
public class IsoClientConfiguration extends WebMvcConfigurerAdapter {

//    @Autowired
//    private SessionFactory sessionFactory;
//
//    @Autowired
//    private EntityManagerFactory emFactory;

    @Autowired
    private MappingJackson2HttpMessageConverter jsonMapper;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(localeChangeInterceptor());
//
//        OpenSessionInViewInterceptor sessionIntercepetor = new OpenSessionInViewInterceptor();
//        sessionIntercepetor.setSessionFactory(sessionFactory);
//        registry.addWebRequestInterceptor(sessionIntercepetor);
//
//        OpenEntityManagerInViewInterceptor emInterceptor = new OpenEntityManagerInViewInterceptor();
//        emInterceptor.setEntityManagerFactory(emFactory);
//        registry.addWebRequestInterceptor(emInterceptor);
//    }

//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new Converter<CharacterString, String>() {
//
//            @Override
//            public String convert(CharacterString cs) {
//                return CharacterString.asString(cs);
//            }
//        });
//
//        registry.addConverter(new Converter<String, CharacterString>() {
//
//            @Override
//            public CharacterString convert(String s) {
//                return new CharacterString(s);
//            }
//        });
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(new PageRequest(1, 5));
        argumentResolvers.add(resolver);
    }

    public AbstractHttpMessageConverter<?> jaxbConverter() {
        return new Jaxb2RootElementHttpMessageConverter();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(this.jsonMapper);
        converters.add(jaxbConverter());
    }

    @Bean
    public BeanNameUrlHandlerMapping beanNameUrlHandlerMapping() {
        return new BeanNameUrlHandlerMapping();
    }

//    @Bean
//    public SpringSecurityDialect springSecurityDialect() {
//        return new SpringSecurityDialect();
//    }
//
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
//
//    @Bean
//    @Autowired
//    public DomainClassConverter<?> domainClassConverter(FormattingConversionService conversionService) {
//        return new DomainClassConverter<>(conversionService);
//    }
}
