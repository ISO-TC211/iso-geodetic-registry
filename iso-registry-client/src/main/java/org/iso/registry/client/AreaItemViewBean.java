package org.iso.registry.client;

import org.iso.registry.core.model.crs.AreaItem;

import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class AreaItemViewBean extends IdentifiedItemViewBean
{
	private double eastBoundLongitude;
	private double northBoundLatitude;
	private double southBoundLatitude;
	private double westBoundLongitude;
	private String isoA2Code;
	private String isoA3Code;
	private String isoNCode;

	public AreaItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public AreaItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public AreaItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public AreaItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public AreaItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem) {
		if (!(registerItem instanceof AreaItem)) {
			return;
		}
		
		AreaItem item = (AreaItem)registerItem;
		
		this.setCode(item.getCode());
		this.setIsoA2Code(item.getIsoA2Code());
		this.setIsoA3Code(item.getIsoA3Code());
		this.setIsoNCode(item.getIsoNCode());
		this.setEastBoundLongitude(item.getBoundingBox().getEastBoundLongitude());
		this.setWestBoundLongitude(item.getBoundingBox().getWestBoundLongitude());
		this.setNorthBoundLatitude(item.getBoundingBox().getNorthBoundLatitude());
		this.setSouthBoundLatitude(item.getBoundingBox().getSouthBoundLatitude());
		this.addAdditionalProperty("code", item.getCode());
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
