//package org.iso.registry.core.model.iso19115.extent;
//
//import javax.persistence.Access;
//import javax.persistence.AccessType;
//import javax.persistence.Embeddable;
//import javax.persistence.ManyToOne;
//
///**
// * Vertical domain of dataset
// * @author Florian.Esser
// * @created 17-Apr-2014 10:38:10
// */
//@Access(AccessType.FIELD)
//@Embeddable
//public class EX_VerticalExtent 
//{
//	/**
//	 * Highest vertical extent contained in the dataset 
//	 */
//	private double maximumValue;
//	/**
//	 * Lowest vertical extent contained in the dataset 
//	 */
//	private double minimumValue;
//	
//	@ManyToOne
//	private SC_CRS verticalCRS;
//
//	public double getMaximumValue(){
//		return maximumValue;
//	}
//
//	public double getMinimumValue(){
//		return minimumValue;
//	}
//
//	public SC_CRS getVerticalCRS(){
//		return verticalCRS;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMaximumValue(double newVal){
//		maximumValue = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMinimumValue(double newVal){
//		minimumValue = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setVerticalCRS(SC_CRS newVal){
//		verticalCRS = newVal;
//	}
//}//end EX_VerticalExtent