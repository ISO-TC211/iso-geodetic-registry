package de.bespire.registry.iso.importer;

import org.iso.registry.core.model.AliasRepository;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.NamingSystemItem;
import org.iso.registry.core.model.NamingSystemItemRepository;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
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
import org.iso.registry.core.model.operation.ConversionItem;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.iso.registry.core.model.operation.TransformationItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Row;

import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class AliasesImporter extends AbstractImporter
{
	private static final Logger logger = LoggerFactory.getLogger(AliasesImporter.class);
	
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
	
//	private <T extends Entity> T getObjectEx(String itemClass, Integer objectCode, EntityRepository<T> repository) {
//		UUID uuid = findMappedCode(itemClass, objectCode);
//		if (uuid != null) {
//			return repository.findOne(uuid);
//		}
//		return null;
//	}
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) {
		Integer namingSystemCode = (Integer)row.get(NAMING_SYSTEM_CODE);
		String aliasText = (String)row.get(ALIAS);
		Integer aliasCode = (Integer)row.get(ALIAS_CODE);
		String remarks = (String)row.get(REMARKS);
		String objectType = (String)row.get(OBJECT_TABLE_NAME);
		Integer objectCode = (Integer)row.get(OBJECT_CODE);
	
		try {
//			if (namingSystemCode == null) {
//				logger.info("Skipped alias '{}' (#{}) with missing naming system code", new Object[] { aliasText, aliasCode });
//				return;
//			}
	
	
	//		NamingSystemItem namingSystem = namingSystemRepository.findByIdentifier(namingSystemCode);
	//		Alias alias = new Alias(aliasText, namingSystem);
			IdentifiedItem item;
			
	//		if (objectType.equals("Area")) {
	//			item = areaRepository.findByIdentifier(findMappedCode("operationMethod", methodCode));
	//			item = areaRepository.findByIdentifier(objectCode);
	//		}
			if (objectType.equals("Coordinate Axis Name")) {
				item = findMappedEntity("CoordinateSystemAxis", objectCode, CoordinateSystemAxisItem.class);
			}
			else if (objectType.equals("Coordinate Reference System")) {
				item = findMappedEntity("*CRS", objectCode, CoordinateReferenceSystemItem.class);
			}
			else if (objectType.equals("Coordinate_Operation")) {
				item = findMappedEntity("Conversion", objectCode, ConversionItem.class);
				if (item == null) {
					item = findMappedEntity("Transformation", objectCode, TransformationItem.class);					
				}
			}
			else if (objectType.equals("Coordinate_Operation Method")) {
				item = findMappedEntity("OperationMethod", objectCode, OperationMethodItem.class);
			}
			else if (objectType.equals("Coordinate_Operation Parameter")) {
				item = findMappedEntity("OperationParameter", objectCode, OperationParameterItem.class);
			}
			else if (objectType.equals("Datum")) {
				item = findMappedEntity("*Datum", objectCode, DatumItem.class);
			}
			else if (objectType.equals("Ellipsoid")) {
				item = findMappedEntity("Ellipsoid", objectCode, EllipsoidItem.class);
			}
			else if (objectType.equals("Naming System")) {
				item = findMappedEntity("NamingSystem", objectCode, NamingSystemItem.class);
			}
			else if (objectType.equals("Prime Meridian")) {
				item = findMappedEntity("PrimeMeridian", objectCode, PrimeMeridianItem.class);
			}
			else if (objectType.equals("Unit of Measure")) {
				item = findMappedEntity("UnitOfMeasure", objectCode, UnitOfMeasureItem.class);
			}
			else {
				logger.info("Skipped alias '{}' (#{}) for object #{} in unknown table {}", new Object[] { aliasText, aliasCode, objectCode, objectType }); 
				return;
			}
	
			if (item != null) {
				item.getAliases().add(aliasText);
	//			alias.setAliasedItem(item);
				logger.info("Added alias '{}' (#{}) to object '{}' (#{}, {}) of type {}", new Object[] { aliasText, aliasCode, item.getName(), objectCode, item.getUuid().toString(), item.getClass().getName() }); 
			}
			else {
				logger.info("Skipped alias '{}' (#{}) for missing object #{} of table {}", new Object[] { aliasText, aliasCode, objectCode, objectType });
				return;
			}
		}
		catch (Throwable t) {
			logger.info("Skipped alias '{}' (#{}) for missing object #{} of table {}", new Object[] { aliasText, aliasCode, objectCode, objectType });
		}
		
//		alias.setRemarks(remarks);
//		aliasRepository.save(aliasText);
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
//		return ALIAS_CODE;
		return OBJECT_CODE;
	}

}
