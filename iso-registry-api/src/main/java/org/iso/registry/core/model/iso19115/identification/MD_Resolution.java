package org.iso.registry.core.model.iso19115.identification;

import org.iso.registry.core.model.iso19103.Distance;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_Resolution {

	private Distance distance;
	private MD_RepresentativeFraction equivalentScale;

	public Distance getDistance(){
		return distance;
	}

	public MD_RepresentativeFraction getEquivalentScale(){
		return equivalentScale;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDistance(Distance newVal){
		distance = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setEquivalentScale(MD_RepresentativeFraction newVal){
		equivalentScale = newVal;
	}
}//end MD_Resolution