package org.iso.registry.core.model.operation;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Audited @Entity 
public class ParameterValueGroupItem extends GeneralParameterValue<OperationParameterGroupItem>
{
	private static final long serialVersionUID = -5692912518765858211L;

	@ManyToOne
	private OperationParameterGroupItem group;

	public ParameterValueGroupItem() {
		super();
	}

	public ParameterValueGroupItem(UUID id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public OperationParameterGroupItem getGroup() {
		return group;
	}

	public void setGroup(OperationParameterGroupItem group) {
		this.group = group;
	}
	
	
}
