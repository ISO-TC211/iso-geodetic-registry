package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.operation.GeneralParameterValue;
import org.iso.registry.core.model.operation.Measure;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.springframework.util.StringUtils;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.api.soap.Addition_Type;

public class SingleOperationItemProposalDTO extends CoordinateOperationItemProposalDTO
{
	public static enum SingleOperationType {
		CONVERSION,
		TRANSFORMATION
	}
	
	private SingleOperationType operationType;
	private OperationMethodItemProposalDTO method;
	private List<ParameterValueDTO> parameterValues;

	public SingleOperationItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItemProposalDTO(SingleOperationItem item) {
		super(item);
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

			if (this.getParameterValues() != null) {
				if (item.getParameterValue() != null) {
					while (!item.getParameterValue().isEmpty()) {
						item.getParameterValue().remove(0);
					}
				}
				for (ParameterValueDTO parameterValue : this.getParameterValues()) {
					OperationParameterItem parameter = entityManager.find(OperationParameterItem.class, parameterValue.getParameterUuid());
					
					String paramValue = parameterValue.getValue();
					
					UnitOfMeasureItem uom = null;
					if (parameterValue.getUomUuid() != null) {
						uom = entityManager.find(UnitOfMeasureItem.class, parameterValue.getUomUuid());
					}
					
					OperationParameterValue value;
					if (uom == null) {
						value = new OperationParameterValue(parameter, parameterValue.getParameterType(), paramValue);
					}
					else {
						Number numValue = Double.parseDouble(paramValue);
						Measure measure = new Measure(numValue, uom);
						value = new OperationParameterValue(parameter, measure);
					}

					item.addParameterValue(value);
				}
			}
//			if (this.getParameterValue() != null) {
//				for (GeneralParameterValue<?> value : this.getParameterValue()) {
//					item.addParameterValue(value);
//				}
//			}
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
					this.parameterValues = new ArrayList<>();
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

	/**
	 * @return the parameterValues
	 */
	public List<ParameterValueDTO> getParameterValues() {
		return parameterValues;
	}

	/**
	 * @param parameterValues the parameterValues to set
	 */
	public void setParameterValues(List<ParameterValueDTO> parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	public void addParameterValue(GeneralParameterValue<?> parameterValue) {
		if (this.parameterValues == null) {
			this.parameterValues = new ArrayList<>();
		}
		
		if (parameterValue instanceof OperationParameterValue) {
			OperationParameterValue opv = (OperationParameterValue)parameterValue;

			Object paramValue = opv.getValue();
			UnitOfMeasureItem uom = null;
			if (paramValue instanceof Measure) {
				uom = ((Measure)paramValue).getUom();
				paramValue = ((Measure)paramValue).getValue();
			}
			else if (paramValue instanceof Collection) {
				throw new UnsupportedOperationException("Parameter lists are not yet supported");
			}
			ParameterValueDTO value;
			if (uom == null) {
				value = new ParameterValueDTO(parameterValue.getParameter().getUuid(), parameterValue.getParameter().getName(), ((OperationParameterValue)parameterValue).getParameterType(), paramValue.toString());
			}
			else {
				value = new ParameterValueDTO(parameterValue.getParameter().getUuid(), parameterValue.getParameter().getName(), paramValue.toString(), uom.getUuid());
			}
	
			this.parameterValues.add(value);
		}
	}

//	public List<GeneralParameterValue<?>> getParameterValue() {
//		return parameterValue;
//	}
//
//	public void setParameterValue(List<GeneralParameterValue<?>> parameterValue) {
//		this.parameterValue = parameterValue;
//	}
//	
//	public void addParameterValue(GeneralParameterValue<?> parameterValue) {
//		if (this.parameterValue == null) {
//			this.parameterValue = new ArrayList<>();
//		}
//		this.parameterValue.add(parameterValue);
//	}

}
