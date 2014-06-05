package org.iso.registry.core.model.crs;

import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

public class DerivedCoordinateReferenceSystemItem<D extends DatumItem> extends GeneralDerivedCoordinateReferenceSystemItem<D>
{
	private SingleCoordinateReferenceSystemItem<D> baseCrs;

	public DerivedCoordinateReferenceSystemItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DerivedCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			D datum, CoordinateSystemItem coordinateSystem) {
		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, coordinateSystem);
		// TODO Auto-generated constructor stub
	}

	public SingleCoordinateReferenceSystemItem<D> getBaseCrs() {
		return baseCrs;
	}

	public void setBaseCrs(SingleCoordinateReferenceSystemItem<D> baseCrs) {
		this.baseCrs = baseCrs;
	}

}
