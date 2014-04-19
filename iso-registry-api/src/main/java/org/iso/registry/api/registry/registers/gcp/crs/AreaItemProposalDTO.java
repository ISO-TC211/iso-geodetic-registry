package org.iso.registry.api.registry.registers.gcp.crs;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class AreaItemProposalDTO extends RegisterItemProposalDTO
{
	private Integer code;
	private double eastBoundLongitude;
	private double northBoundLatitude;
	private double southBoundLatitude;
	private double westBoundLongitude;
	private String isoA2Code;
	private String isoA3Code;
	private String isoNCode;

	public AreaItemProposalDTO() {
		super();
	}

	public AreaItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public AreaItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public AreaItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public AreaItemProposalDTO(AreaItem area) {
		super(area);
		
//		this.setItemUuid(area.getUuid());
//		this.code = area.getCode();
//		this.eastBoundLongitude = area.getBoundingBox().getEastBoundLongitude();
//		this.westBoundLongitude = area.getBoundingBox().getWestBoundLongitude();
//		this.southBoundLatitude = area.getBoundingBox().getSouthBoundLatitude();
//		this.northBoundLatitude = area.getBoundingBox().getNorthBoundLatitude();
//		this.isoA2Code = area.getIsoA2Code();
//		this.isoA3Code = area.getIsoA3Code();
//		this.isoNCode = area.getIsoNCode();
	}

//	public AreaItemProposalDTO(UUID uuid) {
//		super();
//		this.setItemUuid(uuid);
//	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof AreaItem) {
			AreaItem area = (AreaItem)item;
			area.setCode(this.getCode());
			area.setBoundingBox(new EX_GeographicBoundingBox(
				this.getEastBoundLongitude(),
				this.getNorthBoundLatitude(),
				this.getWestBoundLongitude(),
				this.getSouthBoundLatitude())
			);
			area.setIsoA2Code(this.getIsoA2Code());
			area.setIsoA3Code(this.getIsoA3Code());
			area.setIsoNCode(this.getIsoNCode());
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof AreaItem) {
			AreaItem area = (AreaItem)item;
			this.setCode(area.getCode());
			this.setEastBoundLongitude(area.getBoundingBox().getEastBoundLongitude());
			this.setWestBoundLongitude(area.getBoundingBox().getWestBoundLongitude());
			this.setSouthBoundLatitude(area.getBoundingBox().getSouthBoundLatitude());
			this.setNorthBoundLatitude(area.getBoundingBox().getNorthBoundLatitude());
			this.setIsoA2Code(area.getIsoA2Code());
			this.setIsoA3Code(area.getIsoA3Code());
			this.setIsoNCode(area.getIsoNCode());
		}
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public double getEastBoundLongitude() {
		return eastBoundLongitude;
	}

	public void setEastBoundLongitude(double eastBoundLongitude) {
		this.eastBoundLongitude = eastBoundLongitude;
	}

	public double getNorthBoundLatitude() {
		return northBoundLatitude;
	}

	public void setNorthBoundLatitude(double northBoundLatitude) {
		this.northBoundLatitude = northBoundLatitude;
	}

	public double getSouthBoundLatitude() {
		return southBoundLatitude;
	}

	public void setSouthBoundLatitude(double southBoundLatitude) {
		this.southBoundLatitude = southBoundLatitude;
	}

	public double getWestBoundLongitude() {
		return westBoundLongitude;
	}

	public void setWestBoundLongitude(double westBoundLongitude) {
		this.westBoundLongitude = westBoundLongitude;
	}

	public String getIsoA2Code() {
		return isoA2Code;
	}

	public void setIsoA2Code(String isoA2Code) {
		this.isoA2Code = isoA2Code;
	}

	public String getIsoA3Code() {
		return isoA3Code;
	}

	public void setIsoA3Code(String isoA3Code) {
		this.isoA3Code = isoA3Code;
	}

	public String getIsoNCode() {
		return isoNCode;
	}

	public void setIsoNCode(String isoNCode) {
		this.isoNCode = isoNCode;
	}

}
