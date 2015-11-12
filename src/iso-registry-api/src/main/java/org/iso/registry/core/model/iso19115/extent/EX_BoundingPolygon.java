package org.iso.registry.core.model.iso19115.extent;

import javax.persistence.Embeddable;

import org.iso.registry.core.model.iso19107.GM_Object;

/**
 * Boundary enclosing the dataset expressed as the closed set of (x,y) coordinates of the polygon (last point replicates
 * first point)
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:10
 */
@Embeddable
public class EX_BoundingPolygon extends EX_GeographicExtent 
{

	/**
	 * Sets of points in a particular coordinate reference system.
	 */
	private GM_Object polygon;

	public GM_Object getPolygon(){
		return polygon;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setPolygon(GM_Object newVal){
		polygon = newVal;
	}
}//end EX_BoundingPolygon