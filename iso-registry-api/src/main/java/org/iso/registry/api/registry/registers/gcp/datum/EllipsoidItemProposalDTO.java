package org.iso.registry.api.registry.registers.gcp.datum;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.datum.EllipsoidItem;

public class EllipsoidItemProposalDTO extends IdentifiedItemProposalDTO
{
	/**
	 * Length of the semi-major axis of the ellipsoid. 
	 */
	private double semiMajorAxis;
	
	private UnitOfMeasureItemProposalDTO semiMajorAxisUom;
	
	/**
	 * Inverse flattening value of the ellipsoid. 
	 */
	private double inverseFlattening;
	
	private UnitOfMeasureItemProposalDTO inverseFlatteningUom;
	
	/**
	 * The ellipsoid is degenerate and is actually a sphere. The sphere is completely defined by the semi-major axis, which is
	 * the radius of the sphere. This attribute this has the value "true" if the figure is a sphere.
	 */
	private boolean isSphere = true;
	
	/**
	 * Length of the semi-minor axis of the ellipsoid. 
	 */
	private double semiMinorAxis;
	
	private UnitOfMeasureItemProposalDTO semiMinorAxisUom;

	public EllipsoidItemProposalDTO() { }
	
	public EllipsoidItemProposalDTO(EllipsoidItem item) {
		super(item);
	}

	public double getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public void setSemiMajorAxis(double semiMajorAxis) {
		this.semiMajorAxis = semiMajorAxis;
	}

	public UnitOfMeasureItemProposalDTO getSemiMajorAxisUom() {
		return semiMajorAxisUom;
	}

	public void setSemiMajorAxisUom(UnitOfMeasureItemProposalDTO semiMajorAxisUom) {
		this.semiMajorAxisUom = semiMajorAxisUom;
		this.semiMinorAxisUom = semiMajorAxisUom;
		this.inverseFlatteningUom = semiMajorAxisUom;
	}

	public double getInverseFlattening() {
		return inverseFlattening;
	}

	public void setInverseFlattening(double inverseFlattening) {
		this.inverseFlattening = inverseFlattening;
	}

	public UnitOfMeasureItemProposalDTO getInverseFlatteningUom() {
		return inverseFlatteningUom;
	}

	public void setInverseFlatteningUom(UnitOfMeasureItemProposalDTO inverseFlatteningUom) {
		this.inverseFlatteningUom = inverseFlatteningUom;
	}

	public boolean isSphere() {
		return isSphere;
	}

	public void setSphere(boolean isSphere) {
		this.isSphere = isSphere;
	}

	public double getSemiMinorAxis() {
		return semiMinorAxis;
	}

	public void setSemiMinorAxis(double semiMinorAxis) {
		this.semiMinorAxis = semiMinorAxis;
	}

	public UnitOfMeasureItemProposalDTO getSemiMinorAxisUom() {
		return semiMinorAxisUom;
	}

	public void setSemiMinorAxisUom(UnitOfMeasureItemProposalDTO semiMinorAxisUom) {
		this.semiMinorAxisUom = semiMinorAxisUom;
	}

	
}
