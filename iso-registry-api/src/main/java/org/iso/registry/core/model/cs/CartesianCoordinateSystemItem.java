package org.iso.registry.core.model.cs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("CartesianCS")
@Access(AccessType.FIELD)
@Table(name = "CartesianCS")
@Audited @Entity
public class CartesianCoordinateSystemItem extends CoordinateSystemItem
{

}
