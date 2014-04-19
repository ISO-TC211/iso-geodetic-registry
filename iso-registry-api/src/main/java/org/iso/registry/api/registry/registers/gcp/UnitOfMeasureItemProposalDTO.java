package org.iso.registry.api.registry.registers.gcp;

import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.iso19103.MeasureType;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;

public class UnitOfMeasureItemProposalDTO extends RegisterItemProposalDTO
{
	private MeasureType measureType;
	private String symbol;
	
	public UnitOfMeasureItemProposalDTO() { }

	public UnitOfMeasureItemProposalDTO(UnitOfMeasureItem uom) {
		super(uom);
	}

	public MeasureType getMeasureType() {
		return measureType;
	}

	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
