package org.iso.registry.core.model.iso19115.extent;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Geographic area of the dataset
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@MappedSuperclass
public abstract class EX_GeographicExtent implements Serializable 
{
	/**
	 * Identifies whether the  bounding polygon encompasses an area covered by the data or an area where data is not present 
	 */
	private boolean extentTypeCode = true;

	public boolean getExtentTypeCode() {
		return extentTypeCode;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExtentTypeCode(boolean newVal){
		extentTypeCode = newVal;
	}
}//end EX_GeographicExtent