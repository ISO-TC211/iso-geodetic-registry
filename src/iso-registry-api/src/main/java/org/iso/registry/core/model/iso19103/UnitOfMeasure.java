package org.iso.registry.core.model.iso19103;


public interface UnitOfMeasure
{
	MeasureType getMeasureType();
	void setMeasureType(MeasureType type);

	String getNameStandardUnit();
	void setNameStandardUnit(String nameStandardUnit);

	Double getOffsetToStandardUnit();
	void setOffsetToStandardUnit(Double offsetToStandardUnit);

	Double getScaleToStandardUnit();
	void setScaleToStandardUnit(Double scaleToStandardUnit);

	String getUomName();
	void setUomName(String uomName);

	String getUomSymbol();
	void setUomSymbol(String uomSymbol);
}
