package org.iso.registry.core.model.iso19115.dataquality;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * quantitative_result from Quality Procedures
 * 
 * renamed to remove implied use limitiation.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
public class DQ_ConformanceResult extends DQ_Result {

	private CharacterString explanation;
	private boolean pass;
	private CI_Citation specification;

	public CharacterString getExplanation(){
		return explanation;
	}

	public boolean getPass(){
		return pass;
	}

	public CI_Citation getSpecification(){
		return specification;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExplanation(CharacterString newVal){
		explanation = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPass(boolean newVal){
		pass = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSpecification(CI_Citation newVal){
		specification = newVal;
	}
}//end DQ_ConformanceResult