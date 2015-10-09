package org.iso.registry.core.model.iso19115.dataquality;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Audited @Entity
public abstract class QuantitativeResult extends Result implements DQ_QuantitativeResult
{
}
