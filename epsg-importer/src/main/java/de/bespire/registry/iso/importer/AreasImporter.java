package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

import org.iso.registry.api.registry.registers.gcp.crs.AreaItemProposalDTO;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class AreasImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(AreasImporter.class);
	
	public static final String AREA_CODE = "AREA_CODE";
	public static final String AREA_NAME = "AREA_NAME";
	public static final String AREA_OF_USE = "AREA_OF_USE";
	public static final String AREA_SOUTH_BOUND_LAT = "AREA_SOUTH_BOUND_LAT";
	public static final String AREA_NORTH_BOUND_LAT = "AREA_NORTH_BOUND_LAT";
	public static final String AREA_WEST_BOUND_LON = "AREA_WEST_BOUND_LON";
	public static final String AREA_EAST_BOUND_LON = "AREA_EAST_BOUND_LON";
	public static final String ISO_A2_CODE = "ISO_A2_CODE";
	public static final String ISO_A3_CODE = "ISO_A3_CODE";
	public static final String ISO_N_CODE = "ISO_N_CODE";

	@Autowired
	private AreaItemRepository areaRepository;

	private RE_ItemClass itemClass;
	
	public AreasImporter() { }
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws UnauthorizedException {
		AreaItemProposalDTO proposal = new AreaItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
//		proposal.setIdentifier((Integer)row.get(AREA_CODE));
		proposal.setName((String)row.get(AREA_NAME));
		proposal.setDescription((String)row.get(AREA_OF_USE));
		
		if ((Double)row.get(AREA_SOUTH_BOUND_LAT) == null
		    || (Double)row.get(AREA_NORTH_BOUND_LAT) == null
		    || (Double)row.get(AREA_WEST_BOUND_LON) == null
		    || (Double)row.get(AREA_EAST_BOUND_LON) == null) {
		    	logger.error("Cannot create EX_GeographicBoundingBox [e = {}, n = {}, s = {}, w = {}]", 
		    			new Object[] { (Double)row.get(AREA_EAST_BOUND_LON),
		    						   (Double)row.get(AREA_NORTH_BOUND_LAT),
		    						   (Double)row.get(AREA_SOUTH_BOUND_LAT),
		    						   (Double)row.get(AREA_WEST_BOUND_LON)
		    			});
		    }
		else {
			proposal.setSouthBoundLatitude((Double)row.get(AREA_SOUTH_BOUND_LAT));
			proposal.setNorthBoundLatitude((Double)row.get(AREA_NORTH_BOUND_LAT));
			proposal.setWestBoundLongitude((Double)row.get(AREA_WEST_BOUND_LON));
			proposal.setEastBoundLongitude((Double)row.get(AREA_EAST_BOUND_LON));
		}
		proposal.setIsoA2Code((String)row.get(ISO_A2_CODE));
		proposal.setIsoA3Code((String)row.get(ISO_A3_CODE));
		proposal.setIsoNCode((Integer)row.get(ISO_N_CODE));
		proposal.setRemarks((String)row.get(REMARKS));
		proposal.setInformationSource((String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent);

			logger.info(">> Imported '{}'...", proposal.getName());
		}
		catch (InvalidProposalException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		if (this.itemClass == null) {
			itemClass = itemClassRepository.findByName("Area");
			if (itemClass == null) {
				this.addItemClass("Area", register);
				itemClass = itemClassRepository.findByName("Area");
			}
		}
	
		return this.itemClass;
	}
	
	@Override
	@Transactional
	protected void clearAway() {
		areaRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return AREA_CODE;
	}

}
