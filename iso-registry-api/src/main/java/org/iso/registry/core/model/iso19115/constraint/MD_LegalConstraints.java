package org.iso.registry.core.model.iso19115.constraint;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Restrictions and legal prerequisites for accessing and using the dataset.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_LegalConstraints extends MD_Constraints {

	/**
	 * Access constraints applied to assure the protection of privacy or intellectual property, and any special restrictions
	 * or limitations on obtaining the dataset.
	 */
	private MD_RestrictionCode accessConstraints;
	/**
	 * Other restrictions and legal prerequisites for accessing and using the dtaset
	 */
	private CharacterString otherConstraints;
	/**
	 * Constraints applied to assure the protection of privacy or intellectual property, and any special restrictions or
	 * limitations or warnings on using the dataset
	 * -Examples: "copyright", "license", "non-commercial", "none"
	 */
	private MD_RestrictionCode useConstraints;

	public MD_RestrictionCode getAccessConstraints(){
		return accessConstraints;
	}

	public CharacterString getOtherConstraints(){
		return otherConstraints;
	}

	public MD_RestrictionCode getUseConstraints(){
		return useConstraints;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAccessConstraints(MD_RestrictionCode newVal){
		accessConstraints = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOtherConstraints(CharacterString newVal){
		otherConstraints = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUseConstraints(MD_RestrictionCode newVal){
		useConstraints = newVal;
	}
}//end MD_LegalConstraints