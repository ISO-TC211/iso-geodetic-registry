package org.iso.registry.core.model.iso19115.content;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Set of adjacent wavelengths in the electro-magnetic spectrum with a common characteristic, such as the visible band
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_RangeDimension {

	private CharacterString descriptor;
	/**
	 * Number that uniquely identifies instances of bands of wavelengths on which a sensor operates
	 */
	private String sequenceIdentifier;

	public CharacterString getDescriptor(){
		return descriptor;
	}

	public String getSequenceIdentifier(){
		return sequenceIdentifier;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDescriptor(CharacterString newVal){
		descriptor = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSequenceIdentifier(String newVal){
		sequenceIdentifier = newVal;
	}
}//end MD_RangeDimension