package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;

import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.EllipsoidItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.PrimeMeridianItemProposalDTO;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.datum.PrimeMeridianItemRepository;
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
public class PrimeMeridiansImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(PrimeMeridiansImporter.class);

	public static final String PRIME_MERIDIAN_CODE = "PRIME_MERIDIAN_CODE";
	public static final String PRIME_MERIDIAN_NAME = "PRIME_MERIDIAN_NAME";
	public static final String GREENWICH_LONGITUDE = "GREENWICH_LONGITUDE";
	public static final String UOM_CODE = "UOM_CODE";
	
	@Autowired
	private UnitOfMeasureItemRepository uomRepository;
	
	@Autowired
	private PrimeMeridianItemRepository pmRepository;

	private RE_ItemClass itemClass;

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException {
		PrimeMeridianItemProposalDTO proposal = new PrimeMeridianItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		Integer epsgCode = (Integer)row.get(PRIME_MERIDIAN_CODE);
//		proposal.setIdentifier(determineIdentifier("Ellipsoid", epsgCode));

		proposal.setName((String)row.get(PRIME_MERIDIAN_NAME));
		proposal.setGreenwichLongitude((Double)row.get(GREENWICH_LONGITUDE));

		Integer uomCode = (Integer)row.get(UOM_CODE);
		UnitOfMeasureItem uom = uomRepository.findByIdentifier(findMappedCode("UnitOfMeasurement", uomCode));
		proposal.setGreenwichLongitudeUom(new UnitOfMeasureItemProposalDTO(uom));

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
			itemClass = itemClassRepository.findByName("PrimeMeridian");
			if (itemClass == null) {
				this.addItemClass("PrimeMeridian", register);
				itemClass = itemClassRepository.findByName("PrimeMeridian");
			}
		}
	
		return this.itemClass;
	}

	@Override
	@Transactional
	protected void clearAway() {
		pmRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return PRIME_MERIDIAN_CODE;
	}

}
