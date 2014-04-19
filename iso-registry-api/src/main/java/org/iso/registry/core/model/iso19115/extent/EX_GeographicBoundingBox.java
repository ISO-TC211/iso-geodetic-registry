package org.iso.registry.core.model.iso19115.extent;

import javax.persistence.Embeddable;

/**
 * Geographic area of the entire dataset referenced to WGS 84
 * 
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Embeddable
public class EX_GeographicBoundingBox extends EX_GeographicExtent
{
	protected EX_GeographicBoundingBox() { }
	
	public EX_GeographicBoundingBox(double eastBoundLongitude,
									double northBoundLatitude,
									double westBoundLongitude,
									double southBoundLatitude) {
		this.eastBoundLongitude = eastBoundLongitude;
		this.northBoundLatitude = northBoundLatitude;
		this.westBoundLongitude = westBoundLongitude;
		this.southBoundLatitude = southBoundLatitude;
	}

	/**
	 * Eastern-most coordinate of the limit of the dataset extent expressed in
	 * longitude, in decimal degrees
	 */
	private double eastBoundLongitude;
	/**
	 * Northern-most coordinate of the limit of the dataset extent expressed in
	 * latitude, in decimal degrees
	 */
	private double northBoundLatitude;
	/**
	 * Southern-most coordinate of the limit of the dataset extent expressed in
	 * latitude, in decimal degrees
	 */
	private double southBoundLatitude;
	/**
	 * Western-most coordinate of the limit of the dataset extent expressed in
	 * longitude, in decimal degrees
	 */
	private double westBoundLongitude;

	public double getEastBoundLongitude() {
		return eastBoundLongitude;
	}

	public double getNorthBoundLatitude() {
		return northBoundLatitude;
	}

	public double getSouthBoundLatitude() {
		return southBoundLatitude;
	}

	public double getWestBoundLongitude() {
		return westBoundLongitude;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEastBoundLongitude(double newVal) {
		eastBoundLongitude = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setNorthBoundLatitude(double newVal) {
		northBoundLatitude = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSouthBoundLatitude(double newVal) {
		southBoundLatitude = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setWestBoundLongitude(double newVal) {
		westBoundLongitude = newVal;
	}
}// end EX_GeographicBoundingBox