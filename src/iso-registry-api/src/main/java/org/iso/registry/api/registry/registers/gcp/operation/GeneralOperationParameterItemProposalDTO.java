package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.soap.AbstractGeneralOperationParameterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.OperationParameterGroupItemProposal_PropertyType;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

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

	public GeneralOperationParameterItemProposalDTO(AbstractGeneralOperationParameterItemProposal_Type itemDetails) {
		super(itemDetails);
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
	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		super.initializeFromItemDetails(itemDetails);
	
		if (itemDetails instanceof AbstractGeneralOperationParameterItemProposal_Type) {
			AbstractGeneralOperationParameterItemProposal_Type xmlProposal = (AbstractGeneralOperationParameterItemProposal_Type) itemDetails;
	
			this.setMinimumOccurs(xmlProposal.getMinimumOccurs());	
			
//			final OperationParameterGroupItemProposal_PropertyType groupProperty = xmlProposal.getGroup();
//			if (groupProperty != null) {
//				final OperationParameterGroupItemProposalDTO dto;
//				if (groupProperty.isSetOperationParameterGroupItemProposal()) {
//					dto = new OperationParameterGroupItemProposalDTO(groupProperty.getOperationParameterGroupItemProposal());
//				}
//				else if (groupProperty.isSetUuidref()) {
//					dto = new OperationParameterGroupItemProposalDTO();
//					dto.setReferencedItemUuid(UUID.fromString(groupProperty.getUuidref()));
//				}
//				else {
//					throw new RuntimeException("unexpected reference");
//				}
//				
//				this.setGroup(dto);
//			}
		}	
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
