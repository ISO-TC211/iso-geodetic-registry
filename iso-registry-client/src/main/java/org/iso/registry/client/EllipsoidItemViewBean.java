package org.iso.registry.client;

import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.iso19103.MeasureType;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class EllipsoidItemViewBean extends IdentifiedItemViewBean
{
	/**
	 * Length of the semi-major axis of the ellipsoid. 
	 */
	private Double semiMajorAxis;
	
	private UnitOfMeasureItemViewBean semiMajorAxisUom;
	
	/**
	 * Inverse flattening value of the ellipsoid. 
	 */
	private Double inverseFlattening;
	
	private UnitOfMeasureItemViewBean inverseFlatteningUom;
	
	/**
	 * The ellipsoid is degenerate and is actually a sphere. The sphere is completely defined by the semi-major axis, which is
	 * the radius of the sphere. This attribute this has the value "true" if the figure is a sphere.
	 */
	private boolean sphere;
	
	/**
	 * Length of the semi-minor axis of the ellipsoid. 
	 */
	private Double semiMinorAxis;
	
	private UnitOfMeasureItemViewBean semiMinorAxisUom;

	public EllipsoidItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public EllipsoidItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public EllipsoidItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public EllipsoidItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public EllipsoidItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public EllipsoidItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof EllipsoidItem)) {
			return;
		}
		
		EllipsoidItem item = (EllipsoidItem)registerItem;

		this.setSphere(item.isSphere());
		this.setSemiMajorAxis(item.getSemiMajorAxis());
		this.setSemiMinorAxis(item.getSemiMinorAxis());
		this.setInverseFlattening(item.getInverseFlattening());
		
		if (item.getSemiMajorAxisUom() != null) {
			this.setSemiMajorAxisUom(new UnitOfMeasureItemViewBean(item.getSemiMajorAxisUom(), false));
		}
		if (item.getSemiMinorAxisUom() != null) {
			this.setSemiMinorAxisUom(new UnitOfMeasureItemViewBean(item.getSemiMinorAxisUom(), false));
		}
		if (item.getInverseFlatteningUom() != null) {
			this.setInverseFlatteningUom(new UnitOfMeasureItemViewBean(item.getInverseFlatteningUom(), false));
		}
	}
		
	public Double getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public void setSemiMajorAxis(Double semiMajorAxis) {
		this.semiMajorAxis = semiMajorAxis;
	}

	public UnitOfMeasureItemViewBean getSemiMajorAxisUom() {
		return semiMajorAxisUom;
	}

	public void setSemiMajorAxisUom(UnitOfMeasureItemViewBean semiMajorAxisUom) {
		this.semiMajorAxisUom = semiMajorAxisUom;
	}

	public Double getInverseFlattening() {
		return inverseFlattening;
	}

	public void setInverseFlattening(Double inverseFlattening) {
		this.inverseFlattening = inverseFlattening;
	}

	public UnitOfMeasureItemViewBean getInverseFlatteningUom() {
		return inverseFlatteningUom;
	}

	public void setInverseFlatteningUom(UnitOfMeasureItemViewBean inverseFlatteningUom) {
		this.inverseFlatteningUom = inverseFlatteningUom;
	}

	public boolean getSphere() {
		return sphere;
	}

	public void setSphere(boolean isSphere) {
		this.sphere = isSphere;
	}

	public Double getSemiMinorAxis() {
		return semiMinorAxis;
	}

	public void setSemiMinorAxis(Double semiMinorAxis) {
		this.semiMinorAxis = semiMinorAxis;
	}

	public UnitOfMeasureItemViewBean getSemiMinorAxisUom() {
		return semiMinorAxisUom;
	}

	public void setSemiMinorAxisUom(UnitOfMeasureItemViewBean semiMinorAxisUom) {
		this.semiMinorAxisUom = semiMinorAxisUom;
	}

}
