package org.iso.registry.api.registry.registers.gcp;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.iso.registry.core.model.iso19115.extent.ExtentItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class ExtentItemViewBean extends RegisterItemViewBean
{
	private String description;
	private List<EX_GeographicBoundingBox> geographicBoundingBoxes;

	public ExtentItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public ExtentItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public ExtentItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public ExtentItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public ExtentItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public ExtentItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	public void setXmlValues(RE_RegisterItem_Type result) {
		super.setXmlValues(result);
		// TODO Implement after extending isogcp-registry.xsd
//		if (result instanceof ExtentItem_Type) {
//			final UnitOfMeasureItem_Type xmlBean = (UnitOfMeasureItem_Type)result;
//			final UnitOfMeasureItemViewBean viewBean = this;
//			if (viewBean.getStandardUnit() != null && viewBean.getStandardUnit().getUuid() != null) {
//				final UnitOfMeasureItem_PropertyType xmlBeanProp = new UnitOfMeasureItem_PropertyType();
//				xmlBeanProp.setUuidref(viewBean.getStandardUnit().getUuid().toString());
//				xmlBean.setStandardUnit(xmlBeanProp);
//			}
//
//			xmlBean.setMeasureType(RegistryModelXmlConverter.convertModelToXmlMeasureType(viewBean.getMeasureType()));
//			xmlBean.setSymbol(GcoConverter.convertToGcoString(viewBean.getSymbol()));
//			xmlBean.setOffsetToStandardUnit(IsoXmlFactory.real(viewBean.getOffsetToStandardUnit()));
//			xmlBean.setScaleToStandardUnitDenominator(IsoXmlFactory.real(viewBean.getScaleToStandardUnitDenominator()));
//			xmlBean.setScaleToStandardUnitNumerator(IsoXmlFactory.real(viewBean.getScaleToStandardUnitNumerator()));
//			xmlBean.setSymbol(IsoXmlFactory.characterString(viewBean.getSymbol()));
//		}
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);

		if (!(registerItem instanceof ExtentItem)) {
			return;
		}

		ExtentItem item = (ExtentItem)registerItem;

		ExtentDTO dto = new ExtentDTO(item.getExtent());
		this.description = dto.getDescription();
		this.getGeographicBoundingBoxes().addAll(dto.getGeographicBoundingBoxes());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<EX_GeographicBoundingBox> getGeographicBoundingBoxes() {
		if (geographicBoundingBoxes == null) {
			geographicBoundingBoxes = new ArrayList<>();
		}
		return geographicBoundingBoxes;
	}

	public void setGeographicBoundingBoxes(List<EX_GeographicBoundingBox> geographicBoundingBoxes) {
		this.geographicBoundingBoxes = geographicBoundingBoxes;
	}

}
