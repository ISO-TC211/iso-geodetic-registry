package org.iso.registry.core.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class EpsgIsoMapping extends de.geoinfoffm.registry.core.Entity
{
	private String itemClass;
	private Integer epsgCode;
	@Type(type = "pg-uuid")	
	private UUID isoUuid;

	private EpsgIsoMapping() { }
	
	public EpsgIsoMapping(String itemClass, Integer epsgCode, UUID isoUuid) {
		this.itemClass = itemClass;
		this.epsgCode = epsgCode;
		this.isoUuid = isoUuid;
	}

	public String getItemClass() {
		return itemClass;
	}

	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}

	public Integer getEpsgCode() {
		return epsgCode;
	}

	public void setEpsgCode(Integer epsgCode) {
		this.epsgCode = epsgCode;
	}

	public UUID getIsoUuid() {
		return isoUuid;
	}

	public void setIsoUuid(UUID isoUuid) {
		this.isoUuid = isoUuid;
	}

}
