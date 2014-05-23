package de.geoinfoffm.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemAxisProposalDTO;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItemRepository;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class CoordinateSystemAxesImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(CoordinateSystemAxesImporter.class);

	public static final String COORD_AXIS_CODE = "COORD_AXIS_CODE";
	public static final String COORD_SYS_CODE = "COORD_SYS_CODE";
	public static final String COORD_AXIS_NAME_CODE = "COORD_AXIS_NAME_CODE";
	public static final String COORD_AXIS_ORIENTATION = "COORD_AXIS_ORIENTATION";
	public static final String COORD_AXIS_ABBREVIATION = "COORD_AXIS_ABBREVIATION";
	public static final String UOM_CODE = "UOM_CODE";
	public static final String ORDER = "ORDER";

	public static final String COORD_AXIS_NAME = "COORD_AXIS_NAME";
	public static final String DESCRIPTION = "DESCRIPTION";

	@Autowired
	private UnitOfMeasureItemRepository uomRepository;
	
	@Autowired
	private CoordinateSystemAxisItemRepository axisRepository;
	
	private Table namesTable;
	private RE_ItemClass itemClass;
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		CoordinateSystemAxisProposalDTO proposal = new CoordinateSystemAxisProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification("Import from EPSG database");
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(COORD_AXIS_NAME_CODE, (Integer)row.get(COORD_AXIS_NAME_CODE));
		if (!namesTable.getDefaultCursor().findFirstRow(m)) {
			return;
		}
		Row nameRow = namesTable.getDefaultCursor().getCurrentRow();
		
		String directionName = (String)row.get(COORD_AXIS_ORIENTATION);
		CS_AxisDirection direction = new CS_AxisDirection(directionName, "EPSG");
		proposal.setAxisDirection(direction);
		
		proposal.setAxisAbbreviation((String)row.get(COORD_AXIS_ABBREVIATION));
		
		Integer uomCode = (Integer)row.get(UOM_CODE);
		UnitOfMeasureItem uom = uomRepository.findByCode(uomCode);
		proposal.setAxisUnit(new UnitOfMeasureItemProposalDTO(uom));
		
		proposal.setCode((Integer)row.get(COORD_AXIS_CODE));
		proposal.setName((String)nameRow.get(COORD_AXIS_NAME));
		proposal.setDescription((String)nameRow.get(DESCRIPTION));
		
		proposal.setRemarks((String)nameRow.get(REMARKS));
		proposal.setInformationSource((String)nameRow.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)nameRow.get(DATA_SOURCE));
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = "Import from EPSG database";
			acceptProposal(ai, decisionEvent, BigInteger.valueOf(proposal.getCode().longValue()));

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
			itemClass = itemClassRepository.findByName("CoordinateSystemAxis");
			if (itemClass == null) {
				this.addItemClass("CoordinateSystemAxis", register);
				itemClass = itemClassRepository.findByName("CoordinateSystemAxis");
			}
		}
	
		return this.itemClass;
	}

	public Table getNamesTable() {
		return namesTable;
	}

	public void setNamesTable(Table namesTable) {
		this.namesTable = namesTable;
	}
	
	@Override
	@Transactional
	protected void clearAway() {
		axisRepository.deleteAll();
	}

}
