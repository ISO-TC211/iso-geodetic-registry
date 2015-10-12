package org.iso.registry.core.model.iso19115.dataquality;

import java.util.Collection;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
public class LI_Lineage {

	private CharacterString statement;
	private Collection<LI_ProcessStep> processStep;
	private Collection<LI_Source> source;

	public Collection<LI_ProcessStep> getProcessStep(){
		return processStep;
	}

	public Collection<LI_Source> getSource(){
		return source;
	}

	public CharacterString getStatement(){
		return statement;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProcessStep(Collection<LI_ProcessStep> newVal){
		processStep = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSource(Collection<LI_Source> newVal){
		source = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setStatement(CharacterString newVal){
		statement = newVal;
	}
}//end LI_Lineage