package org.iso.registry.core.model.datum;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("EngineeringDatum")
@Access(AccessType.FIELD)
@Table(name = "EngineeringDatum")
@Audited @Entity
public class EngineeringDatumItem extends DatumItem
{
	protected EngineeringDatumItem() { 
	}

}
