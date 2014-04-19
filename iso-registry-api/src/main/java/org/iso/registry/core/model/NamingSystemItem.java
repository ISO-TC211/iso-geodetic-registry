package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("NamingSystem")
@Access(AccessType.FIELD)
@Audited @Entity
public class NamingSystemItem extends IdentifiedItem
{

}
