package org.iso.registry.core.model.iso19115.content;

import de.geoinfoffm.registry.core.model.iso19115.MD_Identifier;

/**
 * Information about an image's suitability for use
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_ImageDescription extends MD_CoverageDescription {

	/**
	 * Code which ndicates whether or not constants are available  which allow for camera calibration corrections.
	 */
	private boolean cameraCalibrationInformationAvailability;
	/**
	 * Area of the dataset obscured by clouds, expressed as a percentage of the spatial extent
	 */
	private double cloudCoverPercentage;
	/**
	 * Counts the number of lossy compression cycles performed on the image
	 */
	private int compressionGenerationQuantity;
	/**
	 * Code which indicates whether or not Calibration Reseau information is available
	 */
	private boolean filmDistortionInformationAvailability;
	/**
	 * Illumination azimuth measured in degrees clockwise from true north at the time the image is taken
	 */
	private double illuminationAzimuthAngle;
	/**
	 * Illumination elevation measured in degrees clockwise from the target plane at intersection of the optical line of sight
	 * with the earth's surface
	 */
	private double illuminationElevationAngle;
	/**
	 * Specifies the image quality 
	 */
	private MD_Identifier imageQualityCode;
	/**
	 * Code which indicates conditions which affect the quality of the image
	 */
	private MD_ImagingConditionCode imagingCondition;
	/**
	 * Code which indicates whether or not lens aberration correction information is available
	 */
	private boolean lensDistortionInformationAvailability;
	/**
	 * Image distributor's code that identifies the level of radiometric and geometric processing applied against the image
	 * -Examples: "LEVEL1A", "LEVEL1B", "SPOTVIEWORTHO", "SPOTVIEWPRECISIO"
	 */
	private MD_Identifier processingLevelCode;
	/**
	 * Code which indicates whether or not Standard Radiometric Product data is available
	 */
	private boolean radiometricCalibrationDataAvailability;
	/**
	 * Code which indicates whether or not triangulation has been performed upon the image
	 */
	private boolean triangulationIndicator;

	public boolean getCameraCalibrationInformationAvailability(){
		return cameraCalibrationInformationAvailability;
	}

	public double getCloudCoverPercentage(){
		return cloudCoverPercentage;
	}

	public int getCompressionGenerationQuantity(){
		return compressionGenerationQuantity;
	}

	public boolean getFilmDistortionInformationAvailability(){
		return filmDistortionInformationAvailability;
	}

	public double getIlluminationAzimuthAngle(){
		return illuminationAzimuthAngle;
	}

	public double getIlluminationElevationAngle(){
		return illuminationElevationAngle;
	}

	public MD_Identifier getImageQualityCode(){
		return imageQualityCode;
	}

	public MD_ImagingConditionCode getImagingCondition(){
		return imagingCondition;
	}

	public boolean getLensDistortionInformationAvailability(){
		return lensDistortionInformationAvailability;
	}

	public MD_Identifier getProcessingLevelCode(){
		return processingLevelCode;
	}

	public boolean getRadiometricCalibrationDataAvailability(){
		return radiometricCalibrationDataAvailability;
	}

	public boolean getTriangulationIndicator(){
		return triangulationIndicator;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCameraCalibrationInformationAvailability(boolean newVal){
		cameraCalibrationInformationAvailability = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCloudCoverPercentage(double newVal){
		cloudCoverPercentage = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCompressionGenerationQuantity(int newVal){
		compressionGenerationQuantity = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFilmDistortionInformationAvailability(boolean newVal){
		filmDistortionInformationAvailability = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setIlluminationAzimuthAngle(double newVal){
		illuminationAzimuthAngle = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setIlluminationElevationAngle(double newVal){
		illuminationElevationAngle = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setImageQualityCode(MD_Identifier newVal){
		imageQualityCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setImagingCondition(MD_ImagingConditionCode newVal){
		imagingCondition = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setLensDistortionInformationAvailability(boolean newVal){
		lensDistortionInformationAvailability = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProcessingLevelCode(MD_Identifier newVal){
		processingLevelCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setRadiometricCalibrationDataAvailability(boolean newVal){
		radiometricCalibrationDataAvailability = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTriangulationIndicator(boolean newVal){
		triangulationIndicator = newVal;
	}
}//end MD_ImageDescription