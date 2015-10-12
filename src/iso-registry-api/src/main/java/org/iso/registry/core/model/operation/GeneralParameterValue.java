package org.iso.registry.core.model.operation;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Audited @Entity
public abstract class GeneralParameterValue<T extends GeneralOperationParameterItem> extends de.geoinfoffm.registry.core.Entity
{
	private static final long serialVersionUID = 7949551150334805213L;

	@ManyToOne(targetEntity = GeneralOperationParameterItem.class)
	private T parameter;

	protected GeneralParameterValue() {
		super();
	}

	public GeneralParameterValue(UUID id) {
		super(id);
	}

	public T getParameter() {
		return parameter;
	}

	public void setParameter(T parameter) {
		this.parameter = parameter;
	}

}
