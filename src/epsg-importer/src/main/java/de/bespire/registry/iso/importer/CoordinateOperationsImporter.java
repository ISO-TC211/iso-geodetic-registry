package de.bespire.registry.iso.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.CoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.ConcatenatedOperationItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.CoordinateOperationItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.OperationMethodItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.SingleOperationItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.operation.SingleOperationItemProposalDTO.SingleOperationType;
import org.iso.registry.api.registry.registers.gcp.operation.TransformationAccuracy;
import org.iso.registry.core.model.EpsgIsoMappingRepository;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.UnitOfMeasureItemRepository;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.crs.AreaItemRepository;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItemRepository;
import org.iso.registry.core.model.iso19115.dataquality.DQ_AbsoluteExternalPositionalAccuracy;
import org.iso.registry.core.model.operation.CoordinateOperationItem;
import org.iso.registry.core.model.operation.CoordinateOperationItemRepository;
import org.iso.registry.core.model.operation.GeneralOperationParameterItem;
import org.iso.registry.core.model.operation.Measure;
import org.iso.registry.core.model.operation.OperationMethodItem;
import org.iso.registry.core.model.operation.OperationMethodItemRepository;
import org.iso.registry.core.model.operation.OperationParameterItem;
import org.iso.registry.core.model.operation.OperationParameterItemRepository;
import org.iso.registry.core.model.operation.OperationParameterValue;
import org.iso.registry.core.model.operation.SingleOperationItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

@Component
public class CoordinateOperationsImporter extends AbstractImporter
{
	public static enum Mode {
		CONVERSION,
		TRANSFORMATION,
		CONCATENATED
	}
	
	private static final Logger logger = LoggerFactory.getLogger(CoordinateOperationsImporter.class);
	
	public static final String COORD_OP_CODE = "COORD_OP_CODE";
	public static final String COORD_OP_NAME = "COORD_OP_NAME";
	public static final String COORD_OP_TYPE = "COORD_OP_TYPE";

	public static final String SOURCE_CRS_CODE = "SOURCE_CRS_CODE";
	public static final String TARGET_CRS_CODE = "TARGET_CRS_CODE";
	public static final String COORD_TFM_VERSION = "COORD_TFM_VERSION";
	public static final String COORD_OP_VARIANT = "COORD_OP_VARIANT";
	public static final String AREA_OF_USE_CODE = "AREA_OF_USE_CODE";
	public static final String COORD_OP_SCOPE = "COORD_OP_SCOPE";
	public static final String COORD_OP_ACCURACY = "COORD_OP_ACCURACY";
	public static final String UOM_CODE_SOURCE_COORD_DIFF = "UOM_CODE_SOURCE_COORD_DIFF";
	public static final String UOM_CODE_TARGET_COORD_DIFF = "UOM_CODE_TARGET_COORD_DIFF";

	public static final String COORD_OP_METHOD_CODE = "COORD_OP_METHOD_CODE";
	public static final String COORD_OP_METHOD_NAME = "COORD_OP_METHOD_NAME";
	public static final String REVERSE_OP = "REVERSE_OP";
	public static final String FORMULA = "FORMULA";
	public static final String EXAMPLE = "EXAMPLE";
	
	public static final String PARAMETER_CODE = "PARAMETER_CODE";
	public static final String PARAMETER_NAME = "PARAMETER_NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	
	public static final String SORT_ORDER = "SORT_ORDER";
	public static final String PARAM_SIGN_REVERSAL = "PARAM_SIGN_REVERSAL";
	
	public static final String PARAMETER_VALUE = "PARAMETER_VALUE";
	public static final String PARAM_VALUE_FILE_REF = "PARAM_VALUE_FILE_REF";
	public static final String UOM_CODE = "UOM_CODE";
	
	public static final String CONCAT_OPERATION_CODE = "CONCAT_OPERATION_CODE";
	public static final String SINGLE_OPERATION_CODE = "SINGLE_OPERATION_CODE";
	public static final String OP_PATH_STEP = "OP_PATH_STEP";
	
	@Autowired
	private CoordinateOperationItemRepository opsRepository;
	
	@Autowired
	private AreaItemRepository areaRepository;
	
	@Autowired
	private UnitOfMeasureItemRepository uomRepository;
	
	@Autowired
	private CoordinateReferenceSystemItemRepository crsRepository;
	
	@Autowired
	private OperationParameterItemRepository paramRepository;
	
	@Autowired
	private OperationMethodItemRepository methodRepository;
	
	private RE_ItemClass icConversion;
	private RE_ItemClass icTransformation;
	private RE_ItemClass icConcatOp;
	private RE_ItemClass icOpMethod;
	private RE_ItemClass icOpParam;
//	private RE_ItemClass icOpParamValue;
	
	private Table parametersUsageTable;
	private Table parameterValuesTable;
	private Table pathTable;
	
	private Mode mode;
	
	@Override
	@Transactional
	protected void importRow(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException, InvalidProposalException {
		String type = (String)row.get(COORD_OP_TYPE);
		if ("conversion".equalsIgnoreCase(type)) {
			if (!Mode.CONVERSION.equals(this.getMode())) {
				logger.info(">> Skipping conversion");
				return;
			}
			importConversion(row, itemClass, sponsor, register);
		}
		else if ("transformation".equalsIgnoreCase(type)) {
			if (!Mode.TRANSFORMATION.equals(this.getMode())) {
				logger.info(">> Skipping transformation");
				return;
			}
			importTransformation(row, itemClass, sponsor, register);
		}
		else if ("concatenated operation".equalsIgnoreCase(type)) {
			if (!Mode.CONCATENATED.equals(this.getMode())) {
				logger.info(">> Skipping conversion");
				return;
			}
			try {
				importConcatenated(row, icConcatOp, sponsor, register);
			}
			catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		else {
			throw new RuntimeException(String.format("Unexpected operation type: %s", type));
		}
	}
	
	@Transactional
	protected void importConversion(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException, InvalidProposalException {
		SingleOperationItemProposalDTO proposal = createSingleOperationProposal(row, icConversion, sponsor, register);
		proposal.setOperationType(SingleOperationType.CONVERSION);
		Addition ai = processProposal(proposal);
		addMapping(ai.getItem().getItemClass().getName(), (Integer)row.get(codeProperty()), ai.getItem().getUuid());
	}

	@Transactional
	protected void importTransformation(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException, UnauthorizedException, InvalidProposalException {
		SingleOperationItemProposalDTO proposal = createSingleOperationProposal(row, icTransformation, sponsor, register);
		proposal.setOperationType(SingleOperationType.TRANSFORMATION);
		Addition ai = processProposal(proposal);
		addMapping(ai.getItem().getItemClass().getName(), (Integer)row.get(codeProperty()), ai.getItem().getUuid());
	}

	private void setOperationItemValues(CoordinateOperationItemProposalDTO proposal, Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());
		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
		
		Integer epsgCode = (Integer)row.get(COORD_OP_CODE);
//		proposal.setIdentifier(determineIdentifier("CoordinateOperation", epsgCode));
		
		proposal.setName((String)row.get(COORD_OP_NAME));
		
		Integer sourceCrsCode = (Integer)row.get(SOURCE_CRS_CODE);
		if (sourceCrsCode != null) {
			CoordinateReferenceSystemItem sourceCrs = crsRepository.findOne(findMappedCode(sourceCrsCode));
			if (sourceCrs != null) {
				proposal.setSourceCrs(new CoordinateReferenceSystemItemProposalDTO(sourceCrs));
			}
			else {
				logger.error("!!! Did not find referenced source CRS with code {}", sourceCrsCode.toString());
			}
		}

		Integer targetCrsCode = (Integer)row.get(TARGET_CRS_CODE);
		if (targetCrsCode != null) {
			CoordinateReferenceSystemItem targetCrs = crsRepository.findOne(findMappedCode(targetCrsCode));
			if (targetCrs != null) {
				proposal.setTargetCrs(new CoordinateReferenceSystemItemProposalDTO(targetCrs));
			}
			else {
				logger.error("!!! Did not find referenced target CRS with code {}", targetCrsCode.toString());
			}
		}
		
		String transformationVersion = (String)row.get(COORD_TFM_VERSION);
		proposal.setOperationVersion(transformationVersion);
		
		Integer areaCode = (Integer)row.get(AREA_OF_USE_CODE);
		if (areaCode != null) {
			AreaItem area = areaRepository.findOne(findMappedCode("Area", areaCode));
			if (area != null) {
				ExtentDTO extent = new ExtentDTO();
				extent.getGeographicBoundingBoxes().add(area.getBoundingBox());
				extent.setDescription(area.getName());
				proposal.setDomainOfValidity(extent);
			}
		}
		
		proposal.setScope(Arrays.asList((String)row.get(COORD_OP_SCOPE)));

		Float accuracyValue = (Float)row.get(COORD_OP_ACCURACY);
		if (accuracyValue != null) {
			UnitOfMeasureItem metre = uomRepository.findByNameAndStatus("metre", RE_ItemStatus.VALID);
//			TransformationAccuracy trafoAccuracy = new TransformationAccuracy(accuracyValue, metre);
//			DQ_AbsoluteExternalPositionalAccuracy accuracy = new DQ_AbsoluteExternalPositionalAccuracy();
//			accuracy.setResult(trafoAccuracy);
//			proposal.addCoordinateOperationAccuracy(accuracy);
			proposal.setAccuracy(accuracyValue);
			proposal.setAccuracyUom(new UnitOfMeasureItemProposalDTO(metre));
		}		
		
		proposal.setRemarks((String)row.get(REMARKS));
		addInformationSource(proposal, (String)row.get(INFORMATION_SOURCE));
		proposal.setDataSource((String)row.get(DATA_SOURCE));
	}
	
	private SingleOperationItemProposalDTO createSingleOperationProposal(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		SingleOperationItemProposalDTO proposal = new SingleOperationItemProposalDTO();
		this.setOperationItemValues(proposal, row, itemClass, sponsor, register);

		Integer operationCode = (Integer)row.get(COORD_OP_CODE);

		Integer methodCode = (Integer)row.get(COORD_OP_METHOD_CODE);
		OperationMethodItem method = methodRepository.findOne(findMappedCode("OperationMethod", methodCode));
		if (method != null) {
			proposal.setMethod(new OperationMethodItemProposalDTO(method));
		}
		
		Integer sourceCrsDiffUomCode = (Integer)row.get(UOM_CODE_SOURCE_COORD_DIFF);
		if (sourceCrsDiffUomCode != null) {
//			UnitOfMeasureItem uom = uomRepository.findByIdentifier(sourceCrsUomCode);
			// TODO Wohin damit??? 
		}

		Integer targetCrsDiffUomCode = (Integer)row.get(UOM_CODE_TARGET_COORD_DIFF);
		if (targetCrsDiffUomCode != null) {
//			UnitOfMeasureItem uom = uomRepository.findByIdentifier(targetCrsUomCode);
			// TODO Wohin damit??? 
		}

		List<GeneralOperationParameterItem> parameters = findParameters(parametersUsageTable, paramRepository, methodCode, mapRepository);
		for (GeneralOperationParameterItem parameter : parameters) {
			OperationParameterValue paramValue = findParameterValue(operationCode, methodCode, findMappedCode(parameter.getUuid()));
			proposal.addParameterValue(paramValue);
		}

		return proposal;
	}
	
	@Transactional
	protected void importConcatenated(Row row, RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register) throws IOException {
		ConcatenatedOperationItemProposalDTO proposal = new ConcatenatedOperationItemProposalDTO();
		this.setOperationItemValues(proposal, row, icConcatOp, sponsor, register);

		// TODO fix
//		List<SingleOperationItem> ops = findOperations((Integer)row.get(COORD_OP_CODE));
//		for (SingleOperationItem op : ops) {
//			proposal.addCoordOperation(new SingleOperationItemProposalDTO(op));
//		}
	}

	private Addition processProposal(CoordinateOperationItemProposalDTO proposal) throws UnauthorizedException, InvalidProposalException {
		logger.info(">> Imported operation '{}'...", proposal.getName());
		
		try {
			Addition ai = proposalService.createAdditionProposal(proposal);
			proposalService.submitProposal(ai);
			
			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
			acceptProposal(ai, decisionEvent);
			
			return ai;
		}
		catch (InvalidProposalException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}		
	}


//	protected OperationMethodItem importMethod(RE_ItemClass itemClass, RE_SubmittingOrganization sponsor, RE_Register register, Integer code) throws IOException {
//		OperationMethodItemProposalDTO proposal = new OperationMethodItemProposalDTO();
//		proposal.setItemClassUuid(itemClass.getUuid());
//		proposal.setSponsorUuid(sponsor.getUuid());
//		proposal.setTargetRegisterUuid(register.getUuid());
//		proposal.setJustification(AbstractImporter.IMPORT_SOURCE);
//
//		Map<String, Object> search = new HashMap<String, Object>();
//		search.put(COORD_OP_METHOD_CODE, code);
//		Row row = CursorBuilder.findRow(methodsTable, search);
//		
//		proposal.setName((String)row.get(COORD_OP_METHOD_NAME));
//		proposal.setReversible((Boolean)row.get(REVERSE_OP));
//		proposal.setFormula((String)row.get(FORMULA));
//		proposal.setDescription((String)row.get(EXAMPLE));
//		
//		for (OperationParameterItem parameter : findParameters(icOpParamValue, sponsor, register, code)) {
//			proposal.addParameter(new OperationParameterItemProposalDTO(parameter));
//		}
//		
//		proposal.setRemarks((String)row.get(REMARKS));
//		addInformationSource(proposal, (String)row.get(INFORMATION_SOURCE));
//		proposal.setDataSource((String)row.get(DATA_SOURCE));
//		
//		try {
//			Addition ai = proposalService.createAdditionProposal(proposal);
//			proposalService.submitProposal(ai);
//			
//			String decisionEvent = AbstractImporter.IMPORT_SOURCE;
//			acceptProposal(ai, decisionEvent, BigInteger.valueOf(proposal.getCode().longValue()));
//
//			logger.info(">> Imported Conversion '{}' (#{})...", proposal.getName(), code);
//			
//			return (OperationMethodItem)ai.getItem();
//		}
//		catch (InvalidProposalException e) {
//			logger.error(e.getMessage(), e);
//		}
//		
//		return null;
//	}
//
	static List<GeneralOperationParameterItem> findParameters(Table parametersUsageTable, OperationParameterItemRepository paramRepository, Integer code, EpsgIsoMappingRepository mapRepository) throws IOException {
		List<GeneralOperationParameterItem> result = new ArrayList<>();
		
		Cursor usageCursor = parametersUsageTable.getDefaultCursor();
		
		Map<String, Object> search = new HashMap<String, Object>();
		search.put(COORD_OP_METHOD_CODE, code);
		if (usageCursor.findFirstRow(search)) {
			do {
				Row usageRow = usageCursor.getCurrentRow();
				Integer parameterCode = (Integer)usageRow.get(PARAMETER_CODE);
				
				UUID paramIdentifier = findMappedCode("OperationParameter", parameterCode, mapRepository);
				OperationParameterItem param = paramRepository.findOne(paramIdentifier);
				if (param != null) {
					result.add(param);
				}
				else {
					logger.error("!!! Unknown parameter referenced in usage table: {}", parameterCode.toString());
				}
				
			} while (usageCursor.findNextRow(search));
		}			
		
		return result;
	}
	
	protected OperationParameterValue findParameterValue(Integer operationCode, Integer methodCode, Integer paramCode) throws IOException {
//		Cursor valueCursor = parameterValuesTable.getDefaultCursor();
//		Map<String, Object> search = new HashMap<String, Object>();
//		search.put(COORD_OP_CODE, operationCode);
//		search.put(COORD_OP_METHOD_CODE, methodCode);
//		search.put(PARAMETER_CODE, paramCode);
		
		Row valueRow = CursorBuilder.findRowByPrimaryKey(parameterValuesTable, operationCode, paramCode, methodCode); 
		if (valueRow == null) {
			logger.error("!!! Could not extract parameter value for [{}, {}, {}]", new Object[] { operationCode.toString(), methodCode.toString(), paramCode.toString() });
		}
		else {
			Double paramValue = (Double)valueRow.get(PARAMETER_VALUE);
			String fileRef = (String)valueRow.get(PARAM_VALUE_FILE_REF);
			OperationParameterItem parameter = paramRepository.findOne(findMappedCode("OperationParameter", paramCode));
			if (paramValue != null && fileRef == null) {
				Integer uomCode = (Integer)valueRow.get(UOM_CODE);
				UnitOfMeasureItem uom = uomRepository.findOne(findMappedCode("UnitOfMeasure", uomCode));
				Measure measure = new Measure(paramValue, uom);
				return new OperationParameterValue(parameter, measure);
			}
			else if (paramValue == null && fileRef != null) {
				File file = new File(fileRef);
				return new OperationParameterValue(parameter, file);
			}
			else {
				logger.error("!!! Ambiguous parameter value for [{}, {}, {}]: value = {} // fileRef = {}", new Object[] { operationCode.toString(), methodCode.toString(), paramCode.toString(), paramValue, fileRef });
			}
		}
		
		return null;
	}
	
	@Override
	protected void clearAway() {
	}


	@Override
	@Transactional
	protected void fixReference(Row row, RE_SubmittingOrganization sponsor, RE_Register register) {
	}

	protected List<SingleOperationItem> findOperations(Integer concatOperationCode) throws IOException {
		List<SingleOperationItem> result = new ArrayList<>();
		
		Cursor pathCursor = pathTable.getDefaultCursor();
		
		Map<Integer, Integer> orderedOps = new HashMap<>();
		
		Map<String, Object> search = new HashMap<String, Object>();
		search.put(CONCAT_OPERATION_CODE, concatOperationCode);
		if (pathCursor.findFirstRow(search)) {
			do {
				Row pathRow = pathCursor.getCurrentRow();
				Integer singleOpCode = (Integer)pathRow.get(SINGLE_OPERATION_CODE);
				
				orderedOps.put((Integer)pathRow.get(OP_PATH_STEP), singleOpCode);
			} while (pathCursor.findNextRow(search));
		}			
		
		for (int i = 1; i <= orderedOps.size(); i++) {
			CoordinateOperationItem param = opsRepository.findOne(findMappedCode("CoordinateOperation", orderedOps.get(i)));
			if (param != null && param instanceof SingleOperationItem) {
				result.add((SingleOperationItem)param);
			}
			else {
				logger.error("!!! Unknown/illegal parameter referenced in usage table: {}", orderedOps.get(i).toString());
			}
		}
		
		return result;
	}

	@Override
	public boolean mustFixReferences() {
		return true;
	}

	@Override
	@Transactional
	public RE_ItemClass getOrCreateItemClass(RE_Register register, Row row) {
		if (icConversion == null) {
			icConversion = getOrCreateItemClass("Conversion", register);
		}
		if (icTransformation == null) {
			icTransformation = getOrCreateItemClass("Transformation", register);
		}
		if (icConcatOp == null) {
			icConcatOp = getOrCreateItemClass("ConcatenatedOperation", register);
		}
		if (icOpMethod == null) {
			icOpMethod = getOrCreateItemClass("OperationMethod", register);
		}
		if (icOpParam == null) {
			icOpParam = getOrCreateItemClass("OperationParameter", register);
		}

		if (row == null) {
			return null;
		}
		
		String type = (String)row.get(COORD_OP_TYPE);
		if ("conversion".equalsIgnoreCase(type)) {
			return icConversion;
		}
		else if ("transformation".equalsIgnoreCase(type)) {
			return icTransformation;
		}
		else if ("concatenated operation".equalsIgnoreCase(type)) {
			return icConcatOp;
		}
		else {
			throw new RuntimeException(String.format("Unexpected operation type: %s", type));
		}
	}

	public RE_ItemClass getOrCreateItemClass(String name, RE_Register register) {
		RE_ItemClass itemClass = itemClassRepository.findByName(name);
		if (itemClass == null) {
			this.addItemClass(name, register);
			itemClass = itemClassRepository.findByName(name);
		}
		
		return itemClass;
	}

	public Table getParametersUsageTable() {
		return parametersUsageTable;
	}

	public void setParametersUsageTable(Table parametersUsageTable) {
		this.parametersUsageTable = parametersUsageTable;
	}

	public Table getParameterValuesTable() {
		return parameterValuesTable;
	}

	public void setParameterValuesTable(Table parameterValuesTable) {
		this.parameterValuesTable = parameterValuesTable;
	}

	public Table getPathTable() {
		return pathTable;
	}

	public void setPathTable(Table pathTable) {
		this.pathTable = pathTable;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	@Override
	protected String codeProperty() {
		return COORD_OP_CODE;
	}
}
