package org.iso.registry.core.model.iso19115.extent;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

/**
 * Information about spatial, vertical, and temporal extent
 * 
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class EX_Extent extends de.geoinfoffm.registry.core.Entity
{
	/**
	 * Spatial and temporal extent for the referring object
	 */
	@Column(columnDefinition = "text")
	private String description;
	
//	@OneToMany(mappedBy = "extent")
//	private Collection<EX_TemporalExtent> temporalElement;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Collection<EX_VerticalExtent> verticalElement;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Collection<EX_GeographicExtent> geographicElement;

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

	public Collection<EX_GeographicExtent> getGeographicElement() {
		if (this.geographicElement == null) {
			this.geographicElement = new ArrayList<>();
		}
		return geographicElement;
	}
//
//	public Collection<EX_TemporalExtent> getTemporalElement() {
//		return temporalElement;
//	}

	public Collection<EX_VerticalExtent> getVerticalElement() {
		if (this.verticalElement == null) {
			this.verticalElement = new ArrayList<>();
		}
		return verticalElement;
	}

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