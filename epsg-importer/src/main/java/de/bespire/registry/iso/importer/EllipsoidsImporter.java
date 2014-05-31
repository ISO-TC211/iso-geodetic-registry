package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;

import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.AreaItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.datum.EllipsoidItemProposalDTO;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.datum.EllipsoidItemRepository;
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
public class EllipsoidsImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(EllipsoidsImporter.class);

	public static final String ELLIPSOID_CODE = "ELLIPSOID_CODE";
	public static final String ELLIPSOID_NAME = "ELLIPSOID_NAME";
	public static final String SEMI_MAJOR_AXIS = "SEMI_MAJOR_AXIS";
	public static final String UOM_CODE = "UOM_CODE";
	public static final String INV_FLATTENING = "INV_FLATTENING";
	public static final String SEMI_MINOR_AXIS = "SEMI_MINOR_AXIS";
	public static final String ELLIPSOID_SHAPE = "ELLIPSOID_SHAPE";
	
	@Autowired
	private UnitOfMeasureItemRepository uomRepository;
	
	@Autowired
	private EllipsoidItemRepository ellipsoidRepository;

	private RE_ItemClass itemClass;

	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		EllipsoidItemProposalDTO proposal = new EllipsoidItemProposalDTO();
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		proposal.setCode((Integer)row.get(ELLIPSOID_CODE));
		proposal.setName((String)row.get(ELLIPSOID_NAME));
		proposal.setSemiMajorAxis((Double)row.get(SEMI_MAJOR_AXIS));

		Integer uomCode = (Integer)row.get(UOM_CODE);
		UnitOfMeasureItem uom = uomRepository.findByCode(uomCode);
		proposal.setSemiMajorAxisUom(new UnitOfMeasureItemProposalDTO(uom));
		
		if ((Double)row.get(SEMI_MINOR_AXIS) != null) {
			proposal.setSemiMinorAxis((Double)row.get(SEMI_MINOR_AXIS));
			proposal.setSemiMinorAxisUom(new UnitOfMeasureItemProposalDTO(uom));
		}
		else {
			proposal.setInverseFlattening((Double)row.get(INV_FLATTENING));
			proposal.setInverseFlatteningUom(new UnitOfMeasureItemProposalDTO(uom));
		}

		proposal.setSphere(!((Boolean)row.get(ELLIPSOID_SHAPE)).booleanValue());

		proposal.setRemarks((String)row.get(REMARKS));
		proposal.setInformationSource((String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
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
			itemClass = itemClassRepository.findByName("Ellipsoid");
			if (itemClass == null) {
				this.addItemClass("Ellipsoid", register);
				itemClass = itemClassRepository.findByName("Ellipsoid");
			}
		}
	
		return this.itemClass;
	}

	@Override
	@Transactional
	protected void clearAway() {
		ellipsoidRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return ELLIPSOID_CODE;
	}

}
