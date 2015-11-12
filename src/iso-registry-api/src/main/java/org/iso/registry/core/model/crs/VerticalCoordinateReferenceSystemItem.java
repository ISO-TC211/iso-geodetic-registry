package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.VerticalCoordinateSystemItem;
import org.iso.registry.core.model.datum.VerticalDatumItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("VerticalCRS")
@Access(AccessType.FIELD)
@Table(name = "VerticalCRS")
@Audited @Entity
public class VerticalCoordinateReferenceSystemItem extends SingleCoordinateReferenceSystemItem<VerticalDatumItem>
{

	protected VerticalCoordinateReferenceSystemItem() {
	}

	protected VerticalCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, EX_Extent domainOfValidity, String scope,
			VerticalDatumItem datum, CoordinateSystemItem cs) {

		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, cs);
	}

	public VerticalCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, EX_Extent domainOfValidity, String scope,
			VerticalDatumItem datum, VerticalCoordinateSystemItem cs) {

		this(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, (CoordinateSystemItem)cs);
	}

	@Override
	public void setCoordinateSystem(CoordinateSystemItem newVal) {
		if (!(newVal instanceof VerticalCoordinateSystemItem)) { 
			throw new IllegalArgumentException("Illegal coordinate system type for Geodetic CRS");
		}
		super.setCoordinateSystem(newVal);
	}
	
	public void setCoordinateSystem(VerticalCoordinateSystemItem cs) {
		super.setCoordinateSystem(cs);
	}

	@Override
	public VerticalDatumItem getDatum() {
		return (VerticalDatumItem)super.getDatum();
	}

}
