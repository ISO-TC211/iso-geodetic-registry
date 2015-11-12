package org.iso.registry.core.model.iso19115.spatial;

/**
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_GeometricObjects {

	/**
	 * Total number of the point or vector object type occurring in the dataset
	 */
	private int geometricObjectCount;
	/**
	 * Name of point and vector spatial objects used to locate zero-, one-, and two-dimensional spatial locations in the
	 * dataset 
	 */
	private MD_GeometricObjectTypeCode geometricObjectType;

	public int getGeometricObjectCount(){
		return geometricObjectCount;
	}

	public MD_GeometricObjectTypeCode getGeometricObjectType(){
		return geometricObjectType;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setGeometricObjectCount(int newVal){
		geometricObjectCount = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setGeometricObjectType(MD_GeometricObjectTypeCode newVal){
		geometricObjectType = newVal;
	}
}//end MD_GeometricObjects