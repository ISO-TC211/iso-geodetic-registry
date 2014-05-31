package org.iso.registry.core.model.iso19115.dataquality;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.MD_Identifier;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Audited @Entity
public abstract class DQ_Element extends de.geoinfoffm.registry.core.Entity
{

	@Temporal(TemporalType.TIMESTAMP)
	@ElementCollection
	private List<Date> dateTime;
	
	@Column(columnDefinition = "text")
	private String evaluationMethodDescription;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "value", column = @Column(name = "evaluationMethodType")),
		@AttributeOverride(name = "codeList", column = @Column(name = "evaluationMethodType_codeList")),
		@AttributeOverride(name = "codeListValue", column = @Column(name = "evaluationMethodType_codeListValue")),
		@AttributeOverride(name = "codeSpace", column = @Column(name = "evaluationMethodType_codeSpace"))
	})
	private DQ_EvaluationMethodTypeCode evaluationMethodType;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private CI_Citation evaluationProcedure;
	
	@Column(columnDefinition = "text")
	private String measureDescription;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "authority", column = @Column(name = "measureIdentificaion_authority")),
		@AttributeOverride(name = "code", column = @Column(name = "measureIdentification"))
	})
	private MD_Identifier measureIdentification;
	
	@Column(columnDefinition = "text")
	private String nameOfMeasure;

	@ManyToOne(cascade = CascadeType.ALL)
	private Result result;

	public List<Date> getDateTime(){
		return dateTime;
	}

	public String getEvaluationMethodDescription(){
		return evaluationMethodDescription;
	}

	public DQ_EvaluationMethodTypeCode getEvaluationMethodType(){
		return evaluationMethodType;
	}

	public CI_Citation getEvaluationProcedure(){
		return evaluationProcedure;
	}

	public String getMeasureDescription(){
		return measureDescription;
	}

	public MD_Identifier getMeasureIdentification(){
		return measureIdentification;
	}

	public String getNameOfMeasure(){
		return nameOfMeasure;
	}

	public DQ_Result getResult(){
		return result;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDateTime(List<Date> newVal){
		dateTime = newVal;
	}
	
	public void addDateTime(Date dateTime) {
		if (this.dateTime == null) {
			this.dateTime = new ArrayList<>();
		}
		this.dateTime.add(dateTime);
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEvaluationMethodDescription(String newVal){
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
	public void setMeasureDescription(String newVal){
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
	public void setNameOfMeasure(String newVal){
		nameOfMeasure = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setResult(DQ_Result newVal){
		if (!(newVal instanceof Result)) {
			throw new IllegalArgumentException("Result must be of type " + Result.class.getCanonicalName());
		}
		result = (Result)newVal;
	}
	
	public void setResult(Result result) {
		this.result = result;
	}
}//end DQ_Element