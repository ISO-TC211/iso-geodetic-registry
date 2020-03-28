package org.iso.registry.api.registry.registers.gcp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.IdentifiedItemWithObjectUsage;
import org.iso.registry.core.model.ObjectDomain;
import org.iso.registry.core.model.iso19115.extent.ExtentItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.AbstractIdentifiedItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

/**
 * Data transfer object for {@link IdentifiedItemWithObjectUsage} proposals
 *
 * @author Florian Esser
 *
 */
public class IdentifiedItemWithObjectUsageProposalDTO extends IdentifiedItemProposalDTO
{
	private List<ObjectDomainProposalDTO> domains;

	public IdentifiedItemWithObjectUsageProposalDTO() {
		super();
	}

	public IdentifiedItemWithObjectUsageProposalDTO(AbstractIdentifiedItemProposal_Type itemDetails) {
		super(itemDetails);
	}

	public IdentifiedItemWithObjectUsageProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public IdentifiedItemWithObjectUsageProposalDTO(IdentifiedItem item) {
		super(item);
	}

	public IdentifiedItemWithObjectUsageProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public IdentifiedItemWithObjectUsageProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public IdentifiedItemWithObjectUsageProposalDTO(String itemClassName) {
		super(itemClassName);
	}

	@Override
	public List<RegisterItemProposalDTO> getAggregateDependencies() {
		final List<RegisterItemProposalDTO> result = new ArrayList<RegisterItemProposalDTO>();
		result.addAll(super.getAggregateDependencies());
		for (ObjectDomainProposalDTO domain : this.getDomains()) {
			result.add(domain.getDomainOfValidity());
		}

		return super.findDependentProposals((RegisterItemProposalDTO[])result.toArray(new RegisterItemProposalDTO[result.size()]));
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof IdentifiedItemWithObjectUsage) {
			IdentifiedItemWithObjectUsage item = (IdentifiedItemWithObjectUsage)registerItem;

			List<ObjectDomain> domains = new ArrayList<>();
			for (ObjectDomainProposalDTO domainDto : this.getDomains()) {
				if (domainDto.getDomainOfValidity() == null) continue;

				UUID referencedExtentUuid = domainDto.getDomainOfValidity().getReferencedItemUuid();
				if (referencedExtentUuid == null) {
					throw new RuntimeException(MessageFormat.format("Object domain does not reference an extent", referencedExtentUuid));
				}

				ExtentItem referencedExtent = entityManager.find(ExtentItem.class, referencedExtentUuid);
				if (referencedExtent == null) {
					throw new RuntimeException(MessageFormat.format("Referenced Extent '{0}' does not exist", referencedExtentUuid));
				}

				ObjectDomain domain = new ObjectDomain(domainDto.getScope(), referencedExtent);
				domains.add(domain);
			}
			item.setDomains(domains);
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);

		if (registerItem instanceof IdentifiedItemWithObjectUsage) {
			IdentifiedItemWithObjectUsage item = (IdentifiedItemWithObjectUsage)registerItem;

			for (ObjectDomain domain : item.getDomains()) {
				this.getDomains().add(new ObjectDomainProposalDTO(domain));
			}
		}
	}

	public Collection<ObjectDomainProposalDTO> getDomains() {
		if (domains == null) {
			domains = new ArrayList<>();
		}
		return domains;
	}

	public void setDomains(Collection<ObjectDomainProposalDTO> domains) {
		this.domains = new ArrayList<>();
		this.domains.addAll(domains);
	}

}
