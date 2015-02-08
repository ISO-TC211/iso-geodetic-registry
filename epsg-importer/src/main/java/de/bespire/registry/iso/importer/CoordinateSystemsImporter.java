package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemAxisItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemProposalDTO.CoordinateSystemType;
import org.iso.registry.api.registry.registers.gcp.operation.AxisDTO;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItemRepository;
import org.iso.registry.core.model.cs.CoordinateSystemItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class CoordinateSystemsImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(CoordinateSystemsImporter.class);

	public static final String COORD_SYS_CODE = "COORD_SYS_CODE";
	public static final String COORD_SYS_NAME = "COORD_SYS_NAME";
	public static final String COORD_SYS_TYPE = "COORD_SYS_TYPE";
	public static final String DIMENSION = "DIMENSION";

	@Autowired
	private CoordinateSystemAxisItemRepository axisRepository;
	
	@Autowired
	private CoordinateSystemItemRepository csRepository;
	
	private Table axisTable;
	private RE_ItemClass icSpherical;
	private RE_ItemClass icCartesian;
	private RE_ItemClass icEllipsoidal;
	private RE_ItemClass icVertical;

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException {
		CoordinateSystemItemProposalDTO proposal = new CoordinateSystemItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);

		// Add axes
		Integer epsgCode = (Integer)row.get(COORD_SYS_CODE);
		proposal.setIdentifier(determineIdentifier("CoordinateSystem", epsgCode));
		
		Cursor cursor = axisTable.getDefaultCursor();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(COORD_SYS_CODE, epsgCode);
		if (cursor.findFirstRow(m)) {
			do {
				Row axisRow = cursor.getCurrentRow();
				Integer axisCode = (Integer)axisRow.get(CoordinateSystemAxesImporter.COORD_AXIS_CODE);
				CoordinateSystemAxisItem axis = axisRepository.findByIdentifier(findMappedCode("CoordinateSystemAxis", axisCode));
				if (axis == null) {
					logger.error("!!! Missing axis #{}", axisCode.toString());
				}
				
				proposal.addAxis(new AxisDTO(axis));
			} while (cursor.findNextRow(m));
		}
		
		proposal.setName((String)row.get(COORD_SYS_NAME));
		
		String csTypeName = (String)row.get(COORD_SYS_TYPE);
		CoordinateSystemType csType = CoordinateSystemType.valueOf(csTypeName.toUpperCase());
		proposal.setType(csType);
		
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
			logger.error("!! Failed to imported CS #{}...", proposal.getIdentifier());
			logger.error(e.getMessage(), e);
		}

	}

	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, String csType) {
		if (csType.equalsIgnoreCase("ELLIPSOIDAL")) {
			if (icEllipsoidal == null) {
				icEllipsoidal = itemClassRepository.findByName("EllipsoidalCS");
				if (icEllipsoidal == null) {
					this.addItemClass("EllipsoidalCS", register);
					icEllipsoidal = itemClassRepository.findByName("EllipsoidalCS");
				}
			}

			return icEllipsoidal;
		}
		else if (csType.equalsIgnoreCase("SPHERICAL")) {
			if (icSpherical == null) {
				icSpherical = itemClassRepository.findByName("SphericalCS");
				if (icSpherical == null) {
					this.addItemClass("SphericalCS", register);
					icSpherical = itemClassRepository.findByName("SphericalCS");
				}
			}

			return icSpherical;
		}
		else if (csType.equalsIgnoreCase("CARTESIAN")) {
			if (icCartesian == null) {
				icCartesian = itemClassRepository.findByName("CartesianCS");
				if (icCartesian == null) {
					this.addItemClass("CartesianCS", register);
					icCartesian = itemClassRepository.findByName("CartesianCS");
				}
			}

			return icCartesian;
		}
		else if (csType.equalsIgnoreCase("VERTICAL")) {
			if (icVertical == null) {
				icVertical = itemClassRepository.findByName("VerticalCS");
				if (icVertical == null) {
					this.addItemClass("VerticalCS", register);
					icVertical = itemClassRepository.findByName("VerticalCS");
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
		String csType = (String)row.get(COORD_SYS_TYPE);
	
		return getOrCreateItemClass(register, csType);
	}
	
	public void setAxisTable(Table table) {
		this.axisTable = table;
	}

	@Override
	@Transactional
	protected void clearAway() {
		csRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return COORD_SYS_CODE;
	}

}
