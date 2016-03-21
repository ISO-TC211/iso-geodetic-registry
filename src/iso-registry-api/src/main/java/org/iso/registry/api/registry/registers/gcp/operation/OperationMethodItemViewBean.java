package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.api.Formula_Type;
import org.iso.registry.api.GcoConverter;
import org.iso.registry.api.GeneralOperationParameterItem_PropertyType;
import org.iso.registry.api.OperationMethodItem_Type;
import org.iso.registry.api.registry.registers.gcp.CitationDTO;
import org.iso.registry.api.registry.registers.gcp.IdentifiedItemViewBean;
import org.iso.registry.api.registry.registers.gcp.operation.OperationMethodItemProposalDTO.FormulaType;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.iso.IsoXmlFactory;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class OperationMethodItemViewBean extends IdentifiedItemViewBean
{
	private String formula;
	private CitationDTO formulaCitation;
	private FormulaType formulaType = FormulaType.FORMULA;
	private Integer sourceDimensions;
	private Integer targetDimensions;
	private List<GeneralOperationParameterItemViewBean> parameter;
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
	public void setXmlValues(RE_RegisterItem_Type result) {
		super.setXmlValues(result);
		if (result instanceof OperationMethodItem_Type) {
			final OperationMethodItem_Type xmlBean = (OperationMethodItem_Type)result;
			final OperationMethodItemViewBean viewBean = this;
			
			Formula_Type formula = new Formula_Type();
			switch (viewBean.getFormulaType()) {
				case FORMULA:
					formula.setFormula(IsoXmlFactory.characterString(viewBean.getFormula()));
					break;
				case REFERENCE:
					formula.setFormulaCitation(IsoXmlFactory.citation(viewBean.getFormulaCitation().toCitation()));
					break;
			}
			xmlBean.setFormulaReference(formula);
			
			xmlBean.setSourceDimensions(GcoConverter.convertToGcoInteger(viewBean.getSourceDimensions()));
			xmlBean.setTargetDimensions(GcoConverter.convertToGcoInteger(viewBean.getTargetDimensions()));
			xmlBean.setReversible(GcoConverter.convertToGcoBoolean(viewBean.getReversible()));
			for (GeneralOperationParameterItemViewBean viewBeanValue : viewBean.getParameter()) {
				if (viewBeanValue != null && viewBeanValue.getUuid() != null) {
					final GeneralOperationParameterItem_PropertyType xmlBeanProp= new GeneralOperationParameterItem_PropertyType(); 
					xmlBeanProp.setUuidref(viewBeanValue.getUuid().toString());
					xmlBean.getParameter().add(xmlBeanProp);
				}	
			}
		}
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
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
			this.setFormulaCitation(new CitationDTO(item.getFormulaCitation()));
		}
		
		this.setReversible(item.getReversible());
		for (GeneralOperationParameterItem parameter : item.getParameter()) {
			if (parameter instanceof OperationParameterItem) {
				this.getParameter().add(new GeneralOperationParameterItemViewBean((OperationParameterItem)parameter, false));
			}
		}
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public CitationDTO getFormulaCitation() {
		return formulaCitation;
	}

	public void setFormulaCitation(CitationDTO formulaCitation) {
		this.formulaCitation = formulaCitation;
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

	public List<GeneralOperationParameterItemViewBean> getParameter() {
		if (this.parameter == null) {
			this.parameter = new ArrayList<>();
		}
		return parameter;
	}

	public void setParameter(List<GeneralOperationParameterItemViewBean> parameter) {
		this.parameter = parameter;
	}
	
	public Boolean getReversible() {
		return reversible;
	}

	public void setReversible(Boolean reversible) {
		this.reversible = reversible;
	}

}
