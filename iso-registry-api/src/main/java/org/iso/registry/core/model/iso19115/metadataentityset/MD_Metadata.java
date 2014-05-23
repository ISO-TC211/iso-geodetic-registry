
package org.iso.registry.core.model.iso19115.metadataentityset;

/**
 * Information about the metadata
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_Metadata {

//	/**
//	 * Full name of the ISO character coding standard used for the metadata set
//	 */
//	private MD_CharacterSetCode characterSet /*= "utf8"*/;
//	/**
//	 * Party responsible for the metadata information
//	 */
//	private CI_ResponsibleParty contact;
//	private CharacterString dataSet;
//	/**
//	 * Date that the metadata were created or last updated
//	 */
//	private Date dateStamp;
//	/**
//	 * Unique identifier for this metadata file
//	 */
//	private CharacterString fileIdentifier;
//	/**
//	 * Scope to which the metadata applies (see informative Annex J for more information about metadata heirarchy levels)
//	 */
//	private MD_ScopeCode hierarchyLevel = "dataset";
//	/**
//	 * Name of the hierarchy level
//	 */
//	private CharacterString hierarchyLevelName;
//	/**
//	 * Language used for documenting metadata
//	 */
//	private CharacterString language;
//	private PT_Locale locale;
//	/**
//	 * Name of the metadata standard used
//	 */
//	private CharacterString metadataStandardName;
//	/**
//	 * Version of the metadata standard used
//	 */
//	private CharacterString metadataStandardVersion;
//	/**
//	 * Unique identifier of the parent metadata file
//	 */
//	private CharacterString parentIdentifier;
//	private Collection<DQ_DataQuality> dataQualityInfo;
//	private Collection<DS_Aggregate> series;
//	private Collection<MD_Identification> identificationInfo;
//	private Collection<MD_ApplicationSchemaInformation> applicationSchemaInfo;
//	private Collection<MD_ContentInformation> contentInfo;
//	private Collection<MD_SpatialRepresentation> spatialRepresentationInfo;
//	private Collection<MD_Constraints> metadataConstraints;
//	private Collection<MD_MetadataExtensionInformation> metadataExtensionInfo;
//	private Collection<MD_ReferenceSystem> referenceSystemInfo;
//	private MD_MaintenanceInformation metadataMaintenance;
//	private Collection<MD_PortrayalCatalogueReference> portrayalCatalogueInfo;
//	private Collection<DS_DataSet> describes;
//	private MD_Distribution distributionInfo;
//
//	public MD_ApplicationSchemaInformation getApplicationSchemaInfo(){
//		return applicationSchemaInfo;
//	}
//
//	public MD_CharacterSetCode getCharacterSet(){
//		return characterSet;
//	}
//
//	public CI_ResponsibleParty getContact(){
//		return contact;
//	}
//
//	public MD_ContentInformation getContentInfo(){
//		return contentInfo;
//	}
//
//	public DQ_DataQuality getDataQualityInfo(){
//		return dataQualityInfo;
//	}
//
//	public CharacterString getDataSet(){
//		return dataSet;
//	}
//
//	public Date getDateStamp(){
//		return dateStamp;
//	}
//
//	public DS_DataSet getDescribes(){
//		return describes;
//	}
//
//	public MD_Distribution getDistributionInfo(){
//		return distributionInfo;
//	}
//
//	public GF_AttributeType getFeatureAttribute(){
//		return featureAttribute;
//	}
//
//	public GF_FeatureType getFeatureType(){
//		return featureType;
//	}
//
//	public CharacterString getFileIdentifier(){
//		return fileIdentifier;
//	}
//
//	public MD_ScopeCode getHierarchyLevel(){
//		return hierarchyLevel;
//	}
//
//	public CharacterString getHierarchyLevelName(){
//		return hierarchyLevelName;
//	}
//
//	public MD_Identification getIdentificationInfo(){
//		return identificationInfo;
//	}
//
//	public CharacterString getLanguage(){
//		return language;
//	}
//
//	public PT_Locale getLocale(){
//		return locale;
//	}
//
//	public MD_Constraints getMetadataConstraints(){
//		return metadataConstraints;
//	}
//
//	public MD_MetadataExtensionInformation getMetadataExtensionInfo(){
//		return metadataExtensionInfo;
//	}
//
//	public MD_MaintenanceInformation getMetadataMaintenance(){
//		return metadataMaintenance;
//	}
//
//	public CharacterString getMetadataStandardName(){
//		return metadataStandardName;
//	}
//
//	public CharacterString getMetadataStandardVersion(){
//		return metadataStandardVersion;
//	}
//
//	public CharacterString getParentIdentifier(){
//		return parentIdentifier;
//	}
//
//	public MD_PortrayalCatalogueReference getPortrayalCatalogueInfo(){
//		return portrayalCatalogueInfo;
//	}
//
//	public GF_PropertyType getPropertyType(){
//		return propertyType;
//	}
//
//	public MD_ReferenceSystem getReferenceSystemInfo(){
//		return referenceSystemInfo;
//	}
//
//	public DS_Aggregate getSeries(){
//		return series;
//	}
//
//	public MD_SpatialRepresentation getSpatialRepresentationInfo(){
//		return spatialRepresentationInfo;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setApplicationSchemaInfo(MD_ApplicationSchemaInformation newVal){
//		applicationSchemaInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setCharacterSet(MD_CharacterSetCode newVal){
//		characterSet = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setContact(CI_ResponsibleParty newVal){
//		contact = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setContentInfo(MD_ContentInformation newVal){
//		contentInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setDataQualityInfo(DQ_DataQuality newVal){
//		dataQualityInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setDataSet(CharacterString newVal){
//		dataSet = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setDateStamp(Date newVal){
//		dateStamp = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setDescribes(DS_DataSet newVal){
//		describes = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setDistributionInfo(MD_Distribution newVal){
//		distributionInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setFeatureAttribute(GF_AttributeType newVal){
//		featureAttribute = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setFeatureType(GF_FeatureType newVal){
//		featureType = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setFileIdentifier(CharacterString newVal){
//		fileIdentifier = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setHierarchyLevel(MD_ScopeCode newVal){
//		hierarchyLevel = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setHierarchyLevelName(CharacterString newVal){
//		hierarchyLevelName = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setIdentificationInfo(MD_Identification newVal){
//		identificationInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setLanguage(CharacterString newVal){
//		language = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setLocale(PT_Locale newVal){
//		locale = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMetadataConstraints(MD_Constraints newVal){
//		metadataConstraints = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMetadataExtensionInfo(MD_MetadataExtensionInformation newVal){
//		metadataExtensionInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMetadataMaintenance(MD_MaintenanceInformation newVal){
//		metadataMaintenance = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMetadataStandardName(CharacterString newVal){
//		metadataStandardName = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMetadataStandardVersion(CharacterString newVal){
//		metadataStandardVersion = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setParentIdentifier(CharacterString newVal){
//		parentIdentifier = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setPortrayalCatalogueInfo(MD_PortrayalCatalogueReference newVal){
//		portrayalCatalogueInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setPropertyType(GF_PropertyType newVal){
//		propertyType = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setReferenceSystemInfo(MD_ReferenceSystem newVal){
//		referenceSystemInfo = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setSeries(DS_Aggregate newVal){
//		series = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setSpatialRepresentationInfo(MD_SpatialRepresentation newVal){
//		spatialRepresentationInfo = newVal;
//	}
}//end MD_Metadata