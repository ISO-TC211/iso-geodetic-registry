package org.iso.registry.api.registry.registers.gcp.datum;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.PrimeMeridianItemProposal_Type;
import de.geoinfoffm.registry.api.soap.UnitOfMeasureItemProposal_PropertyType;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class PrimeMeridianItemProposalDTO extends IdentifiedItemProposalDTO
{
	/**
	 * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward.
	 * 
	 * Default value: 0 degrees.
	 * 
	 * Note: If the value of the prime meridian name is "Greenwich" then the value of greenwichLongitude shall be 0 degrees.
	 */
	private Double greenwichLongitude;
	
	@ManyToOne
	private UnitOfMeasureItemProposalDTO greenwichLongitudeUom;

	public PrimeMeridianItemProposalDTO() { 
		super("PrimeMeridian");
	}
	
	public PrimeMeridianItemProposalDTO(PrimeMeridianItem item) {
		super(item);
	}

	public PrimeMeridianItemProposalDTO(PrimeMeridianItemProposal_Type itemDetails) {
		super(itemDetails);
	}
		
	public PrimeMeridianItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemProposalDTO(IdentifiedItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public PrimeMeridianItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public Double getGreenwichLongitude() {
		return greenwichLongitude;
	}

	public void setGreenwichLongitude(Double greenwichLongitude) {
		this.greenwichLongitude = greenwichLongitude;
	}

	public UnitOfMeasureItemProposalDTO getGreenwichLongitudeUom() {
		return greenwichLongitudeUom;
	}

	public void setGreenwichLongitudeUom(UnitOfMeasureItemProposalDTO greenwichLongitudeUom) {
		this.greenwichLongitudeUom = greenwichLongitudeUom;
	}

	@Override
	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		super.initializeFromItemDetails(itemDetails);
	
		if (itemDetails instanceof PrimeMeridianItemProposal_Type) {
			PrimeMeridianItemProposal_Type xmlProposal = (PrimeMeridianItemProposal_Type) itemDetails;
	
			this.setGreenwichLongitude(xmlProposal.getGreenwichLongitude());	
			
			final UnitOfMeasureItemProposal_PropertyType greenwichLongitudeUomProperty = xmlProposal.getGreenwichLongitudeUom();
			if (greenwichLongitudeUomProperty != null) {
				final UnitOfMeasureItemProposalDTO dto;
				if (greenwichLongitudeUomProperty.isSetUnitOfMeasureItemProposal()) {
					dto = new UnitOfMeasureItemProposalDTO(greenwichLongitudeUomProperty.getUnitOfMeasureItemProposal());
				}
				else if (greenwichLongitudeUomProperty.isSetUuidref()) {
					dto = new UnitOfMeasureItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(greenwichLongitudeUomProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setGreenwichLongitudeUom(dto);
			}
		}	
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof PrimeMeridianItem) {
			PrimeMeridianItem pm = (PrimeMeridianItem)item;
			pm.setGreenwichLongitude(this.getGreenwichLongitude());
			if (this.getGreenwichLongitudeUom() != null) {
				UnitOfMeasureItem uom = entityManager.find(UnitOfMeasureItem.class, this.getGreenwichLongitudeUom().getReferencedItemUuid());
				pm.setGreenwichLongitudeUom(uom);
			}
		}
	}

	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof PrimeMeridianItem) {
			PrimeMeridianItem pm = (PrimeMeridianItem)item;
			this.setGreenwichLongitude(pm.getGreenwichLongitude());
			if (pm.getGreenwichLongitudeUom() != null) {
				this.setGreenwichLongitudeUom(new UnitOfMeasureItemProposalDTO(pm.getGreenwichLongitudeUom()));
			}
		}
	}

	@Override
	public List<RegisterItemProposalDTO> getAggregateDependencies() {
		return super.findDependentProposals(this.getGreenwichLongitudeUom());
	}

}
