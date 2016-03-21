package org.iso.registry.api.registry.registers.gcp.crs; 

import org.iso.registry.api.AbstractReferenceSystemItem_Type;
import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.IdentifiedItemViewBean;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.isotc211.iso19139.metadata.EX_Extent_PropertyType;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class ReferenceSystemItemViewBean extends IdentifiedItemViewBean {

	private ExtentDTO domainOfValidity;
	
	public ReferenceSystemItemViewBean(RE_RegisterItem item) {
		super(item);
	}
	
	public ReferenceSystemItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}
	
	public ReferenceSystemItemViewBean(Proposal proposal) {
		super(proposal);
	}
	
	public ReferenceSystemItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}
	
	public ReferenceSystemItemViewBean(Appeal appeal) {
		super(appeal);
	}
	
	public ReferenceSystemItemViewBean(Supersession supersession) {
		super(supersession);
	}

	// getters and setters
	
	public ExtentDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}


		@Override
		public void setXmlValues(RE_RegisterItem_Type result) {
			super.setXmlValues(result);
			if (result instanceof AbstractReferenceSystemItem_Type) {
				final AbstractReferenceSystemItem_Type xmlBean = (AbstractReferenceSystemItem_Type)result;
				final ReferenceSystemItemViewBean viewBean = this;
				if (viewBean.getDomainOfValidity() != null) {
					final EX_Extent_PropertyType xmlBeanProp = new EX_Extent_PropertyType(); 
					xmlBeanProp.setUuidref(viewBean.getUuid().toString());
					xmlBean.setDomainOfValidity(xmlBeanProp);
				}
			}
		}

		@Override
		protected void addAdditionalProperties(RE_RegisterItem item, boolean loadDetails) {
			super.addAdditionalProperties(item, loadDetails);
			
			if (item instanceof ReferenceSystemItem) {
				final ReferenceSystemItem modelBean = (ReferenceSystemItem)item;
				final ReferenceSystemItemViewBean viewBean = this;
				if (loadDetails) {
					if (modelBean.getDomainOfValidity() != null) {
						this.setDomainOfValidity(new ExtentDTO(modelBean.getDomainOfValidity()));
					}
				}
			}
		}
}
