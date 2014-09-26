package org.iso.registry.core.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class EpsgIsoMapping extends de.geoinfoffm.registry.core.Entity
{
	private String itemClass;
	private Integer epsgCode;
	@Column(unique = true)
	private Integer isoCode;

	private EpsgIsoMapping() { }
	
	public EpsgIsoMapping(String itemClass, Integer epsgCode, Integer isoCode) {
		this.itemClass = itemClass;
		this.epsgCode = epsgCode;
		this.isoCode = isoCode;
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

	public Integer getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(Integer isoCode) {
		this.isoCode = isoCode;
	}

}
