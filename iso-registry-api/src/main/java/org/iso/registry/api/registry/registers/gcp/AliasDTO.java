package org.iso.registry.api.registry.registers.gcp;

import java.util.UUID;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.core.model.Alias;

public class AliasDTO
{
	// Used to reference existing aliases
	private UUID referencedItemUuid;

	private IdentifiedItemProposalDTO aliasedItem;
	private String alias;
	private NamingSystemItemProposalDTO namingSystem;
	
	public AliasDTO() { }
	
	public AliasDTO(Alias alias) {
		this.referencedItemUuid = alias.getUuid();
	}

	public UUID getReferencedItemUuid() {
		return referencedItemUuid;
	}

	public void setReferencedItemUuid(UUID referencedItemUuid) {
		this.referencedItemUuid = referencedItemUuid;
	}

	public IdentifiedItemProposalDTO getAliasedItem() {
		return aliasedItem;
	}

	public void setAliasedItem(IdentifiedItemProposalDTO aliasedItem) {
		this.aliasedItem = aliasedItem;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public NamingSystemItemProposalDTO getNamingSystem() {
		return namingSystem;
	}

	public void setNamingSystem(NamingSystemItemProposalDTO namingSystem) {
		this.namingSystem = namingSystem;
	}
	
	
}
