package org.iso.registry.core.model.iso19115.constraint;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Handling restrictions imposed on the dataset because of national security, privacy, or other concerns
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_SecurityConstraints extends MD_Constraints {

	/**
	 * Name of the handling restrictions on the dataset
	 */
	private MD_ClassificationCode classification;
	/**
	 * Name of the classification system
	 */
	private CharacterString classificationSystem;
	/**
	 * Additional information about the restrictions on handling the dataset
	 */
	private CharacterString handlingDescription;
	/**
	 * Additional information about the classification
	 */
	private CharacterString userNote;

	public MD_ClassificationCode getClassification(){
		return classification;
	}

	public CharacterString getClassificationSystem(){
		return classificationSystem;
	}

	public CharacterString getHandlingDescription(){
		return handlingDescription;
	}

	public CharacterString getUserNote(){
		return userNote;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setClassification(MD_ClassificationCode newVal){
		classification = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setClassificationSystem(CharacterString newVal){
		classificationSystem = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setHandlingDescription(CharacterString newVal){
		handlingDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUserNote(CharacterString newVal){
		userNote = newVal;
	}
}//end MD_SecurityConstraints