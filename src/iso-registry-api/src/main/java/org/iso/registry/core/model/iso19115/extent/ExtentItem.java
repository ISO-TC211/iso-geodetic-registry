package org.iso.registry.core.model.iso19115.extent;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@Access(AccessType.FIELD)
@ItemClass("Extent")
@Audited @Entity
public class ExtentItem extends RE_RegisterItem
{
	@ManyToOne(cascade = CascadeType.ALL)
	private EX_Extent extent;

	protected ExtentItem() {
		super();
	}

	public ExtentItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {

		super(register, itemClass, name, definition, additionInformation);
	}

	public ExtentItem(EX_Extent extent) {
		this.extent = extent;
	}

	public EX_Extent getExtent() {
		return this.extent;
	}

	public void setExtent(EX_Extent extent) {
		this.extent = extent;
	}
}
