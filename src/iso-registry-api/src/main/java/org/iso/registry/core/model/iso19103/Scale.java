package org.iso.registry.core.model.iso19103;

import javax.persistence.Embeddable;

@Embeddable
public class Scale extends Measure
{
	private String sourceUnit;
	private String targetUnit;
	private String unitOfMeasure;
	
	public Scale(Number value, String sourceUnit, String targetUnit, String unitOfMeasure) {
		super(value);
		
		this.sourceUnit = sourceUnit;
		this.targetUnit = targetUnit;
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getSourceUnit() {
		return sourceUnit;
	}

	public void setSourceUnit(String sourceUnit) {
		this.sourceUnit = sourceUnit;
	}

	public String getTargetUnit() {
		return targetUnit;
	}

	public void setTargetUnit(String targetUnit) {
		this.targetUnit = targetUnit;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
}
