package org.iso.registry.client.configuration;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class BasePathUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler implements
		AuthenticationFailureHandler
{

	public BasePathUrlAuthenticationFailureHandler() {
		super();
		this.setRedirectStrategy(new BasePathRedirectStrategy());
	}
}
