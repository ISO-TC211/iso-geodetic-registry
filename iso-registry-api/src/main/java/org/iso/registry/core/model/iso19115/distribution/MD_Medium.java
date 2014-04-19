package org.iso.registry.core.model.iso19115.distribution;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Information about the media on which the data can be distributed
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_Medium {

	/**
	 * Density in which the dataset can be recorded
	 */
	private double density;
	/**
	 * Units of measure for the recording density
	 */
	private CharacterString densityUnits;
	/**
	 * Options available or method used to write the dataset to the medium
	 * -Examples: "cpio", "tar", "High Sierra", "ISO 9660", "ISO 9660 with Rock Ridge extensions", "ISO 9660 with Apple HFS
	 * extensions"
	 */
	private MD_MediumFormatCode mediumFormat;
	/**
	 * Description of other limitations or requirements for using the medium
	 */
	private CharacterString mediumNote;
	/**
	 * Name of the media on which the dataset can be received
	 * -Examples:"CD-ROM", "3.5 inch floppy disk", "5.25 inch floppy disk", "9-track tape", "4 mm cartridge tape", "8 mm
	 * cartridge tape", "1/4 inch cartridge tape", "on-line", " satellite", "telephone link", "brochure"
	 */
	private MD_MediumNameCode name;
	/**
	 * Number of items in the media identified
	 */
	private int volumes;

	public double getDensity(){
		return density;
	}

	public CharacterString getDensityUnits(){
		return densityUnits;
	}

	public MD_MediumFormatCode getMediumFormat(){
		return mediumFormat;
	}

	public CharacterString getMediumNote(){
		return mediumNote;
	}

	public MD_MediumNameCode getName(){
		return name;
	}

	public int getVolumes(){
		return volumes;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDensity(double newVal){
		density = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDensityUnits(CharacterString newVal){
		densityUnits = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMediumFormat(MD_MediumFormatCode newVal){
		mediumFormat = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMediumNote(CharacterString newVal){
		mediumNote = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setName(MD_MediumNameCode newVal){
		name = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setVolumes(int newVal){
		volumes = newVal;
	}
}//end MD_Medium