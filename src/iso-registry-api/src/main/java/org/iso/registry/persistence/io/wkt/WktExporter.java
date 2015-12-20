package org.iso.registry.persistence.io.wkt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Quantity;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.charts.AxisCrossBetween;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.resource.SaveOptions;
import org.hibernate.cfg.CreateKeySecondPass;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
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
import org.iso.registry.core.model.iso19103.MeasureType;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.operation.ConversionItem;
import org.iso.registry.core.model.operation.GeneralParameterValue;
import org.iso.registry.core.model.operation.Measure;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import de.bespire.LoggerFactory;
import de.bespire.io.wkt.WellKnownTextStandaloneSetup;
import de.bespire.io.wkt.services.WellKnownTextGrammarAccess.AxisDirectionElements;
import de.bespire.io.wkt.services.WellKnownTextGrammarAccess.CoordinateSystemTypeElements;
import de.bespire.io.wkt.wkt.AngleUnit;
import de.bespire.io.wkt.wkt.Axis;
import de.bespire.io.wkt.wkt.AxisDirection;
import de.bespire.io.wkt.wkt.AxisOrder;
import de.bespire.io.wkt.wkt.BaseGeodeticCRS;
import de.bespire.io.wkt.wkt.CRS;
import de.bespire.io.wkt.wkt.CompoundCRS;
import de.bespire.io.wkt.wkt.Conversion;
import de.bespire.io.wkt.wkt.CoordinateSystem;
import de.bespire.io.wkt.wkt.CoordinateSystemType;
import de.bespire.io.wkt.wkt.Datum;
import de.bespire.io.wkt.wkt.Ellipsoid;
import de.bespire.io.wkt.wkt.EngineeringCRS;
import de.bespire.io.wkt.wkt.EngineeringDatum;
import de.bespire.io.wkt.wkt.GeodeticCRS;
import de.bespire.io.wkt.wkt.GeodeticDatum;
import de.bespire.io.wkt.wkt.Identifier;
import de.bespire.io.wkt.wkt.LengthUnit;
import de.bespire.io.wkt.wkt.OperationMethod;
import de.bespire.io.wkt.wkt.ParameterUnit;
import de.bespire.io.wkt.wkt.PrimeMeridian;
import de.bespire.io.wkt.wkt.ProjectedCRS;
import de.bespire.io.wkt.wkt.ProjectionParameter;
import de.bespire.io.wkt.wkt.Remark;
import de.bespire.io.wkt.wkt.ScaleUnit;
import de.bespire.io.wkt.wkt.TimeUnit;
import de.bespire.io.wkt.wkt.Unit;
import de.bespire.io.wkt.wkt.VerticalCRS;
import de.bespire.io.wkt.wkt.VerticalDatum;
import de.bespire.io.wkt.wkt.WKT;
import de.bespire.io.wkt.wkt.WktFactory;
import de.bespire.io.wkt.wkt.impl.AxisDirectionImpl;
import de.bespire.io.wkt.wkt.impl.AxisImpl;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;

public class WktExporter
{
	private static final Logger logger = LoggerFactory.make();
	private static final WktFactory wktFactory = WktFactory.eINSTANCE;
	
	private WktExporter() { }
	
	public static void exportCrs(CoordinateReferenceSystemItem crsItem, OutputStream target) throws IOException, JAXBException {
		WellKnownTextStandaloneSetup.doSetup();
		
		WKT wkt = WktFactory.eINSTANCE.createWKT();
		wkt.getWkt().add(crs(crsItem));
		
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createFileURI("D:/test.wkt"));
		resource.getContents().add(wkt);
		
		Map<Object, Object> options = new HashMap<>();
		SaveOptions.newBuilder().format().getOptions().addTo(options);
		
		resource.save(target, options);
	}
	
	private static BaseGeodeticCRS baseGeodeticCrs(GeodeticCoordinateReferenceSystemItem crsItem) {
		BaseGeodeticCRS result = wktFactory.createBaseGeodeticCRS();
		result.setName(crsItem.getName());
		result.setDatum((GeodeticDatum)datum(crsItem.getDatum()));
		if (crsItem.getCoordinateSystem() instanceof EllipsoidalCoordinateSystemItem) {
			EllipsoidalCoordinateSystemItem ellipsoidalCs = (EllipsoidalCoordinateSystemItem)crsItem.getCoordinateSystem();
			if (!ellipsoidalCs.getAxes().isEmpty()) {
				result.setEllipsoidalCoordinateSystemUnit(unit(ellipsoidalCs.getAxes().get(0).getAxisUnit(), MeasureType.ANGLE, AngleUnit.class));
			}
		}
		
		return result;
	}
	
	private static CRS crs(CoordinateReferenceSystemItem crsItem) {
		CRS crs;
		if (crsItem instanceof GeodeticCoordinateReferenceSystemItem) {
			crs = wktFactory.createGeodeticCRS();
			GeodeticCoordinateReferenceSystemItem gcrs = (GeodeticCoordinateReferenceSystemItem)crsItem;
			((GeodeticCRS)crs).setDatum((GeodeticDatum)datum(gcrs.getDatum()));
			((GeodeticCRS)crs).setCoordinateSystem(cs(((GeodeticCoordinateReferenceSystemItem)crsItem).getCoordinateSystem()));
		}
		else if (crsItem instanceof EngineeringCoordinateReferenceSystemItem) {
			crs = wktFactory.createEngineeringCRS();
			EngineeringCoordinateReferenceSystemItem ecrs = (EngineeringCoordinateReferenceSystemItem)crsItem;
			((EngineeringCRS)crs).setDatum((EngineeringDatum)datum(ecrs.getDatum()));
			((EngineeringCRS)crs).setCoordinateSystem(cs(((GeodeticCoordinateReferenceSystemItem)crsItem).getCoordinateSystem()));			
		}
		else if (crsItem instanceof VerticalCoordinateReferenceSystemItem) {
			crs = wktFactory.createVerticalCRS();
			VerticalCoordinateReferenceSystemItem vcrs = (VerticalCoordinateReferenceSystemItem)crsItem;
			((VerticalCRS)crs).setDatum((VerticalDatum)datum(vcrs.getDatum()));
			((VerticalCRS)crs).setCoordinateSystem(cs(((VerticalCoordinateReferenceSystemItem)crsItem).getCoordinateSystem()));			
		}
		else if (crsItem instanceof ProjectedCoordinateReferenceSystemItem) {
			crs = wktFactory.createProjectedCRS();
			ProjectedCoordinateReferenceSystemItem pcrs = (ProjectedCoordinateReferenceSystemItem)crsItem;
			((ProjectedCRS)crs).setBaseCRS(baseGeodeticCrs(pcrs.getBaseCrs()));
			((ProjectedCRS)crs).setCoordinateSystem(cs(pcrs.getCoordinateSystem()));
			if (pcrs.getOperation() instanceof ConversionItem) {
				((ProjectedCRS)crs).setOperation(conversion((ConversionItem)pcrs.getOperation()));
			}
		}
		else if (crsItem instanceof CompoundCoordinateReferenceSystemItem) {
			crs = wktFactory.createCompoundCRS();
			CompoundCoordinateReferenceSystemItem ccrs = (CompoundCoordinateReferenceSystemItem)crsItem;
			((CompoundCRS)crs).setHorizontalCRS((GeodeticCRS)crs(ccrs.getComponentReferenceSystem().get(0)));
			((CompoundCRS)crs).setVerticalCRS((VerticalCRS)crs(ccrs.getComponentReferenceSystem().get(1)));
		}
		else {
			throw new RuntimeException(String.format("CRS type %s not supported", crsItem.getClass().getCanonicalName()));
		}
		crs.setName(crsItem.getName());
		crs.setIdentifier(identifier("ISO", crsItem.getIdentifier()));
		crs.setRemark(remark(crsItem.getRemarks()));
		
		return crs;
	}
	
	private static Remark remark(String remark) {
		if (StringUtils.isEmpty(remark)) {
			return null;
		}
		
		Remark result = wktFactory.createRemark();
		result.setRemark(remark);
		
		return result;
	}
	
	private static Identifier identifier(String authority, int id) {
		Identifier result = wktFactory.createIdentifier();
		result.setAuthority(authority);
		result.setUniqueIdentifierNr(id);
		
		return result;
	}
	
	private static Identifier identifier(String authority, String id) {
		Identifier result = wktFactory.createIdentifier();
		result.setAuthority(authority);
		result.setUniqueIdentifierText(id);
		
		return result;
	}
	
	private static Datum datum(DatumItem datumItem) {
		Datum result;
		if (datumItem instanceof VerticalDatumItem) {
			result = wktFactory.createVerticalDatum();
		}
		else if (datumItem instanceof EngineeringDatumItem) {
			result = wktFactory.createEngineeringDatum();
		}
		else if (datumItem instanceof GeodeticDatumItem) {
			result = wktFactory.createGeodeticDatum();
			((GeodeticDatum)result).setEllipsoid(ellipsoid(((GeodeticDatumItem)datumItem).getEllipsoid()));
			if (!"Greenwich".equalsIgnoreCase(((GeodeticDatumItem)datumItem).getPrimeMeridian().getName())) { // Omit "Greenwich" prime meridian as per Note in ISO 19162:2015 Sec. 8.2.3 p. 30
				((GeodeticDatum)result).setPrimeMeridian(primeMeridian(((GeodeticDatumItem)datumItem).getPrimeMeridian()));
			}
		}
		else {
			throw new IllegalArgumentException(
					String.format("Datums of type %s not supported", datumItem.getClass().getCanonicalName()));
		}
		
		result.setName(datumItem.getName());

		return result;
	}

	private static CoordinateSystem cs(CoordinateSystemItem csItem) {
		CoordinateSystem result = wktFactory.createCoordinateSystem();
		if (csItem instanceof VerticalCoordinateSystemItem) {
			result.setType(CoordinateSystemType.VERTICAL);
		}
		else if (csItem instanceof CartesianCoordinateSystemItem) {
			switch (csItem.getAxes().size()) {
				case 2:
				case 3:
					result.setType(CoordinateSystemType.CARTESIAN);
					break;
				default:
					throw new IllegalStateException(
							String.format("Cartesian CRS '%s' has invalid number of axes [is: %d] [expected: 2|3]",
									csItem.getName(), csItem.getAxes().size()));
			}
		}
		else if (csItem instanceof EllipsoidalCoordinateSystemItem) {
			switch (csItem.getAxes().size()) {
				case 2:
				case 3:
					result.setType(CoordinateSystemType.ELLIPSOIDAL);
					break;
				default:
					throw new IllegalStateException(
							String.format("Ellipsoidal CRS '%s' has invalid number of axes [is: %d] [expected: 2|3]",
									csItem.getName(), csItem.getAxes().size()));
			}
		}
		else if (csItem instanceof SphericalCoordinateSystemItem) {
			result.setType(CoordinateSystemType.SPHERICAL);
		}
		else {
			throw new IllegalArgumentException(
					String.format("CS of type %s not supported", csItem.getClass().getCanonicalName()));
		}
		
		result.setDimension(Integer.toString(csItem.getAxes().size()));

		UnitOfMeasureItem axisUnit = null;
		for (int i = 0; i < csItem.getAxes().size(); i++) {
			CoordinateSystemAxisItem axisItem = csItem.getAxes().get(i);
			result.getAxis().add(axis(axisItem, (csItem.getAxes().size() > 1) ? i + 1 : null));
			if (axisUnit == null) {
				axisUnit = axisItem.getAxisUnit();
			}
		}
		
		if (axisUnit != null) {
			result.setCsUnit(unit(axisUnit));
		}
		
		return result;
	}
	
	private static AxisOrder axisOrder(int index) {
		AxisOrder result = wktFactory.createAxisOrder();
		result.setIndex(index);
		
		return result;
	}
	
	private static AxisDirection axisDirection(CS_AxisDirection direction) {
		if (direction == null || StringUtils.isEmpty(direction.getCode())) {
			return wktFactory.createUnspecifiedDirection();
		}
		
		switch (direction.getCode()) {
			case "columnNegative":
				return wktFactory.createColumnNegative();
			case "columnPositive":
				return wktFactory.createColumnPositive();
			case "displayDown":
				return wktFactory.createDisplayDown();
			case "displayLeft":
				return wktFactory.createDisplayLeft();
			case "displayUp":
				return wktFactory.createDisplayUp();
			case "down":
				return wktFactory.createDown();
			case "east":
				return wktFactory.createEast();
			case "eastNorthEast":
				return wktFactory.createEastNorthEast();
			case "eastSouthEast":
				return wktFactory.createEastNorthEast();
			case "geocentricX":
				return wktFactory.createGeocentricX();
			case "geocentricY":
				return wktFactory.createGeocentricY();
			case "geocentricZ":
				return wktFactory.createGeocentricZ();
			case "north":
				return wktFactory.createNorth();
			case "northEast":
				return wktFactory.createNorthEast();
			case "northNorthEast":
				return wktFactory.createNorthNorthEast();
			case "northNorthWest":
				return wktFactory.createNorthNorthWest();
			case "northWest":
				return wktFactory.createNorthWest();
			case "rowNegative":
				return wktFactory.createRowNegative();
			case "rowPositive":
				return wktFactory.createRowPositive();
			case "south":
				return wktFactory.createSouth();
			case "southEast":
				return wktFactory.createSouthEast();
			case "southSouthEast":
				return wktFactory.createSouthSouthEast();
			case "southSouthWest":
				return wktFactory.createSouthSouthWest();
			case "southWest":
				return wktFactory.createSouthWest();
			case "up":
				return wktFactory.createUp();
			case "west":
				return wktFactory.createWest();
			case "westNorthWest":
				return wktFactory.createWestNorthWest();
			case "westSouthWest":
				return wktFactory.createWestSouthWest();
			default:
				return wktFactory.createOtherDirection();
		}
	}

	private static Axis axis(CoordinateSystemAxisItem axisItem) {
		return axis(axisItem, null);
	}

	private static Axis axis(CoordinateSystemAxisItem axisItem, Integer index) {
		Axis result = wktFactory.createAxis();
		
		StringBuilder nameBuilder = new StringBuilder(axisItem.getName());
		if (!StringUtils.isEmpty(axisItem.getAxisAbbreviation())) {
			nameBuilder.append(" (");
			nameBuilder.append(axisItem.getAxisAbbreviation());
			nameBuilder.append(")");
		}
		
		result.setName(nameBuilder.toString());
		result.setDirection(axisDirection(axisItem.getAxisDirection()));
		if (index != null) {
			result.setOrder(axisOrder(index));
		}
		
//		AxisName name = null;
//		AxisAbbreviation abbreviation = null;
//		if (!StringUtils.isEmpty(axisItem.getName())) {
//			name = wktFactory.createAxisName();
//			name.setName(axisItem.getName());
//		}
//
//		if (!StringUtils.isEmpty(axisItem.getAxisAbbreviation())) {
//			abbreviation = wktFactory.createAxisAbbreviation();
//			abbreviation.setAxisAbbreviation(axisItem.getAxisAbbreviation());
//		}		
//		
//		if (name != null && abbreviation != null) {
//			AxisNameAndAbbreviation anaa = wktFactory.createAxisNameAndAbbreviation();
//			anaa.setName(name);
//			anaa.setAbbreviation(abbreviation);
//			result.setAxisNameAndAbbreviation(anaa);
//		}
//		else if (name != null) {
//			result.setAxisName(name);
//		}
//		else if (abbreviation != null) {
//			result.setAxisAbbreviation(abbreviation);
//		}
		
		return result;
	}
	
	private static PrimeMeridian primeMeridian(PrimeMeridianItem pmi) {
		PrimeMeridian result = wktFactory.createPrimeMeridian();
		result.setName(pmi.getName());
		result.setIrmLongitude(pmi.getGreenwichLongitude());
		
		return result;
	}

	private static Ellipsoid ellipsoid(EllipsoidItem ellipsoidItem) {
		if (ellipsoidItem.getSemiMinorAxis() == null && ellipsoidItem.getInverseFlattening() == null
				|| ellipsoidItem.getSemiMinorAxis() != null && ellipsoidItem.getInverseFlattening() != null) {
			throw new IllegalArgumentException("Ellipsoid must either specify semi minor axis or inverse flattening");
		}
		
		Ellipsoid result = wktFactory.createEllipsoid();
		result.setName(ellipsoidItem.getName());
		result.setSemiMajorAxis(ellipsoidItem.getSemiMajorAxis());

		if (ellipsoidItem.getSemiMinorAxis() != null) {
			result.setInverseFlattening(ellipsoidItem.getSemiMinorAxis());
		}
		else {
			result.setInverseFlattening(ellipsoidItem.getInverseFlattening());
		}
		
		return result;
	}
	
	private static Conversion conversion(ConversionItem conversionItem) {
		Conversion result = wktFactory.createConversion();
		result.setName(conversionItem.getName());
		result.setMethod(method(conversionItem.getMethod()));
		for (GeneralParameterValue<?> parameterValue : conversionItem.getParameterValue()) {
			if (!(parameterValue instanceof OperationParameterValue)) {
				logger.error("Encountered invalid parameter value in Conversion '{}': unsupported type {}", conversionItem.getName(), parameterValue.getClass().getCanonicalName());
				continue;
			}
			OperationParameterValue opParamValue = (OperationParameterValue)parameterValue;
			if (!Arrays.asList(OperationParameterValueType.INTEGER, OperationParameterValueType.MEASURE).contains(opParamValue.getParameterType())) {
				logger.debug("Encountered parameter value that cannot be serialized as WKT in Conversion '{}': Parameter '{}' has unsupported type '{}'", new Object[] { conversionItem.getName(), opParamValue.getParameter().getName(), opParamValue.getParameterType() });
				continue;
			}
			if (opParamValue.getParameterValue().isEmpty()) {
				logger.debug("Encountered parameter value that cannot be serialized as WKT in Conversion '{}': Parameter '{}' has no values", new Object[] { conversionItem.getName(), opParamValue.getParameter().getName() });
				continue;				
			}
			else if (opParamValue.getParameterValue().size() > 1) {
				logger.debug("Encountered parameter value that cannot be serialized as WKT in Conversion '{}': Parameter '{}' has multiple values", new Object[] { conversionItem.getName(), opParamValue.getParameter().getName() });
				continue;								
			}

			result.getParameters().add(parameter((OperationParameterValue)parameterValue));
		}
		
		return result;
	}
	
	private static OperationMethod method(OperationMethodItem methodItem) {
		OperationMethod result = wktFactory.createOperationMethod();
		result.setName(methodItem.getName());
		
		return result;
	}

	private static ProjectionParameter parameter(OperationParameterValue parameterValue) {
		ProjectionParameter result = wktFactory.createProjectionParameter();
		OperationParameterItem parameter = parameterValue.getParameter();
		
		if (parameterValue.getParameterValue().isEmpty()) {
			throw new IllegalArgumentException(String.format("Parameter value cannot be serialized as WKT: Parameter '%s' has no values", parameterValue.getParameter().getName()));
			
		}
		else if (parameterValue.getParameterValue().size() > 1) {
			throw new IllegalArgumentException(String.format("Parameter value cannot be serialized as WKT: Parameter '%s' has multiple values", parameterValue.getParameter().getName()));
		}
		
		result.setName(parameter.getName());
		
		Measure measure = parameterValue.getParameterValue().get(0);
		result.setValue(measure.getValue().doubleValue());
		if (measure.getUom() != null) {
			result.setUnit(parameterUnit(measure.getUom()));			
		}
		
		return result;
	}
	
	private static Unit unit(UnitOfMeasureItem unitItem) {
		Class<? extends Unit> result;
		switch (unitItem.getMeasureType()) {
			case ANGLE:
				result = AngleUnit.class;
				break;
			case LENGTH:
				result = LengthUnit.class;
				break;
			case SCALE:
				result = ScaleUnit.class;
				break;
			case TIME:
				result = TimeUnit.class;
				break;
			default:
				throw new IllegalArgumentException(String.format("Unsupported measure type for WKT: %s", unitItem.getMeasureType()));
		}
		
		return unit(unitItem, unitItem.getMeasureType(), result);
	}
	
	private static <U extends Unit> U unit(UnitOfMeasureItem unitItem, MeasureType expectedMeasureType, Class<U> unitType) {
		if (!expectedMeasureType.equals(unitItem.getMeasureType())) {
			throw new IllegalArgumentException(String.format("Unit of measure '%s' is not a %s unit", unitItem.getName(), expectedMeasureType.name()));
		}

		U result;
		try {
			Method factoryMethod = wktFactory.getClass().getMethod("create" + unitType.getSimpleName());
			result = (U)factoryMethod.invoke(wktFactory);
			
			Method nameSetter = result.getClass().getMethod("setName", String.class);
			nameSetter.invoke(result, unitItem.getName());
			
			Method factorSetter = result.getClass().getMethod("setFactor", double.class);
			if (unitItem.getScaleToStandardUnit() != null) {
				factorSetter.invoke(result, unitItem.getScaleToStandardUnit());
			}
			else {
				factorSetter.invoke(result, 1.);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return result;
	}

	private static ParameterUnit parameterUnit(UnitOfMeasureItem unitItem) {
		if (unitItem == null) {
			throw new NullPointerException("Unit of measure must not be null");
		}
		
		ParameterUnit result;
		switch (unitItem.getMeasureType()) {
			case ANGLE:
				result = unit(unitItem, MeasureType.ANGLE, AngleUnit.class); 
				break;
			case LENGTH:
				result = unit(unitItem, MeasureType.LENGTH, LengthUnit.class);
//				result = wktFactory.createLengthUnit();
//				((LengthUnit)result).setName(unitItem.getName());
//				if (unitItem.getScaleToStandardUnit() != null) {
//					((LengthUnit)result).setFactor(unitItem.getScaleToStandardUnit());
//				}
				break;
			case SCALE:
				result = unit(unitItem, MeasureType.SCALE, ScaleUnit.class);
//				result = wktFactory.createAngleUnit();
//				((ScaleUnit)result).setName(unitItem.getName());
//				if (unitItem.getScaleToStandardUnit() != null) {
//					((ScaleUnit)result).setFactor(unitItem.getScaleToStandardUnit());
//				}
				break;
			default:
				throw new IllegalArgumentException(String.format("Invalid measure type for ParameterUnit: %s", unitItem.getMeasureType()));
		}
		
		return result;
	}

	public static void main(String[] args) {
		CoordinateReferenceSystemItem item = new CoordinateReferenceSystemItem(new RE_Register("test"), new RE_ItemClass(), "Test", "Definition", new RE_AdditionInformation(), new EX_Extent(), "schkoppa");
		FileOutputStream target = null;
		try {
			target = new FileOutputStream("D:/test.wkt");
			WktExporter.exportCrs(item, target);
			target.flush();
		}
		catch (IOException | JAXBException e) {
		}
		finally {
			IOUtils.closeQuietly(target);
		}
	}

//	public static void exportCs(CoordinateSystemItem csItem, Appendable target) throws IOException {
//		WKTFormat wkt = new WKTFormat(Locale.ENGLISH, TimeZone.getTimeZone("UTC"));
//		wkt.format(cs(csItem), target);
//	}
//	
//	private static <Q extends Quantity> Unit<Q> unit(UnitOfMeasureItem unitItem, Class<Q> quantity) {
//		if (unitItem == null) {
//			throw new NullPointerException("Unit of measure must not be null");
//		}
//		else if (StringUtils.isEmpty(unitItem.getSymbol())) {
//			return new BaseUnit<Q>(unitItem.getName());
//		}
//		else {
//			Unit<Q> unit = (Unit<Q>)Unit.valueOf(unitItem.getSymbol());
//			if (unit == null) {
//				unit = new BaseUnit<Q>(unitItem.getSymbol());
//			}
//			
//			return unit;
//		}
//	}
//	
//	private static AxisDirection axisDirection(CS_AxisDirection direction) {
//		switch (direction.getCode()) {
//			case "columnNegative":
//				return AxisDirection.COLUMN_NEGATIVE;
//			case "columnPositive":
//				return AxisDirection.COLUMN_POSITIVE;
//			case "displayDown":
//				return AxisDirection.DISPLAY_DOWN;
//			case "displayLeft":
//				return AxisDirection.DISPLAY_LEFT;
//			case "displayUp":
//				return AxisDirection.DISPLAY_UP;
//			case "down":
//				return AxisDirection.DOWN;
//			case "east":
//				return AxisDirection.EAST;
//			case "eastNorthEast":
//				return AxisDirection.EAST_NORTH_EAST;
//			case "eastSouthEast":
//				return AxisDirection.EAST_SOUTH_EAST;
//			case "geocentricX":
//				return AxisDirection.GEOCENTRIC_X;
//			case "geocentricY":
//				return AxisDirection.GEOCENTRIC_Y;
//			case "geocentricZ":
//				return AxisDirection.GEOCENTRIC_Z;
//			case "north":
//				return AxisDirection.NORTH;
//			case "northEast":
//				return AxisDirection.NORTH_EAST;
//			case "northNorthEast":
//				return AxisDirection.NORTH_NORTH_EAST;
//			case "northNorthWest":
//				return AxisDirection.NORTH_NORTH_WEST;
//			case "northWest":
//				return AxisDirection.NORTH_WEST;
//			case "rowNegative":
//				return AxisDirection.ROW_NEGATIVE;
//			case "rowPositive":
//				return AxisDirection.ROW_POSITIVE;
//			case "south":
//				return AxisDirection.SOUTH;
//			case "southEast":
//				return AxisDirection.SOUTH_EAST;
//			case "southSouthEast":
//				return AxisDirection.SOUTH_SOUTH_EAST;
//			case "southSouthWest":
//				return AxisDirection.SOUTH_SOUTH_WEST;
//			case "southWest":
//				return AxisDirection.SOUTH_WEST;
//			case "up":
//				return AxisDirection.UP;
//			case "west":
//				return AxisDirection.WEST;
//			case "westNorthWest":
//				return AxisDirection.WEST_NORTH_WEST;
//			case "westSouthWest":
//				return AxisDirection.WEST_SOUTH_WEST;
//			default:
//				return AxisDirection.OTHER;
//		}
//	}
//	
//	private static CoordinateSystemAxis axis(CoordinateSystemAxisItem axisItem) {
//		Unit<?> unit = unit(axisItem.getAxisUnit(), Length.class);
////		Unit<?> unit = unit(axisItem.getAxisUnit());
//		return new DefaultCoordinateSystemAxis(singletonMap(DefaultCoordinateSystemAxis.NAME_KEY, axisItem.getName()), axisItem.getAxisAbbreviation(), axisDirection(axisItem.getAxisDirection()), unit);
//	}
//	
//	private static CoordinateSystem cs(CoordinateSystemItem csItem) {
//		if (csItem instanceof VerticalCoordinateSystemItem) {
//			return new DefaultVerticalCS(singletonMap(DefaultVerticalCS.NAME_KEY, csItem.getName()), axis(csItem.getAxes().get(0)));
//		}
//		else if (csItem instanceof CartesianCoordinateSystemItem) {
//			switch (csItem.getAxes().size()) {
//				case 2:
//					return new DefaultCartesianCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)));
//				case 3:
//					return new DefaultCartesianCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)), axis(csItem.getAxes().get(2)));
//				default:
//					throw new IllegalStateException(String.format("Cartesian CRS '%s' has invalid number of axes [is: %d] [expected: 2|3]", csItem.getName(), csItem.getAxes().size()));
//			}
//		}
//		else if (csItem instanceof EllipsoidalCoordinateSystemItem) {
//			switch (csItem.getAxes().size()) {
//				case 2:
//					return new DefaultEllipsoidalCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)));
//				case 3:
//					return new DefaultEllipsoidalCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)), axis(csItem.getAxes().get(2)));
//				default:
//					throw new IllegalStateException(String.format("Ellipsoidal CRS '%s' has invalid number of axes [is: %d] [expected: 2|3]", csItem.getName(), csItem.getAxes().size()));
//			}
//		}
//		else if (csItem instanceof SphericalCoordinateSystemItem) {
//			return new DefaultSphericalCS(name(csItem), axis(csItem.getAxes().get(0)), axis(csItem.getAxes().get(1)), axis(csItem.getAxes().get(2)));
//		}
//		else {
//			throw new IllegalArgumentException(String.format("CS of type %s not supported", csItem.getClass().getCanonicalName()));			
//		}
//	}
//	
//	private static CoordinateReferenceSystem crs(CoordinateReferenceSystemItem crsItem) {
//		Map<String, Object> properties = new HashMap<>();
//		properties.put(AbstractCRS.NAME_KEY, crsItem.getName());
//		properties.put(AbstractCRS.IDENTIFIERS_KEY, id(crsItem));
//		properties.put(AbstractCRS.ALIAS_KEY, alias(crsItem));
//		properties.put(AbstractCRS.SCOPE_KEY, new DefaultInternationalString(crsItem.getScope()));
//		properties.put(AbstractCRS.DOMAIN_OF_VALIDITY_KEY, extent(crsItem));
//		
//		CoordinateReferenceSystem result;
//		if (crsItem instanceof VerticalCoordinateReferenceSystemItem) {
//			VerticalCoordinateReferenceSystemItem vcrs = (VerticalCoordinateReferenceSystemItem)crsItem;
//			result = new DefaultVerticalCRS(properties, (VerticalDatum)datum(vcrs.getDatum()), (VerticalCS)cs(vcrs.getCoordinateSystem())); 		
//		}
//		else if (crsItem instanceof GeodeticCoordinateReferenceSystemItem) {
//			GeodeticCoordinateReferenceSystemItem gcrs = (GeodeticCoordinateReferenceSystemItem)crsItem;
//			if (gcrs.getCoordinateSystem() instanceof EllipsoidalCoordinateSystemItem) {
//				result = new DefaultGeographicCRS(properties, (GeodeticDatum)datum(gcrs.getDatum()), (EllipsoidalCS)cs(gcrs.getCoordinateSystem()));				
//			}
//			else if (gcrs.getCoordinateSystem() instanceof SphericalCoordinateSystemItem) {
//				result = new DefaultGeocentricCRS(properties, (GeodeticDatum)datum(gcrs.getDatum()), (SphericalCS)cs(gcrs.getCoordinateSystem()));				
//			}
//			else if (gcrs.getCoordinateSystem() instanceof CartesianCoordinateSystemItem) {
//				result = new DefaultGeocentricCRS(properties, (GeodeticDatum)datum(gcrs.getDatum()), (CartesianCS)cs(gcrs.getCoordinateSystem()));
//			}
//			else {
//				throw new IllegalStateException(String.format("Geodetic CRS '%s' has invalid coordinate system of type '%s'", crsItem.getName(), gcrs.getCoordinateSystem().getClass().getCanonicalName()));
//			}
//		}
//		else if (crsItem instanceof EngineeringCoordinateReferenceSystemItem) {
//			EngineeringCoordinateReferenceSystemItem ecrs = (EngineeringCoordinateReferenceSystemItem)crsItem;
//			result = new DefaultEngineeringCRS(properties, (EngineeringDatum)datum(ecrs.getDatum()), cs(ecrs.getCoordinateSystem())); 		
//		}
//		else if (crsItem instanceof ProjectedCoordinateReferenceSystemItem) {
//			ProjectedCoordinateReferenceSystemItem pcrs = (ProjectedCoordinateReferenceSystemItem)crsItem;
//			if (pcrs.getOperation() instanceof ConversionItem) {
//				ConversionItem conv = (ConversionItem)pcrs.getOperation(); 
//				result = new DefaultProjectedCRS(properties, (GeographicCRS)crs(pcrs.getBaseCrs()), conversion(conv), (CartesianCS)cs(pcrs.getCoordinateSystem()));
//			}
//			else {
//				throw new IllegalStateException(String.format("Cannot create WKT representation because projected CRS '%s' is incompatible with ISO 19162: projection method is not a Conversion", crsItem.getName()));
//			}
//		}
//		else {
//			throw new IllegalArgumentException(String.format("CRS of type %s not supported", crsItem.getClass().getCanonicalName()));			
//		}
//		
//		return result;
//	}
//	
//	private static NamedIdentifier id(IdentifiedItem item) {
//		Map<String, String> properties = new HashMap<>();
//		properties.put(NamedIdentifier.AUTHORITY_KEY, "ISO");
//		properties.put(NamedIdentifier.CODE_KEY, item.getIdentifier().toString());
//		
//		return new NamedIdentifier(properties);
//	}
//	
//	private static Collection<GenericName> alias(IdentifiedItem item) {
//		Collection<GenericName> result = new ArrayList<>();
//		for (String alias : item.getAliases()) {
//			Map<String, String> properties = new HashMap<>();
//			properties.put(NamedIdentifier.CODE_KEY, alias);
//			result.add(new NamedIdentifier(properties));
//		}
//		
//		return result;
//	}
//	
//	private static Extent extent(CoordinateReferenceSystemItem item) {
//		DefaultExtent result = new DefaultExtent();
//		for (EX_GeographicExtent extent : item.getDomainOfValidity().getGeographicElement()) {
//			if (extent instanceof EX_GeographicBoundingBox) {
//				EX_GeographicBoundingBox box = (EX_GeographicBoundingBox)extent;
//				result.getGeographicElements().add(new DefaultGeographicBoundingBox(box.getWestBoundLongitude(), box.getEastBoundLongitude(), box.getSouthBoundLatitude(), box.getNorthBoundLatitude()));
//			}
//		}
//		
//		return result;
//	}
//	
//	private static Conversion conversion(ConversionItem conversion) {
//		return new DefaultConversion(name(conversion), method(conversion.getMethod()), null, parameterValues(conversion.getMethod(), conversion.getParameterValue()));
//	}
//	
//	private static OperationMethod method(OperationMethodItem methodItem) {
//		return new DefaultOperationMethod(name(methodItem), methodItem.getSourceDimensions(), methodItem.getTargetDimensions(), parameters(methodItem.getName(), methodItem.getParameter())); 
//	}
//	
//	private static ParameterDescriptorGroup parameters(String methodName, Collection<GeneralOperationParameterItem> parameterItems) {
//		ArrayList<GeneralParameterDescriptor> parameters = new ArrayList<>();
//		for (GeneralOperationParameterItem parameter : parameterItems) {
//			parameters.add(parameter(parameter, null));
//		}
//		
//		return new DefaultParameterDescriptorGroup(singletonMap(DefaultParameterDescriptorGroup.NAME_KEY, methodName), 1, 1, parameters.toArray(new GeneralParameterDescriptor[] { }));
//	}
//
//	private static DefaultParameterDescriptor<?> parameter(GeneralOperationParameterItem parameter, OperationParameterValueType valueType) {
//		if (valueType != null) {
//			switch (valueType) {
//				case STRING:
//					return new DefaultParameterDescriptor<String>(name(parameter), nullTo0(parameter.getMinimumOccurs()), 1, String.class, null, null, null);
//				case INTEGER:
//					return new DefaultParameterDescriptor<Integer>(name(parameter), nullTo0(parameter.getMinimumOccurs()), 1, Integer.class, null, null, null);
//				case INTEGER_LIST:
//					return new DefaultParameterDescriptor<Integer>(name(parameter), nullTo0(parameter.getMinimumOccurs()), Integer.MAX_VALUE, Integer.class, null, null, null);
//				case BOOLEAN:
//					return new DefaultParameterDescriptor<Boolean>(name(parameter), nullTo0(parameter.getMinimumOccurs()), 1, Boolean.class, null, null, null);
//				case FILE:
//					return new DefaultParameterDescriptor<String>(name(parameter), nullTo0(parameter.getMinimumOccurs()), 1, String.class, null, null, null);
//				case MEASURE:
//					return new DefaultParameterDescriptor<Measure>(name(parameter), nullTo0(parameter.getMinimumOccurs()), 1, Measure.class, null, null, null);
//				case MEASURE_LIST:
//					return new DefaultParameterDescriptor<Measure>(name(parameter), nullTo0(parameter.getMinimumOccurs()), Integer.MAX_VALUE, Measure.class, null, null, null);
//				default:
//					return parameter(parameter, null);
//			}
//		}
//		else {
//			return new DefaultParameterDescriptor<Object>(name(parameter), parameter.getMinimumOccurs(), 1, Object.class, null, null, null);
//		} 
//	}
//	
//	private static int nullTo0(Integer i) {
//		return (i == null) ? 0 : i;
//	}
//	
//	private static ParameterValueGroup parameterValues(OperationMethodItem method, Collection<GeneralParameterValue<?>> values) {
//		ArrayList<org.opengis.parameter.GeneralParameterValue> sisValues = new ArrayList<>();
//		for (GeneralParameterValue<?> value : values) {
//			if (value instanceof OperationParameterValue) {
//				OperationParameterValue opv = (OperationParameterValue)value;
//				ParameterValue<?> pvalue = parameter(opv.getParameter(), opv.getParameterType()).createValue();
//				pvalue.setValue(opv.getValue());
//				sisValues.add(pvalue);
//			}
//		}
//		
//		return parameters(method.getName(), method.getParameter()).createValue();
//	}
//	
//	private static Datum datum(DatumItem datumItem) {
//		Datum result;
//		if (datumItem instanceof VerticalDatumItem) {
//			result = new DefaultVerticalDatum(singletonMap(VerticalDatum.NAME_KEY, datumItem.getName()), VerticalDatumType.GEOIDAL);
//		}
//		else if (datumItem instanceof EngineeringDatumItem) {
//			result = new DefaultEngineeringDatum(singletonMap(VerticalDatum.NAME_KEY, datumItem.getName()));
//		}
//		else if (datumItem instanceof GeodeticDatumItem) {
//			result = new DefaultGeodeticDatum(singletonMap(VerticalDatum.NAME_KEY, datumItem.getName()), ellipsoid(((GeodeticDatumItem)datumItem).getEllipsoid()), primeMeridian(((GeodeticDatumItem)datumItem).getPrimeMeridian()));
//		}
//		else {
//			throw new IllegalArgumentException(String.format("Datums of type %s not supported", datumItem.getClass().getCanonicalName()));
//		}
//		
//		return result;
//	}
//	
//	private static PrimeMeridian primeMeridian(PrimeMeridianItem pmi) {
//		return new DefaultPrimeMeridian(name(pmi), pmi.getGreenwichLongitude(), unit(pmi.getGreenwichLongitudeUom(), Angle.class));
//	}
//	
//	private static Ellipsoid ellipsoid(EllipsoidItem ellipsoidItem) {
//		if (ellipsoidItem.getSemiMinorAxis() == null && ellipsoidItem.getInverseFlattening() == null || ellipsoidItem.getSemiMinorAxis() != null && ellipsoidItem.getInverseFlattening() != null) {
//			throw new IllegalArgumentException("Ellipsoid must either specify semi minor axis or inverse flattening");
//		}
//		
//		Unit<Length> unit = unit(ellipsoidItem.getSemiMajorAxisUom(), Length.class);
//		if (ellipsoidItem.getSemiMinorAxis() != null) {
//			return DefaultEllipsoid.createEllipsoid(name(ellipsoidItem), ellipsoidItem.getSemiMajorAxis(), ellipsoidItem.getSemiMinorAxis(), unit);
//		}	
//		else {
//			return DefaultEllipsoid.createFlattenedSphere(name(ellipsoidItem), ellipsoidItem.getSemiMajorAxis(), ellipsoidItem.getInverseFlattening(), unit);
//		}
//	}
//	
//	private static Map<String, String> name(RE_RegisterItem item) {
//		return singletonMap("name", item.getName());
//	}

}
