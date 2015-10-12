package org.iso.registry.core.model.iso19115.applicationschema;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * Information about the application schema used to build the dataset
 * 
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_ApplicationSchemaInformation 
{
	private CharacterString constraintLanguage;
	/**
	 * Full application schema given as a graphics file.
	 */
	private byte[] graphicsFile;
	/**
	 * Name of the application schema used
	 */
	private CI_Citation name;
	/**
	 * Full application schema given as an ASCII file.
	 */
	private CharacterString schemaAscii;
	/**
	 * Identification of the schema language used
	 */
	private CharacterString schemaLanguage;
	/**
	 * Full application schema given as a software defelopment file.
	 */
	private byte[] softwareDevelopmentFile;
	/**
	 * Software dependent format used for the application schema software
	 * dependent file.
	 */
	private CharacterString softwareDevelopmentFileFormat;

	public CharacterString getConstraintLanguage() {
		return constraintLanguage;
	}

	public byte[] getGraphicsFile() {
		return graphicsFile;
	}

	public CI_Citation getName() {
		return name;
	}

	public CharacterString getSchemaAscii() {
		return schemaAscii;
	}

	public CharacterString getSchemaLanguage() {
		return schemaLanguage;
	}

	public byte[] getSoftwareDevelopmentFile() {
		return softwareDevelopmentFile;
	}

	public CharacterString getSoftwareDevelopmentFileFormat() {
		return softwareDevelopmentFileFormat;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setConstraintLanguage(CharacterString newVal) {
		constraintLanguage = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setGraphicsFile(byte[] newVal) {
		graphicsFile = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setName(CI_Citation newVal) {
		name = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSchemaAscii(CharacterString newVal) {
		schemaAscii = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSchemaLanguage(CharacterString newVal) {
		schemaLanguage = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSoftwareDevelopmentFile(byte[] newVal) {
		softwareDevelopmentFile = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSoftwareDevelopmentFileFormat(CharacterString newVal) {
		softwareDevelopmentFileFormat = newVal;
	}
}// end MD_ApplicationSchemaInformation