package org.iso.registry.api.registry;

import org.iso.registry.api.MeasureType_Type;
import org.iso.registry.core.model.iso19103.MeasureType;
import org.iso.registry.core.model.operation.OperationParameterValue.OperationParameterValueType;

public class RegistryModelXmlConverter {

	public static MeasureType_Type convertModelToXmlMeasureType(MeasureType value) {
		if (value == null) {
			return null;
		}
		switch (value) {
		case ANGLE:
			return MeasureType_Type.ANGLE;
		case AREA:
			return MeasureType_Type.AREA;
		case LENGTH:
			return MeasureType_Type.LENGTH;
		case SCALE:
			return MeasureType_Type.SCALE;
		case TIME:
			return MeasureType_Type.TIME;
		case VELOCITY:
			return MeasureType_Type.VELOCITY;
		case VOLUME:
			return MeasureType_Type.VOLUME;
		case WEIGHT:
			return MeasureType_Type.WEIGHT;
		default:
			return null;
		}
	}

	public static MeasureType convertXmlToModelMeasureType(MeasureType_Type value) {
		if (value == null) {
			return null;
		}
		switch (value) {
		case ANGLE:
			return MeasureType.ANGLE;
		case AREA:
			return MeasureType.AREA;
		case LENGTH:
			return MeasureType.LENGTH;
		case SCALE:
			return MeasureType.SCALE;
		case TIME:
			return MeasureType.TIME;
		case VELOCITY:
			return MeasureType.VELOCITY;
		case VOLUME:
			return MeasureType.VOLUME;
		case WEIGHT:
			return MeasureType.WEIGHT;
		default:
			return null;
		}
	}

	public static MeasureType convertSoapXmlToModelMeasureType(de.geoinfoffm.registry.api.soap.MeasureType_Type value) {
		if (value == null) {
			return null;
		}
		switch (value) {
		case ANGLE:
			return MeasureType.ANGLE;
		case AREA:
			return MeasureType.AREA;
		case LENGTH:
			return MeasureType.LENGTH;
		case SCALE:
			return MeasureType.SCALE;
		case TIME:
			return MeasureType.TIME;
		case VELOCITY:
			return MeasureType.VELOCITY;
		case VOLUME:
			return MeasureType.VOLUME;
		case WEIGHT:
			return MeasureType.WEIGHT;
		default:
			return null;
		}
	}
}
