package org.iso.registry.core.model.iso19115.identification;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Graphic that provides an illustration of the dataset (should include a legend for the graphic)
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_BrowseGraphic {

	/**
	 * Text description of the illustration
	 */
	private CharacterString fileDescription;
	/**
	 * Name of the file that contains a graphic that provides an illustration of the dataset
	 */
	private CharacterString fileName;
	/**
	 * Graphic file type of a related graphic file
	 * Examples:  CGM, EPS, GIF, JPEG, PBM, PS, TIFF, XWD
	 */
	private CharacterString fileType;

	public CharacterString getFileDescription(){
		return fileDescription;
	}

	public CharacterString getFileName(){
		return fileName;
	}

	public CharacterString getFileType(){
		return fileType;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFileDescription(CharacterString newVal){
		fileDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFileName(CharacterString newVal){
		fileName = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFileType(CharacterString newVal){
		fileType = newVal;
	}
}//end MD_BrowseGraphic