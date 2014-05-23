package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.cs.CartesianCoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.SphericalCoordinateSystemItem;
import org.iso.registry.core.model.cs.VerticalCoordinateSystemItem;
import org.iso.registry.core.model.datum.EngineeringDatumItem;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("EngineeringCRS")
@Access(AccessType.FIELD)
@Table(name = "EngineeringCRS")
@Audited @Entity
public class EngineeringCoordinateReferenceSystemItem extends SingleCoordinateReferenceSystemItem<EngineeringDatumItem>
{
	protected EngineeringCoordinateReferenceSystemItem() {
	}

	protected EngineeringCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			EngineeringDatumItem datum, CoordinateSystemItem cs) {

		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, cs);
	}

	public EngineeringCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			EngineeringDatumItem datum, CartesianCoordinateSystemItem cs) {

		this(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, (CoordinateSystemItem)cs);
	}

	public void setCoordinateSystem(CoordinateSystemItem newVal) {
		if (!(newVal instanceof CartesianCoordinateSystemItem) && !(newVal instanceof SphericalCoordinateSystemItem)) { 
			throw new IllegalArgumentException("Illegal coordinate system type for Geodetic CRS");
		}
		super.setCoordinateSystem(newVal);
	}
	
	public void setCoordinateSystem(CartesianCoordinateSystemItem cs) {
		super.setCoordinateSystem(cs);
	}

	public void setCoordinateSystem(SphericalCoordinateSystemItem cs) {
		super.setCoordinateSystem(cs);
	}

	@Override
	public EngineeringDatumItem getDatum() {
		return (EngineeringDatumItem)super.getDatum();
	}


}
