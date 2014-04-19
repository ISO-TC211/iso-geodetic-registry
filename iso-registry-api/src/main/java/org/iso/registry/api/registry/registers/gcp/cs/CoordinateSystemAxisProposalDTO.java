package org.iso.registry.api.registry.registers.gcp.cs;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.iso.registry.core.model.iso19111.cs.CS_RangeMeaning;

public class CoordinateSystemAxisProposalDTO extends IdentifiedItemProposalDTO
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
	
	public CoordinateSystemAxisProposalDTO() { }
	
	public CoordinateSystemAxisProposalDTO(CoordinateSystemAxisItem item) {
		super(item);
//		this.uuid = item.getUuid();
//		this.axisAbbreviation = item.getAxisAbbreviation();
//		this.axisDirection = item.getAxisDirection();
//		this.axisUnit = new UnitOfMeasureItemProposalDTO(item.getAxisUnit());
//		this.maximumValue = item.getMaximumValue();
//		this.minimumValue = item.getMinimumValue();
//		this.rangeMeaning = item.getRangeMeaning();
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

}
