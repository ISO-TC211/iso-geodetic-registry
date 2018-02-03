package org.iso.registry.core.model.iso19115.extent;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * Geographic area of the entire dataset referenced to WGS 84
 * 
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class EX_GeographicBoundingBox extends EX_GeographicExtent
{
	public EX_GeographicBoundingBox() { }
	
	public EX_GeographicBoundingBox(Double eastBoundLongitude,
									Double northBoundLatitude,
									Double westBoundLongitude,
									Double southBoundLatitude) {
		this.eastBoundLongitude = eastBoundLongitude;
		this.northBoundLatitude = northBoundLatitude;
		this.westBoundLongitude = westBoundLongitude;
		this.southBoundLatitude = southBoundLatitude;
	}

	public EX_GeographicBoundingBox(BigDecimal eastBoundLongitude,
			BigDecimal northBoundLatitude,
			BigDecimal westBoundLongitude,
			BigDecimal southBoundLatitude) {
		this.eastBoundLongitude = eastBoundLongitude.doubleValue();
		this.northBoundLatitude = northBoundLatitude.doubleValue();
		this.westBoundLongitude = westBoundLongitude.doubleValue();
		this.southBoundLatitude = southBoundLatitude.doubleValue();
	}

	/**
	 * Eastern-most coordinate of the limit of the dataset extent expressed in
	 * longitude, in decimal degrees
	 */
	private Double eastBoundLongitude;
	/**
	 * Northern-most coordinate of the limit of the dataset extent expressed in
	 * latitude, in decimal degrees
	 */
	private Double northBoundLatitude;
	/**
	 * Southern-most coordinate of the limit of the dataset extent expressed in
	 * latitude, in decimal degrees
	 */
	private Double southBoundLatitude;
	/**
	 * Western-most coordinate of the limit of the dataset extent expressed in
	 * longitude, in decimal degrees
	 */
	private Double westBoundLongitude;

	public Double getEastBoundLongitude() {
		return eastBoundLongitude;
	}

	public Double getNorthBoundLatitude() {
		return northBoundLatitude;
	}

	public Double getSouthBoundLatitude() {
		return southBoundLatitude;
	}

	public Double getWestBoundLongitude() {
		return westBoundLongitude;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEastBoundLongitude(Double newVal) {
		eastBoundLongitude = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setNorthBoundLatitude(Double newVal) {
		northBoundLatitude = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSouthBoundLatitude(Double newVal) {
		southBoundLatitude = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setWestBoundLongitude(Double newVal) {
		westBoundLongitude = newVal;
	}
}// end EX_GeographicBoundingBox