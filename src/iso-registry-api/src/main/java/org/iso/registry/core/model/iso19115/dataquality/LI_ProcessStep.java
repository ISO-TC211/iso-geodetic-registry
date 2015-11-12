package org.iso.registry.core.model.iso19115.dataquality;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19103.DateTime;
import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
public class LI_ProcessStep {

	private DateTime dateTime;
	private CharacterString description;
	private CI_ResponsibleParty processor;
	private CharacterString rationale;

	public DateTime getDateTime() {
		return dateTime;
	}

	public CharacterString getDescription() {
		return description;
	}

	public CI_ResponsibleParty getProcessor() {
		return processor;
	}

	public CharacterString getRationale() {
		return rationale;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDateTime(DateTime newVal) {
		dateTime = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDescription(CharacterString newVal) {
		description = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProcessor(CI_ResponsibleParty newVal) {
		processor = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setRationale(CharacterString newVal) {
		rationale = newVal;
	}
}// end LI_ProcessStep