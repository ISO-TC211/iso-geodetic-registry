package org.iso.registry.client;

import java.util.Locale;

import org.iso.registry.client.configuration.BasePathRedirectView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;

public class RegistryClientViewResolver extends ThymeleafViewResolver
{
    private static final Logger vrlogger = LoggerFactory.getLogger(RegistryClientViewResolver.class);

	public RegistryClientViewResolver() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
        if (!canHandle(viewName, locale)) {
            vrlogger.trace("[GDIREG] View \"{}\" cannot be handled by RegistryClientViewResolver. Passing on to the next resolver in the chain.", viewName);
            return null;
        }
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            vrlogger.trace("[GDIREG] View \"{}\" is a redirect, and will not be handled directly by RegistryClientViewResolver.", viewName);
            final String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
//          return new BasePathRedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
          return new BasePathRedirectView(redirectUrl, false, isRedirectHttp10Compatible());
        }
        if (viewName.startsWith(FORWARD_URL_PREFIX)) {
        	throw new RuntimeException("Prefix 'forward:' not yet implemented");
//            vrlogger.trace("[GDIREG] View \"{}\" is a forward, and will not be handled directly by RegistryClientViewResolver.", viewName);
//            final String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
//            return new InternalResourceView(forwardUrl);
        }
        vrlogger.trace("[GDIREG] View {} will be handled by RegistryClientViewResolver and a " +
        		"{} instance will be created for it", viewName, this.getViewClass().getSimpleName());
        return loadView(viewName, locale);
	}
}
