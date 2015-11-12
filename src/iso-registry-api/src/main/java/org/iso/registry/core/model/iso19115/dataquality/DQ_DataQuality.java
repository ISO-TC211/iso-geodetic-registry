package org.iso.registry.core.model.iso19115.dataquality;

import java.util.Collection;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
public class DQ_DataQuality {

	private DQ_Scope scope;
	private LI_Lineage lineage;
	private Collection<DQ_Element> report;

	public LI_Lineage getLineage(){
		return lineage;
	}

	public Collection<DQ_Element> getReport(){
		return report;
	}

	public DQ_Scope getScope(){
		return scope;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLineage(LI_Lineage newVal){
		lineage = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setReport(Collection<DQ_Element> newVal){
		report = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setScope(DQ_Scope newVal){
		scope = newVal;
	}
}//end DQ_DataQuality