package org.iso.registry.client.boot;


import de.geoinfoffm.registry.core.CoreConfiguration;
import de.geoinfoffm.registry.persistence.PersistenceConfiguration;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfigurationImpl;
import org.iso.registry.client.boot.controller.HomeController;
import org.iso.registry.client.boot.web.IsoClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@Import({PersistenceConfiguration.class, CoreConfiguration.class})
//@Import({ HomeController.class })
//@ComponentScan(basePackages = { "org.iso.registry", "de.bespire.registry", "de.geoinfoffm.registry" })
public class Main extends SpringBootServletInitializer {

//    @Bean
//    public HibernateConfiguration hibernateConfiguration() {
//        return new HibernateConfigurationImpl();
//    }

//    @Bean
//    public PersistenceConfiguration persistenceConfiguration() {
//        return new PersistenceConfiguration();
//    }

//    @Override
//    public void configureDefaultServletHandling(
//        DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }

//    public void onStartup(ServletContext servletContext) throws ServletException {
//
//    }

//    @Bean
//    public HandlerMapping handlerMapping() {
//        return new RequestMappingHandlerMapping();
//    }


//    @Bean
//    public HandlerAdapter handlerAdapter() {
//        return new RequestMappingHandlerAdapter();
//    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") //TODO set origin to properties
////                        .allowedOrigins("http://test-phuonghqh-ang.s3-website-us-east-1.amazonaws.com")
//                        .allowedMethods("PUT", "DELETE", "GET", "POST");
//            }
//        };
//    }


//    @Bean
//    public HandlerExceptionResolver handlerExceptionResolver() {
//        return new HandlerExceptionResolver() {
//
//            @Override
//            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//                return null;
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
