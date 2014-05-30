package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity
public class OperationMethodItem extends IdentifiedItem
{
	@ManyToOne(optional = false)
	private Formula formulaReference;
	
	private Integer sourceDimensions;
	
	private Integer targetDimensions;
	
	@ManyToMany
	private List<GeneralOperationParameterItem> parameter;

	protected OperationMethodItem() {
		super();
	}

	public OperationMethodItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
	}

	public Formula getFormulaReference() {
		return formulaReference;
	}

	public void setFormulaReference(Formula formulaReference) {
		this.formulaReference = formulaReference;
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

	public List<GeneralOperationParameterItem> getParameter() {
		return parameter;
	}

	public void setParameter(List<GeneralOperationParameterItem> parameter) {
		this.parameter = parameter;
	}
	
	public void addParameter(GeneralOperationParameterItem parameter) {
		if (this.parameter == null) {
			this.parameter = new ArrayList<>();
		}
		this.parameter.add(parameter);
	}
	
}
