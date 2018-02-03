package org.iso.registry.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.ProposalAcceptedEvent;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.persistence.RegisterRepository;

@Transactional
@Component
public class ProposalAcceptedEventListener implements ApplicationListener<ProposalAcceptedEvent> {
	@Autowired
	private RegisterRepository registerRepository;

	@Override
	public void onApplicationEvent(ProposalAcceptedEvent event) {
		if (event.isAnnotated(this.getClass())) return;
		
		Proposal proposal = event.getSource();

		Date dateOfLastChange = new Date();
		for (RE_ProposalManagementInformation pmi : proposal.getProposalManagementInformations()) {
			pmi = (RE_ProposalManagementInformation) Entity.unproxify(pmi);
			if (pmi.isAccepted()) {
				dateOfLastChange = pmi.getDateDisposed();
				break;
			}
		}
		
		for (RE_Register register : proposal.getAffectedRegisters()) {
			register.setDateOfLastChange(dateOfLastChange);
			registerRepository.save(register);
		}

		event.annotate(this.getClass());
	}
	

}
