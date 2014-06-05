package org.iso.registry.core.model.iso19103;

public class TypeWrapper implements Type
{
	private Class<?> type;

	public TypeWrapper(Class<?> type) {
		this.type = type;
	}

	@Override
	public String getTypeName() {
		return type.getCanonicalName();
	}
	
	public static Type fromClass(Class<?> type) {
		return new TypeWrapper(type);
	}

}
