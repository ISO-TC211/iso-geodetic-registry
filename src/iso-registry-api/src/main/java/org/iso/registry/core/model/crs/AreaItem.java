package org.iso.registry.core.model.crs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
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
@Table(name = "Area")
@Audited @Entity
public class AreaItem extends IdentifiedItem
{
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "eastBoundLongitude", column = @Column(name = "AREA_EAST_BOUND_LON")),
		@AttributeOverride(name = "northBoundLatitude", column = @Column(name = "AREA_NORTH_BOUND_LAT")),
		@AttributeOverride(name = "westBoundLongitude", column = @Column(name = "AREA_WEST_BOUND_LON")),
		@AttributeOverride(name = "southBoundLatitude", column = @Column(name = "AREA_SOUTH_BOUND_LAT")),
		@AttributeOverride(name = "extentTypeCode", column = @Column(name = "AREA_EXTENT_TYPE_CODE"))
	})
	private EX_GeographicBoundingBox boundingBox;
	
	@Column(name = "ISO_A2_CODE")
	private String isoA2Code;

	@Column(name = "ISO_A3_CODE")
	private String isoA3Code;

	@Column(name = "ISO_N_CODE")
	private Integer isoNCode;
	
	private AreaItem() {}
	
	public AreaItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, Integer code) {

		super(register, itemClass, name, definition, additionInformation);
		
		this.setIdentifier(code);
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

	public Integer getIsoNCode() {
		return isoNCode;
	}

	public void setIsoNCode(Integer isoNCode) {
		this.isoNCode = isoNCode;
	}
}
