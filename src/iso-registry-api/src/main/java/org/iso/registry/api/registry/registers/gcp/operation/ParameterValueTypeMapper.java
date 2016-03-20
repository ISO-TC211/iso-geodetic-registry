package org.iso.registry.api.registry.registers.gcp.operation;

import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;

import de.bespire.registry.io.excel.ValueMapper;

public class ParameterValueTypeMapper implements ValueMapper<String, OperationParameterValueType> {

	@Override
	public OperationParameterValueType map(String value) {
		switch (value) {
			case "Measure":
				return OperationParameterValueType.MEASURE;
			case "Sequence<Measure>":
				return OperationParameterValueType.MEASURE_LIST;
			case "Integer":
				return OperationParameterValueType.INTEGER;
			case "Sequence<Integer>":
				return OperationParameterValueType.INTEGER_LIST;
			case "CharacterString":
				return OperationParameterValueType.STRING;
			case "Boolean":
				return OperationParameterValueType.BOOLEAN;
			case "Reference File":
				return OperationParameterValueType.FILE;
		}
		
		try {
			return OperationParameterValueType.valueOf(value);
		}
		catch (Throwable t) {
			throw new IllegalArgumentException(String.format("Value '%s' cannot be a mapped to OperationParameterValueType", value));
		}
	}

	@Override
	public String mapInverse(OperationParameterValueType value) {
		switch (value) {
			case MEASURE:
				return "Measure";
			case MEASURE_LIST:
				return "Sequence<Measure>";
			case INTEGER:
				return "Integer";
			case INTEGER_LIST:
				return "Sequence<Integer>";
			case STRING:
				return "CharacterString";
			case BOOLEAN:
				return "Boolean";
			case FILE:
				return "Reference File";
			default:
				return value.name();
		}
	}
}
