package org.iso.registry.core.model.operation;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity
public class GeneralOperationParameterItem extends IdentifiedItem
{
	private static final long serialVersionUID = -9162996295486776660L;

	private Integer minimumOccurs;
	
	@ManyToOne
	private OperationParameterGroupItem group;
	
	protected GeneralOperationParameterItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GeneralOperationParameterItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
		// TODO Auto-generated constructor stub
	}

	public Integer getMinimumOccurs() {
		return minimumOccurs;
	}

	public void setMinimumOccurs(Integer minimumOccurs) {
		this.minimumOccurs = minimumOccurs;
	}

	public OperationParameterGroupItem getGroup() {
		return group;
	}

	public void setGroup(OperationParameterGroupItem group) {
		this.group = group;
	}

}
