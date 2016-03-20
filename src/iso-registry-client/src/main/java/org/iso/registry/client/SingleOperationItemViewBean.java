package org.iso.registry.client;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.api.registry.registers.gcp.operation.SingleOperationType;
import org.iso.registry.core.model.operation.ConversionItem;
import org.iso.registry.core.model.operation.GeneralParameterValue;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.iso.registry.core.model.operation.TransformationItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class SingleOperationItemViewBean extends CoordinateOperationItemViewBean
{
	private OperationMethodItemViewBean method;
	private List<GeneralParameterValueViewBean> parameterValues;
	private SingleOperationType operationType;

	public SingleOperationItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof SingleOperationItem)) {
			return;
		}
		
		SingleOperationItem item = (SingleOperationItem)registerItem;

		if (item.getMethod() != null) {
			this.setMethod(new OperationMethodItemViewBean(item.getMethod(), false));
		}
		if (item.getParameterValue() != null) {
			for (GeneralParameterValue<?> parameterValue : item.getParameterValue()) {
				if (parameterValue instanceof OperationParameterValue) {
					this.getParameterValues().add(new OperationParameterValueViewBean((OperationParameterValue)parameterValue));
				}
				else {
					this.getParameterValues().add(new GeneralParameterValueViewBean(parameterValue));
				}
			}
		}
		
		if (item instanceof TransformationItem) {
			this.setOperationType(SingleOperationType.TRANSFORMATION);
		}
		else if (item instanceof ConversionItem) {
			this.setOperationType(SingleOperationType.CONVERSION);
		}
	}

	public OperationMethodItemViewBean getMethod() {
		return method;
	}

	public void setMethod(OperationMethodItemViewBean method) {
		this.method = method;
	}

	public List<GeneralParameterValueViewBean> getParameterValues() {
		if (this.parameterValues == null) {
			this.parameterValues = new ArrayList<>();
		}

		return parameterValues;
	}

	public void setParameterValue(List<GeneralParameterValueViewBean> parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	public SingleOperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(SingleOperationType operationType) {
		this.operationType = operationType;
	}

}
