package org.iso.registry.api.registry.registers.gcp.datum;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.EllipsoidItemProposal_Type;
import de.geoinfoffm.registry.api.soap.UnitOfMeasureItemProposal_PropertyType;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

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

	public EllipsoidItemProposalDTO(EllipsoidItemProposal_Type itemDetails) {
		super(itemDetails);
	}
	
	public EllipsoidItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public EllipsoidItemProposalDTO(IdentifiedItem item) {
		super(item);
	}

	public EllipsoidItemProposalDTO(Proposal proposal) {
		super(proposal);
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
	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		super.initializeFromItemDetails(itemDetails);
	
		if (itemDetails instanceof EllipsoidItemProposal_Type) {
			EllipsoidItemProposal_Type xmlProposal = (EllipsoidItemProposal_Type) itemDetails;
	
			this.setInverseFlattening(xmlProposal.getInverseFlattening());	
			this.setSphere(xmlProposal.isIsSphere());	
			this.setSemiMajorAxis(xmlProposal.getSemiMajorAxis());	
			
			final UnitOfMeasureItemProposal_PropertyType inverseFlatteningUomProperty = xmlProposal.getInverseFlatteningUom();
			if (inverseFlatteningUomProperty != null) {
				final UnitOfMeasureItemProposalDTO dto;
				if (inverseFlatteningUomProperty.isSetUnitOfMeasureItemProposal()) {
					dto = new UnitOfMeasureItemProposalDTO(inverseFlatteningUomProperty.getUnitOfMeasureItemProposal());
				}
				else if (inverseFlatteningUomProperty.isSetUuidref()) {
					dto = new UnitOfMeasureItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(inverseFlatteningUomProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setInverseFlatteningUom(dto);
			}
			final UnitOfMeasureItemProposal_PropertyType semiMajorAxisUomProperty = xmlProposal.getSemiMajorAxisUom();
			if (semiMajorAxisUomProperty != null) {
				final UnitOfMeasureItemProposalDTO dto;
				if (semiMajorAxisUomProperty.isSetUnitOfMeasureItemProposal()) {
					dto = new UnitOfMeasureItemProposalDTO(semiMajorAxisUomProperty.getUnitOfMeasureItemProposal());
				}
				else if (semiMajorAxisUomProperty.isSetUuidref()) {
					dto = new UnitOfMeasureItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(semiMajorAxisUomProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setSemiMajorAxisUom(dto);
			}
		}	
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

	@Override
	public List<RegisterItemProposalDTO> getAggregateDependencies() {
		return super.findDependentProposals(this.getSemiMajorAxisUom(), this.getSemiMinorAxisUom(), this.getInverseFlatteningUom());
	}
}
