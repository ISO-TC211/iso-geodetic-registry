package org.iso.registry.core.model.iso19115.identification;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.MD_Identifier;

/**
 * Encapsulates the dataset aggregation information
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_AggregateInformation {

	private MD_Identifier aggregateDataSetIdentifier;
	private CI_Citation aggregateDataSetName;
	private DS_AssociationTypeCode associationType;
	private DS_InitiativeTypeCode initiativeType;

	public MD_Identifier getAggregateDataSetIdentifier(){
		return aggregateDataSetIdentifier;
	}

	public CI_Citation getAggregateDataSetName(){
		return aggregateDataSetName;
	}

	public DS_AssociationTypeCode getAssociationType(){
		return associationType;
	}

	public DS_InitiativeTypeCode getInitiativeType(){
		return initiativeType;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAggregateDataSetIdentifier(MD_Identifier newVal){
		aggregateDataSetIdentifier = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAggregateDataSetName(CI_Citation newVal){
		aggregateDataSetName = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAssociationType(DS_AssociationTypeCode newVal){
		associationType = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setInitiativeType(DS_InitiativeTypeCode newVal){
		initiativeType = newVal;
	}
}//end MD_AggregateInformation