package org.iso.registry.core.model.iso19115.dataquality;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * Position error estimate (or accuracy) data.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
@Audited @Entity
public abstract class DQ_PositionalAccuracy extends DQ_Element {

}//end DQ_PositionalAccuracy