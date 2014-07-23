package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.cs.CartesianCoordinateSystemItem;
import org.iso.registry.core.model.datum.GeodeticDatumItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("ProjectedCRS")
@Access(AccessType.FIELD)
@Table(name = "ProjectedCRS")
@Audited @Entity
public class ProjectedCoordinateReferenceSystemItem extends SingleCoordinateReferenceSystemItem<GeodeticDatumItem>
{
	public ProjectedCoordinateReferenceSystemItem() {
		super();
	}
	
	public ProjectedCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, EX_Extent domainOfValidity, String scope,
			GeodeticDatumItem datum, CartesianCoordinateSystemItem coordinateSystem, GeodeticCoordinateReferenceSystemItem baseCrs) {
		
		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, coordinateSystem);
		
		super.setBaseCrs(baseCrs);
	}
	
}
