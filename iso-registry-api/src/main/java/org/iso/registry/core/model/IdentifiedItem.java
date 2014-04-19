package org.iso.registry.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19115.RS_Identifier;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@Access(AccessType.FIELD)
@Audited @Entity
public abstract class IdentifiedItem extends RE_RegisterItem
{
	@ElementCollection
	private List<Alias> aliases;
	
	private String remarks;

	protected IdentifiedItem() {
		super();
	}

	public IdentifiedItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		
		super(register, itemClass, name, definition, additionInformation);
	}

	public List<Alias> getAliases() {
		return Collections.unmodifiableList(this.aliases);
	}
	
	public void setAliases(List<Alias> aliases) {
		this.aliases = new ArrayList<Alias>();
		this.aliases.addAll(aliases);
	}
	
	public void addAlias(Alias alias) {
		if (this.aliases == null) {
			this.aliases = new ArrayList<Alias>();
		}
		this.aliases.add(alias);
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
