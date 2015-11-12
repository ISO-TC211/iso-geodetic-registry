//package de.geoinfoffm.registry.core.model.iso19115.content;
//
///**
// * @author Florian.Esser
// * @created 17-Apr-2014 10:38:11
// */
//public class MD_Band extends MD_RangeDimension {
//
//	/**
//	 * Maximum number of significant bits for the value in each band of each pixel without compression
//	 */
//	private int bitsPerValue;
//	/**
//	 * Highest wavelength that the sensor is capable of collecting within a designated band in metres
//	 */
//	private double maxValue;
//	/**
//	 * Lowest wavelength that the sensor is capable of collecting within a designated band in metres
//	 */
//	private double minValue;
//	private double offset;
//	private double peakResponse;
//	private double scaleFactor;
//	/**
//	 * Number of colours present in the image
//	 */
//	private int toneGradation;
//	private UomLength units;
//
//	public int getBitsPerValue(){
//		return bitsPerValue;
//	}
//
//	public double getMaxValue(){
//		return maxValue;
//	}
//
//	public double getMinValue(){
//		return minValue;
//	}
//
//	public double getOffset(){
//		return offset;
//	}
//
//	public double getPeakResponse(){
//		return peakResponse;
//	}
//
//	public double getScaleFactor(){
//		return scaleFactor;
//	}
//
//	public int getToneGradation(){
//		return toneGradation;
//	}
//
//	public UomLength getUnits(){
//		return units;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setBitsPerValue(int newVal){
//		bitsPerValue = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMaxValue(double newVal){
//		maxValue = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setMinValue(double newVal){
//		minValue = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setOffset(double newVal){
//		offset = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setPeakResponse(double newVal){
//		peakResponse = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setScaleFactor(double newVal){
//		scaleFactor = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setToneGradation(int newVal){
//		toneGradation = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setUnits(UomLength newVal){
//		units = newVal;
//	}
//}//end MD_Band