package org.iso.registry.client;

import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;

import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateSystemItemViewBean extends IdentifiedItemViewBean
{
	public CoordinateSystemItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public CoordinateSystemItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public CoordinateSystemItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public CoordinateSystemItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public CoordinateSystemItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public CoordinateSystemItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem) {
		super.addAdditionalProperties(registerItem);
		
		if (!(registerItem instanceof CoordinateSystemItem)) {
			return;
		}
		
		CoordinateSystemItem item = (CoordinateSystemItem)registerItem;
		
		// TODO
	}

}
