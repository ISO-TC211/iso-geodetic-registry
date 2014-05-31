package de.bespire.registry.iso.importer;

import org.iso.registry.core.model.Alias;
import org.iso.registry.core.model.AliasRepository;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.NamingSystemItem;
import org.iso.registry.core.model.NamingSystemItemRepository;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItemRepository;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItemRepository;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.DatumItemRepository;
import org.iso.registry.core.model.datum.EllipsoidItem;
import org.iso.registry.core.model.datum.EllipsoidItemRepository;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.iso.registry.core.model.datum.PrimeMeridianItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class AliasesImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(AreasImporter.class);
	
	public static final String ALIAS_CODE = "ALIAS CODE";
	public static final String OBJECT_TABLE_NAME = "OBJECT_TABLE_NAME";
	public static final String OBJECT_CODE = "OBJECT_CODE";
	public static final String NAMING_SYSTEM_CODE = "NAMING_SYSTEM_CODE";
	public static final String ALIAS = "ALIAS";
	public static final String REMARKS = "REMARKS";

	@Autowired private AliasRepository aliasRepository;
	@Autowired private NamingSystemItemRepository namingSystemRepository;
	@Autowired private AreaItemRepository areaRepository;
	@Autowired private CoordinateSystemAxisItemRepository axisRepository;
	@Autowired private CoordinateReferenceSystemItemRepository crsRepository;
	@Autowired private DatumItemRepository datumRepository;
	@Autowired private EllipsoidItemRepository ellipsoidRepository;
	@Autowired private PrimeMeridianItemRepository pmRepository;
	@Autowired private UnitOfMeasureItemRepository uomRepository;

	private RE_ItemClass itemClass;
	
	public AliasesImporter() { }
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) {
		Integer namingSystemCode = (Integer)row.get(NAMING_SYSTEM_CODE);
		String aliasText = (String)row.get(ALIAS);
		Integer aliasCode = (Integer)row.get(ALIAS_CODE);
		String remarks = (String)row.get(REMARKS);

		if (namingSystemCode == null) {
			logger.info("Skipped alias '{}' (#{}) with missing naming system code", new Object[] { aliasText, aliasCode });
			return;
		}

		String objectType = (String)row.get(OBJECT_TABLE_NAME);
		Integer objectCode = (Integer)row.get(OBJECT_CODE);

		NamingSystemItem namingSystem = namingSystemRepository.findByCode(namingSystemCode);
		Alias alias = new Alias(aliasText, namingSystem);
		IdentifiedItem item;
		
		if (objectType.equals("Area")) {
			item = areaRepository.findByCode(objectCode);
		}
		else if (objectType.equals("Coordinate Axis Name")) {
			item = axisRepository.findByCode(objectCode);
		}
		else if (objectType.equals("Coordinate Reference System")) {
			item = crsRepository.findByCode(objectCode);
		}
		else if (objectType.equals("Coordinate_Operation")) {
			return;
		}
		else if (objectType.equals("Coordinate_Operation Method")) {
			return;
		}
		else if (objectType.equals("Coordinate_Operation Parameter")) {
			return;
		}
		else if (objectType.equals("Datum")) {
			item = datumRepository.findByCode(objectCode);
		}
		else if (objectType.equals("Ellipsoid")) {
			item = ellipsoidRepository.findByCode(objectCode);
		}
		else if (objectType.equals("Naming System")) {
			item = namingSystemRepository.findByCode(objectCode);
		}
		else if (objectType.equals("Prime Meridian")) {
			item = pmRepository.findByCode(objectCode);
		}
		else if (objectType.equals("Unit of Measure")) {
			item = uomRepository.findByCode(objectCode);
		}
		else {
			logger.info("Skipped alias '{}' (#{}) for object #{} in unknown table {}", new Object[] { aliasText, aliasCode, objectCode, objectType }); 
			return;
		}

		if (item != null) {
			item.addAlias(alias);
			alias.setAliasedItem(item);
			logger.info("Added alias '{}' (#{}) to object '{}' (#{}, {}) of type {}", new Object[] { aliasText, aliasCode, item.getName(), objectCode, item.getUuid().toString(), item.getClass().getName() }); 
		}
		else {
			logger.info("Skipped alias '{}' (#{}) for missing object #{} of table {}", new Object[] { aliasText, aliasCode, objectCode, objectType });
			return;
		}
		
		alias.setRemarks(remarks);
		aliasRepository.save(alias);
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		return null;
	}
	
	@Override
	@Transactional
	protected void clearAway() {
		areaRepository.deleteAll();
	}

	@Override
	protected String codeProperty() {
		return ALIAS_CODE;
	}

}
