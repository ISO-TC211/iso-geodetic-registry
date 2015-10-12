package org.iso.registry.core.model.iso19103;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Length extends Measure
{
	@Embedded
	private UnitOfMeasure unitOfMeasure;
	
	public Length(Number value, UnitOfMeasure unitOfMeasure) {
		super(value);
		
		this.unitOfMeasure = unitOfMeasure;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
}
