package org.iso.registry.client;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.core.model.IdentifiedItem;

import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class IdentifiedItemViewBean extends RegisterItemViewBean
{
	private Integer identifier;
	private String remarks;
	private List<String> aliases;
	private String informationSource;
	private String dataSource;

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
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof IdentifiedItem)) {
			return;
		}
		
		IdentifiedItem item = (IdentifiedItem)registerItem;

		this.setIdentifier(item.getIdentifier());
		this.setRemarks(item.getRemarks());
		this.setInformationSource(item.getInformationSource());
		this.setDataSource(item.getDataSource());
		for (String alias : item.getAliases()) {
			this.addAlias(alias);
		}
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	public void addAlias(String alias) {
		if (this.aliases == null) {
			this.aliases = new ArrayList<String>();
		}
		this.aliases.add(alias);
	}

	public String getInformationSource() {
		return informationSource;
	}

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

}
