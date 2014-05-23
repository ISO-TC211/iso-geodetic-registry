package org.iso.registry.core.model.iso19115.metadataextension;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;

/**
 * New metadata element, not found in ISO 19115, which is required to describe geographic data
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_ExtendedElementInformation {

	private CharacterString condition;
	/**
	 * Code which identifies the kind of value provided in the extended element
	 */
	private MD_DatatypeCode dataType;
	/**
	 * Definition of the extended element
	 */
	private CharacterString definition;
	private int domainCode;
	/**
	 * Valid values that can be assigned to the extended element. The same rules as those for standard elements are applied
	 * here
	 */
	private CharacterString domainValue;
	/**
	 * Maximum occurrence of the extended element within the "..."
	 */
	private CharacterString maximumOccurrence;
	/**
	 * Name of the extended metadata element.
	 * NOTE: Do not duplicate any other Standard element name.
	 */
	private CharacterString name;
	/**
	 * Obligation and condition of the extended element
	 */
	private MD_ObligationCode obligation;
	/**
	 * Name of the metadata entity(s) under which this extended metadata element may appear. The name(s) may be standard or
	 * other extended metadata element(s).
	 * (Must be the name of an existing standard or extended element.)
	 */
	private CharacterString parentEntity;
	/**
	 * Reason for creating the extended element
	 */
	private CharacterString rationale;
	/**
	 * Relationship rule for the element, specified using the form given in this standard.
	 */
	private CharacterString rule;
	/**
	 * Unique numeric identifier of the extended element
	 * NOTE: Do not duplicate another identifier used by the standard.
	 */
	private CharacterString shortName;
	/**
	 * Name of the entity creating the extended element
	 */
	private CI_ResponsibleParty source;

	public CharacterString getCondition(){
		return condition;
	}

	public MD_DatatypeCode getDataType(){
		return dataType;
	}

	public CharacterString getDefinition(){
		return definition;
	}

	public int getDomainCode(){
		return domainCode;
	}

	public CharacterString getDomainValue(){
		return domainValue;
	}

	public CharacterString getMaximumOccurrence(){
		return maximumOccurrence;
	}

	public CharacterString getName(){
		return name;
	}

	public MD_ObligationCode getObligation(){
		return obligation;
	}

	public CharacterString getParentEntity(){
		return parentEntity;
	}

	public CharacterString getRationale(){
		return rationale;
	}

	public CharacterString getRule(){
		return rule;
	}

	public CharacterString getShortName(){
		return shortName;
	}

	public CI_ResponsibleParty getSource(){
		return source;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCondition(CharacterString newVal){
		condition = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDataType(MD_DatatypeCode newVal){
		dataType = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDefinition(CharacterString newVal){
		definition = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDomainCode(int newVal){
		domainCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDomainValue(CharacterString newVal){
		domainValue = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMaximumOccurrence(CharacterString newVal){
		maximumOccurrence = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setName(CharacterString newVal){
		name = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setObligation(MD_ObligationCode newVal){
		obligation = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setParentEntity(CharacterString newVal){
		parentEntity = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setRationale(CharacterString newVal){
		rationale = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setRule(CharacterString newVal){
		rule = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setShortName(CharacterString newVal){
		shortName = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSource(CI_ResponsibleParty newVal){
		source = newVal;
	}
}//end MD_ExtendedElementInformation