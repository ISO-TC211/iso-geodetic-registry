package org.iso.registry.core.model.iso19115.dataquality;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * quantitative_result from Quality Procedures
 * 
 * renamed to remove implied use limitiation.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
public interface DQ_ConformanceResult extends DQ_Result 
{
	String getExplanation();
	void setExplanation(String newVal);
	
	Boolean getPass();
	void setPass(Boolean newVal);
	
	CI_Citation getSpecification();
	void setSpecification(CI_Citation newVal);

}//end DQ_ConformanceResult