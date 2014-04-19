package org.iso.registry.core.model.cs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.iso.registry.core.model.iso19111.cs.CS_RangeMeaning;

import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

@ItemClass("CoordinateSystemAxis")
@Access(AccessType.FIELD)
@Audited @Entity
public class CoordinateSystemAxisItem extends IdentifiedItem
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
	@Enumerated(EnumType.STRING)
	private CS_AxisDirection axisDirection;
	/**
	 * Identifier of the unit used for this coordinate system axis. The value of
	 * a coordinate in a coordinate tuple shall be recorded using this unit.
	 */
	private UnitOfMeasureItem axisUnit;
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
	@Enumerated(EnumType.STRING)
	private CS_RangeMeaning rangeMeaning;
	
	protected CoordinateSystemAxisItem() { }
	
	public CoordinateSystemAxisItem(RE_Register register, RE_ItemClass itemClass, String name, String definition,
			RE_AdditionInformation additionInformation, String abbreviation, CS_AxisDirection direction, UnitOfMeasureItem uom, 
			Number maximumValue, Number minimumValue, CS_RangeMeaning rangeMeaning) {

		super(register, itemClass, name, definition, additionInformation);

		this.axisAbbreviation = abbreviation;
		this.axisDirection = direction;
		this.axisUnit = uom;
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
		this.rangeMeaning = rangeMeaning;
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

	public UnitOfMeasureItem getAxisUnit() {
		return axisUnit;
	}

	public void setAxisUnit(UnitOfMeasureItem axisUnit) {
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
