package org.iso.registry.persistence.io.gml;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.EngineeringCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.GeodeticCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.ProjectedCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;
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
import org.iso.registry.core.model.iso19103.RecordType;
import org.iso.registry.core.model.iso19103.UnitOfMeasure;
import org.iso.registry.core.model.iso19115.dataquality.DQ_AbsoluteExternalPositionalAccuracy;
import org.iso.registry.core.model.iso19115.dataquality.DQ_PositionalAccuracy;
import org.iso.registry.core.model.iso19115.dataquality.DQ_RelativeInternalPositionalAccuracy;
import org.iso.registry.core.model.iso19115.dataquality.DQ_Result;
import org.iso.registry.core.model.iso19115.dataquality.QuantitativeResult;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicExtent;
import org.iso.registry.core.model.operation.ConversionItem;
import org.iso.registry.core.model.operation.CoordinateOperationItem;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.isotc211.iso19139.gco.CharacterStringPropertyType;
import org.isotc211.iso19139.gco.DatePropertyType;
import org.isotc211.iso19139.gco.DecimalPropertyType;
import org.isotc211.iso19139.gco.GCOFactory;
import org.isotc211.iso19139.gco.RecordTypePropertyType;
import org.isotc211.iso19139.gco.RecordTypeType;
import org.isotc211.iso19139.gco.UnitOfMeasurePropertyType;
import org.isotc211.iso19139.gmd.AbstractDQPositionalAccuracyType;
import org.isotc211.iso19139.gmd.CICitationType;
import org.isotc211.iso19139.gmd.CISeriesPropertyType;
import org.isotc211.iso19139.gmd.CISeriesType;
import org.isotc211.iso19139.gmd.DQQuantitativeResultType;
import org.isotc211.iso19139.gmd.DQResultPropertyType;
import org.isotc211.iso19139.gmd.EXExtentType;
import org.isotc211.iso19139.gmd.EXGeographicBoundingBoxType;
import org.isotc211.iso19139.gmd.EXGeographicExtentPropertyType;
import org.isotc211.iso19139.gmd.GMDFactory;
import org.isotc211.iso19139.gmd.GMDPackage;
import org.isotc211.iso19139.gmd.impl.EXGeographicExtentPropertyTypeImpl;
import org.w3.xlink.TypeType;

import de.geoinfoffm.registry.api.iso.IsoXmlFactory;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.CI_Series;
import net.opengis.gml32.AbstractCRSType;
import net.opengis.gml32.AbstractCoordinateOperationType;
import net.opengis.gml32.AbstractCoordinateSystemType;
import net.opengis.gml32.AbstractDatumType;
import net.opengis.gml32.AbstractGMLType;
import net.opengis.gml32.AngleType;
import net.opengis.gml32.BaseUnitType;
import net.opengis.gml32.CartesianCSPropertyType;
import net.opengis.gml32.CartesianCSType;
import net.opengis.gml32.CodeType;
import net.opengis.gml32.CodeWithAuthorityType;
import net.opengis.gml32.CompoundCRSType;
import net.opengis.gml32.ConventionalUnitType;
import net.opengis.gml32.ConversionPropertyType;
import net.opengis.gml32.ConversionToPreferredUnitType;
import net.opengis.gml32.ConversionType;
import net.opengis.gml32.CoordinateOperationAccuracyType;
import net.opengis.gml32.CoordinateSystemAxisPropertyType;
import net.opengis.gml32.CoordinateSystemAxisType;
import net.opengis.gml32.DefinitionType;
import net.opengis.gml32.DocumentRoot;
import net.opengis.gml32.DomainOfValidityType;
import net.opengis.gml32.EllipsoidPropertyType;
import net.opengis.gml32.EllipsoidType;
import net.opengis.gml32.EllipsoidalCSPropertyType;
import net.opengis.gml32.EllipsoidalCSType;
import net.opengis.gml32.EngineeringCRSType;
import net.opengis.gml32.EngineeringDatumPropertyType;
import net.opengis.gml32.EngineeringDatumType;
import net.opengis.gml32.FormulaCitationType;
import net.opengis.gml32.FormulaType;
import net.opengis.gml32.GMLFactory;
import net.opengis.gml32.GMLPackage;
import net.opengis.gml32.GeneralConversionPropertyType;
import net.opengis.gml32.GeodeticCRSType;
import net.opengis.gml32.GeodeticDatumPropertyType;
import net.opengis.gml32.GeodeticDatumType;
import net.opengis.gml32.LengthType;
import net.opengis.gml32.MeasureType;
import net.opengis.gml32.OperationMethodPropertyType;
import net.opengis.gml32.OperationMethodType;
import net.opengis.gml32.PrimeMeridianPropertyType;
import net.opengis.gml32.PrimeMeridianType;
import net.opengis.gml32.ProjectedCRSType;
import net.opengis.gml32.SecondDefiningParameterType;
import net.opengis.gml32.SecondDefiningParameterType1;
import net.opengis.gml32.SingleCRSPropertyType;
import net.opengis.gml32.SphericalCSPropertyType;
import net.opengis.gml32.SphericalCSType;
import net.opengis.gml32.StringOrRefType;
import net.opengis.gml32.UnitDefinitionType;
import net.opengis.gml32.VerticalCRSType;
import net.opengis.gml32.VerticalCSPropertyType;
import net.opengis.gml32.VerticalCSType;
import net.opengis.gml32.VerticalDatumPropertyType;
import net.opengis.gml32.VerticalDatumType;

public class GmlExporter
{
	private static final GMLFactory gmlFactory = GMLFactory.eINSTANCE;
	private static final GMDFactory gmdFactory = GMDFactory.eINSTANCE;
	private static final GCOFactory gcoFactory = GCOFactory.eINSTANCE;
	
	public static void exportCrs(CoordinateReferenceSystemItem crsItem, OutputStream target) throws IOException, JAXBException {
		EPackage.Registry.INSTANCE.put(GMLPackage.eNS_URI, GMLPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("gml", new XMLResourceFactoryImpl());

		DocumentRoot root = GMLFactory.eINSTANCE.createDocumentRoot();
		root.getXSISchemaLocation().put("http://www.opengis.net/gml/3.2", "http://schemas.opengis.net/gml/3.2.1/gml.xsd");
		root.getXMLNSPrefixMap().put("gml", "http://www.opengis.net/gml/3.2");
		
		if (crsItem instanceof VerticalCoordinateReferenceSystemItem) {
			VerticalCoordinateReferenceSystemItem vcrs = (VerticalCoordinateReferenceSystemItem)crsItem;
			root.setVerticalCRS(verticalCrs(vcrs));
		}
		else if (crsItem instanceof GeodeticCoordinateReferenceSystemItem) {
			GeodeticCoordinateReferenceSystemItem gcrs = (GeodeticCoordinateReferenceSystemItem)crsItem;
			root.setGeodeticCRS(geodeticCrs(gcrs));
		}
		else if (crsItem instanceof EngineeringCoordinateReferenceSystemItem) {
			EngineeringCoordinateReferenceSystemItem ecrs = (EngineeringCoordinateReferenceSystemItem)crsItem;
			root.setEngineeringCRS(engineeringCrs(ecrs));
		}
		else if (crsItem instanceof ProjectedCoordinateReferenceSystemItem) {
			ProjectedCoordinateReferenceSystemItem pcrs = (ProjectedCoordinateReferenceSystemItem)crsItem;
			if (pcrs.getOperation() instanceof ConversionItem) {
				ConversionItem conv = (ConversionItem)pcrs.getOperation();
				root.setProjectedCRS(projectedCrs(pcrs));
			}
			else {
				throw new IllegalStateException(String.format("Cannot create WKT representation because projected CRS '%s' is incompatible with ISO 19162: projection method is not a Conversion", crsItem.getName()));
			}
		}
		else if (crsItem instanceof CompoundCoordinateReferenceSystemItem) {
			CompoundCoordinateReferenceSystemItem ccrs = (CompoundCoordinateReferenceSystemItem)crsItem;
			root.setCompoundCRS(compoundCrs(ccrs));
		}
		else {
			throw new IllegalArgumentException(String.format("CRS of type %s not supported", crsItem.getClass().getCanonicalName()));			
		}
		
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createFileURI("D:/isogml.gml"));
		resource.getContents().add(root);
		
		Map<Object, Object> options = new HashMap<>();
		options.put(XMLResource.OPTION_EXTENDED_META_DATA, true);
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		
		resource.save(target, options);
	}
	
	private static CodeWithAuthorityType codeWithAuthority(String codeSpace, String value) {
		CodeWithAuthorityType result = gmlFactory.createCodeWithAuthorityType();
		result.setValue(StringEscapeUtils.escapeXml(value));
		result.setCodeSpace(codeSpace);
		return result;
	}
	
	private static CodeType code(String codeSpace, String value) {
		CodeType result = gmlFactory.createCodeType();
		result.setCodeSpace(codeSpace);
		result.setValue(StringEscapeUtils.escapeXml(value));
		return result;
	}
	
	private static void setAbstractGmlAttributes(IdentifiedItem item, AbstractGMLType gml, String idPrefix) {
		gml.setId(idPrefix + item.getIdentifier().toString());
		gml.setIdentifier(codeWithAuthority("ISO-TC211", item.getIdentifier().toString()));
		gml.setDescription(stringOrRef(item.getDescription()));
		gml.getName().add(code(null, item.getName()));
	}
	
	private static void setDefinitionTypeAttributes(IdentifiedItem item, DefinitionType def, String idPrefix) {
		setAbstractGmlAttributes(item, def, idPrefix);
		def.setRemarks(item.getRemarks());		
	}
	
	private static void setCrsAttributes(CoordinateReferenceSystemItem item, AbstractCRSType crs) {
		setDefinitionTypeAttributes(item, crs, "iso-crs-");
		crs.getScope().add(item.getScope());
		crs.getDomainOfValidity().add(domainOfValidity(item.getDomainOfValidity()));
	}
	
	private static void setCsAttribtues(CoordinateSystemItem item, AbstractCoordinateSystemType cs) {
		setDefinitionTypeAttributes(item, cs, "iso-cs-");
		for (CoordinateSystemAxisItem axis : item.getAxes()) {
			CoordinateSystemAxisPropertyType axisProperty = gmlFactory.createCoordinateSystemAxisPropertyType();
			axisProperty.setCoordinateSystemAxis(axis(axis));
			cs.getAxis().add(axisProperty);
		}
	}
	
	private static void setDatumAttributes(DatumItem item, AbstractDatumType datum) {
		setDefinitionTypeAttributes(item, datum, "iso-datum-");
		
		datum.setDomainOfValidity(domainOfValidity(item.getDomainOfValidity()));
		datum.getScope().add(item.getScope());
		
		datum.setAnchorDefinition(code(null, item.getAnchorDefinition()));
		if (item.getRealizationEpoch() != null) {
			datum.setRealizationEpoch(IsoXmlFactory.xmlGregorianCalendar(item.getRealizationEpoch()));
		}
	}
	
	private static VerticalCRSType verticalCrs(VerticalCoordinateReferenceSystemItem item) {
		VerticalCRSType result = gmlFactory.createVerticalCRSType();

		setCrsAttributes(item, result);

		VerticalCSPropertyType vcsProperty = gmlFactory.createVerticalCSPropertyType();
		vcsProperty.setVerticalCS(verticalCs((VerticalCoordinateSystemItem)item.getCoordinateSystem()));
		result.setVerticalCS(vcsProperty);
		VerticalDatumPropertyType datumProperty = gmlFactory.createVerticalDatumPropertyType();
		datumProperty.setVerticalDatum(verticalDatum(item.getDatum()));
		result.setVerticalDatum(datumProperty);
		
		return result;
	}
	
	private static CompoundCRSType compoundCrs(CompoundCoordinateReferenceSystemItem ccrs) {
		CompoundCRSType result = gmlFactory.createCompoundCRSType();
		setCrsAttributes(ccrs, result);
		
		for (CoordinateReferenceSystemItem component : ccrs.getComponentReferenceSystem()) {
			SingleCRSPropertyType componentCrsProperty = gmlFactory.createSingleCRSPropertyType();
			if (component instanceof VerticalCoordinateReferenceSystemItem) {
				VerticalCoordinateReferenceSystemItem vcrs = (VerticalCoordinateReferenceSystemItem)component;
				componentCrsProperty.getAbstractSingleCRSGroup().add(GMLPackage.eINSTANCE.getDocumentRoot_VerticalCRS(), verticalCrs(vcrs));
			}
			else if (component instanceof GeodeticCoordinateReferenceSystemItem) {
				GeodeticCoordinateReferenceSystemItem gcrs = (GeodeticCoordinateReferenceSystemItem)component;
				componentCrsProperty.getAbstractSingleCRSGroup().add(GMLPackage.eINSTANCE.getDocumentRoot_GeodeticCRS(), geodeticCrs(gcrs));
			}
			else if (component instanceof EngineeringCoordinateReferenceSystemItem) {
				EngineeringCoordinateReferenceSystemItem ecrs = (EngineeringCoordinateReferenceSystemItem)component;
				componentCrsProperty.getAbstractSingleCRSGroup().add(GMLPackage.eINSTANCE.getDocumentRoot_EngineeringCRS(), engineeringCrs(ecrs));
			}
			else if (component instanceof ProjectedCoordinateReferenceSystemItem) {
				ProjectedCoordinateReferenceSystemItem pcrs = (ProjectedCoordinateReferenceSystemItem)component;
				if (pcrs.getOperation() instanceof ConversionItem) {
					ConversionItem conv = (ConversionItem)pcrs.getOperation();
					componentCrsProperty.getAbstractSingleCRSGroup().add(GMLPackage.eINSTANCE.getDocumentRoot_ProjectedCRS(), projectedCrs(pcrs));
				}
				else {
					throw new IllegalStateException(String.format("Cannot create WKT representation because projected CRS '%s' is incompatible with ISO 19162: projection method is not a Conversion", pcrs.getName()));
				}
			}
			
			result.getComponentReferenceSystem().add(componentCrsProperty);
		}
		
		return result;
	}
	
	private static GeodeticCRSType geodeticCrs(GeodeticCoordinateReferenceSystemItem gcrs) {
		GeodeticCRSType result = gmlFactory.createGeodeticCRSType();
		setCrsAttributes(gcrs, result);
		
		if (gcrs.getCoordinateSystem() instanceof EllipsoidalCoordinateSystemItem) {
			EllipsoidalCSPropertyType csProperty = gmlFactory.createEllipsoidalCSPropertyType();
			csProperty.setEllipsoidalCS(ellipsoidalCs((EllipsoidalCoordinateSystemItem)gcrs.getCoordinateSystem()));
			result.setEllipsoidalCS(csProperty);
		}
		else if (gcrs.getCoordinateSystem() instanceof SphericalCoordinateSystemItem) {
			SphericalCSPropertyType csPropertyType = gmlFactory.createSphericalCSPropertyType();
			csPropertyType.setSphericalCS(sphericalCs((SphericalCoordinateSystemItem)gcrs.getCoordinateSystem()));
			result.setSphericalCS(csPropertyType);
		}
		else if (gcrs.getCoordinateSystem() instanceof CartesianCoordinateSystemItem) {
			CartesianCSPropertyType csProperty = gmlFactory.createCartesianCSPropertyType();
			csProperty.setCartesianCS(cartesianCs((CartesianCoordinateSystemItem)gcrs.getCoordinateSystem()));
			result.setCartesianCS(csProperty);
		}
		else {
			throw new IllegalStateException(String.format("Geodetic CRS '%s' has invalid coordinate system of type '%s'", gcrs.getName(), gcrs.getCoordinateSystem().getClass().getCanonicalName()));
		}

		GeodeticDatumPropertyType datumProperty = gmlFactory.createGeodeticDatumPropertyType();
		datumProperty.setGeodeticDatum(geodeticDatum(gcrs.getDatum()));
		result.setGeodeticDatum(datumProperty);
		
		return result;
	}
	
	private static EngineeringCRSType engineeringCrs(EngineeringCoordinateReferenceSystemItem ecrs) {
		EngineeringCRSType result = gmlFactory.createEngineeringCRSType();
		setCrsAttributes(ecrs, result);
		
		if (ecrs.getCoordinateSystem() instanceof SphericalCoordinateSystemItem) {
			SphericalCSPropertyType csPropertyType = gmlFactory.createSphericalCSPropertyType();
			csPropertyType.setSphericalCS(sphericalCs((SphericalCoordinateSystemItem)ecrs.getCoordinateSystem()));
			result.setSphericalCS(csPropertyType);
		}
		else if (ecrs.getCoordinateSystem() instanceof CartesianCoordinateSystemItem) {
			CartesianCSPropertyType csProperty = gmlFactory.createCartesianCSPropertyType();
			csProperty.setCartesianCS(cartesianCs((CartesianCoordinateSystemItem)ecrs.getCoordinateSystem()));
			result.setCartesianCS(csProperty);
		}
		else {
			throw new IllegalStateException(String.format("Engineering CRS '%s' has invalid or unsupported coordinate system of type '%s'", ecrs.getName(), ecrs.getCoordinateSystem().getClass().getCanonicalName()));
		}
		
		EngineeringDatumPropertyType datumProperty = gmlFactory.createEngineeringDatumPropertyType();
		datumProperty.setEngineeringDatum(engineeringDatum(ecrs.getDatum()));
		result.setEngineeringDatum(datumProperty);
		
		return result;
	}
	
	private static void setAbstractCoordinateOperationAttributes(CoordinateOperationItem item, AbstractCoordinateOperationType operation, String idPrefix) {
		setDefinitionTypeAttributes(item, operation, idPrefix);
		operation.setDomainOfValidity(domainOfValidity(item.getDomainOfValidity()));
		for (String scope : item.getScope()) {
			operation.getScope().add(scope);
		}
		operation.setOperationVersion(item.getOperationVersion());
		for (DQ_PositionalAccuracy accuracy : item.getCoordinateOperationAccuracy()) {
			operation.getCoordinateOperationAccuracy().add(coordinateOperationAccuracy(accuracy));			
		}
	}
	
	private static CoordinateOperationAccuracyType coordinateOperationAccuracy(DQ_PositionalAccuracy accuracy) {
		CoordinateOperationAccuracyType result = gmlFactory.createCoordinateOperationAccuracyType();

		AbstractDQPositionalAccuracyType gmdAccuracy;
		EReference reference;
		if (accuracy instanceof DQ_AbsoluteExternalPositionalAccuracy) {
			gmdAccuracy = gmdFactory.createDQAbsoluteExternalPositionalAccuracyType();
			reference = GMDPackage.eINSTANCE.getDocumentRoot_DQAbsoluteExternalPositionalAccuracy();
		}
		else if (accuracy instanceof DQ_RelativeInternalPositionalAccuracy) {
			gmdAccuracy = gmdFactory.createDQRelativeInternalPositionalAccuracyType();
			reference = GMDPackage.eINSTANCE.getDocumentRoot_DQRelativeInternalPositionalAccuracy();
		}
		else {
			throw new RuntimeException(String.format("Accuracy type '%s' is not supported", accuracy.getClass().getCanonicalName()));
		}
		gmdAccuracy.getResult().add(resultProperty(accuracy.getResult()));
		result.getAbstractDQPositionalAccuracyGroup().add(reference, gmdAccuracy);
		
		return result;
	}
	
	private static DQResultPropertyType resultProperty(DQ_Result dqResult) {
		DQResultPropertyType result = gmdFactory.createDQResultPropertyType();
		if (dqResult instanceof QuantitativeResult) {
			result.getAbstractDQResultGroup().add(GMDPackage.eINSTANCE.getDocumentRoot_DQQuantitativeResult(), quantitativeResult((QuantitativeResult)dqResult));
		}
		else {
			throw new RuntimeException(String.format("DQ_Result type %s not supported", dqResult.getClass().getCanonicalName()));
		}
		
		return result;
	}
	
	private static DQQuantitativeResultType quantitativeResult(QuantitativeResult quantitativeResult) {
		DQQuantitativeResultType result = gmdFactory.createDQQuantitativeResultType();
		UnitOfMeasurePropertyType unitProperty = gcoFactory.createUnitOfMeasurePropertyType();
		unitProperty.setUnitDefinition(unitDefinition(quantitativeResult.getValueUnit()));
		result.setValueUnit(unitProperty);
		result.setErrorStatistic(characterStringProperty(quantitativeResult.getErrorStatistic()));
		result.setValueType(recordTypeProperty(quantitativeResult.getValueType()));
//		result.getValue().add(recordProperty(quantitativeResult.getValue()));
		
		return result;
	}
	
//	private static RecordPropertyType recordProperty(Record record) {
//		RecordPropertyType result = gcoFactory.createRecordPropertyType();
//		record.
//		result.setRecord(GMLPackage.eINSTANCE.);
//		
//		return result;
//	}
	
	private static RecordTypePropertyType recordTypeProperty(RecordType recordType) {
		RecordTypePropertyType result = gcoFactory.createRecordTypePropertyType();
		result.setRecordType(recordType(recordType));
		
		return result;
	}
	
	private static RecordTypeType recordType(RecordType recordType) {
		RecordTypeType result = gcoFactory.createRecordTypeType();
		result.setType(TypeType.SIMPLE);
		result.setValue(recordType.getTypeName());
		
		return result;
	}
	
	private static ProjectedCRSType projectedCrs(ProjectedCoordinateReferenceSystemItem item) {
		ProjectedCRSType result = gmlFactory.createProjectedCRSType();
		setCrsAttributes(item, result);
		
		ProjectedCoordinateReferenceSystemItem pcrs = (ProjectedCoordinateReferenceSystemItem)item;
		if (pcrs.getOperation() instanceof ConversionItem) {
			GeneralConversionPropertyType generalConversion = gmlFactory.createGeneralConversionPropertyType();
			generalConversion.getAbstractGeneralConversionGroup().add(GMLPackage.eINSTANCE.getDocumentRoot_Conversion1(), conversion((ConversionItem)pcrs.getOperation()));
			result.setConversion(generalConversion);
			// TODO
		}
		else {
			throw new IllegalStateException(String.format("Cannot create GML representation because projected CRS '%s' is incompatible with ISO 19162: projection method is not a Conversion", pcrs.getName()));
		}
		
		return result;
	}
	
	private static ConversionPropertyType conversionProperty(ConversionItem item) {
		ConversionPropertyType result = gmlFactory.createConversionPropertyType();
		result.setConversion(conversion(item));
		
		return result;
	}
	
	private static ConversionType conversion(ConversionItem item) {
		ConversionType result = gmlFactory.createConversionType();
		setAbstractCoordinateOperationAttributes(item, result, "CO");
		
		for (DQ_PositionalAccuracy accuracy : item.getCoordinateOperationAccuracy()) {
			result.getCoordinateOperationAccuracy().add(coordinateOperationAccuracy(accuracy));
		}
		OperationMethodPropertyType methodProperty = gmlFactory.createOperationMethodPropertyType();
		methodProperty.setOperationMethod(operationMethod(item.getMethod()));
		result.setMethod(methodProperty);
		
		return result;
	}
	
	private static OperationMethodType operationMethod(OperationMethodItem item) {
		OperationMethodType result = gmlFactory.createOperationMethodType();
		setDefinitionTypeAttributes(item, result, "OM");
		
		result.setFormulaCitation(formulaCitation(item.getFormulaCitation()));
		result.setFormula(code(null, item.getFormula()));
		if (item.getSourceDimensions() != null) {
			result.setSourceDimensions(BigInteger.valueOf(item.getSourceDimensions()));
		}
		if (item.getTargetDimensions() != null) { 
			result.setTargetDimensions(BigInteger.valueOf(item.getTargetDimensions()));
		}
		
		return result;
	}
	
	private static FormulaCitationType formulaCitation(CI_Citation citation) {
		if (citation == null) {
			return null;
		}
		FormulaCitationType result = gmlFactory.createFormulaCitationType();
		result.setCICitation(citation(citation));
		
		return result;
	}
	
	private static CICitationType citation(CI_Citation citation) {
		CICitationType result = gmdFactory.createCICitationType();
		result.setTitle(characterStringProperty(citation.getTitle()));
		result.setCollectiveTitle(characterStringProperty(citation.getCollectiveTitle()));
		result.setEdition(characterStringProperty(citation.getEdition()));
		result.setEditionDate(dateProperty(citation.getEditionDate()));
		result.setISBN(characterStringProperty(citation.getISBN()));
		result.setISSN(characterStringProperty(citation.getISSN()));
		result.setOtherCitationDetails(characterStringProperty(citation.getOtherCitationDetails()));
		result.setSeries(seriesProperty(citation.getSeries()));
		result.setUuid(citation.getUuid().toString());
		
		return result;
	}
	
	private static CISeriesType series(CI_Series series) {
		if (series == null) {
			return null;
		}
		
		CISeriesType result = gmdFactory.createCISeriesType();
		result.setName(characterStringProperty(series.getName()));
		result.setPage(characterStringProperty(series.getPage()));
		result.setIssueIdentification(characterStringProperty(series.getIssueIdentification()));
		
		return result;
	}
	
	private static CISeriesPropertyType seriesProperty(CI_Series series) {
		CISeriesPropertyType result = gmdFactory.createCISeriesPropertyType();
		result.setCISeries(series(series));
		
		return result;
	}
	
	private static VerticalDatumType verticalDatum(VerticalDatumItem item) {
		VerticalDatumType result = gmlFactory.createVerticalDatumType();
		setDatumAttributes(item, result);
		
		return result;
	}
	
	private static GeodeticDatumType geodeticDatum(GeodeticDatumItem item) {
		GeodeticDatumType result = gmlFactory.createGeodeticDatumType();
		setDatumAttributes(item, result);
		
		EllipsoidPropertyType ellipsoidProperty = gmlFactory.createEllipsoidPropertyType();
		ellipsoidProperty.setEllipsoid(ellipsoid(item.getEllipsoid()));
		result.setEllipsoid(ellipsoidProperty);
		
		PrimeMeridianPropertyType pmProperty = gmlFactory.createPrimeMeridianPropertyType();
		pmProperty.setPrimeMeridian(primeMeridian(item.getPrimeMeridian()));
		result.setPrimeMeridian(pmProperty);
		
		return result;
	}
	
	private static EngineeringDatumType engineeringDatum(EngineeringDatumItem item) {
		EngineeringDatumType result = gmlFactory.createEngineeringDatumType();
		setDatumAttributes(item, result);
		
		return result;
	}
	
	private static VerticalCSType verticalCs(VerticalCoordinateSystemItem vcs) {
		VerticalCSType result = gmlFactory.createVerticalCSType();
		setCsAttribtues(vcs, result);
		
		return result;
	}
	
	private static CartesianCSType cartesianCs(CartesianCoordinateSystemItem item) {
		CartesianCSType result = gmlFactory.createCartesianCSType();
		setCsAttribtues(item, result);
		return result;
	}

	private static EllipsoidalCSType ellipsoidalCs(EllipsoidalCoordinateSystemItem item) {
		EllipsoidalCSType result = gmlFactory.createEllipsoidalCSType();
		setCsAttribtues(item, result);
		return result;
	}

	private static SphericalCSType sphericalCs(SphericalCoordinateSystemItem item) {
		SphericalCSType result = gmlFactory.createSphericalCSType();
		setCsAttribtues(item, result);
		return result;
	}

	private static PrimeMeridianType primeMeridian(PrimeMeridianItem item) {
		PrimeMeridianType result = gmlFactory.createPrimeMeridianType();
		setDefinitionTypeAttributes(item, result, "iso-primemeridian-");
		
		result.setGreenwichLongitude(angle(item.getGreenwichLongitude(), item.getGreenwichLongitudeUom()));
		
		return result;
	}
	
	private static EllipsoidType ellipsoid(EllipsoidItem item) {
		if (item.getSemiMinorAxis() == null && item.getInverseFlattening() == null || item.getSemiMinorAxis() != null && item.getInverseFlattening() != null) {
			throw new IllegalArgumentException("Ellipsoid must either specify semi minor axis or inverse flattening");
		}

		EllipsoidType result = gmlFactory.createEllipsoidType();
		setDefinitionTypeAttributes(item, result, "iso-ellipsoid-");
		result.setSemiMajorAxis(measure(item.getSemiMajorAxis(), item.getSemiMajorAxisUom()));
		SecondDefiningParameterType sdp = gmlFactory.createSecondDefiningParameterType();
		if (item.getSemiMinorAxis() != null) {
			sdp.setSemiMinorAxis(length(item.getSemiMinorAxis(), item.getSemiMinorAxisUom()));
		}
		else if (item.getInverseFlattening() != null) {
			sdp.setInverseFlattening(measure(item.getInverseFlattening(), item.getInverseFlatteningUom()));
		}
		SecondDefiningParameterType1 sdp1 = gmlFactory.createSecondDefiningParameterType1();
		sdp1.setSecondDefiningParameter(sdp);
		result.setSecondDefiningParameter(sdp1);

		return result;
	}
	
	private static LengthType length(Double value, UnitOfMeasureItem unit) {
		LengthType result = gmlFactory.createLengthType();
		result.setValue(value);
		result.setUom(unit.getIdentifier().toString());
		return result;		
	}
	
	private static AngleType angle(Double value, UnitOfMeasureItem unit) {
		AngleType result = gmlFactory.createAngleType();
		result.setUom(unit.getIdentifier().toString());
		result.setValue(value);
		
		return result;
	}
	
	private static MeasureType measure(Double value, UnitOfMeasureItem unit) {
		MeasureType result = gmlFactory.createMeasureType();
		result.setValue(value);
		result.setUom(unit.getIdentifier().toString());
		return result;
	}
	
	private static UnitDefinitionType unitDefinition(UnitOfMeasure uom) {
		if (uom.getNameStandardUnit() == null || uom.getNameStandardUnit().equals(uom.getUomName())) {
			return baseUnit(uom);
		}
		else {
			return conventionalUnit(uom);
		}
	}
	
	private static BaseUnitType baseUnit(UnitOfMeasure uom) {
		BaseUnitType result = gmlFactory.createBaseUnitType();
		if (uom instanceof IdentifiedItem) {
			setDefinitionTypeAttributes((IdentifiedItem)uom, result, "UM");
		}
		
		result.setQuantityType(stringOrRef(uom.getMeasureType().name().toLowerCase()));
		
		return result;
	}
	
	private static ConventionalUnitType conventionalUnit(UnitOfMeasure uom) {
		ConventionalUnitType result = gmlFactory.createConventionalUnitType();
		if (uom instanceof IdentifiedItem) {
			setDefinitionTypeAttributes((IdentifiedItem)uom, result, "UM");
		}
		
		result.setQuantityType(stringOrRef(uom.getMeasureType().name().toLowerCase()));
		if (uom instanceof UnitOfMeasureItem) {
			result.setConversionToPreferredUnit(conversionToPreferredUnit(((UnitOfMeasureItem)uom).getStandardUnit()));
		}
		
		return result;		
	}
	
	private static ConversionToPreferredUnitType conversionToPreferredUnit(UnitOfMeasureItem item) {
		ConversionToPreferredUnitType result = gmlFactory.createConversionToPreferredUnitType();
		result.setFormula(formula(item.getScaleToStandardUnitNumerator(), item.getScaleToStandardUnitDenominator(), null, null));
		
		return result;
	}
	
	private static FormulaType formula(Double numerator, Double denominator, Double factorA, Double factorD) {
		FormulaType result = gmlFactory.createFormulaType();
		result.setB(numerator);
		result.setC(denominator);
		if (factorA != null) {
			result.setA(factorA);
		}
		if (factorD != null) {
			result.setD(factorD);
		}
		
		return result;
	}
	
	private static CoordinateSystemAxisType axis(CoordinateSystemAxisItem item) {
		CoordinateSystemAxisType result = gmlFactory.createCoordinateSystemAxisType();
		setDefinitionTypeAttributes(item, result, "iso-csaxis-");
		
		result.setAxisAbbrev(code(null, item.getAxisAbbreviation()));
		if (item.getAxisDirection() != null) {
			result.setAxisDirection(codeWithAuthority(item.getAxisDirection().getCodeSpace(), item.getAxisDirection().getCode()));
		}
		if (item.getMaximumValue() != null) {
			result.setMaximumValue(item.getMaximumValue().doubleValue());
		}
		if (item.getMinimumValue() != null) {
			result.setMinimumValue(item.getMinimumValue().doubleValue());
		}
		if (item.getRangeMeaning() != null) {
			result.setRangeMeaning(codeWithAuthority(null, item.getRangeMeaning().name().toLowerCase()));
		}
		if (item.getAxisUnit() != null) {
			result.setUom(item.getAxisUnit().getIdentifier().toString());
		}
		
		return result;
	}
	
	private static DomainOfValidityType domainOfValidity(EX_Extent extent) {
		DomainOfValidityType result = gmlFactory.createDomainOfValidityType();
		for (EX_GeographicExtent geo : extent.getGeographicElement()) {
			result.setEXExtent(extent(geo));
		}
		
		return result;
	}
	
	private static EXExtentType extent(EX_GeographicExtent extent) {
		EXExtentType result = GMDFactory.eINSTANCE.createEXExtentType();
		
		if (extent instanceof EX_GeographicBoundingBox) {
			EX_GeographicBoundingBox modelBox = (EX_GeographicBoundingBox)extent;
			EXGeographicBoundingBoxType bbox = GMDFactory.eINSTANCE.createEXGeographicBoundingBoxType();
			bbox.setEastBoundLongitude(decimal(modelBox.getEastBoundLongitude()));
			bbox.setWestBoundLongitude(decimal(modelBox.getWestBoundLongitude()));
			bbox.setNorthBoundLatitude(decimal(modelBox.getNorthBoundLatitude()));
			bbox.setSouthBoundLatitude(decimal(modelBox.getSouthBoundLatitude()));
			
			EXGeographicExtentPropertyType property = (EXGeographicExtentPropertyTypeImpl)GMDFactory.eINSTANCE.createEXGeographicExtentPropertyType();
			property.getAbstractEXGeographicExtentGroup().add(GMDPackage.eINSTANCE.getDocumentRoot_EXGeographicBoundingBox(), bbox);
			result.getGeographicElement().add(property);
		}
		else {
			throw new RuntimeException(String.format("Extent type %s not supported", extent.getClass().getCanonicalName()));
		}
		
		return result;
	}
	
	private static DecimalPropertyType decimal(Double value) {
		DecimalPropertyType result = GCOFactory.eINSTANCE.createDecimalPropertyType();
		result.setDecimal(BigDecimal.valueOf(value));
		
		return result;
	}
	
	private static StringOrRefType stringOrRef(String text) {
		StringOrRefType result = gmlFactory.createStringOrRefType();
		result.setValue(text);
		
		return result;
	}

	private static CharacterStringPropertyType characterStringProperty(String s) {
		if (s == null) {
			return null;
		}
		
		CharacterStringPropertyType result = gcoFactory.createCharacterStringPropertyType();
		result.setCharacterString(s);
		
		return result;
	}
	
	private static DatePropertyType dateProperty(String date) {
		DatePropertyType result = gcoFactory.createDatePropertyType();
		result.setDate(IsoXmlFactory.xmlGregorianCalendar(date));
		
		return result;
	}
}
