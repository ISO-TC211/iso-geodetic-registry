package org.iso.registry.api.registry.registers.gcp.operation;

import org.iso.registry.api.registry.registers.gcp.IdentifiedItemViewBean;
import org.iso.registry.core.model.operation.GeneralParameterValue;

public class GeneralParameterValueViewBean
{
	private GeneralOperationParameterItemViewBean parameter;

	public GeneralParameterValueViewBean() {
		// TODO Auto-generated constructor stub
	}

	public GeneralParameterValueViewBean(GeneralParameterValue<?> parameterValue) {
		this.parameter = new GeneralOperationParameterItemViewBean(parameterValue.getParameter(), false);
	}
	
	public String getParameterName() {
		return parameter.getName();
	}
	
	public IdentifiedItemViewBean getParameter() {
		return this.parameter;
	}

}
