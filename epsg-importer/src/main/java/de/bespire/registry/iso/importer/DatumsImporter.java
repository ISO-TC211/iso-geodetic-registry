package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.crs.AreaItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.DatumItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.EllipsoidItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.PrimeMeridianItemProposalDTO;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.datum.EllipsoidItemRepository;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.iso.registry.core.model.datum.PrimeMeridianItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class DatumsImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(DatumsImporter.class);

	public static final String DATUM_CODE = "DATUM_CODE";
	public static final String DATUM_NAME = "DATUM_NAME";
	public static final String DATUM_TYPE = "DATUM_TYPE";
	public static final String ORIGIN_DESCRIPTION = "ORIGIN_DESCRIPTION";
	public static final String REALIZATION_EPOCH = "REALIZATION_EPOCH";
	public static final String ELLIPSOID_CODE = "ELLIPSOID_CODE";
	public static final String PRIME_MERIDIAN_CODE = "PRIME_MERIDIAN_CODE";
	public static final String AREA_OF_USE_CODE = "AREA_OF_USE_CODE";
	public static final String DATUM_SCOPE = "DATUM_SCOPE";

	@Autowired
	private EllipsoidItemRepository ellipsoidRepository;
	
	@Autowired
	private PrimeMeridianItemRepository pmRepository;
	
	@Autowired
	private AreaItemRepository areaRepository;
	
	@Autowired
	private DatumItemRepository datumRepository;
	
	private RE_ItemClass icGeodetic;

	private RE_ItemClass icVertical;

	private RE_ItemClass icEngineering;

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		DatumItemProposalDTO proposal = new DatumItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		proposal.setIdentifier((Integer)row.get(DATUM_CODE));
		proposal.setName((String)row.get(DATUM_NAME));
		proposal.setAnchorDefinition((String)row.get(ORIGIN_DESCRIPTION));
		proposal.setScope((String)row.get(DATUM_SCOPE));
		
		String epochText = (String)row.get(REALIZATION_EPOCH);
		if (!StringUtils.isEmpty(epochText)) {
			DateFormat df = new SimpleDateFormat("yyyy");
			Date epoch;
			try {
				epoch = df.parse(epochText);
			}
			catch (ParseException e1) {
				logger.error(e1.getMessage(), e1);
				return;
			}
			proposal.setRealizationEpoch(epoch);
		}
		
		Integer elCode = (Integer)row.get(ELLIPSOID_CODE);
		Integer pmCode = (Integer)row.get(PRIME_MERIDIAN_CODE);
		Integer areaCode = (Integer)row.get(AREA_OF_USE_CODE);

		EllipsoidItem ellipsoid = ellipsoidRepository.findByIdentifier(elCode);
		PrimeMeridianItem primeMeridian = pmRepository.findByIdentifier(pmCode);
		AreaItem area = areaRepository.findByIdentifier(areaCode);

		proposal.setEllipsoid(new EllipsoidItemProposalDTO(ellipsoid));
		proposal.setPrimeMeridian(new PrimeMeridianItemProposalDTO(primeMeridian));
		if (area != null) {
			ExtentDTO extent = new ExtentDTO();
			extent.getGeographicBoundingBoxes().add(area.getBoundingBox());
			extent.setDescription(area.getName());
			proposal.setDomainOfValidity(extent);
		}
		proposal.setRemarks((String)row.get(REMARKS));
		proposal.setInformationSource((String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent, BigInteger.valueOf(proposal.getIdentifier().longValue()));

			logger.info(">> Imported '{}'...", proposal.getName());
		}
		catch (InvalidProposalException e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, String type) {
		if (type.equalsIgnoreCase("GEODETIC")) {
			if (icGeodetic == null) {
				icGeodetic = itemClassRepository.findByName("GeodeticDatum");
				if (icGeodetic == null) {
					this.addItemClass("GeodeticDatum", register);
					icGeodetic = itemClassRepository.findByName("GeodeticDatum");
				}
			}

			return icGeodetic;
		}
		else if (type.equalsIgnoreCase("ENGINEERING")) {
			if (icEngineering == null) {
				icEngineering = itemClassRepository.findByName("EngineeringDatum");
				if (icEngineering == null) {
					this.addItemClass("EngineeringDatum", register);
					icEngineering = itemClassRepository.findByName("EngineeringDatum");
				}
			}

			return icEngineering;
		}
		else if (type.equalsIgnoreCase("VERTICAL")) {
			if (icVertical == null) {
				icVertical = itemClassRepository.findByName("VerticalDatum");
				if (icVertical == null) {
					this.addItemClass("VerticalDatum", register);
					icVertical = itemClassRepository.findByName("VerticalDatum");
				}
			}

			return icVertical;
		}
		else {
			return null;
		}		
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		String type = (String)row.get(DATUM_TYPE);
		
		return getOrCreateItemClass(register, type);
	}

	@Override
	@Transactional
	protected void clearAway() {
		datumRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return DATUM_CODE;
	}

}
