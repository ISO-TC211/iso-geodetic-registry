package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.core.model.operation.ConcatenatedOperationItem;
import org.iso.registry.core.model.operation.CoordinateOperationItem;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.springframework.util.StringUtils;

import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class ConcatenatedOperationItemProposalDTO extends CoordinateOperationItemProposalDTO
{
//	private List<CoordinateOperationItemProposalDTO> coordinateOperations;
	private String coordinateOperations;

	public ConcatenatedOperationItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(ConcatenatedOperationItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public ConcatenatedOperationItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof ConcatenatedOperationItem) {
			ConcatenatedOperationItem item = (ConcatenatedOperationItem)registerItem;

			if (item.getCoordinateOperations() != null) {
				while (!item.getCoordinateOperations().isEmpty()) {
					item.getCoordinateOperations().remove(0);
				}
			}
			if (!StringUtils.isEmpty(this.getCoordinateOperations())) {
				for (String uuidText : StringUtils.delimitedListToStringArray(this.coordinateOperations, ","," ")) {
					UUID uuid = UUID.fromString(uuidText);
					SingleOperationItem operation = entityManager.find(SingleOperationItem.class, uuid);
					item.getCoordinateOperations().add(operation);
				}
			}
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof ConcatenatedOperationItem) {
			ConcatenatedOperationItem item = (ConcatenatedOperationItem)registerItem;
			
			this.setCoordinateOperations(item.getCoordinateOperations());
		}
	}

	public String getCoordinateOperations() {
		return coordinateOperations;
	}

	public void setCoordinateOperations(String coordinateOperations) {
		this.coordinateOperations = coordinateOperations;
	}

	public void setCoordinateOperations(Collection<SingleOperationItem> operations) {
		List<String> uuidList = new ArrayList<>();
		for (CoordinateOperationItem operation : operations) {
			uuidList.add(operation.getUuid().toString());
		}
		this.coordinateOperations = StringUtils.collectionToCommaDelimitedString(uuidList);
	}
	
//	public List<CoordinateOperationItemProposalDTO> getCoordinateOperations() {
//		if (this.coordinateOperations != null) {
//			this.coordinateOperations = new ArrayList<>();
//		}
//
//		return coordinateOperations;
//	}
//
//	public void setCoordinateOperations(List<CoordinateOperationItemProposalDTO> coordinateOperations) {
//		this.coordinateOperations = coordinateOperations;
//	}

}
