package org.iso.registry.core.model.iso19115.content;

import java.util.Collection;

/**
 * Information about the domain of the raster cell
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:11
 */
public class MD_CoverageDescription extends MD_ContentInformation {

	/**
	 * Description of the attribute described by the measurement value
	 */
	private String attributeDescription;
	private MD_CoverageContentTypeCode contentType;
	private Collection<MD_RangeDimension> dimension;

	public String getAttributeDescription(){
		return attributeDescription;
	}

	public MD_CoverageContentTypeCode getContentType(){
		return contentType;
	}

	public Collection<MD_RangeDimension> getDimension(){
		return dimension;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAttributeDescription(String newVal){
		attributeDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setContentType(MD_CoverageContentTypeCode newVal){
		contentType = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDimension(Collection<MD_RangeDimension> newVal){
		dimension = newVal;
	}
}//end MD_CoverageDescription