package org.iso.registry.core.model.iso19115.spatial;

import java.util.List;

/**
 * Types and numbers of raster spatial objects in the dataset
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_GridSpatialRepresentation extends MD_SpatialRepresentation {

	private List<MD_Dimension> axisDimensionProperties;
	/**
	 * Raster spatial objects used to locate zero-, two-, or three-dimensional locations in the dataset
	 */
	private MD_CellGeometryCode cellGeometry;
	private int numberOfDimensions;
	private boolean transformationParameterAvailability;

	public List<MD_Dimension> getAxisDimensionProperties(){
		return axisDimensionProperties;
	}

	public MD_CellGeometryCode getCellGeometry(){
		return cellGeometry;
	}

	public int getNumberOfDimensions(){
		return numberOfDimensions;
	}

	public boolean getTransformationParameterAvailability(){
		return transformationParameterAvailability;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAxisDimensionProperties(List<MD_Dimension> newVal){
		axisDimensionProperties = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCellGeometry(MD_CellGeometryCode newVal){
		cellGeometry = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setNumberOfDimensions(int newVal){
		numberOfDimensions = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransformationParameterAvailability(boolean newVal){
		transformationParameterAvailability = newVal;
	}
}//end MD_GridSpatialRepresentation