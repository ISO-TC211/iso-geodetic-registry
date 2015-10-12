package org.iso.registry.core.model.iso19115.distribution;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Description of the form of the data to be distributed
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:12
 */
public class MD_Format {

	/**
	 * Amendment number of the format version
	 */
	private CharacterString amendmentNumber;
	/**
	 * Recommendations of algorithms or processes that can be applied to read or expand datasets to which data compression
	 * techniques have been applied
	 */
	private CharacterString fileDecompressionTechnique;
	/**
	 * Name of the data transfer format(s) offered by the  distributor for an available dataset.
	 * Example:  SDTS
	 */
	private CharacterString name;
	/**
	 * Name of a subset, profile, or product specification of the format
	 */
	private CharacterString specification;
	/**
	 * Version number of the format
	 */
	private CharacterString version;

	public CharacterString getAmendmentNumber(){
		return amendmentNumber;
	}

	public CharacterString getFileDecompressionTechnique(){
		return fileDecompressionTechnique;
	}

	public CharacterString getName(){
		return name;
	}

	public CharacterString getSpecification(){
		return specification;
	}

	public CharacterString getVersion(){
		return version;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAmendmentNumber(CharacterString newVal){
		amendmentNumber = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFileDecompressionTechnique(CharacterString newVal){
		fileDecompressionTechnique = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setName(CharacterString newVal){
		name = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSpecification(CharacterString newVal){
		specification = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setVersion(CharacterString newVal){
		version = newVal;
	}
}//end MD_Format