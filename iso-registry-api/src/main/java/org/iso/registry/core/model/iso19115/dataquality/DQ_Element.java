package org.iso.registry.core.model.iso19115.dataquality;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19103.DateTime;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.MD_Identifier;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
public abstract class DQ_Element {

	private DateTime dateTime;
	private CharacterString evaluationMethodDescription;
	private DQ_EvaluationMethodTypeCode evaluationMethodType;
	private CI_Citation evaluationProcedure;
	private CharacterString measureDescription;
	private MD_Identifier measureIdentification;
	private CharacterString nameOfMeasure;
	private DQ_Result result;

	public DateTime getDateTime(){
		return dateTime;
	}

	public CharacterString getEvaluationMethodDescription(){
		return evaluationMethodDescription;
	}

	public DQ_EvaluationMethodTypeCode getEvaluationMethodType(){
		return evaluationMethodType;
	}

	public CI_Citation getEvaluationProcedure(){
		return evaluationProcedure;
	}

	public CharacterString getMeasureDescription(){
		return measureDescription;
	}

	public MD_Identifier getMeasureIdentification(){
		return measureIdentification;
	}

	public CharacterString getNameOfMeasure(){
		return nameOfMeasure;
	}

	public DQ_Result getResult(){
		return result;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDateTime(DateTime newVal){
		dateTime = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEvaluationMethodDescription(CharacterString newVal){
		evaluationMethodDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEvaluationMethodType(DQ_EvaluationMethodTypeCode newVal){
		evaluationMethodType = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEvaluationProcedure(CI_Citation newVal){
		evaluationProcedure = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMeasureDescription(CharacterString newVal){
		measureDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMeasureIdentification(MD_Identifier newVal){
		measureIdentification = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setNameOfMeasure(CharacterString newVal){
		nameOfMeasure = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setResult(DQ_Result newVal){
		result = newVal;
	}
}//end DQ_Element