package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class OperationMethodItemProposalDTO extends IdentifiedItemProposalDTO
{
	public static enum FormulaType {
		FORMULA,
		REFERENCE
	}
	
	private String formula;
	private FormulaType formulaType = FormulaType.FORMULA;
	private Integer sourceDimensions;
	private Integer targetDimensions;
	private List<GeneralOperationParameterItemProposalDTO> parameter;
	private Boolean reversible;

	public OperationMethodItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(OperationMethodItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
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
					CI_Citation ref = new CI_Citation();
					ref.setTitle(this.getFormula());
					item.setFormulaCitation(ref);
					break;
			}
			
			item.setReversible(this.getReversible());
			if (this.getParameter() != null) {
				for (GeneralOperationParameterItemProposalDTO paramDto : this.getParameter()) {
					if (paramDto.getReferencedItemUuid() != null) {
						GeneralOperationParameterItem parameter = entityManager.find(GeneralOperationParameterItem.class, paramDto.getReferencedItemUuid());
						item.addParameter(parameter);
					}
				}
			}
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
					this.addParameter(new OperationParameterItemProposalDTO((OperationParameterItem)parameter));
				}
			}
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

	public List<GeneralOperationParameterItemProposalDTO> getParameter() {
		return parameter;
	}

	public void setParameter(List<GeneralOperationParameterItemProposalDTO> parameter) {
		this.parameter = parameter;
	}
	
	public void addParameter(GeneralOperationParameterItemProposalDTO parameter) {
		if (this.parameter == null) {
			this.parameter = new ArrayList<>();
		}
		this.parameter.add(parameter);
	}

	public Boolean getReversible() {
		return reversible;
	}

	public void setReversible(Boolean reversible) {
		this.reversible = reversible;
	}

}
