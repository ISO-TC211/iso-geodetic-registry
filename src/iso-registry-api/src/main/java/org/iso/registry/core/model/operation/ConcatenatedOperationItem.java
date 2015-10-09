package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("ConcatenatedOperation")
@Access(AccessType.FIELD)
@Audited @Entity
public class ConcatenatedOperationItem extends CoordinateOperationItem
{
	@ManyToMany
	private List<CoordinateOperationItem> coordOperation;

	protected ConcatenatedOperationItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation) {
		super(register, itemClass, name, definition, additionInformation);
		// TODO Auto-generated constructor stub
	}

	public List<CoordinateOperationItem> getCoordOperation() {
		return coordOperation;
	}

	public void setCoordOperation(List<CoordinateOperationItem> coordOperation) {
		this.coordOperation = coordOperation;
	}
	
	public void addCoordOperation(CoordinateOperationItem coordOperation) {
		if (this.coordOperation != null) {
			this.coordOperation = new ArrayList<>();
		}
		this.coordOperation.add(coordOperation);
	}

}
