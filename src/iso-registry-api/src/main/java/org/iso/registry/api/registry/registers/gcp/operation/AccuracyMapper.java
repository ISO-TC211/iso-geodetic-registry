package org.iso.registry.api.registry.registers.gcp.operation;

import de.bespire.registry.io.excel.ValueMapper;

public class AccuracyMapper implements ValueMapper<String, Float> {

	@Override
	public Float map(String value) {
		return Float.parseFloat(value);
	}

	@Override
	public String mapInverse(Float value) {
		return value.toString();
	}
	

}
