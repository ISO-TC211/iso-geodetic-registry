package org.iso.registry.api.registry.registers.gcp;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.CI_Date;
import de.geoinfoffm.registry.core.model.iso19115.CI_DateTypeCode;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;
import de.geoinfoffm.registry.core.model.iso19115.CI_RoleCode;
import de.geoinfoffm.registry.core.model.iso19115.CI_Series;

/**
 * 
 * title [1]
 * alternateTitle [0..*]
 * author [0..1]
 * publisher [0..1]
 * publicationDate [0..1]
 * revisionDate [0..1]
 * { use of either publicationDate or revisionDate is mandatory }
 * edition [0..1]
 * editionDate [0..1]
 * otherCitationDetails [0..1]
 * series [0..1]
 *      name [0..1]
 *      issueIdentification [0..1]
 *      page [0..1]
 *
 * @author Florian Esser
 *
 */
public class CitationDTO
{
	private String title;
	private List<String> alternateTitle;
	private String author;
	private String publisher;
	private String publicationDate;
	private String revisionDate;
	private String edition;
	private String editionDate;
	private CitationSeriesDTO series;
	private String otherCitationDetails;
	
	public CitationDTO() {
		
	}
	
	public CitationDTO(CI_Citation citation) {
		this.title = citation.getTitle();
//		if (citation.getAlternateTitle() != null) {
//			this.setAlternateTitle(org.springframework.util.StringUtils.arrayToDelimitedString(citation.getAlternateTitle().toArray(new String[] { }), "~"));
//		}
		if (citation.getAlternateTitle() != null) {
			for (String alternateTitle : citation.getAlternateTitle()) {
				this.getAlternateTitle().add(alternateTitle);
			}
		}
		
		if (citation.getCitedResponsibleParty() != null) {
			for (CI_ResponsibleParty party : citation.getCitedResponsibleParty()) {
				if (CI_RoleCode.AUTHOR.equals(party.getRole())) {
					this.author = party.getIndividualName();
				}
				else if (CI_RoleCode.PUBLISHER.equals(party.getRole())) {
					this.publisher = party.getOrganisationName();
				}
			}
		}
		
		if (citation.getDate() != null) {
			for (CI_Date date : citation.getDate()) {
				if (CI_DateTypeCode.PUBLICATION.equals(date.getDateType())) {
					this.publicationDate = date.getDate();
				}
				else if (CI_DateTypeCode.REVISION.equals(date.getDateType())) {
					this.revisionDate = date.getDate();
				}
			}
		}
		
		this.edition = citation.getEdition();
		this.editionDate = citation.getEditionDate();
		this.otherCitationDetails = citation.getOtherCitationDetails();
		
		if (citation.getSeries() != null) {
			CI_Series series = citation.getSeries();
			this.series = new CitationSeriesDTO();
			this.series.name = series.getName();
			this.series.issueIdentification = series.getIssueIdentification();
			this.series.page = series.getPage();
		}
	}
	
	public static CitationDTO fromJson(String json) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			CitationDTO citation = jsonMapper.readValue(json, CitationDTO.class);
			return citation;
		}
		catch (IOException e) {
			CitationDTO result = new CitationDTO();
			result.setOtherCitationDetails(json);
			return result;
		}		
	}

	public String toJson() {
		StringWriter sw = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(sw, this);
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return sw.toString();
	}
	
	public CI_Citation toCitation() {
		CI_Citation result = new CI_Citation();
		
		result.setTitle(this.getTitle());
		result.getAlternateTitle().addAll(this.getAlternateTitle());
		result.setEdition(this.getEdition());
		result.setEditionDate(this.getEditionDate());
		result.setOtherCitationDetails(this.getOtherCitationDetails());

		CI_ResponsibleParty rp = new CI_ResponsibleParty(this.getAuthor(), null, null, CI_RoleCode.AUTHOR);
		result.getCitedResponsibleParty().add(rp);

		if (!StringUtils.isEmpty(this.getPublicationDate())) {
			CI_Date publicationDate = new CI_Date();
			publicationDate.setDateType(CI_DateTypeCode.PUBLICATION);
			publicationDate.setDate(this.getPublicationDate());
			result.getDate().add(publicationDate);
		}
		
		if (!StringUtils.isEmpty(this.getRevisionDate())) {
			CI_Date revisionDate = new CI_Date();
			revisionDate.setDateType(CI_DateTypeCode.REVISION);
			revisionDate.setDate(this.getRevisionDate());
			result.getDate().add(revisionDate);
		}
		
		if (this.getSeries() != null) {
			CI_Series series = new CI_Series();
			series.setIssueIdentification(this.getSeries().getIssueIdentification());
			series.setName(this.getSeries().getName());
			series.setPage(this.getSeries().getPage());
			result.setSeries(series);
		}
		
		return result;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<String> getAlternateTitle() {
		if (alternateTitle == null) {
			this.alternateTitle = new ArrayList<String>();
		}
		return alternateTitle;
	}

	public void setAlternateTitle(List<String> alternateTitle) {
		this.alternateTitle = alternateTitle;
	}
	
//	@JsonIgnore
//	public Set<String> getAlternateTitles() {
//		HashSet<String> result = new HashSet<String>();
//		if (!StringUtils.isEmpty(this.alternateTitle)) {
//			for (String alternateTitle : org.springframework.util.StringUtils.delimitedListToStringArray(this.alternateTitle, "~")) {
//				result.add(alternateTitle);
//			}
//		}
//		
//		return result;
//	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getRevisionDate() {
		return revisionDate;
	}

	public void setRevisionDate(String revisionDate) {
		this.revisionDate = revisionDate;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getEditionDate() {
		return editionDate;
	}

	public void setEditionDate(String editionDate) {
		this.editionDate = editionDate;
	}

	public CitationSeriesDTO getSeries() {
		return series;
	}

	public void setSeries(CitationSeriesDTO series) {
		this.series = series;
	}

	public String getOtherCitationDetails() {
		return otherCitationDetails;
	}

	public void setOtherCitationDetails(String otherCitationDetails) {
		this.otherCitationDetails = otherCitationDetails;
	}
	
	public static class CitationSeriesDTO {
		private String name;
		private String issueIdentification;
		private String page;
		
		public CitationSeriesDTO() {
			
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIssueIdentification() {
			return issueIdentification;
		}
		public void setIssueIdentification(String issueIdentification) {
			this.issueIdentification = issueIdentification;
		}
		public String getPage() {
			return page;
		}
		public void setPage(String page) {
			this.page = page;
		}
		
	}
}
