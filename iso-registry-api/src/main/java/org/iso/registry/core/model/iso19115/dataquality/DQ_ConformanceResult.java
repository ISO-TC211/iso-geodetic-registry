package org.iso.registry.core.model.iso19115.dataquality;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * quantitative_result from Quality Procedures
 * 
 * renamed to remove implied use limitiation.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
@Access(AccessType.FIELD)
@Audited @Entity
public class DQ_ConformanceResult extends DQ_Result 
{
	@Basic(optional = false)
	@Column(columnDefinition = "text")
	private String explanation;
	
	@Basic(optional = false)
	private Boolean pass;
	
	@ManyToOne(optional = false)
	private CI_Citation specification;

	public String getExplanation(){
		return explanation;
	}

	public Boolean getPass(){
		return pass;
	}

	public CI_Citation getSpecification(){
		return specification;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExplanation(String newVal){
		explanation = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPass(Boolean newVal){
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