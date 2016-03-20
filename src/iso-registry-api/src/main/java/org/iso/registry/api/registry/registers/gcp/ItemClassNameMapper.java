package org.iso.registry.api.registry.registers.gcp;

import de.bespire.registry.io.excel.ValueMapper;

public class ItemClassNameMapper implements ValueMapper<String, String> {

	@Override
	public String map(String value) {
		switch (value) {
			case "Cartesian Coordinate System":
				return "CartesianCS";
			case "Ellipsoidal Coordinate System":
				return "EllipsoidalCS";
			case "Spherical Coordinate System":
				return "SphericalCS";
			case "Vertical Coordinate System":
				return "VerticalCS";
			case "Geodetic Datum":
				return "GeodeticDatum";
			case "Vertical Datum":
				return "VerticalDatum";
			case "Engineering Datum":
				return "EngineeringDatum";
			case "Projected CRS":
				return "ProjectedCRS";
			case "Geodetic CRS":
				return "GeodeticCRS";
			case "Vertical CRS":
				return "VerticalCRS";
			case "Derived CRS":
				return "DerivedCRS";
			case "Compound CRS":
				return "CompoundCRS";
			case "Coordinate Conversion (Deriving Operation)":
				return "Conversion";
			default:
				return value;
		}
	}

	@Override
	public String mapInverse(String value) {
		switch (value) {
		case "CartesianCS":
			return "Cartesian Coordinate System";
		case "EllipsodalCS":
			return "Ellipsoidal Coordinate System";
		case "SphericalCS":
			return "Spherical Coordinate System";
		case "VerticalCS":
			return "Vertical Coordinate System";
		case "GeodeticDatum":
			return "Geodetic Datum";
		case "VerticalDatum":
			return "Vertical Datum";
		case "EngineeringDatum":
			return "Engineering Datum";
		case "ProjectedCRS":
			return "Projected CRS";
		case "GeodeticCRS":
			return "Geodetic CRS";
		case "VerticalCRS":
			return "Vertical CRS";
		case "DerivedCRS":
			return "Derived CRS";
		case "CompoundCRS":
			return "Compound CRS";
		case "Conversion":
			return "Coordinate Conversion (Deriving Operation)";
		default:
			return value;
		}
	}

}
