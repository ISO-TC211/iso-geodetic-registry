package org.iso.registry.api.registry.registers.gcp.datum;

import javax.persistence.ManyToOne;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.datum.PrimeMeridianItem;

public class PrimeMeridianItemProposalDTO extends IdentifiedItemProposalDTO
{
	/**
	 * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward.
	 * 
	 * Default value: 0 degrees.
	 * 
	 * Note: If the value of the prime meridian name is "Greenwich" then the value of greenwichLongitude shall be 0 degrees.
	 */
	private double greenwichLongitude = 0.0;
	
	@ManyToOne
	private UnitOfMeasureItemProposalDTO greenwichLongitudeUom;

	public PrimeMeridianItemProposalDTO() { }
	
	public PrimeMeridianItemProposalDTO(PrimeMeridianItem item) {
		super(item);
	}

	public double getGreenwichLongitude() {
		return greenwichLongitude;
	}

	public void setGreenwichLongitude(double greenwichLongitude) {
		this.greenwichLongitude = greenwichLongitude;
	}

	public UnitOfMeasureItemProposalDTO getGreenwichLongitudeUom() {
		return greenwichLongitudeUom;
	}

	public void setGreenwichLongitudeUom(UnitOfMeasureItemProposalDTO greenwichLongitudeUom) {
		this.greenwichLongitudeUom = greenwichLongitudeUom;
	}

}
