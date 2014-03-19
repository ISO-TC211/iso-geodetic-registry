package org.iso.registry.client.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class BasePathUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	public BasePathUrlAuthenticationSuccessHandler() {
		super();
		this.setRedirectStrategy(new BasePathRedirectStrategy());
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
