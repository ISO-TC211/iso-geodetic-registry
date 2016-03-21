package org.iso.registry.api.registry.registers.gcp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.RegistryModelXmlConverter;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.iso19103.MeasureType;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.UnitOfMeasureItemProposal_PropertyType;
import de.geoinfoffm.registry.api.soap.UnitOfMeasureItemProposal_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class UnitOfMeasureItemProposalDTO extends IdentifiedItemProposalDTO
{
	private MeasureType measureType;
	private String symbol;

	private UnitOfMeasureItemProposalDTO standardUnit;
	private Double offsetToStandardUnit;
	private Double scaleToStandardUnitNumerator;
	private Double scaleToStandardUnitDenominator;

	
	public UnitOfMeasureItemProposalDTO() { 
		super("UnitOfMeasure");
	}

	public UnitOfMeasureItemProposalDTO(UnitOfMeasureItemProposal_Type itemDetails) {
		super(itemDetails);
	}

	public UnitOfMeasureItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public UnitOfMeasureItemProposalDTO(IdentifiedItem item) {
		super(item);
	}

	public UnitOfMeasureItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public UnitOfMeasureItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public UnitOfMeasureItemProposalDTO(String itemClassName) {
		super(itemClassName);
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

	public UnitOfMeasureItemProposalDTO getStandardUnit() {
		return standardUnit;
	}

	public void setStandardUnit(UnitOfMeasureItemProposalDTO standardUnit) {
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

	@Override
	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		super.initializeFromItemDetails(itemDetails);
	
		if (itemDetails instanceof UnitOfMeasureItemProposal_Type) {
			UnitOfMeasureItemProposal_Type xmlProposal = (UnitOfMeasureItemProposal_Type) itemDetails;
	
			this.setMeasureType(RegistryModelXmlConverter.convertSoapXmlToModelMeasureType(xmlProposal.getMeasureType()));	
			this.setOffsetToStandardUnit(xmlProposal.getOffsetToStandardUnit());	
			this.setScaleToStandardUnitDenominator(xmlProposal.getScaleToStandardUnitDenominator());	
			this.setScaleToStandardUnitNumerator(xmlProposal.getScaleToStandardUnitNumerator());	
			this.setSymbol(xmlProposal.getSymbol());	
			
			final UnitOfMeasureItemProposal_PropertyType standardUnitProperty = xmlProposal.getStandardUnit();
			if (standardUnitProperty != null) {
				final UnitOfMeasureItemProposalDTO dto;
				if (standardUnitProperty.isSetUnitOfMeasureItemProposal()) {
					dto = new UnitOfMeasureItemProposalDTO(standardUnitProperty.getUnitOfMeasureItemProposal());
				}
				else if (standardUnitProperty.isSetUuidref()) {
					dto = new UnitOfMeasureItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(standardUnitProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setStandardUnit(dto);
			}
		}	
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);
		
		if (registerItem instanceof UnitOfMeasureItem) {
			UnitOfMeasureItem uom = (UnitOfMeasureItem)registerItem;
			
			uom.setMeasureType(this.getMeasureType());
			uom.setSymbol(this.getSymbol());
			
			if (this.getStandardUnit() != null && this.getStandardUnit().getReferencedItemUuid() != null) {
				UnitOfMeasureItem standardUnit = entityManager.find(UnitOfMeasureItem.class, this.getStandardUnit().getReferencedItemUuid());
				uom.setStandardUnit(standardUnit);				
			}
			else if (this.getStandardUnit() == this) {
				uom.setStandardUnit(uom);
			}
			
			uom.setOffsetToStandardUnit(this.getOffsetToStandardUnit());
			uom.setScaleToStandardUnitNumerator(this.getScaleToStandardUnitNumerator());
			uom.setScaleToStandardUnitDenominator(this.getScaleToStandardUnitDenominator());
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);
		
		if (registerItem instanceof UnitOfMeasureItem) {
			UnitOfMeasureItem uom = (UnitOfMeasureItem)registerItem;
		
			this.setMeasureType(uom.getMeasureType());
			this.setSymbol(uom.getSymbol());
			if (uom.getStandardUnit() != null) {
				this.setStandardUnit(new UnitOfMeasureItemProposalDTO(uom.getStandardUnit()));
			}
			this.setOffsetToStandardUnit(uom.getOffsetToStandardUnit());
			this.setScaleToStandardUnitNumerator(uom.getScaleToStandardUnitNumerator());
			this.setScaleToStandardUnitDenominator(uom.getScaleToStandardUnitDenominator());
		}
	}
	
	@Override
	public List<RegisterItemProposalDTO> getAggregateDependencies() {
		final List<RegisterItemProposalDTO> result = new ArrayList<RegisterItemProposalDTO>();
		result.addAll(super.getAggregateDependencies());
		if (this.getStandardUnit() != null && this.getStandardUnit() != this) {
			result.add(this.getStandardUnit());
		}

		return super.findDependentProposals((RegisterItemProposalDTO[])result.toArray(new RegisterItemProposalDTO[result.size()]));
	}

	
	@Override
	public List<RegisterItemProposalDTO> getCompositeDependencies() {
		final List<RegisterItemProposalDTO> result = new ArrayList<RegisterItemProposalDTO>();
		result.addAll(super.getCompositeDependencies());
		
		return super.findDependentProposals((RegisterItemProposalDTO[])result.toArray(new RegisterItemProposalDTO[result.size()]));
	}
	
}
