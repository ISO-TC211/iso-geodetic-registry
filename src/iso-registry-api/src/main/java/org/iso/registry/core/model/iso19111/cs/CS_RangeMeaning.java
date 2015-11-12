package org.iso.registry.core.model.iso19111.cs;
/**
 * Meaning of the axis value range specified through minimumValue and maximumValue.
 * @author Florian.Esser
 * @created 16-Apr-2014 23:49:48
 */
public enum CS_RangeMeaning 
{

	/**
	 * Any value between and including minimumValue and maximumValue is valid.
	 */
	EXACT,
	/**
	 * The axis is continuous with values wrapping around at the minimumValue and maximumValue. Values with the same meaning
	 * repeat modulo the difference between maximumValue and minimumValue.
	 */
	WRAPAROUND;

}//end CS_RangeMeaning