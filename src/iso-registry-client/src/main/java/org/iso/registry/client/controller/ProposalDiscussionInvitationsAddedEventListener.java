package org.iso.registry.client.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import de.geoinfoffm.registry.api.AbstractEventListener;
import de.geoinfoffm.registry.api.forum.ProposalDiscussionInvitationsAddedEvent;
import de.geoinfoffm.registry.client.web.ClientConfiguration;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.forum.ProposalDiscussion;

@Component
public class ProposalDiscussionInvitationsAddedEventListener extends AbstractEventListener implements ApplicationListener<ProposalDiscussionInvitationsAddedEvent> 
{
	private static final Logger logger = LoggerFactory.getLogger(ProposalDiscussionInvitationsAddedEventListener.class);
	
	@Autowired
	private RegistryConfiguration registryConfiguration;

	@Override
	public void onApplicationEvent(ProposalDiscussionInvitationsAddedEvent event) {
		if (event.isAnnotated(this.getClass())) return;

		logger.debug("Received event: Invitations added to propoosal discussion");
		if (!(event.getSource() instanceof ProposalDiscussion)) return;

		final ProposalDiscussion discussion = event.getSource();

		final String confirmationUrlBase = registryConfiguration.getMailBaseUrl() + "discussion/"; 				

		for (String invitee : event.getInviteeMailAddresses()) {
			String token = discussion.getTokenByInvitee(invitee);
			final String discussionUrl = confirmationUrlBase + discussion.getUuid() + "?token=" + token;
			final Map<String, Object> model = new HashMap<String, Object>();
	        model.put("discussionUrl", discussionUrl);
	        model.put("register", discussion.getDiscussedProposal().getAffectedRegisters().get(0));
	        model.put("proposal", discussion.getDiscussedProposal());

	        try {
	        	InternetAddress recipient = new InternetAddress(invitee);
	        	this.sendMail(recipient, "mail.subject.discussion.invitation", "mailtemplates/discussion_invitation", "de", 
	        			registryConfiguration.getMailBaseUrl(), model);
	        }
	    	catch (Throwable t) {
	    		logger.error("Sending mail failed: " + t.getMessage(), t);
	    	}
		}
		
		event.annotate(this.getClass());		
	}

}
