package org.iso.registry.core.model.operation;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("Conversion")
@Access(AccessType.FIELD)
@Audited @Entity
public class ConversionItem extends SingleOperationItem
{

	protected ConversionItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConversionItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
		// TODO Auto-generated constructor stub
	}

}
