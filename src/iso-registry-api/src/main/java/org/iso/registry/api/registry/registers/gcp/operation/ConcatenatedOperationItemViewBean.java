package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.api.ConcatenatedOperationItem_Type;
import org.iso.registry.api.SingleOperationItem_PropertyType;
import org.iso.registry.core.model.operation.ConcatenatedOperationItem;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class ConcatenatedOperationItemViewBean extends CoordinateOperationItemViewBean
{
	private List<SingleOperationItemViewBean> coordinateOperations;
	
	public ConcatenatedOperationItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setXmlValues(RE_RegisterItem_Type result) {
		super.setXmlValues(result);
		if (result instanceof ConcatenatedOperationItem_Type) {
			final ConcatenatedOperationItem_Type xmlBean = (ConcatenatedOperationItem_Type)result;
			final ConcatenatedOperationItemViewBean viewBean = this;
			for (SingleOperationItemViewBean viewBeanValue: viewBean.getCoordinateOperations()) {
				if (viewBeanValue != null && viewBeanValue.getUuid() != null) {
					final SingleOperationItem_PropertyType xmlBeanProp= new SingleOperationItem_PropertyType(); 
					xmlBeanProp.setUuidref(viewBeanValue.getUuid().toString());
					xmlBean.getCoordinateOperations().add(xmlBeanProp);
				}	
			}
		}
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof ConcatenatedOperationItem)) {
			return;
		}
		
		ConcatenatedOperationItem item = (ConcatenatedOperationItem)registerItem;

		if (item.getCoordinateOperations() != null) {
			for (SingleOperationItem operation : item.getCoordinateOperations()) {
				this.getCoordinateOperations().add(new SingleOperationItemViewBean(operation, false));
			}
		}
	}

	public List<SingleOperationItemViewBean> getCoordinateOperations() {
		if (this.coordinateOperations == null) {
			this.coordinateOperations = new ArrayList<>();
		}
		
		return this.coordinateOperations;
	}
	
	protected void setCoordinateOperations(List<SingleOperationItemViewBean> operations) {
		this.coordinateOperations = operations;
	}
}
