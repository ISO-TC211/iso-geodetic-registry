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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.iso.registry.api.registry.registers.gcp.CitationDTO;
import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.operation.ParameterValueDTO;
import org.iso.registry.api.registry.registers.gcp.operation.ParameterValueTypeMapper;
import org.iso.registry.core.model.GlobalIdentifiedItemRepository;
import org.iso.registry.core.model.IdentifiedItem;
import org.iso.registry.core.model.crs.AreaItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.iso19136.CodeType;
import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;
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

import de.bespire.registry.io.excel.ValueMapper;
import de.bespire.registry.io.excel.configuration.ColumnConfiguration;
import de.bespire.registry.io.excel.configuration.ExcelConfiguration;
import de.bespire.registry.io.excel.configuration.SheetConfiguration;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.api.ViewBeanFactory;
import de.geoinfoffm.registry.core.Entity;
import de.geoinfoffm.registry.core.ItemClass;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
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
	private ViewBeanFactory viewBeanFactory;
	
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
				return Double.valueOf(cell.getNumericCellValue()).toString();
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
		Map<String, String> knownExtents = new HashMap<>();
		Map<String, String> knownCitations = new HashMap<>();
		Map<String, Integer> rows = new HashMap<>();
		for (SheetConfiguration config : configuration.getSheet()) {
			rows.put(config.getSheetName(), config.getFirstDataRow() - 1);
		}
		
		logger.debug("Beginning export of {} {}", entities.size(), entities.size() == 1 ? "entity" : "entities");
		
		for (Entity entity : entities) {
			if (entity instanceof AreaItem) continue;
			if (!visitedEntities.containsKey(entity.getUuid())) {
				logger.debug("Exporting entity {} ['{}']", entity.getUuid(), entity.toString());
				exportEntity(entity, workbook, rows, visitedEntities, knownExtents, knownCitations);
			}
			else {
				logger.debug("Skipping previously exported entity {} ['']", entity.getUuid(), entity.toString());
			}
		}
	}
	
	private SheetConfiguration findSheetConfiguration(Class<?> entityClass) {
		return findSheetConfiguration(entityClass, null);
	}
	
	private SheetConfiguration findSheetConfiguration(Class<?> entityClass, String itemClassName) {
		for (SheetConfiguration config : configuration.getSheet()) {
			if (config.getExportType().equals(entityClass.getCanonicalName())) {
				if (!StringUtils.isEmpty(itemClassName) && !config.getItemClassName().contains(itemClassName)) {
					continue;
				}
				else {
					return config;
				}
			}
		}
		
		if (entityClass.getSuperclass() == null) {
			return null;
		}
		else {
			return findSheetConfiguration(entityClass.getSuperclass());
		}
	}
	
	private Cell exportEntity(Object entity, Workbook workbook, Map<String, Integer> rows, Map<UUID, String> visitedEntities, Map<String, String> knownExtents, Map<String, String> knownCitations) {
		if (entity instanceof EX_Extent) {
			entity = new ExtentDTO((EX_Extent)entity);
		}

		SheetConfiguration sheetConfiguration;
		if (entity instanceof RE_RegisterItem) {
			ItemClass itemClassAnnotation = entity.getClass().getAnnotation(ItemClass.class);
			if (itemClassAnnotation != null) {
				sheetConfiguration = findSheetConfiguration(entity.getClass(), itemClassAnnotation.value());
			}
			else {
				sheetConfiguration = findSheetConfiguration(entity.getClass());
			}
		}
		else {
			sheetConfiguration = findSheetConfiguration(entity.getClass());
		}
		if (sheetConfiguration == null) {
			if (entity instanceof RE_RegisterItem) {
				ItemClass itemClassAnnotation = entity.getClass().getAnnotation(ItemClass.class);
				RegisterItemViewBean viewBean = viewBeanFactory.getViewBean((RE_RegisterItem)entity);
				if (itemClassAnnotation != null) {
					sheetConfiguration = findSheetConfiguration(viewBean.getClass(), itemClassAnnotation.value());
				}
				else {
					sheetConfiguration = findSheetConfiguration(viewBean.getClass());
				}
				if (sheetConfiguration == null) {
					return null;
				}
				else {
					entity = viewBean;
				}
			}
			else if (entity instanceof CitationDTO) {
				sheetConfiguration = findSheetConfiguration(CI_Citation.class);
			}
			else if (entity instanceof ExtentDTO) {
				sheetConfiguration = findSheetConfiguration(EX_Extent.class);
			}
			else if (entity instanceof RegisterItemViewBean) {
				sheetConfiguration = findSheetConfiguration(((RegisterItemViewBean)entity).getItemClass());
			}
			else {
				return null;
			}
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
			logger.debug(">>> Assigned ID '{}'", idBuilder.toString());
			
			try {
				logger.debug(">>> UUID = {}", bw.getPropertyValue("uuid"));
				visitedEntities.put((UUID)bw.getPropertyValue("uuid"), idBuilder.toString());
			}
			catch (Exception e) {
				// Ignore
			}
//		}
		for (ColumnConfiguration columnConfiguration : sheetConfiguration.getColumn()) {
			String javaProperty = columnConfiguration.getJavaPropertyExport();
			if (StringUtils.isEmpty(javaProperty)) {
				javaProperty = columnConfiguration.getJavaProperty(); 
			}
			
			if ("justification".equals(javaProperty)) {
				new Object();
			}
			
			String typeRestriction = "";
			if (javaProperty.startsWith("(")) {
				typeRestriction = javaProperty.substring(1, javaProperty.indexOf(")"));
				javaProperty = javaProperty.substring(javaProperty.indexOf(")" + 1));
			}
			
			Cell valueCell = valueRow.getCell(columnNameToIndex(columnConfiguration.getColumnName()), Row.CREATE_NULL_AS_BLANK);
			if (javaProperty.equals("#itemClassName") && (entity instanceof RE_RegisterItem || entity instanceof RegisterItemViewBean)) {
				String itemClassName;
				if (entity instanceof RE_RegisterItem) {
					itemClassName = ((RE_RegisterItem)entity).getItemClass().getName();
				}
				else {
					itemClassName = ((RegisterItemViewBean)entity).getItemClassName();					
				}
				
				if (!StringUtils.isEmpty(columnConfiguration.getValueMapper())) {
					try {
						@SuppressWarnings({ "unchecked", "rawtypes" })
						Class<ValueMapper> valueMapperClass = (Class<ValueMapper>)Class.forName(columnConfiguration.getValueMapper());
						Method mapMethod = valueMapperClass.getMethod("mapInverse", String.class);
						ValueMapper valueMapper = BeanUtils.instantiate(valueMapperClass);
						itemClassName = (String)mapMethod.invoke(valueMapper, itemClassName);
					}
					catch (Throwable t) {
						logger.debug(">>> Unable to invoke ValueMapper {}: {}", columnConfiguration.getValueMapper(), t.getMessage());
					}
				}
				valueCell.setCellValue(itemClassName);
			}
			else if (javaProperty.equals("justification")) {
				if (entity instanceof RegisterItemViewBean) {
					valueCell.setCellValue(((RegisterItemViewBean)entity).getJustification());
				}
				else if (entity instanceof RE_RegisterItem) {
					RegisterItemViewBean viewBean = viewBeanFactory.getViewBean((RE_RegisterItem)entity);
					valueCell.setCellValue(viewBean.getJustification());
				}
				else {
					valueCell.setCellValue("Import from EPSG Geodetic Parameter Data Set v8.9");					
				}
			}
			else if (javaProperty.equals("controlBodyNotes")) {
				if (entity instanceof RegisterItemViewBean) {
					valueCell.setCellValue(((RegisterItemViewBean)entity).getControlBodyNotes());
				}
				else if (entity instanceof RE_RegisterItem) {
					RegisterItemViewBean viewBean = viewBeanFactory.getViewBean((RE_RegisterItem)entity);
					valueCell.setCellValue(viewBean.getControlBodyNotes());
				}
				else {
					valueCell.setCellValue("Import from EPSG Geodetic Parameter Data Set v8.9");					
				}
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
						
						if (propertyValue != null && !StringUtils.isEmpty(columnConfiguration.getValueMapper())) {
							try {
								@SuppressWarnings({ "unchecked", "rawtypes" })
								Class<ValueMapper> valueMapperClass = (Class<ValueMapper>)Class.forName(columnConfiguration.getValueMapper());
								Method mapMethod = valueMapperClass.getMethod("mapInverse", propertyValue.getClass());
								ValueMapper valueMapper = BeanUtils.instantiate(valueMapperClass);
								propertyValue = mapMethod.invoke(valueMapper, propertyValue);
							}
							catch (Throwable t) {
								logger.debug(">>> Unable to invoke ValueMapper {}: {}", columnConfiguration.getValueMapper(), t.getMessage());
							}
						}
					
						if (propertyValue instanceof String) {
							valueCell.setCellValue((String)propertyValue);
							logger.debug(">>> {} = '{}' (String)", javaProperty, (String)propertyValue); 
						}
						else if (propertyValue instanceof File) {
							valueCell.setCellValue(((File)propertyValue).getName());
							logger.debug(">>> {} = '{}' (File)", javaProperty, ((File)propertyValue).getName()); 							
						}
						else if (propertyValue instanceof Enum) {
							valueCell.setCellValue(((Enum)propertyValue).name());
							logger.debug(">>> {} = '{}' (Enum)", javaProperty, ((Enum)propertyValue).name()); 
						}
						else if (propertyValue instanceof CodeType) {
							valueCell.setCellValue(((CodeType)propertyValue).getCode());
							logger.debug(">>> {} = '{}' (Code)", javaProperty, ((CodeType)propertyValue).getCode()); 
						}
						else if (propertyValue instanceof Number) {
							valueCell.setCellValue(Double.valueOf(propertyValue.toString()));
							logger.debug(">>> {} = '{}' (Double)", javaProperty, Double.valueOf(propertyValue.toString())); 
						}
//						else if (propertyValue instanceof IdentifiedItem) {
//							valueCell.setCellValue(((IdentifiedItem)propertyValue).getIdentifier().toString());
//						}
						else if (propertyValue instanceof UUID) {
							valueCell.setCellValue(((UUID)propertyValue).toString());
							logger.debug(">>> {} = '{}' (UUID)", javaProperty, ((UUID)propertyValue).toString()); 
						}
						else if (propertyValue instanceof Boolean) {
							valueCell.setCellValue((Boolean)propertyValue);
							logger.debug(">>> {} = '{}' (Boolean)", javaProperty, (Boolean)propertyValue); 
						}
						else if (propertyValue instanceof Date) {
							valueCell.setCellValue((Date)propertyValue);
							logger.debug(">>> {} = '{}' (Date)", javaProperty, DateTimeFormat.fullDateTime().print(((Date)propertyValue).getTime())); 
						}
						else if (propertyValue instanceof Collection) {
							logger.debug(">>> {} (Collection)", javaProperty); 
							Collection<Object> propertyCollection = (Collection<Object>)propertyValue;
							if (propertyCollection.isEmpty()) continue;
							
							StringBuilder valueBuilder = new StringBuilder();
							for (Object value : propertyCollection) {
								if (valueBuilder.length() > 0) {
									valueBuilder.append(columnConfiguration.getSeparatorChar());
								}
								if (value instanceof Entity) {
									Entity subEntity = (Entity)value;
									if (subEntity instanceof EX_Extent || subEntity instanceof CI_Citation) {
										handleSpecialSubEntity(subEntity, valueBuilder, workbook, rows, visitedEntities, knownExtents, knownCitations);										
									}
									else {
										if (visitedEntities.containsKey(subEntity.getUuid())) {
											valueBuilder.append(visitedEntities.get(subEntity.getUuid()));
										}
										else {
											Cell targetId = exportEntity(subEntity, workbook, rows, visitedEntities, knownExtents, knownCitations);
											valueBuilder.append(getString(targetId));
										}
									}
								}
								else if (value instanceof RegisterItemViewBean) {
									RegisterItemViewBean subEntity = (RegisterItemViewBean)value;
									if (visitedEntities.containsKey(subEntity.getUuid())) {
										valueBuilder.append(visitedEntities.get(subEntity.getUuid()));
									}
									else {
										Cell targetId = exportEntity(subEntity, workbook, rows, visitedEntities, knownExtents, knownCitations);
										valueBuilder.append(getString(targetId));
									}
								}
								else if (value instanceof CitationDTO || value instanceof ExtentDTO || value instanceof ParameterValueDTO) {
									handleSpecialSubEntity(value, valueBuilder, workbook, rows, visitedEntities, knownExtents, knownCitations);
								}
								else if (BeanUtils.isSimpleValueType(value.getClass())) {
									valueBuilder.append(value.toString());
								}
								else {
									handleSpecialSubEntity(value, valueBuilder, workbook, rows, visitedEntities, knownExtents, knownCitations);
								}
							}
							valueCell.setCellValue(valueBuilder.toString());
							logger.debug(">>>>> = {}", valueBuilder.toString());
						}
						else if (propertyValue instanceof ExtentDTO || propertyValue instanceof CitationDTO || propertyValue instanceof ParameterValueDTO) {
							StringBuilder valueBuilder = new StringBuilder();
							handleSpecialSubEntity(propertyValue, valueBuilder, workbook, rows, visitedEntities, knownExtents, knownCitations);							
							valueCell.setCellValue(valueBuilder.toString());
						}
						else if (propertyValue instanceof Entity) {
							StringBuilder valueBuilder = new StringBuilder();
							
							Entity subEntity = (Entity)propertyValue;
							if (subEntity instanceof EX_Extent || subEntity instanceof CI_Citation) {
								handleSpecialSubEntity(subEntity, valueBuilder, workbook, rows, visitedEntities, knownExtents, knownCitations);
							}
							else {
								if (visitedEntities.containsKey(subEntity.getUuid())) {
									valueBuilder.append(visitedEntities.get(subEntity.getUuid()));
								}
								else {
									Cell targetId;
									if (subEntity == entity) {
										// self reference
										targetId = idCell;
									}
									else {
										targetId = exportEntity(subEntity, workbook, rows, visitedEntities, knownExtents, knownCitations);
									}
									valueBuilder.append(getString(targetId));
								}
							}
							
							logger.debug(">>> {} = {}", javaProperty, valueBuilder.toString());
							valueCell.setCellValue(valueBuilder.toString());
						}
						else if (propertyValue instanceof RegisterItemViewBean) {
							StringBuilder valueBuilder = new StringBuilder();

							RegisterItemViewBean viewBean = (RegisterItemViewBean)propertyValue;
							if (visitedEntities.containsKey(viewBean.getUuid())) {
								valueBuilder.append(visitedEntities.get(viewBean.getUuid()));
							}
							else {
								Cell targetId;
								if (viewBean == entity) {
									// self reference
									targetId = idCell;
								}
								else {
									targetId = exportEntity(viewBean, workbook, rows, visitedEntities, knownExtents, knownCitations);
								}
								valueBuilder.append(getString(targetId));
							}

							logger.debug(">>> {} = {}", javaProperty, valueBuilder.toString());
							valueCell.setCellValue(valueBuilder.toString());
						}
						else if (propertyValue != null) {
							logger.debug(">>> Value of type {} was not handled by exportEntity", propertyValue.getClass().getCanonicalName());
						}
						else {
							logger.debug(">>> {} = null", javaProperty);
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
	
	private void handleSpecialSubEntity(Object subEntity, StringBuilder valueBuilder, Workbook workbook, Map<String, Integer> rows, Map<UUID, String> visitedEntities, Map<String, String> knownExtents, Map<String, String> knownCitations) {
		Map<String, String> knownMap;
		String entityKey;
		if (subEntity instanceof EX_Extent) {
			EX_Extent extent = (EX_Extent)subEntity;
			knownMap = knownExtents;
			entityKey = extent.getDescription();
		}
		else if (subEntity instanceof ExtentDTO) {
			ExtentDTO extent = (ExtentDTO)subEntity;
			knownMap = knownExtents;
			entityKey = extent.getDescription();			
		}
		else if (subEntity instanceof CI_Citation) {
			knownMap = knownCitations;
			CI_Citation citation = (CI_Citation)subEntity;
			entityKey = citation.getTitle();
		}
		else if (subEntity instanceof CitationDTO) {
			knownMap = knownCitations;
			CitationDTO citation = (CitationDTO)subEntity;
			entityKey = citation.getTitle();
		}
		else if (subEntity instanceof ParameterValueDTO) {
			knownMap = new HashMap<>();
			ParameterValueDTO value = (ParameterValueDTO)subEntity;
			entityKey = value.getParameter().getName() + "=" + value.getValue();
		}
		else {
			knownMap = new HashMap<>();
			entityKey = subEntity.toString();
		}
		
		if (entityKey == null) {
			return;
		}
		else if (knownMap.containsKey(entityKey)) { 
			valueBuilder.append(knownMap.get(entityKey));
		}
		else {
			Cell targetId = exportEntity(subEntity, workbook, rows, visitedEntities, knownExtents, knownCitations);
			knownMap.put(entityKey, getString(targetId));
			valueBuilder.append(getString(targetId));
		}
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

	public Collection<RegisterItemProposalDTO> extractProposals(Sheet s, RE_Register targetRegister, Organization sponsor, Map<String, RegisterItemProposalDTO> proposals) {
		return extractProposals(s, targetRegister, sponsor, new ArrayList<RegisterItemProposalDTO>(), proposals);
	}

	protected Collection<RegisterItemProposalDTO> extractProposals(Sheet s, RE_Register targetRegister, Organization sponsor, List<RegisterItemProposalDTO> topLevelProposals, Map<String, RegisterItemProposalDTO> proposals) {
		SheetConfiguration sheetConfig = sheetConfigurationForName(s.getSheetName());
		if (sheetConfig == null) {
			logger.debug("Configuration for sheet '{}' not available, skipping...", s.getSheetName());
			return topLevelProposals;
		}
		if (sheetConfig.getSheetName().equals("OpMethod(OM#)")) {
			new Object();
		}
		Row headingRow = s.getRow(sheetConfig.getHeaderRow() - 1);
		
		int row = sheetConfig.getFirstDataRow() - 1;
		while (true) {
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
			
			final RegisterItemProposalDTO proposal;
			if (proposals.containsKey(rowId)) {
				proposal = proposals.get(rowId);
			}
			else {
				logger.debug(">>> Extracting data from row {}...", row + 1);
				proposal = extractProposal(r, headingRow, targetRegister, proposals, sheetConfig);
			}
			
			if (proposal == null) {
				// proposal to be ignored
				row++;
				continue;
			}
			
			proposal.setSponsorUuid(sponsor.getSubmittingOrganization().getUuid());
			proposals.put(rowId, proposal);
			topLevelProposals.add(proposal);
			
			row++;
		}

		return topLevelProposals;
	}
	
	private static final String PROPERTY_ACTION = "#action";
	private static final String ACTION_IGNORE = "I";
	private static final String ACTION_RETIRE = "R";

	protected RegisterItemProposalDTO extractProposal(Row r, Row headingRow, RE_Register targetRegister, Map<String, RegisterItemProposalDTO> proposals, SheetConfiguration sheetConfiguration) {
		final Map<String, String> valueMap = extractData(r, headingRow, sheetConfiguration);

		String proposalAction = null;
		if (valueMap.containsKey(PROPERTY_ACTION)) {
			proposalAction = valueMap.get(PROPERTY_ACTION);
		}
		if (ACTION_IGNORE.equalsIgnoreCase(proposalAction)) {
			return null;
		}
		
		String itemClassName = null;
		if (sheetConfiguration.getItemClassName().isEmpty()) {
			// no item class, cannot create proposal
			return null;
		}
		if (sheetConfiguration.getItemClassName().size() > 1) {
			// Find #itemClassName column
			for (ColumnConfiguration column : sheetConfiguration.getColumn()) {
				if (column.getJavaProperty().equals("#itemClassName")) {
					itemClassName = r.getCell(columnNameToIndex(column.getColumnName())).getStringCellValue();

					if (!StringUtils.isEmpty(column.getValueMapper())) {
						try {
							@SuppressWarnings({ "unchecked", "rawtypes" })
							Class<ValueMapper> valueMapperClass = (Class<ValueMapper>)Class.forName(column.getValueMapper());
							Method mapMethod = valueMapperClass.getMethod("map", String.class);
							ValueMapper valueMapper = BeanUtils.instantiate(valueMapperClass);
							itemClassName = (String)mapMethod.invoke(valueMapper, itemClassName);
						}
						catch (Throwable t) {
							logger.debug(">>> Unable to invoke ValueMapper {}: {}", column.getValueMapper(), t.getMessage());
						}
					}

					break;
				}
			}
			if (itemClassName == null) {
				throw new RuntimeException(String.format("Multiple item classes configured for sheet %s but no '#itemClassName' property found", r.getSheet().getSheetName()));
			}
		}
		else {
			itemClassName = sheetConfiguration.getItemClassName().get(0);
		}
		final RE_ItemClass itemClass = itemClassRepository.findByName(itemClassName);
		if (itemClass == null) {
			throw new RuntimeException(String.format("Item class '%s' does not exist", itemClassName));
		}
		
		RegisterItemProposalDTO proposal = dtoFactory.getProposalDto(itemClass);
		proposal.setItemClassName(itemClassName);
		proposal.setTargetRegisterUuid(targetRegister.getUuid());
		
		if (ACTION_RETIRE.equalsIgnoreCase(proposalAction)) {
			proposal.setProposalType(ProposalType.RETIREMENT);
		}

		proposal = mapData(valueMap, proposal, r, proposals, sheetConfiguration);
		
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
			
			if (!column.getReferences().isEmpty() && !StringUtils.isEmpty(value)) {
				if (value.indexOf(" ") > -1) {
					value = value.substring(0, value.indexOf(" "));
				}
			}
			
			if (result.get(fieldName) != null) {
				if (!StringUtils.isEmpty(value)) {
					throw new RuntimeException(String.format("Multiple values defined for field '%s' in sheet '%s'", fieldName, sheetConfig.getSheetName()));
				}
			}
			else {
			result.put(fieldName, value);
			}
			i++;
		} while (headingCell.getCellType() != Cell.CELL_TYPE_BLANK);
		
		return result;
	}
	
	private <T> void mapReference(T proposal, String property, String value, Row row, ColumnConfiguration column, SheetConfiguration currentSheetConfiguration, Map<String, RegisterItemProposalDTO> proposals) {
		BeanWrapper bw = new BeanWrapperImpl(proposal);

		bw.getPropertyDescriptor(property); // Assert that property exists

		// Value in cell is expected to be separated list of subsheet's row identifiers
		String[] references = StringUtils.delimitedListToStringArray(value, column.getSeparatorChar());
		
		if ("parameterValues".equals(property)) {
			new Object();
		}
		
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
				
				mapValue(bw, column.getJavaProperty(), refDto, column);
			}
			else {
				// Inter-sheet reference
				Sheet subSheet = null;
				SheetConfiguration subSheetConfiguration = null;
				
				String idPrefix = reference.substring(0, 2);
				for (SheetConfiguration otherSheetConfig : configuration.getSheet()) {
					if (otherSheetConfig.getIdPrefix().equalsIgnoreCase(idPrefix)) {
						subSheet = row.getSheet().getWorkbook().getSheet(otherSheetConfig.getSheetName());
						subSheetConfiguration = otherSheetConfig;
						break;
					}
				}
				if (subSheet == null) {
					throw new RuntimeException(String.format("Sheet with values for property '%s' not found in workbook", column.getJavaProperty()));
				}

				Row headingRow = subSheet.getRow(subSheetConfiguration.getHeaderRow() - 1);
				// Find row
				String referencedRowIdx = null;
				Row subRow = null;
				int i = subSheetConfiguration.getFirstDataRow() - 1;
				while (true) {
					subRow = subSheet.getRow(i);
					Cell idCell = subRow.getCell(columnNameToIndex(subSheetConfiguration.getIdColumn()));
					if (idCell == null) {
						subRow = null;
						break;
					}
					if (idCell.getCellType() == Cell.CELL_TYPE_BLANK) {
						break;
					}
					else if (idCell.getCellType() == Cell.CELL_TYPE_STRING || idCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
						if (idCell.getStringCellValue().equals(reference)) {
							referencedRowIdx = idCell.getStringCellValue();
							break;
						}
					}
					else {
						throw new RuntimeException(String.format("Cell type %d in ID column %s of row %d in sheet %s not supported", idCell.getCellType(), subSheetConfiguration.getIdColumn(), i, idCell.getSheet().getSheetName()));
					}

					i++;
				}
				
				if (subRow == null) continue;

				Cell idCell = row.getCell(columnNameToIndex(currentSheetConfiguration.getIdColumn()));

				if (proposals.keySet().contains(referencedRowIdx)) {
					RegisterItemProposalDTO dependentProposal = proposals.get(referencedRowIdx);
					mapValue(bw, column.getJavaProperty(), dependentProposal, column);
				}
				else if (referencedRowIdx.equals(getString(idCell))) {
					mapValue(bw, column.getJavaProperty(), proposal, column);
				}
				else {
					Map<String, String> referencedData = extractData(subRow, headingRow, subSheetConfiguration);
					if (Collection.class.isAssignableFrom(bw.getPropertyType(property))) {
						Type t = ((ParameterizedType)ReflectionUtils.findField(proposal.getClass(), property).getGenericType()).getActualTypeArguments()[0];
						Object referencedValue = BeanUtils.instantiate((Class)t);
						referencedValue = mapData(referencedData, referencedValue, subRow, proposals, subSheetConfiguration);
						mapValue(bw, property, referencedValue, column);

						if (referencedValue instanceof RegisterItemProposalDTO) {
							proposals.put(referencedRowIdx, (RegisterItemProposalDTO)referencedValue);
						}
					}
					else {
						Object referencedValue = BeanUtils.instantiate(bw.getPropertyType(property));
						referencedValue = mapData(referencedData, referencedValue, subRow, proposals, subSheetConfiguration);
						mapValue(bw, property, referencedValue, column);
						
						if (referencedValue instanceof RegisterItemProposalDTO) {
							proposals.put(referencedRowIdx, (RegisterItemProposalDTO)referencedValue);
						}
					}
				}
			}
		}
	}
	
	public <T> T mapData(Map<String, String> data, T proposal, Row row, Map<String, RegisterItemProposalDTO> proposals, SheetConfiguration currentSheetConfiguration) {
		BeanWrapper bw = new BeanWrapperImpl(proposal); 
		bw.setAutoGrowNestedPaths(true);
		
		for (String property : data.keySet()) {
			if (property.equals("axes")) {
				new Object();
			}
			try {
				String value = data.get(property);
				if (value == null) continue;
					
				ColumnConfiguration column = getColumnConfiguration(currentSheetConfiguration, property);

				if (property.startsWith("#")) {
					String specialProperty = property.substring(1);
					// check if special property exists in target object and set value if found
					if (bw.isWritableProperty(specialProperty)) {
						if (!StringUtils.isEmpty(column.getValueMapper())) {
							try {
								@SuppressWarnings({ "unchecked", "rawtypes" })
								Class<ValueMapper> valueMapperClass = (Class<ValueMapper>)Class.forName(column.getValueMapper());
								Method mapMethod = valueMapperClass.getMethod("map", String.class);
								ValueMapper valueMapper = BeanUtils.instantiate(valueMapperClass);
								Object mappedValue = mapMethod.invoke(valueMapper, value);
								value = mappedValue.toString();
							}
							catch (Throwable t) {
								logger.debug(">>> Unable to invoke ValueMapper {}: {}", column.getValueMapper(), t.getMessage());
							}
						}						
						
						mapValue(bw, specialProperty, value, column);
					}
				}
				else if (!column.getReferences().isEmpty()) {
					mapReference(proposal, property, value, row, column, currentSheetConfiguration, proposals);
				}
				else if (!StringUtils.isEmpty(column.getValueMapper())) {
					try {
						@SuppressWarnings({ "unchecked", "rawtypes" })
						Class<ValueMapper> valueMapperClass = (Class<ValueMapper>)Class.forName(column.getValueMapper());
						Method mapMethod = valueMapperClass.getMethod("map", String.class);
						ValueMapper valueMapper = BeanUtils.instantiate(valueMapperClass);
						Object mappedValue = mapMethod.invoke(valueMapper, value);
						value = mappedValue.toString();
					}
					catch (Throwable t) {
						logger.debug(">>> Unable to invoke ValueMapper {}: {}", column.getValueMapper(), t.getMessage());
					}

					mapValue(bw, property, value, column);
				}
				else {
					mapValue(bw, property, value, column);
				}
			}
			finally { 
				
			}
		}

/*		
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
						
						// Value in cell is expected to be separated list of subsheet's row identifiers
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
									else if (idCell.getCellType() == Cell.CELL_TYPE_STRING || idCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
										if (idCell.getStringCellValue().equals(reference)) {
											referencedRowIdx = idCell.getStringCellValue();
											break;
										}
									}
									else {
										throw new RuntimeException(String.format("Cell type %d in ID column %s of row %d in sheet %s not supported", idCell.getCellType(), subSheetConfiguration.getIdColumn(), i, idCell.getSheet().getSheetName()));
									}

									i++;
								}
								
								if (subRow == null) continue;

								Cell idCell = row.getCell(columnNameToIndex(currentSheetConfiguration.getIdColumn()));

								if (referencedRowIdx.equals("UM1")) {
									new Object();
								}
								if (proposals.keySet().contains(referencedRowIdx)) {
									RegisterItemProposalDTO dependentProposal = proposals.get(referencedRowIdx);
									if (subBean instanceof Collection) {
										((Collection)subBean).add(dependentProposal);
									}
									else {
										try {
											pd.getWriteMethod().invoke(current, dependentProposal);
										}
										catch (IllegalArgumentException e) {
											logger.error("Cannot set property {} in class {}: Class cast exception", pd.getName(), current.getClass().getCanonicalName());
											throw e;
										}
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

										if (subBean instanceof RegisterItemProposalDTO) {
											proposals.put(referencedRowIdx, (RegisterItemProposalDTO)subBean);
										}
									}
									else {
										if (subBean == null) { 
											Class<?> propertyType = pd.getPropertyType(); 
											subBean = BeanUtils.instantiate(propertyType);
										}
										subBean = mapData(subData, subBean, isSubProposal, subRow, proposals, subSheetConfiguration);
										pd.getWriteMethod().invoke(current, subBean);
										
										if (subBean instanceof RegisterItemProposalDTO) {
											proposals.put(referencedRowIdx, (RegisterItemProposalDTO)subBean);
										}
									}
								}
							}
						}
					}
					else {
						if (column.isMultiple()) {
							Type t = ((ParameterizedType)ReflectionUtils.findField(proposal.getClass(), pd.getName()).getGenericType()).getActualTypeArguments()[0];
							if (data.get(property).getClass().isAssignableFrom((Class)t)) {
								String[] values = StringUtils.delimitedListToStringArray(data.get(property), column.getSeparatorChar());
								for (String value : values) {
									if (((Collection)pd.getReadMethod().invoke(current)) == null) {
										new Object(); 
									}
									((Collection)pd.getReadMethod().invoke(current)).add(value);
								}
							}
							else {
								Object subBean = pd.getReadMethod().invoke(current);
								current = subBean;
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
		*/
		return proposal;
	}

	private ColumnConfiguration getColumnConfiguration(SheetConfiguration sheetConfiguration, String property) {
		ColumnConfiguration column = null;
		for (ColumnConfiguration columnConfiguration : sheetConfiguration.getColumn()) {
			if (columnConfiguration.getJavaProperty().equals(property)) {
				column = columnConfiguration;
				break;
			}
		}
		if (column == null) {
			throw new RuntimeException(String.format("No column configuration for property %s", property));
		}
		return column;
	}

	private void mapValue(BeanWrapper bw, String property, Object value, ColumnConfiguration column) {
		if (column.isMultiple()) {
			if (value == null) {
				return;
			}
			
			Object[] values;
			if (value instanceof String) {
				values = StringUtils.delimitedListToStringArray((String)value, column.getSeparatorChar());
			}
			else if (value instanceof Collection<?>) {
				values = ((Collection<?>)value).toArray();
			}
			else {
				values = new Object[] { value };
			}
			
			int i = 0;
			if (bw.getPropertyValue(property) instanceof Collection && bw.getPropertyValue(property) != null) {
				i = ((Collection)bw.getPropertyValue(property)).size();
			}
			for (Object listValue : values) {
				bw.setPropertyValue(String.format("%s[%d]", property, i), listValue);
				i++;
			}
		}
		else {
			if (bw.getPropertyDescriptor(property).getPropertyType().equals(Integer.class) && value.toString().contains(".")) {
				Double d = Double.valueOf(value.toString());
				value = d.intValue();
			}
			try {
			bw.setPropertyValue(property, value);
		}
			catch (Throwable t) {
				logger.error(MessageFormat.format("Unable to map value {0} for property {1}: {2}", value, property, t.getMessage()), t);
			}
		}
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
