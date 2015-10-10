package org.iso.registry.api.registry.registers.gcp.datum;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.iso19103.MeasureType;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.soap.Addition_Type;

public class EllipsoidItemProposalDTO extends IdentifiedItemProposalDTO
{
	/**
	 * Length of the semi-major axis of the ellipsoid. 
	 */
	private Double semiMajorAxis;
	
	private UnitOfMeasureItemProposalDTO semiMajorAxisUom;
	
	/**
	 * Inverse flattening value of the ellipsoid. 
	 */
	private Double inverseFlattening;
	
	private UnitOfMeasureItemProposalDTO inverseFlatteningUom;
	
	/**
	 * The ellipsoid is degenerate and is actually a sphere. The sphere is completely defined by the semi-major axis, which is
	 * the radius of the sphere. This attribute this has the value "true" if the figure is a sphere.
	 */
	private boolean isSphere;
	
	/**
	 * Length of the semi-minor axis of the ellipsoid. 
	 */
	private Double semiMinorAxis;
	
	private UnitOfMeasureItemProposalDTO semiMinorAxisUom;

	public EllipsoidItemProposalDTO() { 
		super("Ellipsoid");
	}
	
	public EllipsoidItemProposalDTO(EllipsoidItem item) {
		super(item);
	}
	
	public EllipsoidItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public EllipsoidItemProposalDTO(IdentifiedItem item) {
		super(item);
	}

	public EllipsoidItemProposalDTO(Proposal proposal, ProposalDtoFactory factory) {
		super(proposal, factory);
	}

	public EllipsoidItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public EllipsoidItemProposalDTO(String itemClassName) {
		super(itemClassName);
	}

	public Double getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public void setSemiMajorAxis(Double semiMajorAxis) {
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

	public Double getInverseFlattening() {
		return inverseFlattening;
	}

	public void setInverseFlattening(Double inverseFlattening) {
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

	public Double getSemiMinorAxis() {
		return semiMinorAxis;
	}

	public void setSemiMinorAxis(Double semiMinorAxis) {
		this.semiMinorAxis = semiMinorAxis;
	}

	public UnitOfMeasureItemProposalDTO getSemiMinorAxisUom() {
		return semiMinorAxisUom;
	}

	public void setSemiMinorAxisUom(UnitOfMeasureItemProposalDTO semiMinorAxisUom) {
		this.semiMinorAxisUom = semiMinorAxisUom;
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof EllipsoidItem) {
			EllipsoidItem el = (EllipsoidItem)item;
			el.setInverseFlattening(this.getInverseFlattening());
			if (this.getInverseFlatteningUom() != null) {
				UnitOfMeasureItem uom = entityManager.find(UnitOfMeasureItem.class, this.getInverseFlatteningUom().getReferencedItemUuid());
				el.setInverseFlatteningUom(uom);
			}
			el.setSemiMajorAxis(this.getSemiMajorAxis());
			if (this.getSemiMajorAxisUom() != null) {
				UnitOfMeasureItem uom = entityManager.find(UnitOfMeasureItem.class, this.getSemiMajorAxisUom().getReferencedItemUuid());
				el.setSemiMajorAxisUom(uom);
			}
			el.setSemiMinorAxis(this.getSemiMinorAxis());
			if (this.getSemiMinorAxisUom() != null) {
				UnitOfMeasureItem uom = entityManager.find(UnitOfMeasureItem.class, this.getSemiMinorAxisUom().getReferencedItemUuid());
				el.setSemiMinorAxisUom(uom);
			}
			el.setSphere(this.isSphere());
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof EllipsoidItem) {
			EllipsoidItem el = (EllipsoidItem)item;
			this.setInverseFlattening(el.getInverseFlattening());
			if (el.getInverseFlatteningUom() != null) {
				this.setInverseFlatteningUom(new UnitOfMeasureItemProposalDTO(el.getInverseFlatteningUom()));
			}
			this.setSemiMajorAxis(el.getSemiMajorAxis());
			if (el.getSemiMajorAxisUom() != null) {
				this.setSemiMajorAxisUom(new UnitOfMeasureItemProposalDTO(el.getSemiMajorAxisUom()));
			}
			this.setSemiMinorAxis(el.getSemiMinorAxis());
			if (el.getSemiMinorAxisUom() != null) {
				this.setSemiMinorAxisUom(new UnitOfMeasureItemProposalDTO(el.getSemiMinorAxisUom()));
			}
			this.setSphere(el.isSphere());
		}
	}
	
}
