package org.iso.registry.api.registry.registers.gcp.operation; 

import java.util.HashSet;
import java.util.Set;

import org.iso.registry.api.GcoConverter;
import org.iso.registry.api.GeneralOperationParameterItem_PropertyType;
import org.iso.registry.api.OperationParameterGroupItem_Type;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.OperationParameterGroupItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class OperationParameterGroupItemViewBean extends GeneralOperationParameterItemViewBean {

	private Set<GeneralOperationParameterItemViewBean>  parameter;
	private Integer maximumOccurs;
	
	public OperationParameterGroupItemViewBean(RE_RegisterItem item) {
		super(item);
	}
	
	public OperationParameterGroupItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}
	
	public OperationParameterGroupItemViewBean(Proposal proposal) {
		super(proposal);
	}
	
	public OperationParameterGroupItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}
	
	public OperationParameterGroupItemViewBean(Appeal appeal) {
		super(appeal);
	}
	
	public OperationParameterGroupItemViewBean(Supersession supersession) {
		super(supersession);
	}

	// getters and setters
	
	public Set<GeneralOperationParameterItemViewBean> getParameter() {
		if (this.parameter == null) {
			this.parameter = new HashSet<GeneralOperationParameterItemViewBean> (); 
		}
		return this.parameter;
	}
	
	public Integer getMaximumOccurs() {
		return this.maximumOccurs;
	}
	
	public void setMaximumOccurs(Integer maximumOccurs) {
		this.maximumOccurs = maximumOccurs;
	}

		@Override
		public void setXmlValues(RE_RegisterItem_Type result) {
			super.setXmlValues(result);
			if (result instanceof OperationParameterGroupItem_Type) {
				final OperationParameterGroupItem_Type xmlBean = (OperationParameterGroupItem_Type)result;
				final OperationParameterGroupItemViewBean viewBean = this;
				for (GeneralOperationParameterItemViewBean viewBeanValue: viewBean.getParameter()) {
					if (viewBeanValue != null && viewBeanValue.getUuid() != null) {
						final GeneralOperationParameterItem_PropertyType xmlBeanProp= new GeneralOperationParameterItem_PropertyType(); 
						xmlBeanProp.setUuidref(viewBeanValue.getUuid().toString());
						xmlBean.getParameter().add(xmlBeanProp);
					}	
				}
				xmlBean.setMaximumOccurs(GcoConverter.convertToGcoInteger(viewBean.getMaximumOccurs()));
			}
		}

		@Override
		protected void addAdditionalProperties(RE_RegisterItem item, boolean loadDetails) {
			super.addAdditionalProperties(item, loadDetails);
			
			if (item instanceof OperationParameterGroupItem) {
				final OperationParameterGroupItem modelBean = (OperationParameterGroupItem)item;
				final OperationParameterGroupItemViewBean viewBean = this;
				viewBean.setMaximumOccurs(modelBean.getMaximumOccurs());
				if (loadDetails) {
					for (GeneralOperationParameterItem value: modelBean.getParameter()) {
						if (value != null) {
					viewBean.getParameter().add(new GeneralOperationParameterItemViewBean(value,false));
						}	
					}
				}
			}
		}
}
