package org.iso.registry.core.model.datum;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;

@Access(AccessType.FIELD)
@Table(name = "Datum")
@Audited @Entity
public abstract class DatumItem extends IdentifiedItem
{
	/**
	 * The datum definition - a description, possibly including coordinates of
	 * an identified point or points, of the relationship used to anchor the
	 * coordinate system to the Earth or alternate object.
	 * 
	 * - For a geodetic datum, this anchor may be a point known as the
	 * fundamental point, which is traditionally the point where the
	 * relationship between geoid and ellipsoid is defined, together with a
	 * direction from that point. In other cases, the anchor may consist of a
	 * number of points. In those cases, the parameters defining the
	 * geoid/ellipsoid relationship have then been averaged for these points,
	 * and the coordinates of the points adopted as the datum definition.
	 * 
	 * - For an engineering datum, the anchor may be an identified physical
	 * point with the orientation defined relative to the object.
	 * 
	 * - For an image datum, the anchor is usually either the centre of the
	 * image or the corner of the image. The coordinate system orientation is
	 * defined through the CS_AxisDirection class.
	 */
	@Column(name = "ORIGIN_DESCRIPTION", columnDefinition = "text")
	private String anchorDefinition;
	/**
	 * Area or region or timeframe in which this datum is valid.
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private EX_Extent domainOfValidity;
	/**
	 * The time after which this datum definition is valid. This time may be
	 * precise (e.g. 1997.0 for IRTF97) or merely a year (e.g. 1986 for
	 * NAD83(86)). In the latter case, the epoch usually refers to the year in
	 * which a major recalculation of the geodetic control network, underlying
	 * the datum, was executed or initiated. An old datum may remain valid after
	 * a new datum is defined. Alternatively, a datum may be replaced by a later
	 * datum, in which case the realization epoch for the new datum defines the
	 * upper limit for the validity of the replaced datum.
	 */
	@Column(name = "REALIZATION_EPOCH")
	private String realizationEpoch;
	/**
	 * Description of usage, or limitations of usage, for which this datum is
	 * valid. If unknown, enter "not known".
	 */
	@Column(name = "DATUM_SCOPE", columnDefinition = "text")	
	private String scope;
	
	/**
	 * Non-standard attribute to differentiate between the date of publication (realizationEpoch)
	 * and the reference date of this datum.
	 */
	private String coordinateReferenceEpoch;

	public String getAnchorDefinition() {
		return anchorDefinition;
	}

	public EX_Extent getDomainOfValidity() {
		return domainOfValidity;
	}

	public String getRealizationEpoch() {
		return realizationEpoch;
	}

	public String getScope() {
		return scope;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAnchorDefinition(String newVal) {
		anchorDefinition = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDomainOfValidity(EX_Extent newVal) {
		domainOfValidity = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setRealizationEpoch(String newVal) {
		realizationEpoch = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setScope(String newVal) {
		scope = newVal;
	}

	public String getCoordinateReferenceEpoch() {
		return coordinateReferenceEpoch;
	}

	public void setCoordinateReferenceEpoch(String coordinateReferenceEpoch) {
		this.coordinateReferenceEpoch = coordinateReferenceEpoch;
	}

}
