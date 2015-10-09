package org.iso.registry.core.model.datum;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("GeodeticDatum")
@Access(AccessType.FIELD)
@Table(name = "GeodeticDatum")
@Audited @Entity
public class GeodeticDatumItem extends DatumItem
{
	/**
	 * Association to the ellipsoid used by this geodetic datum.
	 */
	@ManyToOne
	private EllipsoidItem ellipsoid;
	/**
	 * Association to the prime meridian used by this geodetic datum.
	 */
	@ManyToOne
	private PrimeMeridianItem primeMeridian;

	protected GeodeticDatumItem() {
	}

	public GeodeticDatumItem(EllipsoidItem ellipsoid, PrimeMeridianItem primeMeridian) {
		this.ellipsoid = ellipsoid;
		this.primeMeridian = primeMeridian;
	}

	public EllipsoidItem getEllipsoid() {
		return ellipsoid;
	}

	public void setEllipsoid(EllipsoidItem ellipsoid) {
		this.ellipsoid = ellipsoid;
	}

	public PrimeMeridianItem getPrimeMeridian() {
		return primeMeridian;
	}

	public void setPrimeMeridian(PrimeMeridianItem primeMeridian) {
		this.primeMeridian = primeMeridian;
	}

}
