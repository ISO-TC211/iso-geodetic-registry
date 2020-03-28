package org.iso.registry.core.model.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.ObjectDomain;

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
	private List<SingleOperationItem> coordinateOperations;

	protected ConcatenatedOperationItem() {
		super();
	}

	public ConcatenatedOperationItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, Collection<ObjectDomain> domains) {
		super(register, itemClass, name, definition, additionInformation, domains);
	}

	public List<SingleOperationItem> getCoordinateOperations() {
		if (this.coordinateOperations == null) {
			this.coordinateOperations = new ArrayList<>();
		}

		return coordinateOperations;
	}

	public void setCoordinateOperations(List<SingleOperationItem> coordOperation) {
		this.coordinateOperations = coordOperation;
	}
}
