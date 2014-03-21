package org.iso.registry.client.configuration;

import org.springframework.web.servlet.view.RedirectView;

public class BasePathRedirectView extends RedirectView 
{
	public BasePathRedirectView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BasePathRedirectView(String url) {
		super(url, false);
		
		this.setUrl(ClientConfiguration.getBasePath() + url);
	}

	public BasePathRedirectView(String url, boolean contextRelative, boolean http10Compatible,
			boolean exposeModelAttributes) {
		super(url, contextRelative, http10Compatible, exposeModelAttributes);

		this.setUrl(ClientConfiguration.getBasePath() + url);
	}

	public BasePathRedirectView(String url, boolean contextRelative, boolean http10Compatible) {
		super(url, contextRelative, http10Compatible);

		this.setUrl(ClientConfiguration.getBasePath() + url);
	}

	public BasePathRedirectView(String url, boolean contextRelative) {
		super(url, contextRelative);

		this.setUrl(ClientConfiguration.getBasePath() + url);
	}

}
