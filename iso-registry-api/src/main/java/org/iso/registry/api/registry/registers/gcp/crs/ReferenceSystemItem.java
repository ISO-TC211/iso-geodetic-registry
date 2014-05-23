package org.iso.registry.api.registry.registers.gcp.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.crs.AreaItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity 
public abstract class ReferenceSystemItem extends IdentifiedItem
{
	@ManyToOne(cascade = CascadeType.PERSIST)
	private AreaItem domainOfValidity;

	public ReferenceSystemItem() {
		super();
	}

	public ReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name, 
			String definition, RE_AdditionInformation additionInformation, AreaItem domainOfValidity) {
		
		super(register, itemClass, name, definition, additionInformation);
		this.domainOfValidity = domainOfValidity;
	}

	public AreaItem getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(AreaItem domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

}
