package org.iso.registry.persistence.io.wkt;

import static java.util.Collections.*;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.quantity.Quantity;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.Unit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.sis.io.wkt.WKTFormat;
import org.apache.sis.metadata.iso.extent.DefaultExtent;
import org.apache.sis.metadata.iso.extent.DefaultGeographicBoundingBox;
import org.apache.sis.parameter.DefaultParameterDescriptor;
import org.apache.sis.parameter.DefaultParameterDescriptorGroup;
import org.apache.sis.referencing.AbstractIdentifiedObject;
import org.apache.sis.referencing.NamedIdentifier;
import org.apache.sis.referencing.crs.AbstractCRS;
import org.apache.sis.referencing.crs.DefaultEngineeringCRS;
import org.apache.sis.referencing.crs.DefaultGeocentricCRS;
import org.apache.sis.referencing.crs.DefaultGeographicCRS;
import org.apache.sis.referencing.crs.DefaultProjectedCRS;
import org.apache.sis.referencing.crs.DefaultVerticalCRS;
import org.apache.sis.referencing.cs.AbstractCS;
import org.apache.sis.referencing.cs.DefaultCartesianCS;
import org.apache.sis.referencing.cs.DefaultCoordinateSystemAxis;
import org.apache.sis.referencing.cs.DefaultEllipsoidalCS;
import org.apache.sis.referencing.cs.DefaultSphericalCS;
import org.apache.sis.referencing.cs.DefaultVerticalCS;
import org.apache.sis.referencing.datum.DefaultEllipsoid;
import org.apache.sis.referencing.datum.DefaultEngineeringDatum;
import org.apache.sis.referencing.datum.DefaultGeodeticDatum;
import org.apache.sis.referencing.datum.DefaultPrimeMeridian;
import org.apache.sis.referencing.datum.DefaultVerticalDatum;
import org.apache.sis.referencing.operation.DefaultConversion;
import org.apache.sis.referencing.operation.DefaultOperationMethod;
import org.apache.sis.util.iso.DefaultInternationalString;
import org.apache.sis.util.iso.DefaultLocalName;
import org.apache.sis.xml.MarshallerPool;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.EngineeringCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.GeodeticCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.ProjectedCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.VerticalCoordinateReferenceSystemItem;
import org.iso.registry.core.model.cs.CartesianCoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.EllipsoidalCoordinateSystemItem;
import org.iso.registry.core.model.cs.SphericalCoordinateSystemItem;
import org.iso.registry.core.model.cs.VerticalCoordinateSystemItem;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.datum.EngineeringDatumItem;
import org.iso.registry.core.model.datum.GeodeticDatumItem;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.iso.registry.core.model.datum.VerticalDatumItem;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicExtent;
import org.iso.registry.core.model.operation.ConversionItem;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.GeneralParameterValue;
import org.iso.registry.core.model.operation.Measure;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;
import org.opengis.metadata.extent.Extent;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.cs.SphericalCS;
import org.opengis.referencing.cs.VerticalCS;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.EngineeringDatum;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.referencing.datum.VerticalDatumType;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.util.GenericName;

import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class WktExporter
{
	private WktExporter() { }
	
	public static void exportCrs(CoordinateReferenceSystemItem crsItem, Writer target) throws IOException, JAXBException {
//		WKTFormat wkt = new WKTFormat(Locale.ENGLISH, TimeZone.getTimeZone("UTC")); 
//		CRS.fromWKT("GEODCRS[\"NAD83\",DATUM[\"North American Datum 1983\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101,LENGTHUNIT[\"metre\",1.0]]],CS[ellipsoidal,2],AXIS[\"latitude\",north,ORDER[1]],AXIS[\"longitude\",east,ORDER[2]],ANGLEUNIT[\"degree\",0.01745329252],ID[\"EPSG\",4269]]");
//		wkt.format(crs(crsItem), target);
		
		MarshallerPool pool = new MarshallerPool(singletonMap("flo", ""));
//		JAXBContext ctx = JAXBContext.newInstance(AbstractIdentifiedObject.class, AbstractCRS.class, AbstractCS.class, DefaultInternationalString.class, Unit.class);
//		ctx.createMarshaller().marshal(crs(crsItem), target);
		pool.acquireMarshaller().marshal(crs(crsItem), target);
	}

	public static void exportCs(CoordinateSystemItem csItem, Appendable target) throws IOException {
		WKTFormat wkt = new WKTFormat(Locale.ENGLISH, TimeZone.getTimeZone("UTC"));
		wkt.format(cs(csItem), target);
	}
	
	private static <Q extends Quantity> Unit<Q> unit(UnitOfMeasureItem unitItem, Class<Q> quantity) {
		Unit<Q> unit = (Unit<Q>)Unit.valueOf(unitItem.getSymbol());
		if (unit == null) {
			unit = new BaseUnit<Q>(unitItem.getSymbol());
		}
		
		return unit;
	}
	
	private static AxisDirection axisDirection(CS_AxisDirection direction) {
		switch (direction.getCode()) {
			case "columnNegative":
				return AxisDirection.COLUMN_NEGATIVE;
			case "columnPositive":
				return AxisDirection.COLUMN_POSITIVE;
			case "displayDown":
				return AxisDirection.DISPLAY_DOWN;
			case "displayLeft":
				return AxisDirection.DISPLAY_LEFT;
			case "displayUp":
				return AxisDirection.DISPLAY_UP;
			case "down":
				return AxisDirection.DOWN;
			case "east":
				return AxisDirection.EAST;
			case "eastNorthEast":
				return AxisDirection.EAST_NORTH_EAST;
			case "eastSouthEast":
				return AxisDirection.EAST_SOUTH_EAST;
			case "geocentricX":
				return AxisDirection.GEOCENTRIC_X;
			case "geocentricY":
				return AxisDirection.GEOCENTRIC_Y;
			case "geocentricZ":
				return AxisDirection.GEOCENTRIC_Z;
			case "north":
				return AxisDirection.NORTH;
			case "northEast":
				return AxisDirection.NORTH_EAST;
			case "northNorthEast":
				return AxisDirection.NORTH_NORTH_EAST;
			case "northNorthWest":
				return AxisDirection.NORTH_NORTH_WEST;
			case "northWest":
				return AxisDirection.NORTH_WEST;
			case "rowNegative":
				return AxisDirection.ROW_NEGATIVE;
			case "rowPositive":
				return AxisDirection.ROW_POSITIVE;
			case "south":
				return AxisDirection.SOUTH;
			case "southEast":
				return AxisDirection.SOUTH_EAST;
			case "southSouthEast":
				return AxisDirection.SOUTH_SOUTH_EAST;
			case "southSouthWest":
				return AxisDirection.SOUTH_SOUTH_WEST;
			case "southWest":
				return AxisDirection.SOUTH_WEST;
			case "up":
				return AxisDirection.UP;
			case "west":
				return AxisDirection.WEST;
			case "westNorthWest":
				return AxisDirection.WEST_NORTH_WEST;
			case "westSouthWest":
				return AxisDirection.WEST_SOUTH_WEST;
			default:
				return AxisDirection.OTHER;
		}
	}
	
	private static CoordinateSystemAxis axis(CoordinateSystemAxisItem axisItem) {
		Unit<?> unit = unit(axisItem.getAxisUnit(), Length.class);
//		Unit<?> unit = unit(axisItem.getAxisUnit());
		return new DefaultCoordinateSystemAxis(singletonMap(DefaultCoordinateSystemAxis.NAME_KEY, axisItem.getName()), axisItem.getAxisAbbreviation(), axisDirection(axisItem.getAxisDirection()), unit);
	}
	
	private static CoordinateSystem cs(CoordinateSystemItem csItem) {
		if (csItem instanceof VerticalCoordinateSystemItem) {
			return new DefaultVerticalCS(singletonMap(DefaultVerticalCS.NAME_KEY, csItem.getName()), axis(csItem.getAxes().get(0)));
		}
		else if (csItem instanceof CartesianCoordinateSystemItem) {
			switch (csItem.getAxes().size()) {
				case 2:
					return new DefaultCartesianCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)));
				case 3:
					return new DefaultCartesianCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)), axis(csItem.getAxes().get(2)));
				default:
					throw new IllegalStateException(String.format("Cartesian CRS '%s' has invalid number of axes [is: %d] [expected: 2|3]", csItem.getName(), csItem.getAxes().size()));
			}
		}
		else if (csItem instanceof EllipsoidalCoordinateSystemItem) {
			switch (csItem.getAxes().size()) {
				case 2:
					return new DefaultEllipsoidalCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)));
				case 3:
					return new DefaultEllipsoidalCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)), axis(csItem.getAxes().get(2)));
				default:
					throw new IllegalStateException(String.format("Ellipsoidal CRS '%s' has invalid number of axes [is: %d] [expected: 2|3]", csItem.getName(), csItem.getAxes().size()));
			}
		}
		else if (csItem instanceof SphericalCoordinateSystemItem) {
			return new DefaultSphericalCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)), axis(csItem.getAxes().get(2)));
		}
		else {
			throw new IllegalArgumentException(String.format("CS of type %s not supported", csItem.getClass().getCanonicalName()));			
		}
	}
	
	private static CoordinateReferenceSystem crs(CoordinateReferenceSystemItem crsItem) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(AbstractCRS.NAME_KEY, crsItem.getName());
		properties.put(AbstractCRS.IDENTIFIERS_KEY, id(crsItem));
		properties.put(AbstractCRS.ALIAS_KEY, alias(crsItem));
		properties.put(AbstractCRS.SCOPE_KEY, new DefaultInternationalString(crsItem.getScope()));
		properties.put(AbstractCRS.DOMAIN_OF_VALIDITY_KEY, extent(crsItem));
		
		CoordinateReferenceSystem result;
		if (crsItem instanceof VerticalCoordinateReferenceSystemItem) {
			VerticalCoordinateReferenceSystemItem vcrs = (VerticalCoordinateReferenceSystemItem)crsItem;
			result = new DefaultVerticalCRS(properties, (VerticalDatum)datum(vcrs.getDatum()), (VerticalCS)cs(vcrs.getCoordinateSystem())); 		
		}
		else if (crsItem instanceof GeodeticCoordinateReferenceSystemItem) {
			GeodeticCoordinateReferenceSystemItem gcrs = (GeodeticCoordinateReferenceSystemItem)crsItem;
			if (gcrs.getCoordinateSystem() instanceof EllipsoidalCoordinateSystemItem) {
				result = new DefaultGeographicCRS(properties, (GeodeticDatum)datum(gcrs.getDatum()), (EllipsoidalCS)cs(gcrs.getCoordinateSystem()));				
			}
			else if (gcrs.getCoordinateSystem() instanceof SphericalCoordinateSystemItem) {
				result = new DefaultGeocentricCRS(properties, (GeodeticDatum)datum(gcrs.getDatum()), (SphericalCS)cs(gcrs.getCoordinateSystem()));				
			}
			else if (gcrs.getCoordinateSystem() instanceof CartesianCoordinateSystemItem) {
				result = new DefaultGeocentricCRS(properties, (GeodeticDatum)datum(gcrs.getDatum()), (CartesianCS)cs(gcrs.getCoordinateSystem()));
			}
			else {
				throw new IllegalStateException(String.format("Geodetic CRS '%s' has invalid coordinate system of type '%s'", crsItem.getName(), gcrs.getCoordinateSystem().getClass().getCanonicalName()));
			}
		}
		else if (crsItem instanceof EngineeringCoordinateReferenceSystemItem) {
			EngineeringCoordinateReferenceSystemItem ecrs = (EngineeringCoordinateReferenceSystemItem)crsItem;
			result = new DefaultEngineeringCRS(properties, (EngineeringDatum)datum(ecrs.getDatum()), cs(ecrs.getCoordinateSystem())); 		
		}
		else if (crsItem instanceof ProjectedCoordinateReferenceSystemItem) {
			ProjectedCoordinateReferenceSystemItem pcrs = (ProjectedCoordinateReferenceSystemItem)crsItem;
			if (pcrs.getOperation() instanceof ConversionItem) {
				ConversionItem conv = (ConversionItem)pcrs.getOperation();
				result = new DefaultProjectedCRS(properties, (GeographicCRS)crs(pcrs.getBaseCrs()), conversion(conv), (CartesianCS)cs(pcrs.getCoordinateSystem()));
			}
			else {
				throw new IllegalStateException(String.format("Cannot create WKT representation because projected CRS '%s' is incompatible with ISO 19162: projection method is not a Conversion", crsItem.getName()));
			}
		}
		else {
			throw new IllegalArgumentException(String.format("CRS of type %s not supported", crsItem.getClass().getCanonicalName()));			
		}
		
		return result;
	}
	
	private static NamedIdentifier id(IdentifiedItem item) {
		Map<String, String> properties = new HashMap<>();
		properties.put(NamedIdentifier.AUTHORITY_KEY, "ISO");
		properties.put(NamedIdentifier.CODE_KEY, item.getIdentifier().toString());
		
		return new NamedIdentifier(properties);
	}
	
	private static Collection<GenericName> alias(IdentifiedItem item) {
		Collection<GenericName> result = new ArrayList<>();
		for (String alias : item.getAliases()) {
			Map<String, String> properties = new HashMap<>();
			properties.put(NamedIdentifier.CODE_KEY, alias);
			result.add(new NamedIdentifier(properties));
		}
		
		return result;
	}
	
	private static Extent extent(CoordinateReferenceSystemItem item) {
		DefaultExtent result = new DefaultExtent();
		for (EX_GeographicExtent extent : item.getDomainOfValidity().getGeographicElement()) {
			if (extent instanceof EX_GeographicBoundingBox) {
				EX_GeographicBoundingBox box = (EX_GeographicBoundingBox)extent;
				result.getGeographicElements().add(new DefaultGeographicBoundingBox(box.getWestBoundLongitude(), box.getEastBoundLongitude(), box.getSouthBoundLatitude(), box.getNorthBoundLatitude()));
			}
		}
		
		return result;
	}
	
	private static Conversion conversion(ConversionItem conversion) {
		return new DefaultConversion(name(conversion), method(conversion.getMethod()), null, parameterValues(conversion.getMethod(), conversion.getParameterValue()));
	}
	
	private static OperationMethod method(OperationMethodItem methodItem) {
		return new DefaultOperationMethod(name(methodItem), methodItem.getSourceDimensions(), methodItem.getTargetDimensions(), parameters(methodItem.getName(), methodItem.getParameter()));
	}
	
	private static ParameterDescriptorGroup parameters(String methodName, Collection<GeneralOperationParameterItem> parameterItems) {
		ArrayList<GeneralParameterDescriptor> parameters = new ArrayList<>();
		for (GeneralOperationParameterItem parameter : parameterItems) {
			parameters.add(parameter(parameter, null));
		}
		
		return new DefaultParameterDescriptorGroup(singletonMap(DefaultParameterDescriptorGroup.NAME_KEY, methodName + "_PG"), 1, 1, parameters.toArray(new GeneralParameterDescriptor[] { }));
	}

	private static DefaultParameterDescriptor<?> parameter(GeneralOperationParameterItem parameter, OperationParameterValueType valueType) {
		if (valueType != null) {
			switch (valueType) {
				case STRING:
					return new DefaultParameterDescriptor<String>(name(parameter), parameter.getMinimumOccurs(), 1, String.class, null, null, null);
				case INTEGER:
					return new DefaultParameterDescriptor<Integer>(name(parameter), parameter.getMinimumOccurs(), 1, Integer.class, null, null, null);
				case INTEGER_LIST:
					return new DefaultParameterDescriptor<Integer>(name(parameter), parameter.getMinimumOccurs(), Integer.MAX_VALUE, Integer.class, null, null, null);
				case BOOLEAN:
					return new DefaultParameterDescriptor<Boolean>(name(parameter), parameter.getMinimumOccurs(), 1, Boolean.class, null, null, null);
				case FILE:
					return new DefaultParameterDescriptor<String>(name(parameter), parameter.getMinimumOccurs(), 1, String.class, null, null, null);
				case MEASURE:
					return new DefaultParameterDescriptor<Measure>(name(parameter), parameter.getMinimumOccurs(), 1, Measure.class, null, null, null);
				case MEASURE_LIST:
					return new DefaultParameterDescriptor<Measure>(name(parameter), parameter.getMinimumOccurs(), Integer.MAX_VALUE, Measure.class, null, null, null);
				default:
					return parameter(parameter, null);
			}
		}
		else {
			return new DefaultParameterDescriptor<Object>(name(parameter), parameter.getMinimumOccurs(), 1, Object.class, null, null, null);
		}
	}
	
	private static ParameterValueGroup parameterValues(OperationMethodItem method, Collection<GeneralParameterValue<?>> values) {
		ArrayList<org.opengis.parameter.GeneralParameterValue> sisValues = new ArrayList<>();
		for (GeneralParameterValue<?> value : values) {
			if (value instanceof OperationParameterValue) {
				OperationParameterValue opv = (OperationParameterValue)value;
				ParameterValue<?> pvalue = parameter(opv.getParameter(), opv.getParameterType()).createValue();
				pvalue.setValue(opv.getValue());
				sisValues.add(pvalue);
			}
		}
		
		return parameters(method.getName(), method.getParameter()).createValue();
	}
	
	private static Datum datum(DatumItem datumItem) {
		Datum result;
		if (datumItem instanceof VerticalDatumItem) {
			result = new DefaultVerticalDatum(singletonMap(VerticalDatum.NAME_KEY, datumItem.getName()), VerticalDatumType.GEOIDAL);
		}
		else if (datumItem instanceof EngineeringDatumItem) {
			result = new DefaultEngineeringDatum(singletonMap(VerticalDatum.NAME_KEY, datumItem.getName()));
		}
		else if (datumItem instanceof GeodeticDatumItem) {
			result = new DefaultGeodeticDatum(singletonMap(VerticalDatum.NAME_KEY, datumItem.getName()), ellipsoid(((GeodeticDatumItem)datumItem).getEllipsoid()), primeMeridian(((GeodeticDatumItem)datumItem).getPrimeMeridian()));
		}
		else {
			throw new IllegalArgumentException(String.format("Datums of type %s not supported", datumItem.getClass().getCanonicalName()));
		}
		
		return result;
	}
	
	private static PrimeMeridian primeMeridian(PrimeMeridianItem pmi) {
		return new DefaultPrimeMeridian(name(pmi), pmi.getGreenwichLongitude(), unit(pmi.getGreenwichLongitudeUom(), Angle.class));
	}
	
	private static Ellipsoid ellipsoid(EllipsoidItem ellipsoidItem) {
		if (ellipsoidItem.getSemiMinorAxis() == null && ellipsoidItem.getInverseFlattening() == null || ellipsoidItem.getSemiMinorAxis() != null && ellipsoidItem.getInverseFlattening() != null) {
			throw new IllegalArgumentException("Ellipsoid must either specify semi minor axis or inverse flattening");
		}
		
		Unit<Length> unit = unit(ellipsoidItem.getSemiMajorAxisUom(), Length.class);
		if (ellipsoidItem.getSemiMinorAxis() != null) {
			return DefaultEllipsoid.createEllipsoid(name(ellipsoidItem), ellipsoidItem.getSemiMajorAxis(), ellipsoidItem.getSemiMinorAxis(), unit);
		}	
		else {
			return DefaultEllipsoid.createFlattenedSphere(name(ellipsoidItem), ellipsoidItem.getSemiMajorAxis(), ellipsoidItem.getInverseFlattening(), unit);
		}
	}
	
	private static Map<String, String> name(RE_RegisterItem item) {
		return singletonMap("name", item.getName());
	}
}
