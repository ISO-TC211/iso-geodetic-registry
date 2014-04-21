package org.iso.registry.client;

import org.iso.registry.core.model.IdentifiedItem;

import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class IdentifiedItemViewBean extends RegisterItemViewBean
{
	public Integer code;
	private String remarks;

	public IdentifiedItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public IdentifiedItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public IdentifiedItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public IdentifiedItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public IdentifiedItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public IdentifiedItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem) {
		super.addAdditionalProperties(registerItem);
		
		if (!(registerItem instanceof IdentifiedItem)) {
			return;
		}
		
		IdentifiedItem item = (IdentifiedItem)registerItem;

		this.setCode(item.getCode());
		this.setRemarks(item.getRemarks());
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
