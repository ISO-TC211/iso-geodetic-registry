package org.iso.registry.core.model.iso19115.spatial;

import java.util.List;

import org.eclipse.persistence.sessions.Record;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_Georeferenceable extends MD_GridSpatialRepresentation {

	private boolean controlPointAvailability;
	private List<Object> georeferencedParameters;
	private boolean orientationParameterAvailability;
	private CharacterString orientationParameterDescription;
	private CI_Citation parameterCitation;

	public boolean getControlPointAvailability(){
		return controlPointAvailability;
	}

	public List<Object> getGeoreferencedParameters(){
		return georeferencedParameters;
	}

	public boolean getOrientationParameterAvailability(){
		return orientationParameterAvailability;
	}

	public CharacterString getOrientationParameterDescription(){
		return orientationParameterDescription;
	}

	public CI_Citation getParameterCitation(){
		return parameterCitation;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setControlPointAvailability(boolean newVal){
		controlPointAvailability = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setGeoreferencedParameters(List<Object> newVal){
		georeferencedParameters = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOrientationParameterAvailability(boolean newVal){
		orientationParameterAvailability = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOrientationParameterDescription(CharacterString newVal){
		orientationParameterDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setParameterCitation(CI_Citation newVal){
		parameterCitation = newVal;
	}
}//end MD_Georeferenceable