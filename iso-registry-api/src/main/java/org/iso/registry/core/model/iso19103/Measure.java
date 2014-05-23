package org.iso.registry.core.model.iso19103;

import javax.persistence.Embeddable;

@Embeddable
public class Measure
{
	private Number value;
	
	public Measure(Number value) {
		this.value = value;
	}

	public Number getValue() {
		return value;
	}

	public void setValue(Number value) {
		this.value = value;
	}
}
