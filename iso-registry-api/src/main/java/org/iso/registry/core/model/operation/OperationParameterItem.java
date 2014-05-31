package org.iso.registry.core.model.operation;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("OperationParameter")
@Access(AccessType.FIELD)
@Audited @Entity
public class OperationParameterItem extends GeneralOperationParameterItem
{
	private static final long serialVersionUID = -5131420248626658557L;

	public OperationParameterItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OperationParameterItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
		// TODO Auto-generated constructor stub
	}
	
	

}
