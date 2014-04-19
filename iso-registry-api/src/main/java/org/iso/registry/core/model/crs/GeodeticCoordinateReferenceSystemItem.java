package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.cs.CartesianCoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.EllipsoidalCoordinateSystemItem;
import org.iso.registry.core.model.cs.SphericalCoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.GeodeticDatumItem;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

/**
 * A coordinate reference system associated with a geodetic datum.
 * 
 * @author Florian.Esser
 * @created 16-Apr-2014 23:49:49
 */
@ItemClass("GeodeticCRS")
@Access(AccessType.FIELD)
@Audited @Entity
public class GeodeticCoordinateReferenceSystemItem extends SingleCoordinateReferenceSystemItem<GeodeticDatumItem>
{
	/**
	 * Association to the coordinate system used by this geodetic
	 * CRS.
	 */
	@ManyToOne
	private CoordinateSystemItem coordinateSystem;
	
	protected GeodeticCoordinateReferenceSystemItem() {
	}

	protected GeodeticCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			GeodeticDatumItem datum, CoordinateSystemItem cs) {

		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum);
		
		this.coordinateSystem = cs;
	}

	public GeodeticCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			GeodeticDatumItem datum, CartesianCoordinateSystemItem cs) {

		this(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, (CoordinateSystemItem)cs);
	}

	public GeodeticCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			GeodeticDatumItem datum, EllipsoidalCoordinateSystemItem cs) {

		this(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, (CoordinateSystemItem)cs);
	}

	public GeodeticCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			GeodeticDatumItem datum, SphericalCoordinateSystemItem cs) {

		this(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, (CoordinateSystemItem)cs);
	}

	public CoordinateSystemItem getCoordinateSystem() {
		return coordinateSystem;
	}

	protected void setCoordinateSystem(CoordinateSystemItem newVal) {
		coordinateSystem = newVal;
	}
	
	public void setCoordinateSystem(CartesianCoordinateSystemItem cs) {
		this.coordinateSystem = cs;
	}

	public void setCoordinateSystem(EllipsoidalCoordinateSystemItem cs) {
		this.coordinateSystem = cs;
	}

	public void setCoordinateSystem(SphericalCoordinateSystemItem cs) {
		this.coordinateSystem = cs;
	}

	@Override
	public GeodeticDatumItem getDatum() {
		return (GeodeticDatumItem)super.getDatum();
	}

}// end SC_GeodeticCRS