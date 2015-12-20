package de.bespire.registry.iso.importer;

import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.iso19103.MeasureType;
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
public class UnitsOfMeasurementImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(UnitsOfMeasurementImporter.class);

	public static final String UOM_CODE = "UOM_CODE";
	public static final String UNIT_OF_MEAS_NAME = "UNIT_OF_MEAS_NAME";
	public static final String UNIT_OF_MEAS_TYPE = "UNIT_OF_MEAS_TYPE";
	public static final String TARGET_UOM_CODE = "TARGET_UOM_CODE";
	public static final String FACTOR_B = "FACTOR_B";
	public static final String FACTOR_C = "FACTOR_C";

	@Autowired
	private UnitOfMeasureItemRepository uomRepository;
	
	private RE_ItemClass itemClass;
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws UnauthorizedException {
		UnitOfMeasureItemProposalDTO proposal = new UnitOfMeasureItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);

		Integer epsgCode = (Integer)row.get(UOM_CODE);
//		Integer identifier = determineIdentifier("UnitOfMeasure", epsgCode);
		
//		proposal.setCode((Integer)row.get(UOM_CODE));
		proposal.setName((String)row.get(UNIT_OF_MEAS_NAME));
		
		String typeName = (String)row.get(UNIT_OF_MEAS_TYPE);
		MeasureType type = MeasureType.valueOf(typeName.toUpperCase());
		proposal.setMeasureType(type);
		
		proposal.setDescription(proposal.getName());
		
		Integer targetUomCode = (Integer)row.get(TARGET_UOM_CODE);
		if (!targetUomCode.equals(epsgCode)) {
//			UnitOfMeasureItem standardUnitItem = uomRepository.findByIdentifier(targetUomCode);
//			UnitOfMeasureItemProposalDTO standardUnit = new UnitOfMeasureItemProposalDTO(standardUnitItem);
//			proposal.setStandardUnit(standardUnit);
			proposal.setOffsetToStandardUnit(0.0);
			proposal.setScaleToStandardUnitNumerator((Double)row.get(FACTOR_B));
			proposal.setScaleToStandardUnitDenominator((Double)row.get(FACTOR_C));
		}
		
		proposal.setRemarks((String)row.get(REMARKS));
		addInformationSource(proposal, (String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		logger.info(">> Imported '{}'...", proposal.getName());
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent);
			
			addMapping(ai.getItem().getItemClass().getName(), epsgCode, ai.getItem().getUuid());
		}
		catch (InvalidProposalException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		if (this.itemClass == null) {
			itemClass = itemClassRepository.findByName("UnitOfMeasure");
			if (itemClass == null) {
				this.addItemClass("UnitOfMeasure", register);
				itemClass = itemClassRepository.findByName("UnitOfMeasure");
			}
		}
	
		return this.itemClass;
	}
	
	@Override
	public boolean mustFixReferences() {
		return true; 
	}

	@Override
	@Transactional
	protected void fixReference(Row row, RE_SubmittingOrganization sponsor, RE_Register register) {
		Integer uomCode = (Integer)row.get(UOM_CODE);
		logger.info("Fixing references for UoM #{}...", uomCode);
		
		UnitOfMeasureItem uom = null;

		Integer targetUomCode = (Integer)row.get(TARGET_UOM_CODE);
		if (targetUomCode != null) {
			uom = findMappedEntity("UnitOfMeasure", uomCode, UnitOfMeasureItem.class);
			
			UnitOfMeasureItem targetUom = findMappedEntity("UnitOfMeasure", targetUomCode, UnitOfMeasureItem.class);
			if (targetUom != null) {
				uom.setStandardUnit(targetUom);
			}
		}

		if (uom != null) {
			uomRepository.save(uom);
		}
	}

	@Override
	@Transactional
	protected void clearAway() {
		uomRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return UOM_CODE;
	}

}
