package org.iso.registry.client.configuration;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class BasePathUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	public BasePathUrlLogoutSuccessHandler() {
		super();
		this.setRedirectStrategy(new BasePathRedirectStrategy());
	}
}
