package org.iso.registry.core.model.iso19115.extent;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import de.geoinfoffm.registry.core.model.iso19115.MD_Identifier;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Access(AccessType.FIELD)
@Embeddable
public class EX_GeographicDescription extends EX_GeographicExtent {

	@Embedded
	private MD_Identifier geographicIdentifier;

	public MD_Identifier getGeographicIdentifier() {
		return geographicIdentifier;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setGeographicIdentifier(MD_Identifier newVal){
		geographicIdentifier = newVal;
	}
}//end EX_GeographicDescription