package org.iso.registry.core.model.iso19115.distribution;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;

/**
 * Technical means and media by which a dataset is obtained from the distributor
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_DigitalTransferOptions {

	private MD_Medium offLine;
	/**
	 * Information about online sources from which the dataset can be obtained
	 */
	private CI_OnlineResource onLine;
	/**
	 * Estimated size of the transferred dataset in megabytes. The transfer size is > 0.0
	 */
	private double transferSize;
	/**
	 * Tiles, layers, geographic areas, etc., in which data is available
	 */
	private CharacterString unitsOfDistribution;

	public MD_Medium getOffLine(){
		return offLine;
	}

	public CI_OnlineResource getOnLine(){
		return onLine;
	}

	public double getTransferSize(){
		return transferSize;
	}

	public CharacterString getUnitsOfDistribution(){
		return unitsOfDistribution;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOffLine(MD_Medium newVal){
		offLine = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setOnLine(CI_OnlineResource newVal){
		onLine = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransferSize(double newVal){
		transferSize = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUnitsOfDistribution(CharacterString newVal){
		unitsOfDistribution = newVal;
	}
}//end MD_DigitalTransferOptions