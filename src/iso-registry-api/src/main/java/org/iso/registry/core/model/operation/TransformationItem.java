package org.iso.registry.core.model.operation;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.ObjectDomain;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("Transformation")
@Access(AccessType.FIELD)
@Audited @Entity
public class TransformationItem extends SingleOperationItem
{

	protected TransformationItem() {
		super();
	}

	public TransformationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, Collection<ObjectDomain> domains) {
		super(register, itemClass, name, definition, additionInformation, domains);
	}

}
