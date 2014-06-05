package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.iso.registry.api.registry.registers.gcp.operation.OperationMethodItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.OperationMethodItemProposalDTO.FormulaType;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.OperationParameterItemRepository;
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
public class OperationMethodsImporter extends AbstractImporter
{
	public static final Logger logger = LoggerFactory.getLogger(OperationMethodsImporter.class);
	
	public static final String COORD_OP_METHOD_CODE = "COORD_OP_METHOD_CODE";
	public static final String COORD_OP_METHOD_NAME = "COORD_OP_METHOD_NAME";
	public static final String REVERSE_OP = "REVERSE_OP";
	public static final String FORMULA = "FORMULA";
	public static final String EXAMPLE = "EXAMPLE";
	
	@Autowired
	private OperationParameterItemRepository paramRepository;
	
	private Table parametersUsageTable;

	private RE_ItemClass itemClass;

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		OperationMethodItemProposalDTO proposal = new OperationMethodItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());
		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		Integer methodCode = (Integer)row.get(COORD_OP_METHOD_CODE); 
		proposal.setCode(methodCode);
		proposal.setName((String)row.get(COORD_OP_METHOD_NAME));
		proposal.setReversible((Boolean)row.get(REVERSE_OP));
		proposal.setFormulaType(FormulaType.FORMULA);
		proposal.setFormula((String)row.get(FORMULA));
		proposal.setDescription((String)row.get(EXAMPLE));
		
		List<GeneralOperationParameterItem> parameters = CoordinateOperationsImporter.findParameters(getParametersUsageTable(), paramRepository, methodCode);
		proposal.setParameters(parameters);

		proposal.setRemarks((String)row.get(REMARKS));
		proposal.setInformationSource((String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		logger.info(">> Imported method '{}'...", proposal.getName());
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent, BigInteger.valueOf(proposal.getCode().longValue()));
		}
		catch (InvalidProposalException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		if (this.itemClass == null) {
			itemClass = itemClassRepository.findByName("OperationMethod");
			if (itemClass == null) {
				this.addItemClass("OperationMethod", register);
				itemClass = itemClassRepository.findByName("OperationMethod");
			}
		}
	
		return this.itemClass;
	}

	@Override
	@Transactional
	protected void clearAway() {
	}

	@Override
	protected String codeProperty() {
		return COORD_OP_METHOD_CODE;
	}

	/**
	 * @return the parametersUsageTable
	 */
	public Table getParametersUsageTable() {
		return parametersUsageTable;
	}

	/**
	 * @param parametersUsageTable the parametersUsageTable to set
	 */
	public void setParametersUsageTable(Table parametersUsageTable) {
		this.parametersUsageTable = parametersUsageTable;
	}

}
