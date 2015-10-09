package org.iso.registry.core.model.iso19103;

import java.util.Map;

public interface Record
{
	Map<String, Object> getAttributes();
	RecordType getRecordType();
	Object locate(String memberName);
	void set(String memberName, Object value);
}
