package org.iso.registry.core.model.iso19115.dataquality;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.iso19115.maintenance.MD_ScopeCode;
import org.iso.registry.core.model.iso19115.maintenance.MD_ScopeDescription;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
public class DQ_Scope {

	private EX_Extent extent;
	/**
	 * Either level or extent must be non-NULL.
	 */
	private MD_ScopeCode level;
	private MD_ScopeDescription levelDescription;

	public EX_Extent getExtent(){
		return extent;
	}

	public MD_ScopeCode getLevel(){
		return level;
	}

	public MD_ScopeDescription getLevelDescription(){
		return levelDescription;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExtent(EX_Extent newVal){
		extent = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLevel(MD_ScopeCode newVal){
		level = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLevelDescription(MD_ScopeDescription newVal){
		levelDescription = newVal;
	}
}//end DQ_Scope