package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;

public abstract class AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractImporter.class);

	public static final String REMARKS = "REMARKS";
	public static final String INFORMATION_SOURCE = "INFORMATION_SOURCE";
	public static final String DATA_SOURCE = "DATA_SOURCE";
	public static final String REVISION_DATE = "REVISION_DATE";
	public static final String CHANGE_ID = "CHANGE_ID";
	public static final String DEPRECATED = "DEPRECATED";
	
	public static final String IMPORT_SOURCE = "Import from EPSG Geodetic Parameter Data Set v7.6";
	
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
	
	private Set<String> limitToCodes;

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
				if (this.limitToCodes != null && !this.limitToCodes.isEmpty() && this.codeProperty() != null) {
					Integer code = (Integer)row.get(codeProperty());
					if (!limitToCodes.contains(code.toString())) {
						continue;
					}
				}
				RE_ItemClass itemClass = this.getOrCreateItemClass(register, row);
				try {
					this.importRow(row, itemClass, sponsor, register);
				}
				catch (Throwable t) {
					logger.error(t.getMessage(), t);
					if (this.codeProperty() != null) {
						Integer code = (Integer)row.get(codeProperty());
						logger.error("!!!! Failed to import object #{} from table '{}' !!!!", code, cursor.getTable().getName());
					}
					else {
						logger.error("!!!! Failed to import object from table '{}' !!!!", cursor.getTable().getName());
						for (String key : row.keySet()) {
							logger.error("[{}] {}", key, row.get(key));
						}
					}
				}
			}
			else {
				return;
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void fixReferences(Cursor cursor, long count, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		for (int i = 0; i < count; i++) {
			Row row = cursor.getNextRow(); 
			if (row != null) {
				if (this.limitToCodes != null && !this.limitToCodes.isEmpty() && this.codeProperty() != null) {
					Integer code = (Integer)row.get(codeProperty());
					if (!limitToCodes.contains(code.toString())) {
						continue;
					}
				}
				this.fixReference(row, sponsor, register);
			}
			else {
				return;
			}
		}
	}
	
	protected void fixReference(Row row, RE_SubmittingOrganization sponsor, RE_Register register) {
		// Do nothing, must override
	}

	protected abstract void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException;
	protected abstract void clearAway();
	protected abstract String codeProperty();

	public abstract RE_ItemClass getOrCreateItemClass(RE_Register register, Row row);
	
	public boolean mustFixReferences() {
		return false;
	}

	public Set<String> getLimitToCodes() {
		return limitToCodes;
	}

	public void setLimitToCodes(Set<String> limitToCodes) {
		this.limitToCodes = limitToCodes;
	}
	
	public void setLimitToCodes(String limitToCodes) {
		this.limitToCodes = StringUtils.commaDelimitedListToSet(limitToCodes);
	}

}
