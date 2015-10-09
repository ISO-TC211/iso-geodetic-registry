package org.iso.registry.core.model.iso19111.cs;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.xml.namespace.QName;

import org.iso.registry.core.model.iso19136.CodeType;

import de.geoinfoffm.registry.core.model.iso19103.CodeList;
import de.geoinfoffm.registry.core.model.iso19103.CodeListValue;


/**
 * The direction of positive increase in the coordinate value for a coordinate system axis.
 * @author Florian.Esser
 * @created 16-Apr-2014 23:49:48
 */
@Access(AccessType.FIELD)
@Embeddable
public class CS_AxisDirection extends CodeType implements Serializable
{
	/**
	 * Axis positive direction is towards lower pixel column.
	 */
	 public static final CS_AxisDirection COLUMN_NEGATIVE = new CS_AxisDirection("Column negative", "EPSG");
//	/**
//	 * Axis positive direction is towards higher pixel column.
//	 */
//	 COLUMN_POSITIVE,
//	/**
//	 * Axis positive direction is towards bottom of approximately vertical display surface.
//	 */
//	 DISPLAY_DOWN,
//	/**
//	 * Axis positive direction is left in display.
//	 */
//	 DISPLAY_LEFT,
//	/**
//	 * Axis positive direction is right in display.
//	 */
//	 DISPLAY_RIGHT,
//	/**
//	 * Axis positive direction is towards top of approximately vertical display surface.
//	 */
//	 DISPLAY_UP,
//	/**
//	 * Axis positive direction is down relative to gravity.
//	 */
//	 DOWN,
//	/**
//	 * Axis positive direction is  Pi/2 radians clockwise from north.
//	 */
	 public static final CS_AxisDirection EAST = new CS_AxisDirection("east", "EPSG");
//	/**
//	 * Axis positive direction is approximately east-north-east.
//	 */
//	 EAST_NORTH_EAST,
//	/**
//	 * Axis positive direction is approximately east-south-east.
//	 */
//	 EAST_SOUTH_EAST,
//	/**
//	 * Axis positive direction is in the equatorial plane from the centre of the modelled earth towards the intersection of
//	 * the equator with the prime meridian.
//	 */
//	 GEOCENTRIC_X,
//	/**
//	 * Axis positive direction is in the equatorial plane from the centre of the modelled earth towards the intersection of
//	 * the equator and the meridian Pi/2 radians eastwards from the prime meridian.
//	 */
//	 GEOCENTRIC_Y,
//	/**
//	 * Axis positive direction is from the centre of the modelled earth parallel to its rotation axis and towards its north
//	 * pole.
//	 */
//	 GEOCENTRIC_Z,
//	/**
//	 * Axis positive direction is north. In a geodetic or projected CRS, north is defined through the geodetic datum. In an
//	 * engineering CRS, north may be defined with respect to an engineering object rather than a geographical direction.
//	 */
	 public static final CS_AxisDirection NORTH = new CS_AxisDirection("north", "EPSG");
//	/**
//	 * Axis positive direction is approximately north-east.
//	 */
//	 NORTH_EAST,
//	/**
//	 * Axis positive direction is approximately north-north-east.
//	 */
//	 NORTH_NORTH_EAST,
//	/**
//	 * Axis positive direction is approximately north-north-west.
//	 */
//	 NORTH_NORTH_WEST,
//	/**
//	 * Axis positive direction is approximately north-west.
//	 */
//	 NORTH_WEST,
//	/**
//	 * Axis positive direction is towards lower pixel row.
//	 */
//	 ROW_NEGATIVE,
//	/**
//	 * Axis positive direction is towards higher pixel row.
//	 */
//	 ROW_POSITIVE,
//	/**
//	 * Axis positive direction is Pi radians clockwise from north.
//	 */
//	 SOUTH,
//	/**
//	 * Axis positive direction is approximately south-east.
//	 */
//	 SOUTH_EAST,
//	/**
//	 * Axis positive direction is approximately south-south-east.
//	 */
//	 SOUTH_SOUTH_EAST,
//	/**
//	 * Axis positive direction is approximately south-south-west.
//	 */
//	 SOUTH_SOUTH_WEST,
//	/**
//	 * Axis positive direction is approximately south-west.
//	 */
//	 SOUTH_WEST,
//	/**
//	 * Axis positive direction is up relative to gravity.
//	 */
//	 UP,
//	/**
//	 * Axis positive direction is 3Pi/2 radians clockwise from north.
//	 */
//	 WEST,
//	/**
//	 * Axis positive direction is approximately west-north-west.
//	 */
//	 WEST_NORTH_WEST,
//	/**
//	 * Axis positive direction is approximately west-south-west.
//	 */
//	 WEST_SOUTH_WEST;

	public CS_AxisDirection() { }
	
	public CS_AxisDirection(String code, String codeSpace) {
		super(code, codeSpace); 
	}

}//end CS_AxisDirection