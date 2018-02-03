package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.Collection;

import org.iso.registry.api.registry.registers.gcp.CitationDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemViewBean;
import org.iso.registry.core.model.operation.Measure;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;

public class OperationParameterValueViewBean extends GeneralParameterValueViewBean
{
	private Object value;
	private UnitOfMeasureItemViewBean unitOfMeasure;
	private OperationParameterValueType parameterType;
	private CitationDTO referenceFileCitation;

	public OperationParameterValueViewBean() {
		// TODO Auto-generated constructor stub
	}

	public OperationParameterValueViewBean(OperationParameterValue parameterValue) {
		super(parameterValue);
		
		this.parameterType = parameterValue.getParameterType();
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
		
		if (parameterValue.getReferenceFileCitation() != null) {
			this.referenceFileCitation = new CitationDTO(parameterValue.getReferenceFileCitation());
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

	public CitationDTO getReferenceFileCitation() {
		return referenceFileCitation;
	}

	public void setReferenceFileCitation(CitationDTO referenceFileCitation) {
		this.referenceFileCitation = referenceFileCitation;
	}
	
}
