package org.iso.registry.core.model.operation;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Audited @Entity
public class OperationParameterValueItem extends GeneralParameterValue<OperationParameterItem>
{
	private static final long serialVersionUID = -7253389945022467418L;

	private String parameterType;
	
	@Column(columnDefinition = "text")
	private String parameterValue;

	protected OperationParameterValueItem() {
		// TODO Auto-generated constructor stub
	}

	public OperationParameterValueItem(UUID id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

}
