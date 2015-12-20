package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.crs.CoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.CoordinateReferenceSystemItemProposalDTO.CoordinateReferenceSystemType;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.DatumItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.SingleOperationItemProposalDTO;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItemRepository;
import org.iso.registry.core.model.crs.GeneralDerivedCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemItemRepository;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.iso.registry.core.model.operation.ConversionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.impl.CursorImpl;

import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class CoordinateReferenceSystemsImporter extends AbstractImporter
{
	public static enum Mode {
		SINGLE,
		DERIVED_PROJECTED,
		COMPOUND
	}
	
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
	
	private Mode mode;
	
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException {
		CoordinateReferenceSystemItemProposalDTO proposal = new CoordinateReferenceSystemItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		Integer crsCode = (Integer)row.get(COORD_REF_SYS_CODE);
		
		logger.info(">> Starting import of CRS #{}", crsCode);
		
//		proposal.setIdentifier(crsCode);
		proposal.setName((String)row.get(COORD_REF_SYS_NAME));
		proposal.setScope((String)row.get(CRS_SCOPE));
		
		String crsType = (String)row.get(COORD_REF_SYS_KIND);
		if (crsType.equalsIgnoreCase("GEOGRAPHIC 2D") || crsType.equalsIgnoreCase("GEOGRAPHIC 3D") || crsType.equalsIgnoreCase("GEOCENTRIC")) {
			if (Mode.COMPOUND.equals(this.getMode())) {
				logger.error(">> Skipping non-compound CRS {}", crsCode);
				return;
			}
			proposal.setType(CoordinateReferenceSystemType.GEODETIC);
		}
		else if (crsType.equalsIgnoreCase("ENGINEERING")) {			
			if (Mode.COMPOUND.equals(this.getMode())) {
				logger.error(">> Skipping non-compound CRS {}", crsCode);
				return;
			}
			proposal.setType(CoordinateReferenceSystemType.ENGINEERING);
		}
		else if (crsType.equalsIgnoreCase("COMPOUND")) {			
			if (!Mode.COMPOUND.equals(this.getMode())) {
				logger.error(">> Skipping compound CRS {}", crsCode);
				return;
			}
			proposal.setType(CoordinateReferenceSystemType.COMPOUND);
		}
		else if (crsType.equalsIgnoreCase("PROJECTED")) {			
			if (!Mode.DERIVED_PROJECTED.equals(this.getMode())) {
				logger.error(">> Skipping projected CRS {}", crsCode);
				return;
			}
			proposal.setType(CoordinateReferenceSystemType.PROJECTED);
		}
		else if (crsType.equalsIgnoreCase("VERTICAL")) {			
			if (Mode.COMPOUND.equals(this.getMode())) {
				logger.error(">> Skipping non-compound CRS {}", crsCode);
				return;
			}
			proposal.setType(CoordinateReferenceSystemType.VERTICAL);
		}
		else {
			throw new RuntimeException(String.format("Invalid CRS type: %s", crsType));
		}
		
		Integer horizontalCrsCode = (Integer)row.get(CMPD_HORIZCRS_CODE);
		if (horizontalCrsCode != null) {
			CoordinateReferenceSystemItem crs = findMappedEntity("*CRS", horizontalCrsCode, CoordinateReferenceSystemItem.class);
			proposal.setHorizontalCrs(new CoordinateReferenceSystemItemProposalDTO(crs));
		}

		Integer verticalCrsCode = (Integer)row.get(CMPD_VERTCRS_CODE);
		if (verticalCrsCode != null) {
			CoordinateReferenceSystemItem crs = findMappedEntity("*CRS", verticalCrsCode, CoordinateReferenceSystemItem.class);
			proposal.setVerticalCrs(new CoordinateReferenceSystemItemProposalDTO(crs));			
		}

		Integer areaCode = (Integer)row.get(AREA_OF_USE_CODE);
		if (areaCode != null) {
			AreaItem area = findMappedEntity("Area", areaCode, AreaItem.class);
			if (area != null) {
				ExtentDTO extent = new ExtentDTO();
				extent.getGeographicBoundingBoxes().add(area.getBoundingBox());
				extent.setDescription(area.getName());
				proposal.setDomainOfValidity(extent);
			}
			else {
				logger.error(">>> CRS #{} referenced invalid Area #{}", crsCode, areaCode);
			}
		}
			
		Integer csCode = (Integer)row.get(COORD_SYS_CODE);
		if (csCode != null) {
			CoordinateSystemItem cs = findMappedEntity("*CS", csCode, CoordinateSystemItem.class);
			if (cs != null) {
				proposal.setCoordinateSystem(new CoordinateSystemItemProposalDTO(cs));
			}
			else {
				logger.error(">>> CRS #{} referenced non-existent CS #{}", crsCode, csCode);
			}
		}
		
		Integer datumCode = (Integer)row.get(DATUM_CODE);
		if (datumCode != null) {
			DatumItem datum;
			datum = findMappedEntity("*Datum", datumCode, DatumItem.class);
			if (datum != null) {
				proposal.setDatum(new DatumItemProposalDTO(datum));
			}
			else {
				logger.error(">>> CRS #{} referenced non-existent Datum #{}", crsCode, datumCode);				
			}
		}		

		Integer baseCrsCode = (Integer)row.get(SOURCE_GEOGCRS_CODE);
		if (baseCrsCode != null) {
			if (!Mode.DERIVED_PROJECTED.equals(this.getMode())) {
				logger.error(">> Skipping derived/projected CRS {}", crsCode);
				return;
			}
			CoordinateReferenceSystemItem baseCrs = findMappedEntity("*CRS", baseCrsCode, CoordinateReferenceSystemItem.class);
			if (baseCrs != null) {
				proposal.setBaseCrs(new CoordinateReferenceSystemItemProposalDTO(baseCrs));
			}
			else {
				logger.error(">>> CRS #{} referenced non-existent base CRS #{}", crsCode, baseCrsCode);				
			}
		}
		else {
			if (Mode.DERIVED_PROJECTED.equals(this.getMode())) {
				logger.error(">> Skipping non-derived/non-projected CRS {}", crsCode);
				return;
			}			
		}
		
		Integer conversionCode = (Integer)row.get(PROJECTION_CONV_CODE);
		if (conversionCode != null) {
			if (!Mode.DERIVED_PROJECTED.equals(this.getMode())) {
				logger.error(">> Skipping derived/projected CRS {}", crsCode);
				return;
			}
			ConversionItem conversion = findMappedEntity("Conversion", conversionCode, ConversionItem.class);
			if (conversion != null) {
				SingleOperationItemProposalDTO dto = new SingleOperationItemProposalDTO(conversion);
				proposal.setOperation(dto);
			}
			else {
				logger.info(">>> references (yet) unknown conversion {}", conversionCode);
			}
		}
		else {
			if (Mode.DERIVED_PROJECTED.equals(this.getMode())) {
				logger.error(">> Skipping non-derived/non-projected CRS {}", crsCode);
				return;
			}			
		}

		proposal.setRemarks((String)row.get(REMARKS));
		addInformationSource(proposal, (String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent);

			logger.info(">> Imported {} CRS #{} ('{}')...", new Object[] { crsType, crsCode, proposal.getName() });
			
			addMapping(ai.getItem().getItemClass().getName(), crsCode, ai.getItem().getUuid());
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
		
		if (!"COMPOUND".equalsIgnoreCase(crsType) && !"PROJECTED".equalsIgnoreCase(crsType)) {
			
		}
		
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
		
		CoordinateReferenceSystemItem crs = findMappedEntity("*CRS", crsCode,CoordinateReferenceSystemItem.class);
		if (crs == null) {
			logger.info("Skipping unknown CRS #{}", crsCode);
			return;
		}
		switch (this.getMode()) {
			case SINGLE:
				if (crs instanceof GeneralDerivedCoordinateReferenceSystemItem || crs instanceof CompoundCoordinateReferenceSystemItem) {
					logger.info("Skipping CRS #{} (wrong mode)", crsCode);
					return;
				}
				break;
			case DERIVED_PROJECTED:
				if (crs instanceof SingleCoordinateReferenceSystemItem || crs instanceof CompoundCoordinateReferenceSystemItem) {
					logger.info("Skipping CRS #{} (wrong mode)", crsCode);
					return;
				}
				break;
			case COMPOUND:
				if (crs instanceof GeneralDerivedCoordinateReferenceSystemItem || crs instanceof SingleCoordinateReferenceSystemItem) {
					logger.info("Skipping CRS #{} (wrong mode)", crsCode);
					return;
				}
				break;
		}

		logger.info("Fixing references for CRS #{}...", crsCode);

		Integer csCode = (Integer)row.get(COORD_SYS_CODE);
		if (csCode != null) {
			logger.info(">>> data references CS {}", csCode);
			crs = findMappedEntity("*CRS", crsCode, CoordinateReferenceSystemItem.class);
			
			CoordinateSystemItem cs = findMappedEntity("*CS", csCode, CoordinateSystemItem.class);
			if (cs != null) {
				((SingleCoordinateReferenceSystemItem<DatumItem>)crs).setCoordinateSystem(cs);
				logger.info(">>> fixed reference to CS {}", csCode);
			}
		}

		Integer datumCode = (Integer)row.get(DATUM_CODE);
		if (datumCode != null) {
			logger.info(">>> data references Datum {}", datumCode);
			crs = findMappedEntity("*CRS", crsCode, CoordinateReferenceSystemItem.class);
			
			DatumItem datum = findMappedEntity("*Datum", datumCode, DatumItem.class);
			if (datum != null) {
				((SingleCoordinateReferenceSystemItem<DatumItem>)crs).setDatum(datum);
				logger.info(">>> fixed reference to Datum {}", datumCode);
			}
			else {
				logger.error(">>> CRS #{} referenced non-existent Datum #{}", crsCode, datumCode);				
			}
		}		

		Integer baseCrsCode = (Integer)row.get(SOURCE_GEOGCRS_CODE);
		if (baseCrsCode != null) {
			logger.info(">>> data references base CRS {}", baseCrsCode);
			crs = findMappedEntity("*CRS", crsCode, CoordinateReferenceSystemItem.class);
			
			CoordinateReferenceSystemItem baseCrs = findMappedEntity("*CRS", baseCrsCode, CoordinateReferenceSystemItem.class);
			if (baseCrs != null) {
				logger.info(">>> fixed reference to base CRS {}", baseCrsCode);
				((GeneralDerivedCoordinateReferenceSystemItem<DatumItem>)crs).setBaseCrs((SingleCoordinateReferenceSystemItem<DatumItem>)baseCrs);
			}
		}
		
		Integer conversionCode = (Integer)row.get(PROJECTION_CONV_CODE);
		if (conversionCode != null) {
			logger.info(">>> data references conversion {}", conversionCode);
			if (crs == null) {
				crs = findMappedEntity("*CRS", crsCode, CoordinateReferenceSystemItem.class);
			}
			
			ConversionItem conversion = findMappedEntity("Conversion", conversionCode, ConversionItem.class);
			if (conversion == null) {
				logger.info(">>> fixed reference to conversion {}", conversion);
				((GeneralDerivedCoordinateReferenceSystemItem)crs).setOperation(conversion);
			}
			else {
				logger.info(">>> references (yet) unknown conversion {}", conversionCode);
			}
		}

		Integer horizontalCrsCode = (Integer)row.get(CMPD_HORIZCRS_CODE);
		if (horizontalCrsCode != null) {
			logger.info(">>> data references horizontal CRS {}", horizontalCrsCode);
			if (crs == null) {
				crs = findMappedEntity("*CRS", crsCode, CoordinateReferenceSystemItem.class);
			}
			
			CoordinateReferenceSystemItem horizontalCrs = findMappedEntity("*CRS", horizontalCrsCode, CoordinateReferenceSystemItem.class);
			if (horizontalCrs == null) {
				logger.info(">>> fixed reference to horizontal CRS {}", horizontalCrs);
				((CompoundCoordinateReferenceSystemItem)crs).getComponentReferenceSystem().add((SingleCoordinateReferenceSystemItem<? extends DatumItem>)horizontalCrs);
			}
		}
		
		Integer verticalCrsCode = (Integer)row.get(CMPD_VERTCRS_CODE);
		if (verticalCrsCode != null) {
			logger.info(">>> data references vertical CRS {}", verticalCrsCode);
			if (crs == null) {
				crs = findMappedEntity("*CRS", crsCode, CoordinateReferenceSystemItem.class);
			}
			
			CoordinateReferenceSystemItem verticalCrs = findMappedEntity("*CRS", crsCode, CoordinateReferenceSystemItem.class);
			if (verticalCrs == null) {
				((CompoundCoordinateReferenceSystemItem)crs).getComponentReferenceSystem().add((SingleCoordinateReferenceSystemItem<? extends DatumItem>)verticalCrs);
				logger.info(">>> fixed reference to vertical CRS {}", verticalCrs);
			}
		}

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
