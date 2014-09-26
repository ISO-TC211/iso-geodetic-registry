package org.iso.registry.core.model.iso19115.extent;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * Extent with respect to date and time
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class EX_SpatialTemporalExtent extends EX_TemporalExtent {

//	private Collection<EX_GeographicExtent> spatialExtent;
//
//	public Collection<EX_GeographicExtent> getSpatialExtent() {
//		return spatialExtent;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setSpatialExtent(Collection<EX_GeographicExtent> newVal){
//		spatialExtent = newVal;
//	}
}//end EX_SpatialTemporalExtent