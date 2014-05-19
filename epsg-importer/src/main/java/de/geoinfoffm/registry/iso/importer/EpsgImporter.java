package de.geoinfoffm.registry.iso.importer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.SubmittingOrganizationRepository;

public class EpsgImporter
{
	private static final String EPSG_REGISTER_NAME = "EPSG Geodetic Parameter Data Set";
	
	private static final Logger logger = LoggerFactory.getLogger(EpsgImporter.class);

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		File source;
		if (args.length == 0) {
			source = new File("C:/Daten/EPSG_v7_6_original.mdb"); 
		}
		else {
			source = new File(args[0]);
		}
		Database db = DatabaseBuilder.open(source);
		AnnotationConfigApplicationContext context = null;
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		try {
			Authentication authentication = new PreAuthenticatedAuthenticationToken("SYSTEM", "N/A", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			context = new AnnotationConfigApplicationContext("de.geoinfoffm.registry", "org.iso.registry");
			
			NamingSystemsImporter namingSystemsImporter = context.getBean(NamingSystemsImporter.class);

			AreasImporter areasImporter = context.getBean(AreasImporter.class);

			UnitsOfMeasurementImporter uomImporter = context.getBean(UnitsOfMeasurementImporter.class);

			CoordinateSystemAxesImporter axesImporter = context.getBean(CoordinateSystemAxesImporter.class);
			axesImporter.setNamesTable(db.getTable("Coordinate Axis Name"));
			
			CoordinateSystemsImporter csImporter = context.getBean(CoordinateSystemsImporter.class);
			csImporter.setAxisTable(db.getTable("Coordinate Axis"));

			EllipsoidsImporter ellipsoidsImporter = context.getBean(EllipsoidsImporter.class);

			PrimeMeridiansImporter pmImporter = context.getBean(PrimeMeridiansImporter.class);
			
			DatumsImporter datumsImporter = context.getBean(DatumsImporter.class);
			
			CoordinateReferenceSystemsImporter crsImporter = context.getBean(CoordinateReferenceSystemsImporter.class);

			RegistryInitializer initializer = context.getBean(RegistryInitializer.class);
//			initializer.initializeRegistry();

			SubmittingOrganizationRepository suborgRepository = context.getBean(SubmittingOrganizationRepository.class);
			RE_SubmittingOrganization sponsor = suborgRepository.findAll().get(0);
			
			EntityManager em = context.getBean(EntityManager.class);

			RegisterRepository registerRepository = context.getBean(RegisterRepository.class);
			RE_Register register = registerRepository.findByName(EPSG_REGISTER_NAME);
			
			if (register == null) {
				throw new RuntimeException(String.format("Registry not initialized: Register '%s' not found", EPSG_REGISTER_NAME));
			}
			
//			Table namingSystemsTable = db.getTable("Naming System");
//			run(namingSystemsImporter, namingSystemsTable, register, sponsor);
//			
//			Table uomTable = db.getTable("Unit of Measure");
//			run(uomImporter, uomTable, register, sponsor);
//			
//			Table axesTable = db.getTable("Coordinate Axis");
//			run(axesImporter, axesTable, register, sponsor);
//			
//			Table csTable = db.getTable("Coordinate System");
//			run(csImporter, csTable, register, sponsor);
//			
//			Table elTable = db.getTable("Ellipsoid");
//			run(ellipsoidsImporter, elTable, register, sponsor);
//
//			Table pmTable = db.getTable("Prime Meridian");
//			run(pmImporter, pmTable, register, sponsor);
//
//			Table areasTable = db.getTable("Area");
//			run(areasImporter, areasTable, register, sponsor);
//
//			Table datumsTable = db.getTable("Datum");
//			run(datumsImporter, datumsTable, register, sponsor);
//			
//			Table crsTable = db.getTable("Coordinate Reference System");
//			run(crsImporter, crsTable, register, sponsor);
		}
		finally {
			db.close();
			if (context != null) {
				context.close();
			}
		}		
	}
	
	private static void run(AbstractImporter importer, Table table, RE_Register register, RE_SubmittingOrganization sponsor) throws IOException {
		logger.info(".");
		logger.info(".");
		logger.info(".");
		logger.info(".");
		logger.info(".");
		logger.info(".");
		logger.info(".");
		logger.info(".");
		logger.info(".");
		logger.info(".");
		
//		importer.clearAway();

		Cursor cursor = table.getDefaultCursor();
		
		int rowCount = table.getRowCount();
		int i = 1;
		do {
			logger.info(".");
			logger.info(".");
			logger.info(".");
			logger.info("======================================================================");
			logger.info("> Happily importing {} rows from MDB table {}...", table.getRowCount(), table.getName());
			logger.info(">>> Will now import rows #{} to #{}...", new Object[] { i, (i + 99 < rowCount) ? i + 99 : rowCount });
			logger.info("======================================================================");
			logger.info(".");
			logger.info(".");
			logger.info(".");
			importer.importRows(cursor, 100, sponsor, register);
			i += 100;
		} while (i <= rowCount);
		
		if (importer.mustFixReferences()) {
			cursor.beforeFirst();

			i = 1;
			do {
				logger.info(".");
				logger.info(".");
				logger.info(".");
				logger.info("======================================================================");
				logger.info("> Happily fixing references...");
				logger.info(">>> Will now fix rows #{} to #{}...", new Object[] { i, (i + 99 < rowCount) ? i + 99 : rowCount });
				logger.info("======================================================================");
				logger.info(".");
				logger.info(".");
				logger.info(".");
				importer.fixReferences(cursor, 100);
				i += 100;
			} while (i <= rowCount);
		}
		
	
//		for (int i = 0; i < table.getRowCount(); i++) {
//			Row row = table.getNextRow();
//
//			logger.info("> Importing row #{}...", i + 1);
//			RE_ItemClass itemClass = importer.getOrCreateItemClass(register, row); 
//			if (itemClass == null) { 
//				new Object();
//			}
//			importer.importRow(row, itemClass, sponsor, register);
//
////			if (i > 200) break;
//		}
	}

}
