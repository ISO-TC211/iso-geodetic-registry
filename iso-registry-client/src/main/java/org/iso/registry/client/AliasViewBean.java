package org.iso.registry.client;

import org.iso.registry.core.model.Alias;

public class AliasViewBean
{
	private String alias;
	private NamingSystemViewBean namingSystem;
	private String remarks;
	
	public AliasViewBean(Alias alias) {
		this.alias = alias.getAlias();
		this.namingSystem = new NamingSystemViewBean(alias.getNamingSystem());
		this.remarks = alias.getRemarks();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public NamingSystemViewBean getNamingSystem() {
		return namingSystem;
	}

	public void setNamingSystem(NamingSystemViewBean namingSystem) {
		this.namingSystem = namingSystem;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
