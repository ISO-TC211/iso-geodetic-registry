/**
 * 
 */
package org.iso.registry.client.configuration.security;

import javax.sql.DataSource;

import org.iso.registry.api.security.RegistryAuthenticationProvider;
import org.iso.registry.api.security.RegistryUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import de.geoinfoffm.registry.client.web.BasePathUrlAuthenticationFailureHandler;
import de.geoinfoffm.registry.client.web.BasePathUrlAuthenticationSuccessHandler;
import de.geoinfoffm.registry.client.web.BasePathUrlLogoutSuccessHandler;
import de.geoinfoffm.registry.core.security.RegistryLookupStrategy;
import de.geoinfoffm.registry.core.security.RegistryMutableAclService;
import de.geoinfoffm.registry.core.security.RegistryPermission;
import de.geoinfoffm.registry.persistence.jpa.HibernateConfiguration;

/**
 * Spring security configuration class for the GDI-DE Registry Client.
 * 
 * @author Florian Esser
 * 
 */
@Configuration
@ComponentScan(basePackages = { "org.iso.registry", "de.geoinfoffm.registry" })
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	@Autowired
	private HibernateConfiguration configuration;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new RegistryUserDetailsManager();
	}
	
    @Autowired
    public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    	AuthenticationProvider authProvider = new RegistryAuthenticationProvider(userDetailsService());
    	auth.authenticationProvider(authProvider); 
    }
    
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
			.and()
			.logout()
				.logoutSuccessHandler(new BasePathUrlLogoutSuccessHandler())
			.and()
			.formLogin()
				.successHandler(new BasePathUrlAuthenticationSuccessHandler())
				.failureHandler(new BasePathUrlAuthenticationFailureHandler())
				.loginPage("/login")
			.and()
			.exceptionHandling()
				.accessDeniedPage("/403")
			.and()
			.csrf().disable();
	}
	
	@Bean
	public EhCacheFactoryBean ehCacheFactoryBean() {
		EhCacheFactoryBean result = new EhCacheFactoryBean();
		
		EhCacheManagerFactoryBean manager = new EhCacheManagerFactoryBean();
		manager.setCacheManagerName("aclCache");
		
		result.setCacheManager(manager.getObject());
		
		return result;
	}
	
	@Bean
	public AclCache aclCache() {
		return new EhCacheBasedAclCache(ehCacheFactoryBean().getObject(), permissionGrantingStrategy(), aclAuthorizationStrategy());		
	}
	
	@Bean
	public PermissionGrantingStrategy permissionGrantingStrategy() {
		AuditLogger auditLogger = new ConsoleAuditLogger();
		return new DefaultPermissionGrantingStrategy(auditLogger);
	}
	
	@Bean
	public AclAuthorizationStrategy aclAuthorizationStrategy() {
		GrantedAuthority admin = new SimpleGrantedAuthority("ROLE_ADMIN");
		return new AclAuthorizationStrategyImpl(admin, admin, admin);
	}
	
	@Bean
	public LookupStrategy lookupStrategy() {
		DataSource dataSource = configuration.dataSource();
		return new RegistryLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
	}
	
	@Bean
	public MutableAclService mutableAclService() {
		return new RegistryMutableAclService(configuration.dataSource(), lookupStrategy(), aclCache());
	}
	
	@Bean
	public AclService aclService() {
		return mutableAclService();
	}
	
	@Bean
	public RoleVoter roleVoter() {
		return new RoleVoter();
	}
	
	@Bean
	public PermissionFactory permissionFactory() {
		return new DefaultPermissionFactory(RegistryPermission.class);
	}
	
	@Bean
	public AclPermissionEvaluator aclPermissionEvaluator() {
		AclPermissionEvaluator result = new AclPermissionEvaluator(aclService());
		result.setPermissionFactory(permissionFactory());
		
		return result;
	}
}