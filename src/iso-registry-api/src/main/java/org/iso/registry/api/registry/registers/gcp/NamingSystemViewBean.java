package org.iso.registry.api.registry.registers.gcp;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class NamingSystemViewBean extends IdentifiedItemViewBean
{

	public NamingSystemViewBean(Appeal appeal) {
		super(appeal);
	}

	public NamingSystemViewBean(Proposal proposal) {
		super(proposal);
	}

	public NamingSystemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public NamingSystemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public NamingSystemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public NamingSystemViewBean(Supersession supersession) {
		super(supersession);
	}

}
