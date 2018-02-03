package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.CitationDTO;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.isotc211.iso19139.metadata.CI_Citation_PropertyType;

import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.GeneralOperationParameterItemProposal_PropertyType;
import de.geoinfoffm.registry.api.soap.OperationMethodItemProposal_Type;
import de.geoinfoffm.registry.api.soap.OperationParameterItemProposal_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class OperationMethodItemProposalDTO extends IdentifiedItemProposalDTO
{
	public static enum FormulaType {
		FORMULA,
		REFERENCE
	}
	
	private String formula;
	private CitationDTO formulaCitation;
	private FormulaType formulaType = FormulaType.FORMULA;
	private Integer sourceDimensions;
	private Integer targetDimensions;
	private List<OperationParameterItemProposalDTO> parameter;
//	private String parameters;
	private Boolean reversible;

	public OperationMethodItemProposalDTO() {
		super("OperationMethod");
	}

	public OperationMethodItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(OperationMethodItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(OperationMethodItemProposal_Type itemDetails) {
		super(itemDetails);
	}

	public OperationMethodItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public OperationMethodItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		super.initializeFromItemDetails(itemDetails);
	
		if (itemDetails instanceof OperationMethodItemProposal_Type) {
			OperationMethodItemProposal_Type xmlProposal = (OperationMethodItemProposal_Type) itemDetails;
	
			this.setFormula(xmlProposal.getFormula());	
			this.setReversible(xmlProposal.isReversible());	
			this.setSourceDimensions(xmlProposal.getSourceDimensions());	
			this.setTargetDimensions(xmlProposal.getTargetDimensions());	
			
			for (GeneralOperationParameterItemProposal_PropertyType parameterProperty : xmlProposal.getParameter()) {
				if (parameterProperty != null) {
					final OperationParameterItemProposalDTO dto;
					if (parameterProperty.isSetAbstractGeneralOperationParameterItemProposal()) {
						if (parameterProperty.getAbstractGeneralOperationParameterItemProposal() instanceof OperationParameterItemProposal_Type) {
							dto = new OperationParameterItemProposalDTO((OperationParameterItemProposal_Type)parameterProperty.getAbstractGeneralOperationParameterItemProposal());
						}
						else {
							throw new RuntimeException(String.format("Unexpected parameter type: %s", parameterProperty.getAbstractGeneralOperationParameterItemProposal().getClass().getCanonicalName()));
						}
					}
					else if (parameterProperty.isSetUuidref()) {
						dto = new OperationParameterItemProposalDTO();
						dto.setReferencedItemUuid(UUID.fromString(parameterProperty.getUuidref()));
					}
					else {
						throw new RuntimeException("unexpected reference");
					}
					this.getParameter().add(dto);
				}
			}
			
			if (xmlProposal.getInformationSource() != null) {
				for (CI_Citation_PropertyType citationProperty : xmlProposal.getInformationSource()) {
					if (citationProperty.isSetCI_Citation()) {
						CitationDTO citation = new CitationDTO(citationProperty.getCI_Citation());
						this.getInformationSource().add(citation);
					}
				}
			}
		}	
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof OperationMethodItem) {
			OperationMethodItem item = (OperationMethodItem)registerItem;

			switch (formulaType) {
				case FORMULA:
					item.setFormula(this.getFormula());
					break;
				case REFERENCE:
					item.setFormulaCitation(this.getFormulaCitation().toCitation());
					break;
			}
			
			item.setReversible(this.getReversible());
			if (this.getParameter() != null) {
				for (GeneralOperationParameterItemProposalDTO paramDto : this.getParameter()) {
					if (paramDto.getReferencedItemUuid() != null) {
						GeneralOperationParameterItem parameter = entityManager.find(GeneralOperationParameterItem.class, paramDto.getReferencedItemUuid());
						item.getParameter().add(parameter);
					}
				}
			}
			
//			if (!StringUtils.isEmpty(this.getParameters())) {
//				if (item.getParameter() != null) {
//					while (!item.getParameter().isEmpty()) {
//						item.removeParameter(0);
//					}
//				}

//				for (String uuidText : StringUtils.delimitedListToStringArray(this.parameters, ","," ")) {
//					UUID uuid = UUID.fromString(uuidText);
//					GeneralOperationParameterItem parameter = entityManager.find(GeneralOperationParameterItem.class, uuid);
//					item.addParameter(parameter);
//				}
//			}
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof OperationMethodItem) {
			OperationMethodItem item = (OperationMethodItem)registerItem;
			
			if (item.getFormula() != null) {
				this.setFormulaType(FormulaType.FORMULA);
				this.setFormula(item.getFormula());
			}
			else if (item.getFormulaCitation() != null) {
				this.setFormulaType(FormulaType.REFERENCE);
				this.setFormula(item.getFormulaCitation().getTitle());
			}
			
			this.setReversible(item.getReversible());
			for (GeneralOperationParameterItem parameter : item.getParameter()) {
				if (parameter instanceof OperationParameterItem) {
					this.getParameter().add(new OperationParameterItemProposalDTO((OperationParameterItem)parameter));
				}
			}
			
//			this.setParameters(item.getParameter());
//			List<String> uuids = new ArrayList<>();
//			for (GeneralOperationParameterItem parameter : item.getParameter()) {
////				if (parameter instanceof OperationParameterItem) {
////					this.addParameter(new OperationParameterItemProposalDTO((OperationParameterItem)parameter));
////				}
//				uuids.add(parameter.getUuid().toString() + ":[" + parameter.getCode().toString() + "] " + parameter.getName());
//			}
//			String[] uuidTexts = uuids.toArray(new String[] { });
//			this.setParameters(StringUtils.arrayToCommaDelimitedString(uuidTexts));
		}
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public FormulaType getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(FormulaType formulaType) {
		this.formulaType = formulaType;
	}

	public Integer getSourceDimensions() {
		return sourceDimensions;
	}

	public void setSourceDimensions(Integer sourceDimensions) {
		this.sourceDimensions = sourceDimensions;
	}

	public Integer getTargetDimensions() {
		return targetDimensions;
	}

	public void setTargetDimensions(Integer targetDimensions) {
		this.targetDimensions = targetDimensions;
	}

	public List<OperationParameterItemProposalDTO> getParameter() {
		if (this.parameter == null) {
			this.parameter = new ArrayList<>();
		}
		return parameter;
	}

	protected void setParameter(List<OperationParameterItemProposalDTO> parameter) {
		this.parameter = parameter;
	}
//	
//	public void addParameter(OperationParameterItemProposalDTO parameter) {
//		if (this.parameter == null) {
//			this.parameter = new ArrayList<>();
//		}
//		this.parameter.add(parameter);
//	}

//	/**
//	 * @return the parameters
//	 */
//	public String getParameters() {
//		return parameters;
//	}
//
//	/**
//	 * @param parameters the parameters to set
//	 */
//	public void setParameters(String parameters) {
//		this.parameters = parameters;
//	}
	
	public void setParameters(Collection<GeneralOperationParameterItem> parameters) {
//		List<String> uuidList = new ArrayList<>();
		for (GeneralOperationParameterItem parameter : parameters) {
//			uuidList.add(parameter.getUuid().toString());
			if (parameter instanceof OperationParameterItem) {
				this.getParameter().add(new OperationParameterItemProposalDTO((OperationParameterItem)parameter));
			}
			else {
				throw new RuntimeException(String.format("Invalid parameter type: %s", parameter.getClass().getCanonicalName()));
			}
		}
//		this.parameters = StringUtils.collectionToCommaDelimitedString(uuidList);
	}

	public Boolean getReversible() {
		return reversible;
	}

	public void setReversible(Boolean reversible) {
		this.reversible = reversible;
	}

	public CitationDTO getFormulaCitation() {
		return formulaCitation;
	}

	public void setFormulaCitation(CitationDTO formulaCitation) {
		this.formulaCitation = formulaCitation;
		if (formulaCitation != null) {
			this.setFormulaType(FormulaType.REFERENCE);
		}
	}

}
