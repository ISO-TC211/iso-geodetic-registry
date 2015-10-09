package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity 
public class OperationParameterGroupItem extends GeneralOperationParameterItem
{
	private static final long serialVersionUID = 8540130140059976493L;

	@OneToMany(mappedBy = "group")
	private List<GeneralOperationParameterItem> parameter;
	
	private Integer maximumOccurs;

	protected OperationParameterGroupItem() {
	}

	public OperationParameterGroupItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
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

	public Integer getMaximumOccurs() {
		return maximumOccurs;
	}

	public void setMaximumOccurs(Integer maximumOccurs) {
		this.maximumOccurs = maximumOccurs;
	}

}
