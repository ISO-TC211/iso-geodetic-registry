package org.iso.registry.client;

import java.util.Collection;
import java.util.UUID;

import org.iso.registry.core.model.operation.Measure;
import org.iso.registry.core.model.operation.OperationParameterValue;

public class OperationParameterValueViewBean extends GeneralParameterValueViewBean
{
	private Object value;
	private UnitOfMeasureItemViewBean unitOfMeasure;

	public OperationParameterValueViewBean() {
		// TODO Auto-generated constructor stub
	}

	public OperationParameterValueViewBean(OperationParameterValue parameterValue) {
		super(parameterValue);
		
		if (parameterValue.getValue() instanceof Measure) {
			this.unitOfMeasure = new UnitOfMeasureItemViewBean(((Measure)parameterValue.getValue()).getUom(), false);
			this.value = ((Measure)parameterValue.getValue()).getValue();
		}
		else if (parameterValue.getValue() instanceof Collection) {
			throw new UnsupportedOperationException("Lists not yet supported");
		}
		else {
			this.value = parameterValue.getValue();
		}
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public UnitOfMeasureItemViewBean getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(UnitOfMeasureItemViewBean unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	
}
