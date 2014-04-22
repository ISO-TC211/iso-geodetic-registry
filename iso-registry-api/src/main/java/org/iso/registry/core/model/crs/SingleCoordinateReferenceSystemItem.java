package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.datum.DatumItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * Coordinate reference system consisting of one Coordinate System and one Datum (as opposed to a Compound CRS).
 * 
 * Note: In ISO 19111:2003, this class was called SC_CoordinateReferenceSystem.
 * @author Florian.Esser
 * @created 16-Apr-2014 23:49:49
 */
@Access(AccessType.FIELD)
@Audited @Entity
public abstract class SingleCoordinateReferenceSystemItem<D extends DatumItem> extends CoordinateReferenceSystemItem 
{
	/**
	 * Derived association to the datum used by this coordinate reference system.
	 */
	@ManyToOne(targetEntity = DatumItem.class)
	private D datum;
	

	protected SingleCoordinateReferenceSystemItem() {
		super();
	}

	public SingleCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			D datum) {

		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope);
		
		this.datum = datum;
	}

	public D getDatum() {
		return datum;
	}

	public void setDatum(D newVal){
		datum = newVal;
	}

}//end SC_SingleCRS