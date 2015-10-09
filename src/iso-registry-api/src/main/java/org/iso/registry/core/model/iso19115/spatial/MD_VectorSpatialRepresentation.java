package org.iso.registry.core.model.iso19115.spatial;

/**
 * Information about the vector spatial objects in the dataset
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:14
 */
public class MD_VectorSpatialRepresentation extends MD_SpatialRepresentation {

	private MD_GeometricObjects geometricObjects;
	/**
	 * Code which identifies the degree of complexity of the spatial relationships
	 */
	private MD_TopologyLevelCode topologyLevel;

	public MD_GeometricObjects getGeometricObjects(){
		return geometricObjects;
	}

	public MD_TopologyLevelCode getTopologyLevel(){
		return topologyLevel;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setGeometricObjects(MD_GeometricObjects newVal){
		geometricObjects = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTopologyLevel(MD_TopologyLevelCode newVal){
		topologyLevel = newVal;
	}
}//end MD_VectorSpatialRepresentation