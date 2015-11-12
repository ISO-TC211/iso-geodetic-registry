package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.HashMap;
import java.util.Map;

import org.iso.registry.core.model.iso19103.Record;
import org.iso.registry.core.model.iso19103.RecordType;
import org.iso.registry.core.model.iso19103.RecordTypeImpl;

public class TransformationAccuracyValue implements Record
{
	private Float accuracy;
	
	protected TransformationAccuracyValue() { }

	public TransformationAccuracyValue(Float accuracy) {
		this.accuracy = accuracy;
	}

	public Float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}

	@Override
	public Map<String, Object> getAttributes() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("accuracy", this.accuracy);
		
		return result;
	}

	@Override
	public RecordType getRecordType() {
		return new RecordTypeImpl(this.getClass());
	}

	@Override
	public Object locate(String memberName) {
		if ("accuracy".equals(memberName)) {
			return this.accuracy;
		}
		else {
			throw new IllegalArgumentException("Invalid member: " + memberName);
		}
	}

	@Override
	public void set(String memberName, Object value) {
		if ("accuracy".equals(memberName)) {
			if (value == null) {
				throw new NullPointerException("Cannot set null accuracy");
			}
			else if (value instanceof Number) {
				this.accuracy = ((Number)value).floatValue();
			}
			else {
				throw new IllegalArgumentException("Accuracy must be numeric");
			}
		}
		else {
			throw new IllegalArgumentException("Invalid member: " + memberName);
		}
	}

}
