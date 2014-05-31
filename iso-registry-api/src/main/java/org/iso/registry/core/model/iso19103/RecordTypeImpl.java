package org.iso.registry.core.model.iso19103;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

public class RecordTypeImpl extends HashMap<String, Type> implements RecordType
{
	private String typeName; 
	
	protected RecordTypeImpl() {
	}
	
	public RecordTypeImpl(Class<?> type) {
		super();
		
		this.typeName = type.getCanonicalName();
		Map<String, Type> fields = new HashMap<>();
		for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(type)) {
			fields.put(pd.getName(), TypeWrapper.fromClass(pd.getPropertyType()));
		}
		this.putAll(fields);
	}
	
	public RecordTypeImpl(String typeName, Map<String, Type> members) {
		super(members);
		this.typeName = typeName;
	}

	@Override
	public String getTypeName() {
		return this.typeName;
	}

	@Override
	public Set<String> getMembers() {
		return this.keySet();
	}

	@Override
	public Map<String, Type> getMemberTypes() {
		return this;
	}

	@Override
	public boolean isInstance(Object record) {
		return (record instanceof RecordTypeImpl);
	}

	@Override
	public String locate(String memberName) {
		return this.get(memberName).getTypeName();
	}

}
