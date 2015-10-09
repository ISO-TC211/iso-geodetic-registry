package org.iso.registry.core.model.iso19136;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class CodeType
{
	private String codeSpace;
	private String code;
	
	protected CodeType() { }
	
	public CodeType(String code) {
		this.code = code;
	}
	
	public CodeType(String value, String codeSpace) {
		this(value);
		
		this.codeSpace = codeSpace;
	}

	public String getCodeSpace() {
		return codeSpace;
	}

	public void setCodeSpace(String codeSpace) {
		this.codeSpace = codeSpace;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
