package org.iso.registry.api;

import org.isotc211.iso19139.common.Boolean_PropertyType;
import org.isotc211.iso19139.common.CharacterString_PropertyType;
import org.isotc211.iso19139.common.Integer_PropertyType;
import org.isotc211.iso19139.common.Real_PropertyType;

import de.geoinfoffm.registry.api.iso.IsoXmlFactory;

public class GcoConverter
{

	public static CharacterString_PropertyType convertToGcoString(String value) {
		return IsoXmlFactory.characterString(value);
	}

	public static Boolean_PropertyType convertToGcoBoolean(Boolean value) {
		return IsoXmlFactory.booleanProperty(value);
	}

	public static Integer_PropertyType convertToGcoInteger(Integer value) {
		return IsoXmlFactory.integer(value);
	}

	public static Real_PropertyType convertToGcoDouble(Double value) {
		final Real_PropertyType result = new Real_PropertyType();
		if (value != null) {
			result.setReal(value);
		}
		return result;
	}

	public static String convertGcoToString(CharacterString_PropertyType value) {
		return IsoXmlFactory.characterString(value);
	}

	public static Boolean convertGcoToBoolean(Boolean_PropertyType value) {
		if (value.isSetBoolean())
			return value.isBoolean();
		else
			return null;
	}

	public static Integer convertGcoToInteger(Integer_PropertyType value) {
		if (value.isSetInteger())
			return value.getInteger().intValue();
		else
			return null;
	}

	public static Double convertGcoToDouble(Real_PropertyType value) {
		if (value.isSetReal())
			return value.getReal();
		else
			return null;
	}
}
