package org.iso.registry.core.model.iso19115.extent;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Information about spatial, vertical, and temporal extent
 * 
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Access(AccessType.FIELD)
@Embeddable
public class EX_Extent
{
	/**
	 * Spatial and temporal extent for the referring object
	 */
	private String description;
	
//	@ElementCollection
//	private Collection<EX_TemporalExtent> temporalElement;
//	
//	@ElementCollection
//	private Collection<EX_VerticalExtent> verticalElement;
//	
//	@ElementCollection
//	private Collection<EX_GeographicExtent> geographicElement;

	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDescription(String newVal) {
		description = newVal;
	}

//	public Collection<EX_GeographicExtent> getGeographicElement() {
//		return geographicElement;
//	}
//
//	public Collection<EX_TemporalExtent> getTemporalElement() {
//		return temporalElement;
//	}
//
//	public Collection<EX_VerticalExtent> getVerticalElement() {
//		return verticalElement;
//	}
//-
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setGeographicElement(Collection<EX_GeographicExtent> newVal) {
//		geographicElement = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setTemporalElement(Collection<EX_TemporalExtent> newVal) {
//		temporalElement = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setVerticalElement(Collection<EX_VerticalExtent> newVal) {
//		verticalElement = newVal;
//	}
}// end EX_Extent