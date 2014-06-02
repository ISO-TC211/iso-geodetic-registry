package org.iso.registry.core.model.crs;

import javax.persistence.ManyToOne;

import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.operation.ConversionItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

public abstract class GeneralDerivedCoordinateReferenceSystemItem<D extends DatumItem> extends SingleCoordinateReferenceSystemItem<D>
{
	@ManyToOne(optional = false)
	private ConversionItem conversion;

	public GeneralDerivedCoordinateReferenceSystemItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GeneralDerivedCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope,
			D datum, CoordinateSystemItem coordinateSystem) {
		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope, datum, coordinateSystem);
	}

	/**
	 * @return the conversion
	 */
	public ConversionItem getConversion() {
		return conversion;
	}

	/**
	 * @param conversion the conversion to set
	 */
	public void setConversion(ConversionItem conversion) {
		this.conversion = conversion;
	}
	
	

}
