package org.iso.registry.core.model.iso19115.metadataextension;
import java.util.Collection;

import de.geoinfoffm.registry.core.model.iso19115.CI_OnlineResource;

/**
 * Information describing metadata extensions.
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:13
 */
public class MD_MetadataExtensionInformation {

	/**
	 * Information about online sources containing the community profile name and the extended metadata elements.  Information
	 * for all new metadata elements.
	 */
	private CI_OnlineResource extensionOnLineResource;
	private Collection<MD_ExtendedElementInformation> extendedElementInformation;

	public Collection<MD_ExtendedElementInformation> getExtendedElementInformation(){
		return extendedElementInformation;
	}

	public CI_OnlineResource getExtensionOnLineResource(){
		return extensionOnLineResource;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExtendedElementInformation(Collection<MD_ExtendedElementInformation> newVal){
		extendedElementInformation = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setExtensionOnLineResource(CI_OnlineResource newVal){
		extensionOnLineResource = newVal;
	}
}//end MD_MetadataExtensionInformation