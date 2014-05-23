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
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.iso19103.MeasureType;
import org.iso.registry.core.model.iso19103.UnitOfMeasure;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

@ItemClass("UnitOfMeasure")
@Access(AccessType.FIELD)
@Table(name = "UnitOfMeasure")
@Audited @Entity
public class UnitOfMeasureItem extends IdentifiedItem
{
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	private MeasureType measureType;

	@ManyToOne
	private UnitOfMeasureItem standardUnit;
	private Double offsetToStandardUnit;
	private Double scaleToStandardUnitNumerator;
	private Double scaleToStandardUnitDenominator;
	private String symbol;

	protected UnitOfMeasureItem() {
	}

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

	public Double getOffsetToStandardUnit() {
		return offsetToStandardUnit;
	}

	public void setOffsetToStandardUnit(Double offsetToStandardUnit) {
		this.offsetToStandardUnit = offsetToStandardUnit;
	}

	public Double getScaleToStandardUnitNumerator() {
		return scaleToStandardUnitNumerator;
	}

	public void setScaleToStandardUnitNumerator(Double scaleToStandardUnitNumerator) {
		this.scaleToStandardUnitNumerator = scaleToStandardUnitNumerator;
	}

	public Double getScaleToStandardUnitDenominator() {
		return scaleToStandardUnitDenominator;
	}

	public void setScaleToStandardUnitDenominator(Double scaleToStandardUnitDenominator) {
		this.scaleToStandardUnitDenominator = scaleToStandardUnitDenominator;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
