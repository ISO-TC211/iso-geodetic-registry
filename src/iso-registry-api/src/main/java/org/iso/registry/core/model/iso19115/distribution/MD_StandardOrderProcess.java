package org.iso.registry.core.model.iso19115.distribution;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19103.DateTime;

/**
 * Common ways in which the dataset may be obtained or received, and related instructions and fee information
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:14
 */
public class MD_StandardOrderProcess {

	/**
	 * Fees and terms for retrieving the dataset.  Include monetary units.
	 */
	private CharacterString fees;
	/**
	 * General instructions , terms and services provided  by the distributor when ordering the dataset
	 */
	private CharacterString orderingInstructions;
	/**
	 * Date and time when the dataset will be available. 
	 */
	private DateTime plannedAvailableDateTime;
	/**
	 * Typical turnaround time for the filling of an order
	 */
	private CharacterString turnaround;

	public CharacterString getFees(){
		return fees;
	}

	public CharacterString getOrderingInstructions(){
		return orderingInstructions;
	}

	public DateTime getPlannedAvailableDateTime(){
		return plannedAvailableDateTime;
	}

	public CharacterString getTurnaround(){
		return turnaround;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFees(CharacterString newVal){
		fees = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOrderingInstructions(CharacterString newVal){
		orderingInstructions = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPlannedAvailableDateTime(DateTime newVal){
		plannedAvailableDateTime = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTurnaround(CharacterString newVal){
		turnaround = newVal;
	}
}//end MD_StandardOrderProcess