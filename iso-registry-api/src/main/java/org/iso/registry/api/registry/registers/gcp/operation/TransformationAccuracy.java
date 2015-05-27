package org.iso.registry.api.registry.registers.gcp.operation;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.iso19103.Record;
import org.iso.registry.core.model.iso19103.RecordType;
import org.iso.registry.core.model.iso19103.RecordTypeImpl;
import org.iso.registry.core.model.iso19103.UnitOfMeasure;
import org.iso.registry.core.model.iso19115.dataquality.DQ_QuantitativeResult;
import org.iso.registry.core.model.iso19115.dataquality.QuantitativeResult;

import de.geoinfoffm.registry.core.model.iso19103.CharacterString;

@Access(AccessType.FIELD)
@Audited @Entity
public class TransformationAccuracy extends QuantitativeResult
{
	private Float accuracy;
	
	@Column(columnDefinition = "text")
	private String errorStatistic;
	
	@ManyToOne
	private UnitOfMeasureItem accuracyUnit;
	
	protected TransformationAccuracy() { }

	public TransformationAccuracy(Float accuracy, UnitOfMeasureItem accuracyUnit) {
		this.accuracy = accuracy;
		this.accuracyUnit = accuracyUnit;
	}

	@Override
	public String getErrorStatistic() {
		return this.errorStatistic;
	}

	@Override
	public void setErrorStatistic(CharacterString newVal) {
		this.errorStatistic = CharacterString.asString(newVal);
	}
	
	public void setErrorStatistic(String errorStatistic) {
		this.errorStatistic = errorStatistic;
	}

	@Override
	public Record getValue() {
		return new TransformationAccuracyValue(this.accuracy);
	}

	@Override
	public void setValue(Record newVal) {
		Number accuracy = (Number)newVal.locate("accuracy");
		if (accuracy != null) {
			this.accuracy = accuracy.floatValue();
		}
	}
	
	public void setValue(Float accuracy) {
		this.accuracy = accuracy;
	}

	@Override
	public RecordType getValueType() {
		return new RecordTypeImpl(TransformationAccuracyValue.class);
	}

	@Override
	public void setValueType(RecordType newVal) {
		// Ignore
	}

	@Override
	public UnitOfMeasure getValueUnit() {
		return this.accuracyUnit;
	}

	@Override
	public void setValueUnit(UnitOfMeasure newVal) {
		if (!(newVal instanceof UnitOfMeasureItem)) {
			throw new IllegalArgumentException("Parameter must be of type " + UnitOfMeasureItem.class.getCanonicalName());
		}
		
		this.accuracyUnit = (UnitOfMeasureItem)newVal;
	}

}
