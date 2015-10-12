package org.iso.registry.core.model.operation;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Audited @Entity
public class PassThroughOperationItem extends CoordinateOperationItem
{

	protected PassThroughOperationItem() {
	}

	public PassThroughOperationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
	}

}
