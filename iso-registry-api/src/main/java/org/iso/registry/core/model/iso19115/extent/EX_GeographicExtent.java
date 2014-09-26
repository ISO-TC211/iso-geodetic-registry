package org.iso.registry.core.model.iso19115.extent;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;

/**
 * Geographic area of the dataset
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Audited @Entity
public abstract class EX_GeographicExtent extends de.geoinfoffm.registry.core.Entity 
{
	/**
	 * Identifies whether the bounding polygon encompasses an area covered by the data (true) or an area where data is not present (false) 
	 */
	private Boolean extentTypeCode = true;
	
	public Boolean getExtentTypeCode() {
		return extentTypeCode;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExtentTypeCode(Boolean newVal){
		extentTypeCode = newVal;
	}


}//end EX_GeographicExtent