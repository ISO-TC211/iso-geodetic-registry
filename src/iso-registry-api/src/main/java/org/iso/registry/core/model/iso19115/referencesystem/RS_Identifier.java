package org.iso.registry.core.model.iso19115.referencesystem;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * An identification of a CRS object. The first use of a XX_RS_Identifier for an object, if any, is normally the primary
 * identification code, and any others are aliases.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:14
 */
public class RS_Identifier extends MD_Identifier {

	/**
	 * Identifier of a code space within which one or more codes are defined. This code space is optional but is normally
	 * included. This code space is often defined by some authority organization, where one organization may define multiple
	 * code spaces. The range and format of each Code Space identifier is defined by that code space authority.
	 */
	private CharacterString codeSpace;
	/**
	 * Identifier of the version of the associated codeSpace or code, as specified by the codeSpace or code authority. This
	 * version is included only when the "code" or "codeSpace" uses versions. When appropriate, the version is identified by
	 * the effective date, coded using ISO 8601 date format.
	 */
	private CharacterString version;

	public CharacterString getCodeSpace(){
		return codeSpace;
	}

	public CharacterString getVersion(){
		return version;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCodeSpace(CharacterString newVal){
		codeSpace = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setVersion(CharacterString newVal){
		version = newVal;
	}
}//end RS_Identifier