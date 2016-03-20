package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("OperationMethod")
@Access(AccessType.FIELD)
@Audited @Entity
public class OperationMethodItem extends IdentifiedItem
{
	@Column(columnDefinition = "text")
	private String formula;
	
	@OneToOne(cascade = CascadeType.ALL)
	private CI_Citation formulaCitation;
	
	private Integer sourceDimensions;
	
	private Integer targetDimensions;
	
	private Boolean reversible;
	
	@ManyToMany
	private List<GeneralOperationParameterItem> parameter;

	protected OperationMethodItem() {
		super();
	}

	public OperationMethodItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public CI_Citation getFormulaCitation() {
		return formulaCitation;
	}

	public void setFormulaCitation(CI_Citation formulaCitation) {
		this.formulaCitation = formulaCitation;
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
	
	public void removeParameter(int index) {
		if (this.parameter != null) {
			this.parameter.remove(index);
		}
	}

	public Boolean getReversible() {
		return reversible;
	}

	public void setReversible(Boolean reversible) {
		this.reversible = reversible;
	}
	
}
