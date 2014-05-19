package org.iso.registry.api;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.iso.registry.api.registry.registers.gcp.AliasDTO;
import org.iso.registry.core.model.Alias;
import org.iso.registry.core.model.IdentifiedItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class IdentifiedItemProposalDTO extends RegisterItemProposalDTO
{
//	private String nameCodespace;
//	private String nameCodespaceVersion;
//	private CI_Citation nameCodespaceCitation;
	
	private Integer code;
	private List<AliasDTO> aliases;
	private String remarks;
	private String informationSource;
	private String dataSource;

	
	public IdentifiedItemProposalDTO() {
		super();
	}
	
	protected IdentifiedItemProposalDTO(String itemClassName) {
		super(itemClassName);
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public List<AliasDTO> getAliases() {
		return aliases;
	}

	public void setAliases(List<AliasDTO> aliases) {
		this.aliases = aliases;
	}
	
	public void addAlias(AliasDTO alias) {
		if (this.aliases == null) {
			this.aliases = new ArrayList<AliasDTO>();
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
			
			item.setCode(this.getCode());
			item.setItemIdentifier(BigInteger.valueOf(this.getCode().longValue()));
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

			for (Alias alias : item.getAliases()) {
				this.addAlias(new AliasDTO(alias));
			}
			this.setCode(item.getCode());
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
