package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Access(AccessType.FIELD)
@Embeddable
public class Alias
{
	@ManyToOne
	private NamingSystemItem namingSystem;
	
	private String alias;
	
	public Alias(String alias, NamingSystemItem namingSystem) {
		this.alias = alias;
		this.namingSystem = namingSystem;
	}

	public NamingSystemItem getNamingSystem() {
		return namingSystem;
	}

	public void setNamingSystem(NamingSystemItem namingSystem) {
		this.namingSystem = namingSystem;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
