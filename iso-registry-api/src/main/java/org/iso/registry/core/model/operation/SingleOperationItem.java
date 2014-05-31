package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.collections.iterators.AbstractListIteratorDecorator;
import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@Access(AccessType.FIELD)
@Audited @Entity
public class SingleOperationItem extends CoordinateOperationItem
{
	@ManyToOne
	private OperationMethodItem method;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<GeneralParameterValue<?>> parameterValue;

	protected SingleOperationItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SingleOperationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
		// TODO Auto-generated constructor stub
	}

	public OperationMethodItem getMethod() {
		return method;
	}

	public void setMethod(OperationMethodItem method) {
		this.method = method;
	}

	/**
	 * @return the parameterValue
	 */
	public List<GeneralParameterValue<?>> getParameterValue() {
		return parameterValue;
	}

	/**
	 * @param parameterValue the parameterValue to set
	 */
	public void setParameterValue(List<GeneralParameterValue<?>> parameterValue) {
		this.parameterValue = parameterValue;
	}

	public void addParameterValue(GeneralParameterValue<?> parameterValue) {
		if (this.parameterValue == null) {
			this.parameterValue = new ArrayList<>();
		}
		this.parameterValue.add(parameterValue);
	}
}
