package org.iso.registry.api;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.iso.registry.core.model.IdentifiedItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.api.soap.Addition_Type;

public class IdentifiedItemProposalDTO extends RegisterItemProposalDTO
{
	private Set<String> aliases;
	private String remarks;
	private String informationSource;
	private String dataSource;
	
	public IdentifiedItemProposalDTO() {
		super();
		initializeEmpty();
	}
	
	protected IdentifiedItemProposalDTO(String itemClassName) {
		super(itemClassName);
		initializeEmpty();
	}
	
	public IdentifiedItemProposalDTO(IdentifiedItem item) {
		super(item);
	}
	
	public IdentifiedItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}
	
	public IdentifiedItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public IdentifiedItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}
	
	private void initializeEmpty() {
		this.dataSource = "ISO Registry of Geodetic Codes & Parameters";
	}

	public Set<String> getAliases() {
		if (aliases == null) {
			this.aliases = new HashSet<>();
		}
		return aliases;
	}

	public void setAliases(Set<String> aliases) {
		this.aliases = aliases;
	}
	
	public void addAlias(String alias) {
		if (this.aliases == null) {
			this.aliases = new HashSet<String>();
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

	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof IdentifiedItem) {
			IdentifiedItem item = (IdentifiedItem)registerItem;
			
			if (this.getAliases() != null) {
				item.getAliases().clear();
				for (String alias : this.getAliases()) {
					if (!StringUtils.isEmpty(alias)) {
						item.getAliases().add(alias);
					}
				}
			}
			
			item.setIdentifier(-RandomUtils.nextInt());
			item.setItemIdentifier(BigInteger.valueOf(item.getIdentifier().longValue()));
			item.setRemarks(this.getRemarks());
			item.setInformationSource(this.getInformationSource());
			item.setDataSource(this.getDataSource());
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof IdentifiedItem) {
			IdentifiedItem item = (IdentifiedItem)registerItem;

			if (item.getAliases() != null) {
				for (String alias : item.getAliases()) {
					if (!StringUtils.isEmpty(alias)) {
						this.addAlias(alias);
					}
				}
			}
			
			this.setRemarks(item.getRemarks());
			this.setInformationSource(item.getInformationSource());
			this.setDataSource(item.getDataSource());
		}
	}

//	public String getNameCodespace() {
//		return nameCodespace;
//	}
//
//	public void setNameCodespace(String nameCodespace) {
//		this.nameCodespace = nameCodespace;
//	}
//
//	public String getNameCodespaceVersion() {
//		return nameCodespaceVersion;
//	}
//
//	public void setNameCodespaceVersion(String nameCodespaceVersion) {
//		this.nameCodespaceVersion = nameCodespaceVersion;
//	}
}
