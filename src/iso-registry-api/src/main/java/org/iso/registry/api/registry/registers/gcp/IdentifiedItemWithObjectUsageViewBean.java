package org.iso.registry.api.registry.registers.gcp;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.core.model.IdentifiedItemWithObjectUsage;
import org.iso.registry.core.model.ObjectDomain;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

/**
 * View bean for {@link IdentifiedItemWithObjectUsage} objects
 * 
 * @author Florian Esser
 *
 */
public class IdentifiedItemWithObjectUsageViewBean extends IdentifiedItemViewBean
{
	private List<ObjectDomainViewBean> domains;

	public IdentifiedItemWithObjectUsageViewBean(Appeal appeal) {
		super(appeal);
	}

	public IdentifiedItemWithObjectUsageViewBean(Proposal proposal) {
		super(proposal);
	}

	public IdentifiedItemWithObjectUsageViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public IdentifiedItemWithObjectUsageViewBean(RE_RegisterItem item) {
		super(item);
	}

	public IdentifiedItemWithObjectUsageViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public IdentifiedItemWithObjectUsageViewBean(Supersession supersession) {
		super(supersession);
	}

	public List<ObjectDomainViewBean> getDomains() {
		if (domains == null) {
			this.domains = new ArrayList<>();
		}
		return domains;
	}

	public void setDomains(List<ObjectDomainViewBean> domains) {
		this.domains = domains;
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);

		if (registerItem instanceof IdentifiedItemWithObjectUsage) {
			IdentifiedItemWithObjectUsage item = (IdentifiedItemWithObjectUsage)registerItem;

			for (ObjectDomain domain : item.getDomains()) {
				this.getDomains().add(new ObjectDomainViewBean(domain));
			}
		}
	}

}
