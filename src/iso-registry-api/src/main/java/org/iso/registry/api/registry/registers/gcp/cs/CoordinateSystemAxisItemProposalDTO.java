package org.iso.registry.api.registry.registers.gcp.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.iso.registry.core.model.iso19111.cs.CS_RangeMeaning;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.CoordinateSystemAxisItemProposal_Type;
import de.geoinfoffm.registry.api.soap.UnitOfMeasureItemProposal_PropertyType;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class CoordinateSystemAxisItemProposalDTO extends IdentifiedItemProposalDTO
{
	/**
	 * The abbreviation used for this coordinate system axis; this abbreviation
	 * is also used to identify the coordinates in the coordinate tuple.
	 * Examples are X and Y.
	 */
	private String axisAbbreviation;
	/**
	 * Direction of this coordinate system axis (or in the case of Cartesian
	 * projected coordinates, the direction of this coordinate system axis
	 * locally). Examples: north or south, east or west, up or down. Within any
	 * set of coordinate system axes, only one of each pair of terms can be
	 * used. For earth-fixed CRSs, this direction is often approximate and
	 * intended to provide a human interpretable meaning to the axis. When a
	 * geodetic datum is used, the precise directions of the axes may therefore
	 * vary slightly from this approximate direction. Note that an
	 * EngineeringCRS often requires specific descriptions of the directions of
	 * its coordinate system axes.
	 */
	private CS_AxisDirection axisDirection;
	/**
	 * Identifier of the unit used for this coordinate system axis. The value of
	 * a coordinate in a coordinate tuple shall be recorded using this unit.
	 */
	// private UnitOfMeasure axisUnitID;
	private UnitOfMeasureItemProposalDTO axisUnit;
	/**
	 * The maximum value normally allowed for this axis, in the unit of measure
	 * for the axis.
	 */
	private Number maximumValue;
	/**
	 * The minimum value normally allowed for this axis, in the unit of measure
	 * for the axis.
	 */
	private Number minimumValue;
	/**
	 * Meaning of axis value range specified by minimumValue and maximumValue.
	 * This element shall be omitted when both minimumValue and maximumValue are
	 * omitted. It may be included when minimumValue and/or maximumValue are
	 * included. If this element is omitted when minimumValue or maximumValue
	 * are included, the meaning is unspecified.
	 */
	private CS_RangeMeaning rangeMeaning;
	
	public CoordinateSystemAxisItemProposalDTO() { 
		super("CoordinateSystemAxis");
	}
	
	public CoordinateSystemAxisItemProposalDTO(CoordinateSystemAxisItem item) {
		super(item);
	}

	public CoordinateSystemAxisItemProposalDTO(CoordinateSystemAxisItemProposal_Type itemDetails) {
		super(itemDetails);
	}

	public CoordinateSystemAxisItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemProposalDTO(IdentifiedItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public CoordinateSystemAxisItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public String getAxisAbbreviation() {
		return axisAbbreviation;
	}

	public void setAxisAbbreviation(String axisAbbreviation) {
		this.axisAbbreviation = axisAbbreviation;
	}

	public CS_AxisDirection getAxisDirection() {
		return axisDirection;
	}

	public void setAxisDirection(CS_AxisDirection axisDirection) {
		this.axisDirection = axisDirection;
	}

	public UnitOfMeasureItemProposalDTO getAxisUnit() {
		return axisUnit;
	}

	public void setAxisUnit(UnitOfMeasureItemProposalDTO axisUnit) {
		this.axisUnit = axisUnit;
	}

	public Number getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(Number maximumValue) {
		this.maximumValue = maximumValue;
	}

	public Number getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Number minimumValue) {
		this.minimumValue = minimumValue;
	}

	public CS_RangeMeaning getRangeMeaning() {
		return rangeMeaning;
	}

	public void setRangeMeaning(CS_RangeMeaning rangeMeaning) {
		this.rangeMeaning = rangeMeaning;
	}

	@Override
	protected void initializeFromItemDetails(AbstractRegisterItemProposal_Type itemDetails) {
		super.initializeFromItemDetails(itemDetails);
	
		if (itemDetails instanceof CoordinateSystemAxisItemProposal_Type) {
			CoordinateSystemAxisItemProposal_Type xmlProposal = (CoordinateSystemAxisItemProposal_Type) itemDetails;
	
			this.setAxisAbbreviation(xmlProposal.getAxisAbbreviation());
			if (xmlProposal.getAxisDirection().isSetValue()) {
				this.setAxisDirection(new CS_AxisDirection(xmlProposal.getAxisDirection().getValue(), xmlProposal.getAxisDirection().getCodeSpace()));
			}
			this.setMaximumValue(xmlProposal.getMaximumValue());	
			this.setMinimumValue(xmlProposal.getMinimumValue());
			if (xmlProposal.getRangeMeaning().isSetValue()) {
				this.setRangeMeaning(CS_RangeMeaning.valueOf(xmlProposal.getRangeMeaning().getValue()));
			}
			
			final UnitOfMeasureItemProposal_PropertyType axisUnitProperty = xmlProposal.getAxisUnit();
			if (axisUnitProperty != null) {
				final UnitOfMeasureItemProposalDTO dto;
				if (axisUnitProperty.isSetUnitOfMeasureItemProposal()) {
					dto = new UnitOfMeasureItemProposalDTO(axisUnitProperty.getUnitOfMeasureItemProposal());
				}
				else if (axisUnitProperty.isSetUuidref()) {
					dto = new UnitOfMeasureItemProposalDTO();
					dto.setReferencedItemUuid(UUID.fromString(axisUnitProperty.getUuidref()));
				}
				else {
					throw new RuntimeException("unexpected reference");
				}
				
				this.setAxisUnit(dto);
			}
		}	
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof CoordinateSystemAxisItem) {
			CoordinateSystemAxisItem axis = (CoordinateSystemAxisItem)item;
			
			axis.setAxisAbbreviation(this.getAxisAbbreviation());
			axis.setAxisDirection(this.getAxisDirection());

			if (this.getAxisUnit() != null) {
				UnitOfMeasureItem axisUnit = entityManager.find(UnitOfMeasureItem.class, this.getAxisUnit().getReferencedItemUuid());
				axis.setAxisUnit(axisUnit);				
			}
			
			axis.setMaximumValue(this.getMaximumValue());
			axis.setMinimumValue(this.getMinimumValue());
			axis.setRangeMeaning(this.getRangeMeaning());
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof CoordinateSystemAxisItem) {
			CoordinateSystemAxisItem axis = (CoordinateSystemAxisItem)item;
			this.setAxisAbbreviation(axis.getAxisAbbreviation());
			this.setAxisDirection(axis.getAxisDirection());
			if (axis.getAxisUnit() != null) {
				this.setAxisUnit(new UnitOfMeasureItemProposalDTO(axis.getAxisUnit()));
			}
			this.setMaximumValue(axis.getMaximumValue());
			this.setMinimumValue(axis.getMinimumValue());
			this.setRangeMeaning(axis.getRangeMeaning());
		}
	}

	@Override
	public List<RegisterItemProposalDTO> getAggregateDependencies() {
		final List<RegisterItemProposalDTO> result = new ArrayList<RegisterItemProposalDTO>();
		result.addAll(super.getAggregateDependencies());

		result.add(this.getAxisUnit());

		return super.findDependentProposals((RegisterItemProposalDTO[])result.toArray(new RegisterItemProposalDTO[result.size()]));
	}

	@Override
	public List<RegisterItemProposalDTO> getCompositeDependencies() {
		final List<RegisterItemProposalDTO> result = new ArrayList<RegisterItemProposalDTO>();
		result.addAll(super.getCompositeDependencies());
		
		return super.findDependentProposals((RegisterItemProposalDTO[])result.toArray(new RegisterItemProposalDTO[result.size()]));
	}

}
