package org.iso.registry.api.registry.registers.gcp;

import java.util.UUID;

import org.iso.registry.core.model.Alias;

public class AliasDTO
{
	private String alias;
	private UUID namingSystemUuid;
	
	public AliasDTO(Alias alias) {
		this.alias = alias.getAlias();
		this.namingSystemUuid = alias.getNamingSystem().getUuid();
	}
	
	public AliasDTO(String alias, UUID namingSystemUuid) {
		this.alias = alias;
		this.namingSystemUuid = namingSystemUuid;
	}
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public UUID getNamingSystemUuid() {
		return namingSystemUuid;
	}
	public void setNamingSystemUuid(UUID namingSystemUuid) {
		this.namingSystemUuid = namingSystemUuid;
	}
}
