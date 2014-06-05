package org.iso.registry.client;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.core.model.operation.GeneralParameterValue;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.SingleOperationItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class SingleOperationItemViewBean extends CoordinateOperationItemViewBean
{
	private OperationMethodItemViewBean method;
	private List<GeneralParameterValueViewBean> parameterValues;

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
	protected void addAdditionalProperties(RE_RegisterItem registerItem) {
		super.addAdditionalProperties(registerItem);
		
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
					this.addParameterValue(new OperationParameterValueViewBean((OperationParameterValue)parameterValue));
				}
				else {
					this.addParameterValue(new GeneralParameterValueViewBean(parameterValue));
				}
			}
		}
	}

	public OperationMethodItemViewBean getMethod() {
		return method;
	}

	public void setMethod(OperationMethodItemViewBean method) {
		this.method = method;
	}

	public List<GeneralParameterValueViewBean> getParameterValues() {
		return parameterValues;
	}

	public void setParameterValue(List<GeneralParameterValueViewBean> parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	public void addParameterValue(GeneralParameterValueViewBean parameterValue) {
		if (this.parameterValues == null) {
			this.parameterValues = new ArrayList<>();
		}
		this.parameterValues.add(parameterValue);
	}

}
