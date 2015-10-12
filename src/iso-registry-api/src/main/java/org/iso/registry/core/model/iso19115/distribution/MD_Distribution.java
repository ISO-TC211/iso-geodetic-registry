package org.iso.registry.core.model.iso19115.distribution;

import java.util.Collection;

/**
 * Information about the distributor of and options for obtaining the dataset
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_Distribution {

	private Collection<MD_DigitalTransferOptions> transferOptions;
	private Collection<MD_Format> distributionFormat;
	private Collection<MD_Distributor> distributor;

	public Collection<MD_Format> getDistributionFormat(){
		return distributionFormat;
	}

	public Collection<MD_Distributor> getDistributor(){
		return distributor;
	}

	public Collection<MD_DigitalTransferOptions> getTransferOptions(){
		return transferOptions;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDistributionFormat(Collection<MD_Format> newVal){
		distributionFormat = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDistributor(Collection<MD_Distributor> newVal){
		distributor = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransferOptions(Collection<MD_DigitalTransferOptions> newVal){
		transferOptions = newVal;
	}
}//end MD_Distribution