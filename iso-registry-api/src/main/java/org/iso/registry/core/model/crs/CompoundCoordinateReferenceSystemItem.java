package org.iso.registry.core.model.crs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("CompoundCRS")
@Access(AccessType.FIELD)
@Table(name = "CompoundCRS")
@Audited @Entity
public class CompoundCoordinateReferenceSystemItem extends CoordinateReferenceSystemItem
{
	@ManyToMany
	@OrderColumn(name = "INDEX")
	private List<SingleCoordinateReferenceSystemItem<? extends DatumItem>> componentReferenceSystem;

	public CompoundCoordinateReferenceSystemItem() {
		super();
	}
	
	public CompoundCoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name,
			String definition, RE_AdditionInformation additionInformation, EX_Extent domainOfValidity, String scope,
			List<SingleCoordinateReferenceSystemItem<? extends DatumItem>> componentReferenceSystems) {

		super(register, itemClass, name, definition, additionInformation, domainOfValidity, scope);
		
		this.componentReferenceSystem = new ArrayList<SingleCoordinateReferenceSystemItem<? extends DatumItem>>();
		this.componentReferenceSystem.addAll(componentReferenceSystems);
	}

	public List<SingleCoordinateReferenceSystemItem<? extends DatumItem>> getComponentReferenceSystem() {
		if (this.componentReferenceSystem == null) {
			this.componentReferenceSystem = new ArrayList<SingleCoordinateReferenceSystemItem<? extends DatumItem>>();			
		}
		return componentReferenceSystem;
	}
}
