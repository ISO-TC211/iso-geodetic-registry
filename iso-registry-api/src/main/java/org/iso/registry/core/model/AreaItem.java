package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@ItemClass("Area")
@Access(AccessType.FIELD)
@Audited @Entity
public class AreaItem extends RE_RegisterItem
{
	@Basic(optional = false)
	@Column(name = "AREA_CODE", unique = true)
	private Integer code;

	private AreaItem() {}
	
	public AreaItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, Integer code) {

		super(register, itemClass, name, definition, additionInformation);
		
		this.setCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
