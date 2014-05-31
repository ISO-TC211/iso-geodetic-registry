package de.bespire.registry.iso.importer;

import java.math.BigInteger;

import org.iso.registry.api.registry.registers.gcp.NamingSystemItemProposalDTO;
import org.iso.registry.core.model.NamingSystemItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class NamingSystemsImporter extends AbstractImporter
{
	public static final Logger logger = LoggerFactory.getLogger(NamingSystemsImporter.class);
	
	public static final String NAMING_SYSTEM_CODE = "NAMING_SYSTEM_CODE";
	public static final String NAMING_SYSTEM_NAME = "NAMING_SYSTEM_NAME";

	@Autowired
	private NamingSystemItemRepository namingSystemRepository;
	
	private RE_ItemClass itemClass;

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) {
		NamingSystemItemProposalDTO proposal = new NamingSystemItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		proposal.setCode((Integer)row.get(NAMING_SYSTEM_CODE));
		proposal.setName((String)row.get(NAMING_SYSTEM_NAME));
		proposal.setDescription(proposal.getName());
		
		proposal.setRemarks((String)row.get(REMARKS));
		proposal.setInformationSource((String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		logger.info(">> Imported '{}'...", proposal.getName());
		
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
			itemClass = itemClassRepository.findByName("NamingSystem");
			if (itemClass == null) {
				this.addItemClass("NamingSystem", register);
				itemClass = itemClassRepository.findByName("NamingSystem");
			}
		}
	
		return this.itemClass;
	}

	@Override
	@Transactional
	protected void clearAway() {
		namingSystemRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return NAMING_SYSTEM_CODE;
	}

	
}
