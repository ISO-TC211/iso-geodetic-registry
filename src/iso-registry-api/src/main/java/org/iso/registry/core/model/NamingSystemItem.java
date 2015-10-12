package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("NamingSystem")
@Access(AccessType.FIELD)
@Audited @Entity
public class NamingSystemItem extends IdentifiedItem
{
	// The Naming System class has no additional attributes

	public NamingSystemItem() {
		super();
	}

	public NamingSystemItem(RE_Register register, RE_ItemClass itemClass, String name, String definition, RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
	}
}
