package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.crs.AreaItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.CoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.CoordinateReferenceSystemItemProposalDTO.CoordinateReferenceSystemType;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.DatumItemProposalDTO;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItemRepository;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItemRepository;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class CoordinateReferenceSystemsImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(CoordinateReferenceSystemsImporter.class);

	public static final String COORD_REF_SYS_CODE = "COORD_REF_SYS_CODE";
	public static final String COORD_REF_SYS_NAME = "COORD_REF_SYS_NAME";
	public static final String AREA_OF_USE_CODE = "AREA_OF_USE_CODE";
	public static final String COORD_REF_SYS_KIND = "COORD_REF_SYS_KIND";
	public static final String COORD_SYS_CODE = "COORD_SYS_CODE";
	public static final String DATUM_CODE = "DATUM_CODE";
	public static final String SOURCE_GEOGCRS_CODE = "SOURCE_GEOGCRS_CODE";
	public static final String PROJECTION_CONV_CODE = "PROJECTION_CONV_CODE";
	public static final String CMPD_HORIZCRS_CODE = "CMPD_HORIZCRS_CODE";
	public static final String CMPD_VERTCRS_CODE = "CMPD_VERTCRS_CODE";
	public static final String CRS_SCOPE = "CRS_SCOPE";

	@Autowired
	private CoordinateSystemItemRepository csRepository;
	
	@Autowired 
	private CoordinateReferenceSystemItemRepository crsRepository;
	
	@Autowired
	private DatumItemRepository datumRepository;
	
	@Autowired
	private AreaItemRepository areaRepository;
	
	private RE_ItemClass icGeodetic;
	private RE_ItemClass icVertical;
	private RE_ItemClass icEngineering;
	private RE_ItemClass icProjected;
	private RE_ItemClass icCompound;
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException {
		CoordinateReferenceSystemItemProposalDTO proposal = new CoordinateReferenceSystemItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		Integer crsCode = (Integer)row.get(COORD_REF_SYS_CODE);
		proposal.setIdentifier(crsCode);
		proposal.setName((String)row.get(COORD_REF_SYS_NAME));
		proposal.setScope((String)row.get(CRS_SCOPE));
		
		String crsType = (String)row.get(COORD_REF_SYS_KIND);
		if (crsType.equalsIgnoreCase("GEOGRAPHIC 2D") || crsType.equalsIgnoreCase("GEOGRAPHIC 3D") || crsType.equalsIgnoreCase("GEOCENTRIC")) {
			proposal.setType(CoordinateReferenceSystemType.GEODETIC);
		}
		else if (crsType.equalsIgnoreCase("ENGINEERING")) {			
			proposal.setType(CoordinateReferenceSystemType.ENGINEERING);
		}
		else if (crsType.equalsIgnoreCase("COMPOUND")) {			
			proposal.setType(CoordinateReferenceSystemType.COMPOUND);
		}
		else if (crsType.equalsIgnoreCase("PROJECTED")) {			
			proposal.setType(CoordinateReferenceSystemType.PROJECTED);
		}
		else if (crsType.equalsIgnoreCase("VERTICAL")) {			
			proposal.setType(CoordinateReferenceSystemType.VERTICAL);
		}
		else {
			throw new RuntimeException(String.format("Invalid CRS type: %s", crsType));
		}
		
		Integer areaCode = (Integer)row.get(AREA_OF_USE_CODE);
		AreaItem area = areaRepository.findByIdentifier(areaCode);
		if (area != null) {
			ExtentDTO extent = new ExtentDTO();
			extent.getGeographicBoundingBoxes().add(area.getBoundingBox());
			extent.setDescription(area.getName());
			proposal.setDomainOfValidity(extent);
		}
		else {
			logger.error(">>> CRS #{} referenced invalid Area #{}", crsCode, areaCode);
		}
		
		Integer csCode = (Integer)row.get(COORD_SYS_CODE);
		if (csCode != null) {
			CoordinateSystemItem cs = csRepository.findByIdentifier(csCode);
			if (cs != null) {
				proposal.setCoordinateSystem(new CoordinateSystemItemProposalDTO(cs));
			}
			else {
				logger.error(">>> CRS #{} referenced non-existent CS #{}", crsCode, csCode);
			}
		}
		
		Integer datumCode = (Integer)row.get(DATUM_CODE);
		if (datumCode != null) {
			DatumItem datum = datumRepository.findByIdentifier(datumCode);
			if (datum != null) {
				proposal.setDatum(new DatumItemProposalDTO(datum));
			}
			else {
				logger.error(">>> CRS #{} referenced non-existent Datum #{}", crsCode, datumCode);				
			}
		}		
		
		proposal.setRemarks((String)row.get(REMARKS));
		proposal.setInformationSource((String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent, BigInteger.valueOf(proposal.getIdentifier().longValue()));

			logger.info(">> Imported {} CRS #{} ('{}')...", new Object[] { crsType, crsCode, proposal.getName() });
		}
		catch (InvalidProposalException e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, String crsType) {
		if (crsType.equalsIgnoreCase("GEOCENTRIC") || crsType.equalsIgnoreCase("GEOGRAPHIC 2D") || crsType.equalsIgnoreCase("GEOGRAPHIC 3D")) {
			if (icGeodetic == null) {
				icGeodetic = itemClassRepository.findByName("GeodeticCRS");
				if (icGeodetic == null) {
					this.addItemClass("GeodeticCRS", register);
					icGeodetic = itemClassRepository.findByName("GeodeticCRS");
				}
			}

			return icGeodetic;
		}
		else if (crsType.equalsIgnoreCase("ENGINEERING")) {
			if (icEngineering == null) {
				icEngineering = itemClassRepository.findByName("EngineeringCRS");
				if (icEngineering == null) {
					this.addItemClass("EngineeringCRS", register);
					icEngineering = itemClassRepository.findByName("EngineeringCRS");
				}
			}

			return icEngineering;
		}
		else if (crsType.equalsIgnoreCase("VERTICAL")) {
			if (icVertical == null) {
				icVertical = itemClassRepository.findByName("VerticalCRS");
				if (icVertical == null) {
					this.addItemClass("VerticalCRS", register);
					icVertical = itemClassRepository.findByName("VerticalCRS");
				}
			}

			return icVertical;
		}
		else if (crsType.equalsIgnoreCase("PROJECTED")) {
			if (icProjected == null) {
				icProjected = itemClassRepository.findByName("ProjectedCRS");
				if (icProjected == null) {
					this.addItemClass("ProjectedCRS", register);
					icProjected = itemClassRepository.findByName("ProjectedCRS");
				}
			}

			return icProjected;
		}
		else if (crsType.equalsIgnoreCase("COMPOUND")) {
			if (icCompound == null) {
				icCompound = itemClassRepository.findByName("CompoundCRS");
				if (icCompound == null) {
					this.addItemClass("CompoundCRS", register);
					icCompound = itemClassRepository.findByName("CompoundCRS");
				}
			}

			return icCompound;
		}
		else {
			return null;
		}		
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		String crsType = (String)row.get(COORD_REF_SYS_KIND);
		
		return this.getOrCreateItemClass(register, crsType);
	}
	
	
	
//	@Transactional
//	public void fixReferences() {
//		for (Integer crsCode : brokenRefs) {
//			logger.info("Fixing missing references for CRS #{}...", crsCode);
//			CoordinateReferenceSystemItem crs = crsRepository.findByIdentifier(crsCode);
//			if (baseCrsRefs.containsKey(crsCode)) {
//				Integer baseCrsCode = baseCrsRefs.get(crsCode);
//				CoordinateReferenceSystemItem baseCrs = crsRepository.findByIdentifier(baseCrsCode);
//				if (crs instanceof SingleCoordinateReferenceSystemItem<?> && baseCrs instanceof SingleCoordinateReferenceSystemItem<?>) {
//					((SingleCoordinateReferenceSystemItem)crs).setBaseCrs((SingleCoordinateReferenceSystemItem)baseCrs);
//					logger.info(">> Set base CRS #{}", baseCrsCode);
//				}
//				else {
//					logger.error(">> ERROR: Could not set base CRS #{}", baseCrsCode);
//				}
//			}
//			
//			if (horizontalCrsRefs.containsKey(crsCode)) {
//				Integer horizontalCrsCode = horizontalCrsRefs.get(crsCode);
//				CoordinateReferenceSystemItem horizontalCrs = crsRepository.findByIdentifier(horizontalCrsCode);
//				if (crs instanceof CompoundCoordinateReferenceSystemItem && horizontalCrs instanceof SingleCoordinateReferenceSystemItem<?>) {
//					((CompoundCoordinateReferenceSystemItem)crs).addComponentReferenceSystem((SingleCoordinateReferenceSystemItem)horizontalCrs);
//					logger.info(">> Set horizontal CRS #{}", horizontalCrsCode);
//				}
//				else {
//					logger.error(">> ERROR: Could not set base CRS #{}", horizontalCrsCode);
//				}
//			}
//			
//			if (verticalCrsRefs.containsKey(crsCode)) {
//				Integer verticalCrsCode = verticalCrsRefs.get(crsCode);
//				CoordinateReferenceSystemItem verticalCrs = crsRepository.findByIdentifier(verticalCrsCode);
//				if (crs instanceof CompoundCoordinateReferenceSystemItem && verticalCrs instanceof SingleCoordinateReferenceSystemItem<?>) {
//					((CompoundCoordinateReferenceSystemItem)crs).addComponentReferenceSystem((SingleCoordinateReferenceSystemItem)verticalCrs);
//					logger.info(">> Set vertical CRS #{}", verticalCrsCode);
//				}
//				else {
//					logger.error(">> ERROR: Could not set base CRS #{}", verticalCrsCode);
//				}
//			}
//		}
//	}

	@Override
	@Transactional
	protected void fixReference(Row row, RE_SubmittingOrganization sponsor, RE_Register register) {
		Integer crsCode = (Integer)row.get(COORD_REF_SYS_CODE);
		logger.info(".");
		logger.info(".");
		logger.info("Fixing references for CRS #{}...", crsCode);
		
		CoordinateReferenceSystemItem crs = null;

//		Integer csCode = (Integer)row.get(COORD_SYS_CODE);
//		logger.info(">>> data references CS {}", csCode);
//		if (csCode != null) {
//			crs = crsRepository.findByIdentifier(crsCode);
//			
//			CoordinateSystemItem cs = csRepository.findByIdentifier(csCode);
//			if (cs != null) {
//				((SingleCoordinateReferenceSystemItem<DatumItem>)crs).setCoordinateSystem(cs);
//				logger.info(">>> fixed reference to CS {}", csCode);
//			}
//		}

		Integer datumCode = (Integer)row.get(DATUM_CODE);
		logger.info(">>> data references Datum {}", datumCode);
		if (datumCode != null) {
			crs = crsRepository.findByIdentifier(crsCode);
			
			DatumItem datum = datumRepository.findByIdentifier(datumCode);
			if (datum != null) {
				((SingleCoordinateReferenceSystemItem<DatumItem>)crs).setDatum(datum);
				logger.info(">>> fixed reference to Datum {}", datumCode);
			}
			else {
				logger.error(">>> CRS #{} referenced non-existent Datum #{}", crsCode, datumCode);				
			}
		}		

//		Integer baseCrsCode = (Integer)row.get(SOURCE_GEOGCRS_CODE);
//		if (baseCrsCode != null) {
//			crs = crsRepository.findByIdentifier(crsCode);
//			
//			CoordinateReferenceSystemItem baseCrs = crsRepository.findByIdentifier(baseCrsCode);
//			if (baseCrs != null) {
//				((SingleCoordinateReferenceSystemItem<DatumItem>)crs).setBaseCrs((SingleCoordinateReferenceSystemItem<DatumItem>)baseCrs);
//			}
//		}
//
//		Integer horizontalCrsCode = (Integer)row.get(CMPD_HORIZCRS_CODE);
//		if (horizontalCrsCode != null) {
//			if (crs == null) {
//				crs = crsRepository.findByIdentifier(crsCode);
//			}
//			
//			CoordinateReferenceSystemItem horizontalCrs = crsRepository.findByIdentifier(horizontalCrsCode);
//			if (horizontalCrs == null) {
//				((CompoundCoordinateReferenceSystemItem)crs).addComponentReferenceSystem((SingleCoordinateReferenceSystemItem<? extends DatumItem>)horizontalCrs);
//			}
//		}
//		
//		Integer verticalCrsCode = (Integer)row.get(CMPD_VERTCRS_CODE);
//		if (horizontalCrsCode != null) {
//			if (crs == null) {
//				crs = crsRepository.findByIdentifier(crsCode);
//			}
//			
//			CoordinateReferenceSystemItem verticalCrs = crsRepository.findByIdentifier(verticalCrsCode);
//			if (verticalCrs == null) {
//				((CompoundCoordinateReferenceSystemItem)crs).addComponentReferenceSystem((SingleCoordinateReferenceSystemItem<? extends DatumItem>)verticalCrs);
//			}
//		}

		if (crs != null) {
			crsRepository.save(crs);
		}
	}
	
	@Override
	public boolean mustFixReferences() {
		return true; 
	}

	@Override
	@Transactional
	protected void clearAway() {
		crsRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return COORD_REF_SYS_CODE;
	}
}
