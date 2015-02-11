package de.bespire.registry.iso.importer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.iso.registry.core.model.EpsgIsoMapping;
import org.iso.registry.core.model.EpsgIsoMappingRepository;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
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
	
	@Autowired
	protected EpsgIsoMappingRepository mapRepository;
	
	@Autowired
	private UnitOfMeasureItemRepository uomRepository;

	private LinkedHashSet<String> limitToCodes;
	private boolean generateIdentifiers;
	
	protected AbstractImporter() {
		this.generateIdentifiers = true;
	}

	protected void acceptProposal(Addition ai, String decisionEvent)
			throws InvalidProposalException, UnauthorizedException {
		try {
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
			ic.getRegisters().add(r);
			ic = itemClassRepository.save(ic);
		}

		return ic;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void importRows(Cursor cursor, long count, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		if (this.limitToCodes != null && !this.limitToCodes.isEmpty()) {
			for (String code : this.limitToCodes) {
				Map<String, Object> rowPattern = new HashMap<String, Object>();
				rowPattern.put(codeProperty(), Integer.parseInt(code));
				Row row = CursorBuilder.findRow(cursor.getTable(), rowPattern);
				if (row == null) {
					logger.error("!!!! Did not find object with {}='{}' in table '{}' !!!!", new Object[] { codeProperty(), code, cursor.getTable().getName() });
					continue;
				}
				RE_ItemClass itemClass = this.getOrCreateItemClass(register, row);
				try {
					this.importRow(row, itemClass, sponsor, register);
				}
				catch (Throwable t) {
					logger.error(t.getMessage(), t);
					if (this.codeProperty() != null) {
						Integer codeInt = (Integer)row.get(codeProperty());
						logger.error("!!!! Failed to import object #{} from table '{}' !!!!", codeInt, cursor.getTable().getName());
					}
					else {
						logger.error("!!!! Failed to import object from table '{}' !!!!", cursor.getTable().getName());
						for (String key : row.keySet()) {
							logger.error("[{}] {}", key, row.get(key));
						}
					}
				}
			}
		}
		else {
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

	protected abstract void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException;
	protected abstract void clearAway();
	protected abstract String codeProperty();

	public abstract RE_ItemClass getOrCreateItemClass(RE_Register register, Row row);
	
	public boolean mustFixReferences() {
		return false;
	}

	public Set<String> getLimitToCodes() {
		return limitToCodes;
	}
	
	public boolean isLimited() {
		return this.limitToCodes != null && !this.limitToCodes.isEmpty();
	}

	public void setLimitToCodes(LinkedHashSet<String> limitToCodes) {
		this.limitToCodes = limitToCodes;
	}
	
	public void setLimitToCodes(String limitToCodes) {
		this.limitToCodes = new LinkedHashSet<>();
		for (String code : StringUtils.commaDelimitedListToStringArray(limitToCodes)) {
			this.limitToCodes.add(code);
		}
	}
	
	public boolean isGenerateIdentifiers() {
		return generateIdentifiers;
	}

	public void setGenerateIdentifiers(boolean generateIdentifiers) {
		this.generateIdentifiers = generateIdentifiers;
	}

	protected EpsgIsoMapping addMapping(String itemClass, Integer epsgCode, Integer isoCode) {
		EpsgIsoMapping mapping = new EpsgIsoMapping(itemClass, epsgCode, isoCode);
		return mapRepository.save(mapping);
	}
	
	protected Integer determineIdentifier(String itemClass, Integer epsgCode) {
		Integer identifier;
		if (generateIdentifiers) {
			identifier = findNextAvailableIdentifier();
			addMapping(itemClass, epsgCode, identifier);
		}
		else {
			identifier = epsgCode;
		}
		
		return identifier;			
	}
	
	private Integer findNextAvailableIdentifier() {
		String jpql = "SELECT MAX(i.identifier) FROM IdentifiedItem i";
		Integer maxCode = (Integer)em.createQuery(jpql).getResultList().get(0);
		
		Integer result = (maxCode == null) ? 1 : maxCode + 1;
		logger.info(">>> Next available identifier: {}", result);
		return result;
	}
	
	protected Integer findMappedCode(String itemClass, Integer epsgCode) {
		if (this.generateIdentifiers) {
			return mapRepository.findByItemClassAndEpsgCode(itemClass, epsgCode);
		}
		else {
			return epsgCode;
		}
	}

	protected static Integer findMappedCode(String itemClass, Integer epsgCode, EpsgIsoMappingRepository mapRepository) {
		return mapRepository.findByItemClassAndEpsgCode(itemClass, epsgCode);
	}

}
