package org.iso.registry.core.model.iso19103;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

public class RecordImpl extends HashMap<String, Object> implements Record
{
	private RecordType recordType;
	
	protected RecordImpl() {
	}

	public RecordImpl(RecordType type) {
		this.recordType = type;
	}
	
	public RecordImpl(RecordType type, Map<String, Object> values) {
		super(values);
		this.recordType = type;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return this;
	}

	@Override
	public RecordType getRecordType() {
		return this.recordType;
	}

	@Override
	public Object locate(String memberName) {
		return this.get(memberName);
	}

	@Override
	public void set(String memberName, Object value) {
		this.put(memberName, value);
	}
}
