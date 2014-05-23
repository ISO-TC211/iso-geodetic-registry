package de.geoinfoffm.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.SubmittingOrganizationRepository;

public abstract class AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractImporter.class);

	public static final String REMARKS = "REMARKS";
	public static final String INFORMATION_SOURCE = "INFORMATION_SOURCE";
	public static final String DATA_SOURCE = "DATA_SOURCE";
	public static final String REVISION_DATE = "REVISION_DATE";
	public static final String CHANGE_ID = "CHANGE_ID";
	public static final String DEPRECATED = "DEPRECATED";
	
	@PersistenceContext
	protected EntityManager em;

	@Autowired
	protected ItemClassRepository itemClassRepository;

	@Autowired
	protected ProposalService proposalService;

	@Autowired
	private RegisterItemService itemService;

	@Autowired
	protected SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private RegisterRepository registerRepository;

	protected void acceptProposal(Addition ai, String decisionEvent, BigInteger itemIdentifier)
			throws InvalidProposalException {
		try {
			if (itemIdentifier != null) {
				ai.getItem().setItemIdentifier(itemIdentifier);
				itemService.saveRegisterItem(ai.getItem());
			}
			proposalService.reviewProposal(ai);
			proposalService.acceptProposal(ai, decisionEvent);
		}
		catch (IllegalOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected RE_ItemClass addItemClass(String name, RE_Register r) {
		RE_ItemClass ic = null;
		for (RE_ItemClass itemClass : r.getContainedItemClasses()) {
			if (itemClass.getName().equals(name)) {
				ic = itemClass;
				break;
			}
		}

		if (ic == null) {
			logger.info("> Adding item class '{}' to register '{}'...\n", name, r.getName());
			ic = new RE_ItemClass();
			ic.setName(name);
			ic.addRegister(r);
			ic = itemClassRepository.save(ic);
		}

		return ic;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void importRows(Cursor cursor, long count, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		for (int i = 0; i < count; i++) {
			Row row = cursor.getNextRow(); 
			if (row != null) {
				RE_ItemClass itemClass = this.getOrCreateItemClass(register, row); 
				this.importRow(row, itemClass, sponsor, register);
			}
			else {
				return;
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void fixReferences(Cursor cursor, long count) throws IOException {
		for (int i = 0; i < count; i++) {
			Row row = cursor.getNextRow(); 
			if (row != null) {
				this.fixReference(row);
			}
			else {
				return;
			}
		}
	}
	
	protected void fixReference(Row row) {
		// Do nothing, must override
	}

	protected abstract void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException;
	protected abstract void clearAway();

	public abstract RE_ItemClass getOrCreateItemClass(RE_Register register, Row row);
	
	public boolean mustFixReferences() {
		return false;
	}
}
