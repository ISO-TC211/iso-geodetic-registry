package org.iso.registry.core.model.cs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("VerticalCS")
@Access(AccessType.FIELD)
@Table(name = "VerticalCS")
@Audited @Entity
public class VerticalCoordinateSystemItem extends CoordinateSystemItem
{

}
