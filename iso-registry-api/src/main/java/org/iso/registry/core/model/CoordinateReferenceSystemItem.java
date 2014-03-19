package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;
import org.hibernate.type.TrueFalseType;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@ItemClass("CoordinateReferenceSystem")
@Access(AccessType.FIELD)
@Audited @Entity
public class CoordinateReferenceSystemItem extends RE_RegisterItem
{
	@Basic(optional = false)
	@Column(name = "COORD_REF_SYS_CODE", unique = true)
	private Integer code;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "COORD_REF_SYS_KIND")
	private CoordinateSystemType type;
	
	private CoordinateReferenceSystemItem() {}

	public CoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, Integer code) {

		super(register, itemClass, name, definition, additionInformation);
		
		this.code = code;
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
