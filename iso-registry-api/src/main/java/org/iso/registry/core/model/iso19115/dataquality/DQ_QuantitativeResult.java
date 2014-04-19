//package de.geoinfoffm.registry.core.model.iso19115.dataquality;
//
//import org.eclipse.persistence.sessions.Record;
//
//import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
//
///**
// * Quantitative_conformance_measure from Quality Procedures.
// * 
// * Renamed to remove implied use limitation
// * 
// * OCL -- result is type specified by valueDomain result.tupleType = valueDomain
// * 
// * @author Florian.Esser
// * @created 17-Apr-2014 10:38:09
// */
//public class DQ_QuantitativeResult extends DQ_Result {
//
//	private CharacterString errorStatistic;
//	private Record value;
//	private RecordType valueType;
//	private UnitOfMeasure valueUnit;
//
//	public CharacterString getErrorStatistic() {
//		return errorStatistic;
//	}
//
//	public Record getValue() {
//		return value;
//	}
//
//	public RecordType getValueType() {
//		return valueType;
//	}
//
//	public UnitOfMeasure getValueUnit() {
//		return valueUnit;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setErrorStatistic(CharacterString newVal) {
//		errorStatistic = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setValue(Record newVal) {
//		value = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setValueType(RecordType newVal) {
//		valueType = newVal;
//	}
//
//	/**
//	 * 
//	 * @param newVal
//	 */
//	public void setValueUnit(UnitOfMeasure newVal) {
//		valueUnit = newVal;
//	}
//}// end DQ_QuantitativeResult