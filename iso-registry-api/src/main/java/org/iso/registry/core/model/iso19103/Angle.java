package org.iso.registry.core.model.iso19103;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Access(AccessType.FIELD)
@Embeddable
public class Angle extends Measure
{
	private UnitOfMeasure unitOfMeasure;
	
	public Angle(Number value, UnitOfMeasure unitOfMeasure) {
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
