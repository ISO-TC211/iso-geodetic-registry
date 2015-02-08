package de.bespire.registry.iso.importer;

import java.math.BigInteger;

import org.iso.registry.api.registry.registers.gcp.operation.OperationParameterItemProposalDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class OperationParametersImporter extends AbstractImporter
{
	public static final Logger logger = LoggerFactory.getLogger(OperationParametersImporter.class);
	
	public static final String PARAMETER_CODE = "PARAMETER_CODE";
	public static final String PARAMETER_NAME = "PARAMETER_NAME";
	public static final String DESCRIPTION = "DESCRIPTION";

	private RE_ItemClass itemClass;

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws UnauthorizedException {
		OperationParameterItemProposalDTO proposal = new OperationParameterItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());
		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		if (row == null) {
			new Object();
		}
		Integer epsgCode = (Integer)row.get(PARAMETER_CODE);
		proposal.setIdentifier(determineIdentifier("OperationParameter", epsgCode));
		
		proposal.setName((String)row.get(PARAMETER_NAME));
		proposal.setDescription((String)row.get(DESCRIPTION));
		
		proposal.setRemarks((String)row.get(REMARKS));
		proposal.setInformationSource((String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		logger.info(">> Imported parameter '{}'...", proposal.getName());
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent, BigInteger.valueOf(proposal.getIdentifier().longValue()));
		}
		catch (InvalidProposalException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		if (this.itemClass == null) {
			itemClass = itemClassRepository.findByName("OperationParameter");
			if (itemClass == null) {
				this.addItemClass("OperationParameter", register);
				itemClass = itemClassRepository.findByName("OperationParameter");
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
		return PARAMETER_CODE;
	}


}
