package org.iso.registry.core.model.iso19103;

import java.util.Map;
import java.util.Set;

public interface RecordType extends Type
{
	Set<String> getMembers();
	Map<String, Type> getMemberTypes();
	boolean isInstance(Object record);
	String locate(String memberName);
}
