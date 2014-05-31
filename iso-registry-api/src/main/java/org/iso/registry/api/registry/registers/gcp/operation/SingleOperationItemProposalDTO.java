package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.event.internal.OnUpdateVisitor;
import org.iso.registry.core.model.operation.CoordinateOperationItem;
import org.iso.registry.core.model.operation.GeneralParameterValue;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class SingleOperationItemProposalDTO extends CoordinateOperationItemProposalDTO
{
	public static enum SingleOperationType {
		CONVERSION,
		TRANSFORMATION
	}
	
	private SingleOperationType operationType;
	private OperationMethodItemProposalDTO method;
	private List<GeneralParameterValue<?>> parameterValue;

	public SingleOperationItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemProposalDTO(SingleOperationItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemProposalDTO(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof SingleOperationItem) {
			SingleOperationItem item = (SingleOperationItem)registerItem;
			
			if (this.getMethod() != null && this.getMethod().getReferencedItemUuid() != null) {
				OperationMethodItem method = entityManager.find(OperationMethodItem.class, this.getMethod().getReferencedItemUuid());
				item.setMethod(method);
			}
			
			if (this.getParameterValue() != null) {
				for (GeneralParameterValue<?> value : this.getParameterValue()) {
					item.addParameterValue(value);
				}
			}
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof SingleOperationItem) {
			SingleOperationItem item = (SingleOperationItem)registerItem;
			
			if (item.getMethod() != null) {
				this.setMethod(new OperationMethodItemProposalDTO(item.getMethod()));
				if (item.getParameterValue() != null) {
					for (GeneralParameterValue<?> parameterValue : item.getParameterValue()) {
						this.addParameterValue(parameterValue);
					}
				}
			}
		}
	}

	public SingleOperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(SingleOperationType operationType) {
		this.operationType = operationType;
	}

	public OperationMethodItemProposalDTO getMethod() {
		return method;
	}

	public void setMethod(OperationMethodItemProposalDTO method) {
		this.method = method;
	}

	public List<GeneralParameterValue<?>> getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(List<GeneralParameterValue<?>> parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	public void addParameterValue(GeneralParameterValue<?> parameterValue) {
		if (this.parameterValue == null) {
			this.parameterValue = new ArrayList<>();
		}
		this.parameterValue.add(parameterValue);
	}

}
