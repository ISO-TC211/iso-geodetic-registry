package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.UUID;

import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;

public class ParameterValueDTO {
	
	private UUID parameterUuid;
	private String parameterName;
	private OperationParameterValueType parameterType;
	private String value;
	private UUID uomUuid;
	
	public ParameterValueDTO() { }

	public ParameterValueDTO(UUID parameterUuid, String parameterName, OperationParameterValueType parameterType, String value) {
		this.parameterUuid = parameterUuid;
		this.setParameterName(parameterName);
		this.value = value;
		this.parameterType = parameterType;
		this.uomUuid = null;
	}

	public ParameterValueDTO(UUID parameterUuid, String parameterName, String value, UUID uomUuid) {
		this.parameterUuid = parameterUuid;
		this.setParameterName(parameterName);
		this.value = value;
		this.uomUuid = uomUuid;
		if (this.uomUuid != null) {
			this.parameterType = OperationParameterValueType.MEASURE;
		}
		else {
			this.parameterType = OperationParameterValueType.STRING;
		}
	}

	public UUID getParameterUuid() {
		return parameterUuid;
	}

	public void setParameterUuid(UUID parameterUuid) {
		this.parameterUuid = parameterUuid;
	}

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * @return the parameterType
	 */
	public OperationParameterValueType getParameterType() {
		return parameterType;
	}

	/**
	 * @param parameterType the parameterType to set
	 */
	public void setParameterType(OperationParameterValueType parameterType) {
		this.parameterType = parameterType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public UUID getUomUuid() {
		return uomUuid;
	}

	public void setUomUuid(UUID uomUuid) {
		this.uomUuid = uomUuid;
	}
}