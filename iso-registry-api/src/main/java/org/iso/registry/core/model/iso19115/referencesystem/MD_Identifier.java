package org.iso.registry.core.model.iso19115.referencesystem;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_Identifier {

	/**
	 * Organization or party responsible for definition and maintenance of the code space or code. 
	 */
	private CI_Citation authority;
	/**
	 * Identifier code or name, often from a controlled list or pattern defined by a code space.
	 */
	private CharacterString code;

	public CI_Citation getAuthority(){
		return authority;
	}

	public CharacterString getCode(){
		return code;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAuthority(CI_Citation newVal){
		authority = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCode(CharacterString newVal){
		code = newVal;
	}
}//end MD_Identifier