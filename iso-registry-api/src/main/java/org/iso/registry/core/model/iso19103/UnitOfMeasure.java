package org.iso.registry.core.model.iso19103;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.envers.Audited;

public class UnitOfMeasure
{
	private MeasureType measureType;
	private String nameStandardUnit;
	private double offsetToStandardUnit;
	private double scaleToStandardUnit;
	private String uomName;
	private String uomSymbol;
	
	public UnitOfMeasure(MeasureType type, String name, String symbol) {
		this.measureType = type;
		this.uomName = name;
		this.uomSymbol = symbol;
	}

	public MeasureType getMeasureType() {
		return measureType;
	}

	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}

	public String getNameStandardUnit() {
		return nameStandardUnit;
	}

	public void setNameStandardUnit(String nameStandardUnit) {
		this.nameStandardUnit = nameStandardUnit;
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

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getUomSymbol() {
		return uomSymbol;
	}

	public void setUomSymbol(String uomSymbol) {
		this.uomSymbol = uomSymbol;
	}
}
