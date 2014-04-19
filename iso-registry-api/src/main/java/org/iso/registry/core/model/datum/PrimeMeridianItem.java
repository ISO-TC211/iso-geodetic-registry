package org.iso.registry.core.model.datum;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("PrimeMeridian")
@Access(AccessType.FIELD)
@Audited @Entity
public class PrimeMeridianItem extends IdentifiedItem
{
	/**
	 * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward.
	 * 
	 * Default value: 0 degrees.
	 * 
	 * Note: If the value of the prime meridian name is "Greenwich" then the value of greenwichLongitude shall be 0 degrees.
	 */
	private double greenwichLongitude = 0.0;
	
	@ManyToOne
	private UnitOfMeasureItem greenwichLongitudeUom;

	public double getGreenwichLongitude() { 
		return greenwichLongitude;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setGreenwichLongitude(double newVal){
		greenwichLongitude = newVal;
	}
	
	public UnitOfMeasureItem getGreenwichLongitudeUom() {
		return this.greenwichLongitudeUom;
	}

	public void setGreenwichLongitudeUom(UnitOfMeasureItem greenwichLongitudeUom) {
		this.greenwichLongitudeUom = greenwichLongitudeUom;
	}
	
}
