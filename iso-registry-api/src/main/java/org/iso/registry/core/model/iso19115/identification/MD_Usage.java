package org.iso.registry.core.model.iso19115.identification;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19103.DateTime;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;

/**
 * Brief description of ways in which the dataset is currently used.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:14
 */
public class MD_Usage {

	/**
	 * Brief description of the dataset and/or dataset series use
	 */
	private CharacterString specificUsage;
	/**
	 * Date and time of the first occurrence or range of occurrences of the dataset and/or dataset series
	 */
	private DateTime usageDateTime;
	/**
	 * Identification of means of communicating with person(s) and organisation(s) using the dataset and/or dataset series
	 */
	private CI_ResponsibleParty userContactInfo;
	/**
	 * Applications for which the dataset and/or dataset series is not suitable
	 */
	private CharacterString userDeterminedLimitations;

	public CharacterString getSpecificUsage(){
		return specificUsage;
	}

	public DateTime getUsageDateTime(){
		return usageDateTime;
	}

	public CI_ResponsibleParty getUserContactInfo(){
		return userContactInfo;
	}

	public CharacterString getUserDeterminedLimitations(){
		return userDeterminedLimitations;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSpecificUsage(CharacterString newVal){
		specificUsage = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUsageDateTime(DateTime newVal){
		usageDateTime = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUserContactInfo(CI_ResponsibleParty newVal){
		userContactInfo = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUserDeterminedLimitations(CharacterString newVal){
		userDeterminedLimitations = newVal;
	}
}//end MD_Usage