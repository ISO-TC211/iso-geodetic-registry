package org.iso.registry.core.model.iso19115.dataquality;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Audited @Entity 
public abstract class Result extends de.geoinfoffm.registry.core.Entity implements DQ_Result
{

}
