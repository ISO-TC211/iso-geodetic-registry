package org.iso.registry.core.model.datum;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;

import de.geoinfoffm.registry.core.ItemClass;

@ItemClass("Ellipsoid")
@Access(AccessType.FIELD)
@Table(name = "Ellipsoid")
@Audited @Entity
public class EllipsoidItem extends IdentifiedItem
{
	/**
	 * Length of the semi-major axis of the ellipsoid. 
	 */
	private Double semiMajorAxis;
	
	@ManyToOne
	private UnitOfMeasureItem semiMajorAxisUom;
	
	/**
	 * Inverse flattening value of the ellipsoid. 
	 */
	private Double inverseFlattening;
	
	@ManyToOne
	private UnitOfMeasureItem inverseFlatteningUom;
	
	/**
	 * The ellipsoid is degenerate and is actually a sphere. The sphere is completely defined by the semi-major axis, which is
	 * the radius of the sphere. This attribute this has the value "true" if the figure is a sphere.
	 */
	private boolean isSphere = true;
	
	/**
	 * Length of the semi-minor axis of the ellipsoid. 
	 */
	private Double semiMinorAxis;
	
	@ManyToOne
	private UnitOfMeasureItem semiMinorAxisUom;

	public Double getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public void setSemiMajorAxis(Double semiMajorAxis) {
		this.semiMajorAxis = semiMajorAxis;
	}

	public UnitOfMeasureItem getSemiMajorAxisUom() {
		return semiMajorAxisUom;
	}

	public void setSemiMajorAxisUom(UnitOfMeasureItem semiMajorAxisUom) {
		this.semiMajorAxisUom = semiMajorAxisUom;
	}

	public Double getInverseFlattening() {
		return inverseFlattening;
	}

	public void setInverseFlattening(Double inverseFlattening) {
		this.inverseFlattening = inverseFlattening;
	}

	public UnitOfMeasureItem getInverseFlatteningUom() {
		return inverseFlatteningUom;
	}

	public void setInverseFlatteningUom(UnitOfMeasureItem inverseFlatteningUom) {
		this.inverseFlatteningUom = inverseFlatteningUom;
	}

	public boolean isSphere() {
		return isSphere;
	}

	public void setSphere(boolean isSphere) {
		this.isSphere = isSphere;
	}

	public Double getSemiMinorAxis() {
		return semiMinorAxis;
	}

	public void setSemiMinorAxis(Double semiMinorAxis) {
		this.semiMinorAxis = semiMinorAxis;
	}

	public UnitOfMeasureItem getSemiMinorAxisUom() {
		return semiMinorAxisUom;
	}

	public void setSemiMinorAxisUom(UnitOfMeasureItem semiMinorAxisUom) {
		this.semiMinorAxisUom = semiMinorAxisUom;
	}

}
