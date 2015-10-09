package org.iso.registry.core.model.operation;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.iso.registry.core.model.UnitOfMeasureItem;

import de.geoinfoffm.registry.core.ValueObject;

@Embeddable
public class Measure extends ValueObject
{
	private static final long serialVersionUID = -8230130508271453028L;

	private Number value;
	
	@ManyToOne
	private UnitOfMeasureItem uom;

	protected Measure() {
		// TODO Auto-generated constructor stub
	}
	
	public Measure(Number value, UnitOfMeasureItem uom) {
		this.value = value;
		this.uom = uom;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public UnitOfMeasureItem getUom() {
		return uom;
	}

	public void setUom(UnitOfMeasureItem uom) {
		this.uom = uom;
	}

}
