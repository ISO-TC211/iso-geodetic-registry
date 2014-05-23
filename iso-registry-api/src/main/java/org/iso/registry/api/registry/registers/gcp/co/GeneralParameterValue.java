package org.iso.registry.api.registry.registers.gcp.co;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Audited @Entity
public abstract class GeneralParameterValue extends de.geoinfoffm.registry.core.Entity
{

}
