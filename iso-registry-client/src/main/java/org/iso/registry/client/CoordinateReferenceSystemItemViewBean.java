package org.iso.registry.client;

import org.iso.registry.core.model.CoordinateReferenceSystemItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateReferenceSystemItemViewBean extends RegisterItemViewBean
{

	public CoordinateReferenceSystemItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem item) {
		if (!(item instanceof CoordinateReferenceSystemItem)) {
			return;
		}
		
		CoordinateReferenceSystemItem crsItem = (CoordinateReferenceSystemItem)item;
		
		this.addAdditionalProperty("code", crsItem.getCode());
	}
}
