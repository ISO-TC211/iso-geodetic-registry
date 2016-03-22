package org.iso.registry.api.registry.registers.gcp;

import org.iso.registry.api.GcoConverter;
import org.iso.registry.api.UnitOfMeasureItem_PropertyType;
import org.iso.registry.api.UnitOfMeasureItem_Type;
import org.iso.registry.api.registry.RegistryModelXmlConverter;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.iso19103.MeasureType;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.iso.IsoXmlFactory;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class UnitOfMeasureItemViewBean extends IdentifiedItemViewBean
{
	private MeasureType measureType;
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
	public void setXmlValues(RE_RegisterItem_Type result) {
		super.setXmlValues(result);
		if (result instanceof UnitOfMeasureItem_Type) {
			final UnitOfMeasureItem_Type xmlBean = (UnitOfMeasureItem_Type)result;
			final UnitOfMeasureItemViewBean viewBean = this;
			if (viewBean.getStandardUnit() != null && viewBean.getStandardUnit().getUuid() != null) {
				final UnitOfMeasureItem_PropertyType xmlBeanProp = new UnitOfMeasureItem_PropertyType(); 
				xmlBeanProp.setUuidref(viewBean.getStandardUnit().getUuid().toString());
				xmlBean.setStandardUnit(xmlBeanProp);
			}
			
			xmlBean.setMeasureType(RegistryModelXmlConverter.convertModelToXmlMeasureType(viewBean.getMeasureType()));
			xmlBean.setSymbol(GcoConverter.convertToGcoString(viewBean.getSymbol()));
			xmlBean.setOffsetToStandardUnit(IsoXmlFactory.real(viewBean.getOffsetToStandardUnit()));
			xmlBean.setScaleToStandardUnitDenominator(IsoXmlFactory.real(viewBean.getScaleToStandardUnitDenominator()));
			xmlBean.setScaleToStandardUnitNumerator(IsoXmlFactory.real(viewBean.getScaleToStandardUnitNumerator()));
			xmlBean.setSymbol(IsoXmlFactory.characterString(viewBean.getSymbol()));
		}
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof UnitOfMeasureItem)) {
			return;
		}
		
		UnitOfMeasureItem item = (UnitOfMeasureItem)registerItem;

		this.setMeasureType(item.getMeasureType());
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
