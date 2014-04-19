package org.iso.registry.core.model.iso19115.portrayal;
import de.geoinfoffm.registry.core.model.iso19115.CI_Citation;

/**
 * Information identifing the portrayal catalogue used
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_PortrayalCatalogueReference {

	/**
	 * Recommended reference to be used for the referring entity
	 */
	private CI_Citation portrayalCatalogueCitation;

	public CI_Citation getPortrayalCatalogueCitation(){
		return portrayalCatalogueCitation;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPortrayalCatalogueCitation(CI_Citation newVal){
		portrayalCatalogueCitation = newVal;
	}
}//end MD_PortrayalCatalogueReference