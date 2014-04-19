package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.iso19103.MeasureType;
import org.iso.registry.core.model.iso19103.UnitOfMeasure;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@ItemClass("UnitOfMeasure")
@Access(AccessType.FIELD)
@Audited @Entity
public class UnitOfMeasureItem extends RE_RegisterItem
{
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	private MeasureType measureType;
	
	@ManyToOne
	private UnitOfMeasureItem standardUnit;
	private double offsetToStandardUnit;
	private double scaleToStandardUnit;
	private String symbol;
	
	protected UnitOfMeasureItem() { }
	
	public UnitOfMeasureItem(MeasureType type, String symbol) {
		this.measureType = type;
		this.symbol = symbol;
	}
	
	public MeasureType getMeasureType() {
		return measureType;
	}
	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}
	public UnitOfMeasureItem getStandardUnit() {
		return standardUnit;
	}
	public void setStandardUnit(UnitOfMeasureItem standardUnit) {
		this.standardUnit = standardUnit;
	}
	public double getOffsetToStandardUnit() {
		return offsetToStandardUnit;
	}
	public void setOffsetToStandardUnit(double offsetToStandardUnit) {
		this.offsetToStandardUnit = offsetToStandardUnit;
	}
	public double getScaleToStandardUnit() {
		return scaleToStandardUnit;
	}
	public void setScaleToStandardUnit(double scaleToStandardUnit) {
		this.scaleToStandardUnit = scaleToStandardUnit;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
