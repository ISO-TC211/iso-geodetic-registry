package org.iso.registry.client;

import org.iso.registry.core.model.operation.GeneralParameterValue;

public class GeneralParameterValueViewBean
{
	private IdentifiedItemViewBean parameter;

	public GeneralParameterValueViewBean() {
		// TODO Auto-generated constructor stub
	}

	public GeneralParameterValueViewBean(GeneralParameterValue<?> parameterValue) {
		this.parameter = new IdentifiedItemViewBean(parameterValue.getParameter(), false);
	}
	
	public String getParameterName() {
		return parameter.getName();
	}

}
