package org.iso.registry.client;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.api.registry.registers.gcp.operation.GeneralOperationParameterItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.OperationParameterItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.OperationMethodItemProposalDTO.FormulaType;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class OperationMethodItemViewBean extends IdentifiedItemViewBean
{
	private String formula;
	private FormulaType formulaType = FormulaType.FORMULA;
	private Integer sourceDimensions;
	private Integer targetDimensions;
	private List<IdentifiedItemViewBean> parameter;
	private Boolean reversible;
	
	public OperationMethodItemViewBean(Appeal appeal) {
		super(appeal);
	}
	
	public OperationMethodItemViewBean(Proposal proposal) {
		super(proposal);
	}
	
	public OperationMethodItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}
	
	public OperationMethodItemViewBean(RE_RegisterItem item) {
		super(item);
	}
	
	public OperationMethodItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public OperationMethodItemViewBean(Supersession supersession) {
		super(supersession);
	}
	
	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem) {
		super.addAdditionalProperties(registerItem);
		
		if (!(registerItem instanceof OperationMethodItem)) {
			return;
		}
		
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
				this.addParameter(new IdentifiedItemViewBean((OperationParameterItem)parameter, false));
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

	public List<IdentifiedItemViewBean> getParameter() {
		return parameter;
	}

	public void setParameter(List<IdentifiedItemViewBean> parameter) {
		this.parameter = parameter;
	}
	
	public void addParameter(IdentifiedItemViewBean parameter) {
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
