package org.iso.registry.core.model.iso19115.extent;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.UnitOfMeasureItem;

/**
 * Vertical domain of dataset
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class EX_VerticalExtent extends de.geoinfoffm.registry.core.Entity
{
	/**
	 * Highest vertical extent contained in the dataset 
	 */
	private Double maximumValue;
	/**
	 * Lowest vertical extent contained in the dataset 
	 */
	private Double minimumValue;
	
	private UnitOfMeasureItem uomLength;
	
	public Double getMaximumValue(){
		return maximumValue;
	}
	
	public Double getMinimumValue(){
		return minimumValue;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMaximumValue(Double newVal){
		maximumValue = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMinimumValue(Double newVal){
		minimumValue = newVal;
	}

	public UnitOfMeasureItem getUomLength() {
		return uomLength;
	}

	public void setUomLength(UnitOfMeasureItem uomLength) {
		this.uomLength = uomLength;
	}


}//end EX_VerticalExtent