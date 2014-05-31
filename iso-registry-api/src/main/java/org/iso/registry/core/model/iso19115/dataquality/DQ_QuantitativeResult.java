package org.iso.registry.core.model.iso19115.dataquality;

import org.iso.registry.core.model.iso19103.Record;
import org.iso.registry.core.model.iso19103.RecordType;
import org.iso.registry.core.model.iso19103.UnitOfMeasure;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

/**
 * Quantitative_conformance_measure from Quality Procedures.
 * 
 * Renamed to remove implied use limitation
 * 
 * OCL -- result is type specified by valueDomain result.tupleType = valueDomain
 * 
 * @author Florian.Esser
 * @created 17-Apr-2014 10:38:09
 */
public interface DQ_QuantitativeResult extends DQ_Result 
{
	String getErrorStatistic();
	void setErrorStatistic(CharacterString newVal);

	Record getValue();
	void setValue(Record newVal);
	
	RecordType getValueType();
	void setValueType(RecordType newVal);
	
	UnitOfMeasure getValueUnit();
	void setValueUnit(UnitOfMeasure newVal);
	
}// end DQ_QuantitativeResult