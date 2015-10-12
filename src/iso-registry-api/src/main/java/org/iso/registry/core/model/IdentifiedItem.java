package org.iso.registry.core.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@Access(AccessType.FIELD)
@Audited @Entity
public abstract class IdentifiedItem extends RE_RegisterItem
{
	private static final long serialVersionUID = 5495508699522335382L;

	@Basic(optional = false)
	private Integer identifier;

//	@OneToMany(mappedBy = "aliasedItem")
	@ElementCollection
	private Set<String> aliases;
	
	@Column(name = "REMARKS", columnDefinition = "text")
	private String remarks;
	
	@Column(name = "INFORMATION_SOURCE", columnDefinition = "text")
	private String informationSourceText;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CI_Citation> informationSource;

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

	public Set<String> getAliases() {
		if (this.aliases == null) {
			this.aliases = new HashSet<>();
		}
		return this.aliases;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInformationSourceText() {
		return informationSourceText;
	}

	public void setInformationSourceText(String informationSource) {
		this.informationSourceText = informationSource;
	}

	public Set<CI_Citation> getInformationSource() {
		if (this.informationSource == null) {
			this.informationSource = new HashSet<>();
		}
		return informationSource;
	}

	public void setInformationSource(Set<CI_Citation> informationSourceCitation) {
		this.informationSource = informationSourceCitation;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
}
