package org.iso.registry.api;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.iso.registry.api.registry.registers.gcp.CitationDTO;
import org.iso.registry.core.model.IdentifiedItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class IdentifiedItemProposalDTO extends RegisterItemProposalDTO
{
	private List<String> aliases;
	private String remarks;
	private List<CitationDTO> informationSource;
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
//		this.getInformationSource().add(new CitationDTO());
	}

	public List<String> getAliases() {
		if (aliases == null) {
			this.aliases = new ArrayList<>();
		}
		return aliases;
	}

	protected void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public List<CitationDTO> getInformationSource() {
		if (this.informationSource == null) {
			this.informationSource = new ArrayList<CitationDTO>();
		}
		return informationSource;
	}
	
//	public CitationDTO getInformationSource(int index) {
//		if (!this.getInformationSource().isEmpty() && index <= this.getInformationSource().size() - 1) {
//			return this.getInformationSource().get(index);
//		}
//		else {
//			do {
//				this.getInformationSource().add(new CitationDTO());
//			} while (index > this.getInformationSource().size () - 1);
//			return this.getInformationSource().get(index);
//		}
//	}

	public void setInformationSource(List<CitationDTO> informationSource) {
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
			
			for (CitationDTO citation : this.getInformationSource()) {
				item.getInformationSource().add(citation.toCitation());
			}
//			item.setInformationSource(toJson(this.getInformationSource()));
			
			item.setDataSource(this.getDataSource());
		}
	}

	private static String toJson(Object o) {
		StringWriter sw = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(sw, o);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return sw.toString();
	}

	public static <T> T fromJson(String json, Class<T> type) throws IOException {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			T citation = jsonMapper.readValue(json, type);
			return citation;
		}
		catch (IOException e) {
//			T result = new CitationDTO();
//			result.setOtherCitationDetails(json);
//			return result;
			throw e;
		}		
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof IdentifiedItem) {
			IdentifiedItem item = (IdentifiedItem)registerItem;

			for (String alias : item.getAliases()) {
				if (!StringUtils.isEmpty(alias)) {
					this.getAliases().add(alias);
				}
			}
			
			this.setRemarks(item.getRemarks());
			
			if (!StringUtils.isEmpty(item.getInformationSourceText())) {
//				int start = 0;
//				int end = item.getInformationSource().indexOf("}") + 1;
//				while (end > -1) {
//					String json = item.getInformationSource().substring(start, end);
//					this.getInformationSource().add(CitationDTO.fromJson(json));
//					start = end + 1;
//					end = item.getInformationSource().indexOf("}", start);
//				}
				ObjectMapper jsonMapper = new ObjectMapper();
				try {
					List<CitationDTO> citationList = jsonMapper.readValue(item.getInformationSourceText(), jsonMapper.getTypeFactory().constructCollectionType(List.class, CitationDTO.class));
					this.getInformationSource().addAll(citationList);
				}
				catch (JsonParseException | JsonMappingException e) {
					// Try single value
					CitationDTO dto = CitationDTO.fromJson(item.getInformationSourceText());
					this.getInformationSource().add(dto);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for (CI_Citation citation : item.getInformationSource()) {
				this.getInformationSource().add(new CitationDTO(citation));
			}

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
