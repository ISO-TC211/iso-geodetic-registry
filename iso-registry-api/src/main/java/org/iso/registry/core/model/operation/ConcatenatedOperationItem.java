package org.iso.registry.core.model.operation;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Audited @Entity
public class ConcatenatedOperationItem extends CoordinateOperationItem
{

	protected ConcatenatedOperationItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
		// TODO Auto-generated constructor stub
	}

}
