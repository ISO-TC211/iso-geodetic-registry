package org.iso.registry.core.model.crs;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.iso.registry.api.registry.registers.gcp.crs.ReferenceSystemItem;
import org.iso.registry.core.model.Alias;

import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

//@ItemClasses({
//	@ItemClass("CompoundCRS"),
//	@ItemClass("EngineeringCRS"),
//	@ItemClass("DerivedCRS"),
//	@ItemClass("ProjectedCRS"),
//	@ItemClass("GeodeticCRS"),
//	@ItemClass("ImageCRS"), 
//	@ItemClass("VerticalCRS"), 
//	@ItemClass("TemporalCRS") 
//})
@Access(AccessType.FIELD)
@Audited @Entity
public class CoordinateReferenceSystemItem extends ReferenceSystemItem
{
	private String scope;
	
	protected CoordinateReferenceSystemItem() {}

	public CoordinateReferenceSystemItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, AreaItem domainOfValidity, String scope) {

		super(register, itemClass, name, definition, additionInformation, domainOfValidity);
		
		this.scope = scope;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
