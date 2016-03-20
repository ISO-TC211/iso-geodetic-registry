package org.iso.registry.api;

import static org.springframework.security.acls.domain.BasePermission.*;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.core.ProposalCreatedEvent;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Transactional
@Component
public class ProposalCreatedEventListener implements ApplicationListener<ProposalCreatedEvent> 
{
	@Autowired
	private ProposalRepository proposalRepository;
	
	@Autowired
	private RegisterService registerService;
	
	@Override
	public void onApplicationEvent(ProposalCreatedEvent event) {
		if (event.isAnnotated(this.getClass())) return;
		
//		Proposal proposal = event.getSource();
//		for (RE_Register register : proposal.getAffectedRegisters()) {
//			Sid submitterSid = new GrantedAuthoritySid(registerService.getSubmitterRole(register).getName());
//			Sid ownerSid = new GrantedAuthoritySid(registerService.getOwnerRole(register).getName());
//			Sid managerSid = new GrantedAuthoritySid(registerService.getManagerRole(register).getName());
//			Sid controlBodySid = new GrantedAuthoritySid(registerService.getControlBodyRole(register).getName());
//
//			// TODO Change to READ-only!
//			proposalRepository.appendAces(proposal, Arrays.asList(READ, WRITE), submitterSid, true);
//			
//			proposalRepository.appendAces(proposal, Arrays.asList(READ), ownerSid, true);
//			proposalRepository.appendAces(proposal, Arrays.asList(READ, WRITE), managerSid, true);
//			proposalRepository.appendAces(proposal, Arrays.asList(READ), controlBodySid, true);
//		}
		
		event.annotate(this.getClass());
	}
	
	
}
