package org.iso.registry.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@Access(AccessType.FIELD)
@Audited @Entity
public abstract class IdentifiedItem extends RE_RegisterItem
{
	@Basic(optional = false)
	private Integer identifier;

	@OneToMany(mappedBy = "aliasedItem")
	private List<Alias> aliases;
	
	@Column(name = "REMARKS", columnDefinition = "text")
	private String remarks;
	
	@Column(name = "INFORMATION_SOURCE", columnDefinition = "text")
	private String informationSource;

	@Column(name = "DATA_SOURCE", columnDefinition = "text")
	private String dataSource;

	protected IdentifiedItem() {
		super();
	}

	public IdentifiedItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		
		super(register, itemClass, name, definition, additionInformation);
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
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
