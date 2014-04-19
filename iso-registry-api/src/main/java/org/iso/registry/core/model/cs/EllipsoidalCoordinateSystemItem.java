package org.iso.registry.core.model.cs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("EllipsoidalCS")
@Access(AccessType.FIELD)
@Audited @Entity
public class EllipsoidalCoordinateSystemItem extends CoordinateSystemItem
{

}
