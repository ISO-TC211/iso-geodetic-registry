package org.iso.registry.core.model.iso19115.identification;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * Keywords, their type and reference source
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_Keywords {

	/**
	 * Common-use word(s) or phrase(s) used to describe the subject 
	 */
	private CharacterString keyword;
	/**
	 * Name of the formally registered thesaurus or a similar authoritative source of keywords
	 */
	private CI_Citation thesaurusName;
	/**
	 * Method used to group similar keywords
	 */
	private MD_KeywordTypeCode type;

	public CharacterString getKeyword(){
		return keyword;
	}

	public CI_Citation getThesaurusName(){
		return thesaurusName;
	}

	public MD_KeywordTypeCode getType(){
		return type;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setKeyword(CharacterString newVal){
		keyword = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setThesaurusName(CI_Citation newVal){
		thesaurusName = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setType(MD_KeywordTypeCode newVal){
		type = newVal;
	}
}//end MD_Keywords