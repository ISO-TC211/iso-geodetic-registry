package org.iso.registry.core.model.iso19115.metadataapplication;

import java.util.Collection;

/**
 * Identifiable collection of data
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
public class DS_DataSet {

	private Collection<DS_Aggregate> partOf;

	public Collection<DS_Aggregate> getPartOf(){
		return partOf;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPartOf(Collection<DS_Aggregate> newVal){
		partOf = newVal;
	}
}//end DS_DataSet