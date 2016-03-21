package org.iso.registry.client;

import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.iso19103.MeasureType;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class UnitOfMeasureItemViewBean extends IdentifiedItemViewBean
{
	private String measureType;
	private String symbol;

	private UnitOfMeasureItemViewBean standardUnit;
	private Double offsetToStandardUnit;
	private Double scaleToStandardUnitNumerator;
	private Double scaleToStandardUnitDenominator;
	
	public UnitOfMeasureItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public UnitOfMeasureItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public UnitOfMeasureItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public UnitOfMeasureItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public UnitOfMeasureItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public UnitOfMeasureItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof UnitOfMeasureItem)) {
			return;
		}
		
		UnitOfMeasureItem item = (UnitOfMeasureItem)registerItem;

		this.setMeasureType(item.getMeasureType().name());
		this.setSymbol(item.getSymbol());
		if (item.getStandardUnit() != null) { 
			if (item.equals(item.getStandardUnit())) {
				this.setStandardUnit(this);
			}
			else {
				this.setStandardUnit(new UnitOfMeasureItemViewBean(item.getStandardUnit(), false));
			}
		}
		this.setOffsetToStandardUnit(item.getOffsetToStandardUnit());
		this.setScaleToStandardUnitNumerator(item.getScaleToStandardUnitNumerator());
		this.setScaleToStandardUnitDenominator(item.getScaleToStandardUnitDenominator());
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public UnitOfMeasureItemViewBean getStandardUnit() {
		return standardUnit;
	}

	public void setStandardUnit(UnitOfMeasureItemViewBean standardUnit) {
		this.standardUnit = standardUnit;
	}

	public Double getOffsetToStandardUnit() {
		return offsetToStandardUnit;
	}

	public void setOffsetToStandardUnit(Double offsetToStandardUnit) {
		this.offsetToStandardUnit = offsetToStandardUnit;
	}

	public Double getScaleToStandardUnitNumerator() {
		return scaleToStandardUnitNumerator;
	}

	public void setScaleToStandardUnitNumerator(Double scaleToStandardUnitNumerator) {
		this.scaleToStandardUnitNumerator = scaleToStandardUnitNumerator;
	}

	public Double getScaleToStandardUnitDenominator() {
		return scaleToStandardUnitDenominator;
	}

	public void setScaleToStandardUnitDenominator(Double scaleToStandardUnitDenominator) {
		this.scaleToStandardUnitDenominator = scaleToStandardUnitDenominator;
	}

}
