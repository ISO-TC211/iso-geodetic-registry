package org.iso.registry.core.model.iso19115.referencesystem;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_ReferenceSystem {

	private RS_Identifier referenceSystemIdentifier;

	public RS_Identifier getReferenceSystemIdentifier(){
		return referenceSystemIdentifier;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setReferenceSystemIdentifier(RS_Identifier newVal){
		referenceSystemIdentifier = newVal;
	}
}//end MD_ReferenceSystem