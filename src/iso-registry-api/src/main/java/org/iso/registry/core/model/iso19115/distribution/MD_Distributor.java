package org.iso.registry.core.model.iso19115.distribution;
import java.util.Collection;

import de.geoinfoffm.registry.core.model.iso19115.CI_ResponsibleParty;

/**
 * Information about the distributor
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_Distributor {

	/**
	 * Party from whom the dataset may be obtained
	 */
	private CI_ResponsibleParty distributorContact;
	private Collection<MD_StandardOrderProcess> distributionOrderProcess;
	private Collection<MD_DigitalTransferOptions> distributorTransferOptions;
	private Collection<MD_Format> distributorFormat;

	public Collection<MD_StandardOrderProcess> getDistributionOrderProcess(){
		return distributionOrderProcess;
	}

	public CI_ResponsibleParty getDistributorContact(){
		return distributorContact;
	}

	public Collection<MD_Format> getDistributorFormat(){
		return distributorFormat;
	}

	public Collection<MD_DigitalTransferOptions> getDistributorTransferOptions(){
		return distributorTransferOptions;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDistributionOrderProcess(Collection<MD_StandardOrderProcess> newVal){
		distributionOrderProcess = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDistributorContact(CI_ResponsibleParty newVal){
		distributorContact = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDistributorFormat(Collection<MD_Format> newVal){
		distributorFormat = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDistributorTransferOptions(Collection<MD_DigitalTransferOptions> newVal){
		distributorTransferOptions = newVal;
	}
}//end MD_Distributor