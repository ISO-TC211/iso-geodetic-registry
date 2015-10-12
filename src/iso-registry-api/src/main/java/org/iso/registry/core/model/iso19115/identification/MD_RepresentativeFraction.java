package org.iso.registry.core.model.iso19115.identification;

import org.iso.registry.core.model.iso19103.Scale;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_RepresentativeFraction {

	private int denominator;
	private Scale equivalentScale;

	public int getDenominator(){
		return denominator;
	}

	public Scale getEquivalentScale(){
		return equivalentScale;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDenominator(int newVal){
		denominator = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEquivalentScale(Scale newVal){
		equivalentScale = newVal;
	}
}//end MD_RepresentativeFraction