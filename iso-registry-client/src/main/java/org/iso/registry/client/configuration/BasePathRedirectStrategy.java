package org.iso.registry.client.configuration;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.util.StringUtils;

public class BasePathRedirectStrategy extends DefaultRedirectStrategy 
{
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		String basePath = clientConfiguration.configuration().getProperty("basePath");
		
		if (StringUtils.isEmpty(basePath)) {
			super.sendRedirect(request, response, url);
			return;
		}
		else if (basePath.startsWith("~")) {
			// server-relative URL
			basePath = basePath.substring(1);
		}
		
		String redirectUrl = basePath + url;
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        if (logger.isDebugEnabled()) {
            logger.debug("Redirecting to '" + redirectUrl + "'");
        }

        response.sendRedirect(redirectUrl);
	}
}
