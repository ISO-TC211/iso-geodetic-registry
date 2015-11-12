package org.iso.registry.core.model.iso19115.constraint;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Restrictions on the access and use of a dataset or metadata
 * 
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_Constraints {

	/**
	 * Any limitation affecting the fitness for use of the dataset. Example,
	 * "not to be used for navigation"
	 */
	private CharacterString useLimitation;

	public CharacterString getUseLimitation() {
		return useLimitation;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUseLimitation(CharacterString newVal) {
		useLimitation = newVal;
	}
}// end MD_Constraints