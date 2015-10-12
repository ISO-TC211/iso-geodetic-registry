package org.iso.registry.core.model.iso19115.dataquality;

import java.util.Collection;

import org.iso.registry.core.model.iso19115.extent.EX_Extent;
import org.iso.registry.core.model.iso19115.identification.MD_RepresentativeFraction;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;
import de.geoinfoffm.registry.core.model.iso19115.MD_ReferenceSystem;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
public class LI_Source {

	private CharacterString description;
	private MD_RepresentativeFraction scaleDenominator;
	private CI_Citation sourceCitation;
	private EX_Extent sourceExtent;
	private MD_ReferenceSystem sourceReferenceSystem;
	private Collection<LI_ProcessStep> sourceStep;

	public CharacterString getDescription(){
		return description;
	}

	public MD_RepresentativeFraction getScaleDenominator(){
		return scaleDenominator;
	}

	public CI_Citation getSourceCitation(){
		return sourceCitation;
	}

	public EX_Extent getSourceExtent(){
		return sourceExtent;
	}

	public MD_ReferenceSystem getSourceReferenceSystem(){
		return sourceReferenceSystem;
	}

	public Collection<LI_ProcessStep> getSourceStep(){
		return sourceStep;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDescription(CharacterString newVal){
		description = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setScaleDenominator(MD_RepresentativeFraction newVal){
		scaleDenominator = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSourceCitation(CI_Citation newVal){
		sourceCitation = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSourceExtent(EX_Extent newVal){
		sourceExtent = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSourceReferenceSystem(MD_ReferenceSystem newVal){
		sourceReferenceSystem = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSourceStep(Collection<LI_ProcessStep> newVal){
		sourceStep = newVal;
	}
}//end LI_Source