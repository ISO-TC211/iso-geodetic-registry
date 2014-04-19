package org.iso.registry.core.model.iso19115.identification;

import org.iso.registry.core.model.iso19115.extent.EX_Extent;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_DataIdentification extends MD_Identification {

	/**
	 * Full name of the ISO character coding standard used for the data
	 */
	private MD_CharacterSetCode characterSet /* = "utf8"*/;
	/**
	 * Description of the dataset in the producer's processing environment, including items such as the name of the software,
	 * the computer operating system, file name, and the dataset size
	 */
	private CharacterString environmentDescription;
	/**
	 * Additional information about the bounding polygon, vertical, and temporal extent of the dataset
	 */
	private EX_Extent extent;
	/**
	 * Language(s) used within the dataset
	 */
	private CharacterString language;
	private MD_SpatialRepresentationTypeCode spatialRepresentationType;
	/**
	 * Factor which provides a general understanding of the density of spatial data in the dataset.
	 * Example: The denominator of the representative fraction or the mean ground sample distance
	 */
	private MD_Resolution spatialResolution;
	/**
	 * Other descriptive information about the dataset. Example; Data Model
	 */
	private CharacterString supplementalInformation;
	/**
	 * Keywords, describing a subject of a dataset
	 */
	private MD_TopicCategoryCode topicCategory;

	public MD_CharacterSetCode getCharacterSet(){
		return characterSet;
	}

	public CharacterString getEnvironmentDescription(){
		return environmentDescription;
	}

	public EX_Extent getExtent(){
		return extent;
	}

	public CharacterString getLanguage(){
		return language;
	}

	public MD_SpatialRepresentationTypeCode getSpatialRepresentationType(){
		return spatialRepresentationType;
	}

	public MD_Resolution getSpatialResolution(){
		return spatialResolution;
	}

	public CharacterString getSupplementalInformation(){
		return supplementalInformation;
	}

	public MD_TopicCategoryCode getTopicCategory(){
		return topicCategory;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCharacterSet(MD_CharacterSetCode newVal){
		characterSet = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEnvironmentDescription(CharacterString newVal){
		environmentDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExtent(EX_Extent newVal){
		extent = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLanguage(CharacterString newVal){
		language = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSpatialRepresentationType(MD_SpatialRepresentationTypeCode newVal){
		spatialRepresentationType = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSpatialResolution(MD_Resolution newVal){
		spatialResolution = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSupplementalInformation(CharacterString newVal){
		supplementalInformation = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTopicCategory(MD_TopicCategoryCode newVal){
		topicCategory = newVal;
	}
}//end MD_DataIdentification