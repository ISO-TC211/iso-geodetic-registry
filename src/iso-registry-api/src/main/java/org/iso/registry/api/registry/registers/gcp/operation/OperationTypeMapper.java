package org.iso.registry.api.registry.registers.gcp.operation;

import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;

import de.bespire.registry.io.excel.ValueMapper;

public class OperationTypeMapper implements ValueMapper<String, SingleOperationType> {

	@Override
	public SingleOperationType map(String value) {
		switch (value) {
			case "Coordinate Conversion (Deriving Operation)":
				return SingleOperationType.CONVERSION;
			case "Coordinate Transformation":
				return SingleOperationType.TRANSFORMATION;
		}
		
		try {
			return SingleOperationType.valueOf(value);
		}
		catch (Throwable t) {
			throw new IllegalArgumentException(String.format("Value '%s' cannot be a mapped to SingleOperationType", value));
		}
	}

	@Override
	public String mapInverse(SingleOperationType value) {
		switch (value) {
			case CONVERSION: 
				return "Coordinate Conversion (Deriving Operation)";
			case TRANSFORMATION:
				return "Coordinate Transformation";
			default:
				return value.name();
		}
	}

}
