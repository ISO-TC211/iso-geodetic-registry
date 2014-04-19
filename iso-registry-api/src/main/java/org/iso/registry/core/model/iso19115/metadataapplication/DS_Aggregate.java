package org.iso.registry.core.model.iso19115.metadataapplication;

import java.util.Collection;

/**
 * Identifiable collection of datasets
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
public abstract class DS_Aggregate {

	private Collection<DS_Aggregate> subset;

	public Collection<DS_Aggregate> getSubset(){
		return subset;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSubset(Collection<DS_Aggregate> newVal){
		subset = newVal;
	}
}//end DS_Aggregate