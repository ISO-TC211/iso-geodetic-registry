package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@ItemClass("Area")
@Access(AccessType.FIELD)
@AttributeOverrides({
	@AttributeOverride(name = "name", column = @Column(name = "AREA_NAME")),
	@AttributeOverride(name = "description", column = @Column(name = "AREA_OF_USE"))
})
@Audited @Entity
public class AreaItem extends RE_RegisterItem
{
	@Basic(optional = false)
	@Column(name = "AREA_CODE", unique = true)
	private Integer code;

	@Embedded
	private EX_GeographicBoundingBox boundingBox;
	
	@Column(name = "ISO_A2_CODE")
	private String isoA2Code;

	@Column(name = "ISO_A3_CODE")
	private String isoA3Code;

	@Column(name = "ISO_N_CODE")
	private String isoNCode;

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

	public EX_GeographicBoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(EX_GeographicBoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public String getIsoA2Code() {
		return isoA2Code;
	}

	public void setIsoA2Code(String isoA2Code) {
		this.isoA2Code = isoA2Code;
	}

	public String getIsoA3Code() {
		return isoA3Code;
	}

	public void setIsoA3Code(String isoA3Code) {
		this.isoA3Code = isoA3Code;
	}

	public String getIsoNCode() {
		return isoNCode;
	}

	public void setIsoNCode(String isoNCode) {
		this.isoNCode = isoNCode;
	}

}
