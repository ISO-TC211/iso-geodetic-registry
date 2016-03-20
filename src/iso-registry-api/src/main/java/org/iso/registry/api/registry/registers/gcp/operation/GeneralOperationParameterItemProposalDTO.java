package org.iso.registry.api.registry.registers.gcp.operation;

import java.math.BigInteger;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.AliasDTO;
import org.iso.registry.core.model.Alias;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.soap.Addition_Type;

public abstract class GeneralOperationParameterItemProposalDTO extends IdentifiedItemProposalDTO
{
	private Integer minimumOccurs;

	public GeneralOperationParameterItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public GeneralOperationParameterItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public GeneralOperationParameterItemProposalDTO(GeneralOperationParameterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public GeneralOperationParameterItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public GeneralOperationParameterItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public GeneralOperationParameterItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof GeneralOperationParameterItem) {
			GeneralOperationParameterItem item = (GeneralOperationParameterItem)registerItem;
			
			item.setMinimumOccurs(this.getMinimumOccurs());
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof GeneralOperationParameterItem) {
			GeneralOperationParameterItem item = (GeneralOperationParameterItem)registerItem;

			this.setMinimumOccurs(item.getMinimumOccurs());
		}
	}


	public Integer getMinimumOccurs() {
		return minimumOccurs;
	}

	public void setMinimumOccurs(Integer minimumOccurs) {
		this.minimumOccurs = minimumOccurs;
	}

}
