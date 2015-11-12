package org.iso.registry.core.model.iso19115.extent;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * Time period covered by the content of the dataset
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class EX_TemporalExtent extends de.geoinfoffm.registry.core.Entity 
{

//	/**
//	 * Date and time for the content of the dataset. 
//	 */
//	private TM_Primitive extent;
//
//	public TM_Primitive getExtent(){
//		return extent;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setExtent(TM_Primitive newVal){
//		extent = newVal;
//	}
}//end EX_TemporalExtent