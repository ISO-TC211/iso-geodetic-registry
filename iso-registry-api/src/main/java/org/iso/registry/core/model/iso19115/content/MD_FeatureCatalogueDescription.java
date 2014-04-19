package org.iso.registry.core.model.iso19115.content;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * Information identifing the feature catalogue
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_FeatureCatalogueDescription extends MD_ContentInformation {

	/**
	 * Indicates whether or not the cited feature catalogue complies with ISO 19110
	 */
	private boolean complianceCode;
	/**
	 * Complete bibliographic reference to one or more external feature catalogues
	 */
	private CI_Citation featureCatalogueCitation;
	/**
	 * Subset of feature types from cited feature catalogue occurring in dataset
	 */
	private String featureTypes;
	/**
	 * Indicates whether ro not the feature catalogue is included with the dataset
	 */
	private boolean includedWithDataset;
	/**
	 * Language(s) used within the dataset
	 */
	private CharacterString language;

	public boolean getComplianceCode(){
		return complianceCode;
	}

	public CI_Citation getFeatureCatalogueCitation(){
		return featureCatalogueCitation;
	}

	public String getFeatureTypes(){
		return featureTypes;
	}

	public boolean getIncludedWithDataset(){
		return includedWithDataset;
	}

	public CharacterString getLanguage(){
		return language;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setComplianceCode(boolean newVal){
		complianceCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFeatureCatalogueCitation(CI_Citation newVal){
		featureCatalogueCitation = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFeatureTypes(String newVal){
		featureTypes = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setIncludedWithDataset(boolean newVal){
		includedWithDataset = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLanguage(CharacterString newVal){
		language = newVal;
	}
}//end MD_FeatureCatalogueDescription