package org.iso.registry.api.registry.registers.gcp.operation; 

import org.iso.registry.api.AbstractGeneralOperationParameterItem_Type;
import org.iso.registry.api.GcoConverter;
import org.iso.registry.api.OperationParameterGroupItem_PropertyType;
import org.iso.registry.api.registry.registers.gcp.IdentifiedItemViewBean;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class GeneralOperationParameterItemViewBean extends IdentifiedItemViewBean {

	private Integer minimumOccurs;
	private OperationParameterGroupItemViewBean group;
	
	public GeneralOperationParameterItemViewBean(RE_RegisterItem item) {
		super(item);
	}
	
	public GeneralOperationParameterItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}
	
	public GeneralOperationParameterItemViewBean(Proposal proposal) {
		super(proposal);
	}
	
	public GeneralOperationParameterItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}
	
	public GeneralOperationParameterItemViewBean(Appeal appeal) {
		super(appeal);
	}
	
	public GeneralOperationParameterItemViewBean(Supersession supersession) {
		super(supersession);
	}

	// getters and setters
	
	public Integer getMinimumOccurs() {
		return this.minimumOccurs;
	}
	
	public void setMinimumOccurs(Integer minimumOccurs) {
		this.minimumOccurs = minimumOccurs;
	}
	
	public OperationParameterGroupItemViewBean getGroup() {
		return this.group;
	}
	
	public void setGroup(OperationParameterGroupItemViewBean group) {
		this.group = group;
	}

		@Override
		public void setXmlValues(RE_RegisterItem_Type result) {
			super.setXmlValues(result);
			if (result instanceof AbstractGeneralOperationParameterItem_Type) {
				final AbstractGeneralOperationParameterItem_Type xmlBean = (AbstractGeneralOperationParameterItem_Type)result;
				final GeneralOperationParameterItemViewBean viewBean = this;
				xmlBean.setMinimumOccurs(GcoConverter.convertToGcoInteger(viewBean.getMinimumOccurs()));
				if (viewBean.getGroup() != null && viewBean.getGroup().getUuid() != null) {
					final OperationParameterGroupItem_PropertyType xmlBeanProp= new OperationParameterGroupItem_PropertyType(); 
					xmlBeanProp.setUuidref(viewBean.getGroup().getUuid().toString());
					xmlBean.setGroup(xmlBeanProp);
				}
			}
		}

		@Override
		protected void addAdditionalProperties(RE_RegisterItem item, boolean loadDetails) {
			super.addAdditionalProperties(item, loadDetails);
			
			if (item instanceof GeneralOperationParameterItem) {
				final GeneralOperationParameterItem modelBean = (GeneralOperationParameterItem)item;
				final GeneralOperationParameterItemViewBean viewBean = this;
				viewBean.setMinimumOccurs(modelBean.getMinimumOccurs());
				if (loadDetails) {
					if (modelBean.getGroup() != null) {
						viewBean.setGroup(new OperationParameterGroupItemViewBean(modelBean.getGroup(),false));
					}
				}
			}
		}
}
