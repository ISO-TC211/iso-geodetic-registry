package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.UUID;

import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;

public class ParameterValueDTO {
	
	private OperationParameterItemProposalDTO parameter;
	private OperationParameterValueType parameterType;
	private String value;
	private UnitOfMeasureItemProposalDTO parameterUnit;
	
	public ParameterValueDTO() { }

	public ParameterValueDTO(OperationParameterItemProposalDTO parameter, OperationParameterValueType parameterType, String value) {
		this.parameter = parameter;
		this.value = value;
		this.parameterType = parameterType;
		this.parameterUnit = null;
	}

	public ParameterValueDTO(OperationParameterItemProposalDTO parameter, String value, UnitOfMeasureItemProposalDTO parameterUnit) {
		this.parameter = parameter;
		this.value = value;
		this.parameterUnit = parameterUnit;
		if (this.parameterUnit != null) {
			this.parameterType = OperationParameterValueType.MEASURE;
		}
		else {
			this.parameterType = OperationParameterValueType.STRING;
		}
	}

	public OperationParameterItemProposalDTO getParameter() {
		return parameter;
	}

	public void setParameter(OperationParameterItemProposalDTO parameter) {
		this.parameter = parameter;
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

	public UnitOfMeasureItemProposalDTO getParameterUnit() {
		return parameterUnit;
	}

	public void setParameterUnit(UnitOfMeasureItemProposalDTO parameterUnit) {
		this.parameterUnit = parameterUnit;
	}
}