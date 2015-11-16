/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions 
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.
 
 *     * Redistributions in binary form must reproduce the above 
 *     	 copyright notice, this list of conditions and the following 
 *       disclaimer in the documentation and/or other materials 
 *       provided with the distribution.
 
 *     * The names "German Federal Agency for Cartography and Geodesy", 
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE", 
 *       "GDI-DE Registry" and the names of other contributors must not 
 *       be used to endorse or promote products derived from this 
 *       software without specific prior written permission.
 *       
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN 
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.iso.registry.persistence.io.excel;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.iso.registry.core.model.GlobalIdentifiedItemRepository;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import de.bespire.io.excel.ColumnConfiguration;
import de.bespire.io.excel.ExcelConfiguration;
import de.bespire.io.excel.SheetConfiguration;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.ViewBeanFactory;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;

/**
 * The class ExcelAdapter.
 *
 * @author Florian Esser
 */
@Component
public class ExcelAdapter 
{
	private static final Logger logger = LoggerFactory.getLogger(ExcelAdapter.class);
	
	private ExcelConfiguration configuration;
	
	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private ProposalDtoFactory dtoFactory;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private RegisterItemRepository itemRepository;
	
	@Autowired
	private GlobalIdentifiedItemRepository identifiedItemRepository;
	
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	public ExcelAdapter(ExcelConfiguration configuration) {
		this.configuration = configuration;
	}
	
	protected String getString(Cell cell) {
		if (cell == null) return null;
		
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		}
		else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date dateValue = cell.getDateCellValue();
				return DateTimeFormat.forPattern("yyyy-MM-dd").print(dateValue.getTime());
			}
			else {
				return Integer.toString(Double.valueOf(cell.getNumericCellValue()).intValue());
			}
		}
		else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return Boolean.toString(cell.getBooleanCellValue());
		}
		else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return cell.getStringCellValue();
		}
		else {
			return null;
		}
	}
	
	public <E extends Entity> void exportEntities(Collection<E> entities, Workbook workbook) throws IOException {
		Map<UUID, String> visitedEntities = new HashMap<>();
		Map<String, Integer> rows = new HashMap<>();
		for (SheetConfiguration config : configuration.getSheet()) {
			rows.put(config.getSheetName(), config.getFirstDataRow() - 1);
		}
		
		for (Entity entity : entities) {
			if (entity instanceof CoordinateReferenceSystemItem) {
				new Object();
			}
			
			if (!visitedEntities.containsKey(entity.getUuid())) {
				exportEntity(entity, workbook, rows, visitedEntities);
			}
		}
	}
	
	private SheetConfiguration findSheetConfiguration(Class<?> entityClass) {
		for (SheetConfiguration config : configuration.getSheet()) {
			if (config.getExportType().equals(entityClass.getCanonicalName())) {
				return config;
			}
		}
		
		if (entityClass.getSuperclass() == null) {
			return null;
		}
		else {
			return findSheetConfiguration(entityClass.getSuperclass());
		}
	}
	
	private Cell exportEntity(Object entity, Workbook workbook, Map<String, Integer> rows, Map<UUID, String> visitedEntities) {
		SheetConfiguration sheetConfiguration = findSheetConfiguration(entity.getClass());
		if (sheetConfiguration == null) {
//			if (entity instanceof RE_RegisterItem) {
//				RegisterItemViewBean viewBean = viewBeanFactory.getViewBean((RE_RegisterItem)entity);
//				sheetConfiguration = findSheetConfiguration(viewBean.getClass());
//				if (sheetConfiguration == null) {
//					return null;
//				}
//				else {
//					entity = viewBean;
//				}
//			}
//			else {
				return null;
//			}
		}
		
		Sheet sheet = workbook.getSheet(sheetConfiguration.getSheetName());
		BeanWrapper bw = new BeanWrapperImpl(entity); 
		int rowNum = rows.get(sheet.getSheetName());
		rows.put(sheetConfiguration.getSheetName(), rowNum + 1);
		
		Row headerRow = sheet.getRow(sheetConfiguration.getHeaderRow());
		Row valueRow = sheet.getRow(rowNum);
		
		Cell idCell = valueRow.getCell(columnNameToIndex(sheetConfiguration.getIdColumn()));
//		if (entity instanceof IdentifiedItem) {
//			IdentifiedItem identifiedItem = (IdentifiedItem)entity;
//			if (identifiedItem.getIdentifier() < 0) {
//				return null;
//			}
//			idCell.setCellValue(sheetConfiguration.getIdPrefix() + identifiedItem.getIdentifier());
//		}
//		else {
			StringBuilder idBuilder = new StringBuilder(sheetConfiguration.getIdPrefix());
			idBuilder.append(Integer.toString(rowNum - (sheetConfiguration.getFirstDataRow() - 1) + 1));
			idCell.setCellValue(idBuilder.toString());
			if (entity instanceof Entity) {
				visitedEntities.put(((Entity)entity).getUuid(), idBuilder.toString());
			}
//		}
		for (ColumnConfiguration columnConfiguration : sheetConfiguration.getColumn()) {
			String javaProperty = columnConfiguration.getJavaPropertyExport();
			if (StringUtils.isEmpty(javaProperty)) {
				javaProperty = columnConfiguration.getJavaProperty(); 
			}
			
			if (javaProperty.equals("datum")) {
				new Object();
			}
			
			Cell valueCell = valueRow.getCell(columnNameToIndex(columnConfiguration.getColumnName()), Row.CREATE_NULL_AS_BLANK);
			if (javaProperty.equals("#itemClassName") && entity instanceof RE_RegisterItem) {
				valueCell.setCellValue(((RE_RegisterItem)entity).getItemClass().getName());
			}
			else {
				try { 
					Object propertyValue = null;
					
					if (columnConfiguration.isMultiple() && javaProperty.contains(".")) {
						// nested multiple property
						String[] pathParts = StringUtils.split(javaProperty, ".");
						
						Collection<?> collectionValue = (Collection<?>)bw.getPropertyValue(pathParts[0]);
						
						StringBuilder valueBuilder = new StringBuilder();
						for (Object value : collectionValue) {
							BeanWrapper subWrapper = new BeanWrapperImpl(value);
							Object subValue = subWrapper.getPropertyValue(pathParts[1]);
							if (valueBuilder.length() > 0) {
								valueBuilder.append(columnConfiguration.getSeparatorChar());
							}
							valueBuilder.append(subValue.toString());

//						valueBuilder.append(getString(targetId));
//							if (value instanceof Entity) {
//								Entity subEntity = (Entity)value;
//								Cell targetId = exportEntity(subEntity, workbook, rows);
//								if (valueBuilder.length() > 0) {
//									valueBuilder.append(columnConfiguration.getSeparatorChar());
//								}
//								valueBuilder.append(getString(targetId));
//							}
//							else {
//								if (valueBuilder.length() > 0) {
//									valueBuilder.append(columnConfiguration.getSeparatorChar());
//								}
//								valueBuilder.append(value.toString());
//							}
						}
						valueCell.setCellValue(valueBuilder.toString());
					}
					else {
						try {
							propertyValue = bw.getPropertyValue(javaProperty);
						}
						catch (NullValueInNestedPathException e) {
							// Ignore
						}
					
						if (propertyValue instanceof String) {
							valueCell.setCellValue((String)propertyValue);
						}
						else if (propertyValue instanceof Number) {
							valueCell.setCellValue(Double.valueOf(propertyValue.toString()));
						}
//						else if (propertyValue instanceof IdentifiedItem) {
//							valueCell.setCellValue(((IdentifiedItem)propertyValue).getIdentifier().toString());
//						}
						else if (propertyValue instanceof UUID) {
							valueCell.setCellValue(((UUID)propertyValue).toString());
						}
						else if (propertyValue instanceof Boolean) {
							valueCell.setCellValue((Boolean)propertyValue);
						}
						else if (propertyValue instanceof Date) {
							valueCell.setCellValue((Date)propertyValue);
						}
						else if (propertyValue instanceof Collection) {
							Collection<Object> propertyCollection = (Collection<Object>)propertyValue;
							if (propertyCollection.isEmpty()) continue;
							
							StringBuilder valueBuilder = new StringBuilder();
							for (Object value : propertyCollection) {
								if (valueBuilder.length() > 0) {
									valueBuilder.append(columnConfiguration.getSeparatorChar());
								}
								if (value instanceof Entity) {
									Entity subEntity = (Entity)value;
									if (visitedEntities.containsKey(subEntity.getUuid())) {
										valueBuilder.append(visitedEntities.get(subEntity.getUuid()));
									}
									else {
										Cell targetId = exportEntity(subEntity, workbook, rows, visitedEntities);
										valueBuilder.append(getString(targetId));
									}
								}
								else {
									valueBuilder.append(value.toString());
								}
							}
							valueCell.setCellValue(valueBuilder.toString());
						}
						else if (propertyValue instanceof Entity) {
							StringBuilder valueBuilder = new StringBuilder();
							
							Entity subEntity = (Entity)propertyValue;
							if (visitedEntities.containsKey(subEntity.getUuid())) {
								valueCell.setCellValue(visitedEntities.get(subEntity.getUuid()));
							}
							else {
								Cell targetId;
								if (subEntity == entity) {
									// self reference
									targetId = idCell;
								}
								else {
									targetId = exportEntity(subEntity, workbook, rows, visitedEntities);
								}
								if (valueBuilder.length() > 0) {
									valueBuilder.append(columnConfiguration.getSeparatorChar());
								}
								valueBuilder.append(getString(targetId));
								valueCell.setCellValue(valueBuilder.toString());
							}
						}
					}
				}
				catch (NotReadablePropertyException e) {
					logger.trace("Item has no property '{}'", javaProperty);
				}
				catch (Throwable t) {
					logger.error(t.getMessage(), t);
				}
			}
		}
		
		return idCell;
	}
	
	private <E extends Entity> void exportEntity(E entity, Workbook workbook, Sheet mainSheet, int rowNum, Row headerRow, Map<String, List<Entity>> subsheetEntities) {
		BeanWrapper bw = new BeanWrapperImpl(entity);

		Row valueRow = mainSheet.getRow(rowNum);
		
		Cell headingCell;
		int cellIdx = 1;
		do {
			headingCell = headerRow.getCell(cellIdx);
			if (headingCell == null || headingCell.getCellComment() == null) {
				break;
			}
			String fieldName = StringUtils.trimAllWhitespace(headingCell.getCellComment().getString().getString());
			if (fieldName.endsWith(".referencedItemUuid")) {
				fieldName = fieldName.substring(0, fieldName.indexOf(".referencedItemUuid"));
			}
			
			Cell valueCell = valueRow.getCell(cellIdx, Row.CREATE_NULL_AS_BLANK);
			
			if (fieldName.equals("itemClassName") && entity instanceof RE_RegisterItem) {
				valueCell.setCellValue(((RE_RegisterItem)entity).getItemClass().getName());
			}
			else {
				try { 
					Object propertyValue = bw.getPropertyValue(fieldName);
					if (propertyValue instanceof String) {
						valueCell.setCellValue((String)propertyValue);
					}
					else if (propertyValue instanceof Number) {
						valueCell.setCellValue(Double.valueOf(propertyValue.toString()));
					}
					else if (propertyValue instanceof RE_RegisterItem) {
						valueCell.setCellValue(((RE_RegisterItem)propertyValue).getUuid().toString());
					}
					else if (propertyValue instanceof UUID) {
						valueCell.setCellValue(((UUID)propertyValue).toString());
					}
					else if (propertyValue instanceof Collection) {
						if (workbook.getSheet(fieldName) != null) {
							// sub-sheet exists: data must be put into sub-sheet
							Collection<Entity> linkedEntities = (Collection<Entity>)propertyValue;
							if (!linkedEntities.isEmpty()) { 
								int firstIdx = subsheetEntities.get(fieldName).size() + 1;
								int lastIdx = firstIdx + linkedEntities.size() - 1;
								StringBuilder refBuilder = new StringBuilder();
								for (int j = firstIdx; j <= lastIdx; j++) {
									if (refBuilder.length() > 0) {
										refBuilder.append(";");
									}
									refBuilder.append(Integer.toString(j));
								}
								
								valueCell.setCellValue(refBuilder.toString());
								
								subsheetEntities.get(fieldName).addAll(linkedEntities);
							}
						}
						else {
							StringBuilder valueBuilder = new StringBuilder();
							Collection<?> values = (Collection<?>)propertyValue;
							for (Object value : values) {
								if (valueBuilder.length() > 0) {
									valueBuilder.append(";");
								}
								valueBuilder.append(value.toString());
							}
							
							valueCell.setCellValue(valueBuilder.toString());
						}
					}
				}
				catch (NotReadablePropertyException e) {
					logger.trace("Item has no property '{}'", fieldName);
				}
				catch (Throwable t) {
					logger.error(t.getMessage(), t);
				}
			}
			
			cellIdx++;
		} while (headingCell.getCellComment() != null);
	}
	
	public Collection<RegisterItemProposalDTO> extractProposals(Sheet s, RE_Register targetRegister, Organization sponsor) {
		return extractProposals(s, targetRegister, sponsor, new ArrayList<RegisterItemProposalDTO>(), new HashMap<String, RegisterItemProposalDTO>());
	}
		
	protected Collection<RegisterItemProposalDTO> extractProposals(Sheet s, RE_Register targetRegister, Organization sponsor, List<RegisterItemProposalDTO> topLevelProposals, Map<String, RegisterItemProposalDTO> proposals) {
		SheetConfiguration sheetConfig = sheetConfigurationForName(s.getSheetName());
		Row headingRow = s.getRow(sheetConfig.getHeaderRow() - 1);
		
		int row = sheetConfig.getFirstDataRow() - 1;
		while (true) {
			AtomicBoolean isSubProposal = new AtomicBoolean();
			
			Row r = s.getRow(row);
			String rowId = null;
			try {
				rowId = Double.toString(r.getCell(columnNameToIndex(sheetConfig.getIdColumn())).getNumericCellValue());
			}
			catch (Throwable t) {
				// Ignore
			}
			if (rowId == null) {
				try {
					rowId = r.getCell(columnNameToIndex(sheetConfig.getIdColumn())).getStringCellValue();
				}
				catch (Throwable t) {
					
				}
			}

			String first = getString(r.getCell(columnNameToIndex(sheetConfig.getFirstDataColumn())));
			if (StringUtils.isEmpty(first)) {
				break;
			}
			
			if (rowId == null) {
				throw new RuntimeException(String.format("Empty ID column in non-empty row %d of sheet %s", row + 1, s.getSheetName()));
			}

			final RegisterItemProposalDTO proposal = extractProposal(r, headingRow, targetRegister, isSubProposal, proposals, sheetConfig);
			if (proposal == null) {
				// proposal to be ignored
				row++;
				continue;
			}
			
			proposal.setSponsorUuid(sponsor.getSubmittingOrganization().getUuid());
			proposals.put(rowId, proposal);
			if (!isSubProposal.get()) {
				topLevelProposals.add(proposal);
			}
			
			row++;
		}

		return topLevelProposals;
	}
	
	private static final String PROPERTY_ACTION = "#action";
	private static final String ACTION_IGNORE = "I";
	private static final String ACTION_RETIRE = "R";

	protected RegisterItemProposalDTO extractProposal(Row r, Row headingRow, RE_Register targetRegister, AtomicBoolean isTopLevel, Map<String, RegisterItemProposalDTO> proposals, SheetConfiguration sheetConfiguration) {
		final Map<String, String> valueMap = extractData(r, headingRow, sheetConfiguration);

		String proposalAction = null;
		if (valueMap.containsKey(PROPERTY_ACTION)) {
			proposalAction = valueMap.get(PROPERTY_ACTION);
		}
		if (ACTION_IGNORE.equalsIgnoreCase(proposalAction)) {
			return null;
		}
		
		final String itemClassName;
		if (sheetConfiguration.getItemClassName().isEmpty()) {
			// no item class, cannot create proposal
			return null;
		}
		if (sheetConfiguration.getItemClassName().size() > 1) {
			// TODO
			throw new RuntimeException("not yet implemented");
		}
		else {
			itemClassName = sheetConfiguration.getItemClassName().get(0);
		}
		final RE_ItemClass itemClass = itemClassRepository.findByName(itemClassName);

		RegisterItemProposalDTO proposal = dtoFactory.getProposalDto(itemClass);
		proposal.setItemClassName(itemClassName);
		proposal.setTargetRegisterUuid(targetRegister.getUuid());
		
		if (ACTION_RETIRE.equalsIgnoreCase(proposalAction)) {
			proposal.setProposalType(ProposalType.RETIREMENT);
		}

		proposal = mapData(valueMap, proposal, isTopLevel, r, proposals, sheetConfiguration);
		
		return proposal;
	}

	private Map<String, String> extractData(Row row, Row headingRow, SheetConfiguration sheetConfig) {
		Map<String, String> result = new HashMap<>();
				
		int i = columnNameToIndex(sheetConfig.getFirstDataColumn());
		Cell headingCell;
		Cell valueCell;
		do {
			valueCell = row.getCell(i);
			headingCell = headingRow.getCell(i);
			if (headingCell == null /*|| headingCell.getCellComment() == null*/) {
				break;
			}

			ColumnConfiguration column = null;
			for (ColumnConfiguration colConfig : sheetConfig.getColumn()) {
				if (colConfig.getColumnName().equals(CellReference.convertNumToColString(i))) {
					column = colConfig;
					break;
				}
			}
			if (column == null) {
				i++;
				continue;
			}
						
			String fieldName = column.getJavaProperty();
			String value = getString(valueCell);
			
			if (!StringUtils.isEmpty(column.getReferences()) && !StringUtils.isEmpty(value)) {
				if (value.indexOf(" ") > -1) {
					value = value.substring(0, value.indexOf(" "));
				}
			}
			
			result.put(fieldName, value);
			i++;
		} while (headingCell.getCellType() != Cell.CELL_TYPE_BLANK);
		
		return result;
	}
	
	public <T> T mapData(Map<String, String> data, T proposal, AtomicBoolean isSubProposal, Row row, Map<String, RegisterItemProposalDTO> proposals, SheetConfiguration currentSheetConfiguration) {
		for (String property : data.keySet()) {
			try {
				Object current = proposal;
				
				ColumnConfiguration column = null;
				for (ColumnConfiguration columnConfiguration : currentSheetConfiguration.getColumn()) {
					if (columnConfiguration.getJavaProperty().equals(property)) {
						column = columnConfiguration;
						break;
					}
				}
				if (column == null) {
					throw new RuntimeException(String.format("No column configuration for property %s", property));
				}
	
				String[] pathParts = StringUtils.split(property, ".");
				ArrayDeque<String> parts = new ArrayDeque<>();
				if (pathParts != null) {
					parts.addAll(Arrays.asList(pathParts));
				}
				else {
					parts.add(property);
				}
				PropertyDescriptor pd = null;
				while (!parts.isEmpty()) {
					String subProperty = parts.pollFirst();
					pd = BeanUtils.getPropertyDescriptor(current.getClass(), subProperty);
					if (pd == null) {
						System.out.println(String.format("Property %s not found on object of type %s", subProperty, current.getClass().getCanonicalName()));
						break;
					}

					if (data.get(property) == null) continue;

					if (property.equals("informationSource")) {
						new Object();
					}

					if (pd.getName().equals("uuid") && proposal instanceof RegisterItemProposalDTO) {
						UUID uuid = UUID.fromString(data.get(property));
						((RegisterItemProposalDTO)proposal).setReferencedItemUuid(uuid);
					}
					else if (UUID.class.equals(pd.getPropertyType())) {
						try {
							UUID uuid = UUID.fromString(data.get(property));
							RE_RegisterItem item = itemRepository.findOne(uuid);
//							if (item != null && item instanceof OrganizationItem) {
//								OrganizationItemProposalDTO parent = new OrganizationItemProposalDTO(uuid);
//								parent.setReferencedItemUuid(uuid);
//								((HierarchicalRegisterItemProposalDTO)proposal).setParentItem(parent);
//							}
						}
						catch (IllegalArgumentException e) {
							try {
								Integer index = Integer.parseInt(data.get(property));
								if (proposals.keySet().contains(index)) {
									RegisterItemProposalDTO parentProposal = proposals.get(index);
//									if (parentProposal instanceof HierarchicalRegisterItemProposalDTO) {
//										((HierarchicalRegisterItemProposalDTO)parentProposal).addContainedItem((HierarchicalRegisterItemProposalDTO)proposal);
//										isSubProposal.set(true);
//									}
								}
							}
							catch (NumberFormatException e1) {
								// Ignore
							}
						}
					}
//					else if (!BeanUtils.isSimpleValueType(pd.getPropertyType())) { 
					else if (!StringUtils.isEmpty(column.getReferences())) { 
						Object subBean = pd.getReadMethod().invoke(current);
						
						// Value in cell is expected to be comma-separated list of subsheet's row identifiers (first column)
						String[] references = StringUtils.delimitedListToStringArray(data.get(property), column.getSeparatorChar());
						
						for (String reference : references) {
							reference = reference.trim();
							
							if (org.apache.commons.lang3.StringUtils.isNumeric(reference)) {
								// Numeric reference is item identifier
								Integer identifier = Integer.parseInt(reference);
								IdentifiedItem referencedItem = identifiedItemRepository.findByIdentifier(identifier);
								
								RegisterItemProposalDTO refDto;
								try {
									refDto = dtoFactory.getProposalDto(referencedItem);
								}
								catch (ClassNotFoundException e) {
									throw new RuntimeException(e.getMessage(), e);
								}
								
								if (subBean instanceof Collection) {
									((Collection)subBean).add(refDto);
								}
								else {
									pd.getWriteMethod().invoke(current, refDto);
								}
							}
							else {
								// Inter-sheet reference
								Sheet subSheet = null;
								SheetConfiguration subSheetConfiguration = null;
								for (SheetConfiguration otherSheetConfig : configuration.getSheet()) {
									if (otherSheetConfig.getSheetName().equals(column.getReferences())) {
										subSheet = row.getSheet().getWorkbook().getSheet(otherSheetConfig.getSheetName());
										subSheetConfiguration = otherSheetConfig;
										break;
									}
								}
								if (subSheet == null) {
									throw new RuntimeException(String.format("Sheet with values for property '%s' not found in workbook", property));
								}

								Row headingRow = subSheet.getRow(subSheetConfiguration.getHeaderRow() - 1);
								// Find row
								String referencedRowIdx = null;
								Row subRow = null;
								int i = subSheetConfiguration.getFirstDataRow() - 1;
								while (true) {
									subRow = subSheet.getRow(i);
									Cell idCell = subRow.getCell(columnNameToIndex(subSheetConfiguration.getIdColumn()));
									if (idCell.getCellType() == Cell.CELL_TYPE_BLANK) {
										break;
									}
									else if (idCell.getCellType() == Cell.CELL_TYPE_STRING) {
										if (idCell.getStringCellValue().equals(reference)) {
											referencedRowIdx = idCell.getStringCellValue();
											break;
										}
									}
									else {
										throw new RuntimeException(String.format("Cell type %d in ID column %s of row %d not supported", idCell.getCellType(), subSheetConfiguration.getIdColumn(), i));
									}

									i++;
								}
								
								if (subRow == null) continue;

								Cell idCell = row.getCell(columnNameToIndex(currentSheetConfiguration.getIdColumn()));

								if (proposals.keySet().contains(referencedRowIdx)) {
									RegisterItemProposalDTO dependentProposal = proposals.get(referencedRowIdx);
									if (subBean instanceof Collection) {
										((Collection)subBean).add(dependentProposal);
									}
									else {
										pd.getWriteMethod().invoke(current, dependentProposal);
									}
								}
								else if (referencedRowIdx.equals(getString(idCell))) {
									pd.getWriteMethod().invoke(current, current);
								}
								else {
									Map<String, String> subData = extractData(subRow, headingRow, subSheetConfiguration);
									if (subBean instanceof Collection) { 
										Type t = ((ParameterizedType)ReflectionUtils.findField(proposal.getClass(), pd.getName()).getGenericType()).getActualTypeArguments()[0];
										Object subBeanElement = BeanUtils.instantiate((Class)t);
										subBeanElement = mapData(subData, subBeanElement, isSubProposal, subRow, proposals, subSheetConfiguration);
										((Collection)subBean).add(subBeanElement);
									}
									else {
										if (subBean == null) { 
											Class<?> propertyType = pd.getPropertyType();
											subBean = BeanUtils.instantiate(propertyType);
										}
										subBean = mapData(subData, subBean, isSubProposal, subRow, proposals, subSheetConfiguration);
										pd.getWriteMethod().invoke(current, subBean);
									}
								}
							}
						}
					}
					else {
						if (column.isMultiple()) {
							String[] values = StringUtils.delimitedListToStringArray(data.get(property), column.getSeparatorChar());
							for (String value : values) {
								((Collection)pd.getReadMethod().invoke(current)).add(value);
							}
						}
						else {
							if (data.get(property) != null && conversionService.canConvert(data.get(property).getClass(), pd.getPropertyType())) {
								Object setterValue = conversionService.convert(data.get(property), pd.getPropertyType());
								pd.getWriteMethod().invoke(current, setterValue);
							}
						}
					}
				}
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
		return proposal;
	}
	
	private int columnNameToIndex(String columnName) {
		return CellReference.convertColStringToIndex(columnName);
	}
	
	private SheetConfiguration sheetConfigurationForName(String sheetName) {
		for (SheetConfiguration sheetConfiguration : configuration.getSheet()) {
			if (sheetConfiguration.getSheetName().equals(sheetName)) {
				return sheetConfiguration;
			}
		}
		
		return null;
	}
	
	public void setConfiguration(ExcelConfiguration configuration) {
		this.configuration = configuration;
	}
}
