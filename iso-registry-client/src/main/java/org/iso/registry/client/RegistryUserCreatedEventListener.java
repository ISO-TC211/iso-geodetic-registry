package org.iso.registry.client;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.api.AbstractEventListener;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.client.web.ClientConfiguration;
import de.geoinfoffm.registry.core.RegistryUserCreatedEvent;
import de.geoinfoffm.registry.core.model.RegistryUser;

@Component
public class RegistryUserCreatedEventListener extends AbstractEventListener implements ApplicationListener<RegistryUserCreatedEvent>
{
	private Logger logger = LoggerFactory.getLogger(RegistryUserCreatedEventListener.class);
	
	public RegistryUserCreatedEventListener() {
	}
	
	@Override
	public void onApplicationEvent(RegistryUserCreatedEvent event) {
		if (event.isAnnotated(this.getClass())) return;

		logger.debug("Received event: Registry user created - {}", event.getSource().toString());
		if (!event.isAnnotated(RegistryUserService.SEND_CONFIRMATION_MAIL)) return;
		if (!ClientConfiguration.isSendConfirmationMails()) return;
		if (!(event.getSource() instanceof RegistryUser)) return;

		final RegistryUser user = event.getSource();

		if (user.isConfirmed()) return;

		final String confirmationUrlBase = ClientConfiguration.getSignupConfirmationUrl(); 				
		final String confirmationUrl = confirmationUrlBase + "?token=" + user.getConfirmationToken().toString() + "&mail=" + user.getEmailAddress();

		final Map<String, Object> model = new HashMap<String, Object>();
        model.put("confirmationUrl", confirmationUrl);

        try {
        	this.sendMailToUser(user, "mail.subject.signup.confirmation", "mailtemplates/new_user_confirmation", 
        			ClientConfiguration.getMailBaseUrl(), model);
        }
    	catch (Throwable t) {
    		logger.error("Sending mail failed: " + t.getMessage(), t);
    	}
		
		event.annotate(this.getClass());
	}

}
