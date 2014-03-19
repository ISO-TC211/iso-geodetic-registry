package org.iso.registry.api.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.geoinfoffm.registry.core.BCryptHashingStrategy;
import de.geoinfoffm.registry.core.model.RegistryUser;

public class RegistryAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
{
	private PasswordEncoder passwordEncoder;
	private UserDetailsService userDetailsService;
	
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	public RegistryAuthenticationProvider(UserDetailsService userDetailsService) {
		this(new BCryptHashingStrategy(), userDetailsService);
	}

	public RegistryAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		String presentedPassword = authentication.getCredentials().toString();

		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		UserDetails loadedUser;

		try {
			loadedUser = this.userDetailsService.loadUserByUsername(username);
		}
		catch (Exception repositoryProblem) {
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		
		return loadedUser;
	}

//	@Override
//	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
//        // Ensure we return the original credentials the user supplied,
//        // so subsequent attempts are successful even with encoded passwords.
//        // Also ensure we return the original getDetails(), so that future
//        // authentication events after cache expiry contain the details
//		UsernamePasswordAuthenticationToken result;
//		if (principal instanceof RegistryUser) {
//	        result = new RegistryUserAuthenticationToken(principal,
//	                authentication.getCredentials(), ((RegistryUser)principal).getEmailAddress(), 
//	                authoritiesMapper.mapAuthorities(user.getAuthorities())); 
//		}
//		else {
//			result = (UsernamePasswordAuthenticationToken)super.createSuccessAuthentication(principal, authentication, user);
//		}
//        result.setDetails(authentication.getDetails());
//
//        return result;
//	}
	
}
