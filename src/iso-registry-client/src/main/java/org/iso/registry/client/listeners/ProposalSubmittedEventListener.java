/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions 
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.
 
 *     * Redistributions in binary form must reproduce the above 
 *     	 copyright notice, this list of conditions and the following 
 *       disclaimer in the documentation and/or other materials 
 *       provided with the distribution.
 
 *     * The names "German Federal Agency for Cartography and Geodesy", 
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE", 
 *       "GDI-DE Registry" and the names of other contributors must not 
 *       be used to endorse or promote products derived from this 
 *       software without specific prior written permission.
 *       
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN 
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.iso.registry.client.listeners;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.AbstractEventListener;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.core.ProposalSubmittedEvent;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;
import de.geoinfoffm.registry.core.model.Authorization;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;

/**
 *
 * @author Florian Esser
 */
@Transactional
@Component
public class ProposalSubmittedEventListener extends AbstractEventListener implements ApplicationListener<ProposalSubmittedEvent>
{
	private static final Logger logger = LoggerFactory.getLogger(ProposalSubmittedEventListener.class);
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalWorkflowManager workflowManager;

	@Override
	public void onApplicationEvent(ProposalSubmittedEvent event) {
		if (event.isAnnotated(this.getClass())) return;
		event.annotate(this.getClass());
		
		Proposal proposal = event.getSource();
		
		for (Authorization authorization : proposalService.findAuthorizedRegisterManager(proposal)) {
			if (authorization instanceof Delegation) {
				Delegation delegation = (Delegation)authorization;
				
				if (delegation.getUser() instanceof RegistryUser) {
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("delegation", delegation);
					model.put("proposal", RegisterItemViewBean.forProposal(proposal, workflowManager));
					model.put("register", proposal.getAffectedRegisters().iterator().next());
					
			    	try {
			    		this.sendMailToUser((RegistryUser)delegation.getUser(), "mail.subject.proposal.submitted", "mailtemplates/proposal_submitted", 
			    				RegistryConfiguration.getInstance().getMailBaseUrl(), model);
			    	}
			    	catch (Throwable t) {
			    		logger.error("Sending mail failed: " + t.getMessage(), t);
			    	}
				}
			}
		}
	}

}
