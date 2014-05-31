package org.iso.registry.core.model.operation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Access(AccessType.FIELD)
@Audited @Entity
public class OperationParameterValue extends GeneralParameterValue<OperationParameterItem>
{
	private static final long serialVersionUID = -7253389945022467418L;
	
	public static enum OperationParameterValueType {
		MEASURE,
		STRING,
		INTEGER,
		BOOLEAN,
		MEASURE_LIST,
		INTEGER_LIST,
		FILE
	}

	private OperationParameterValueType parameterType;
	
	@Embedded 
	@ElementCollection
	private List<Measure> parameterValue;
	
	@Column(columnDefinition = "text")
	@ElementCollection
	private List<String> parameterValueSimple;
	

	protected OperationParameterValue() {
		super();
	}

	protected OperationParameterValue(UUID id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public OperationParameterValue(OperationParameterValueType type) {
		this.parameterType = type;
	}
	
	public OperationParameterValue(Measure value) {
		this.parameterType = OperationParameterValueType.MEASURE;
		this.parameterValue = new ArrayList<>();
		this.parameterValue.add(value);
	}
	
	public OperationParameterValue(File file) {
		this.parameterType = OperationParameterValueType.FILE;
		this.parameterValueSimple = new ArrayList<>();
		this.parameterValueSimple.add(file.getName());
	}
	
	public OperationParameterValue(String string) {
		this.parameterType = OperationParameterValueType.STRING;
		this.parameterValueSimple = new ArrayList<>();
		this.parameterValueSimple.add(string);
	}
	
	public OperationParameterValue(Integer integer) {
		this.parameterType = OperationParameterValueType.INTEGER;
		this.parameterValueSimple = new ArrayList<>();
		this.parameterValueSimple.add(integer.toString());
	}
	
	public OperationParameterValue(Boolean bool) {
		this.parameterType = OperationParameterValueType.BOOLEAN;
		this.parameterValueSimple = new ArrayList<>();
		this.parameterValueSimple.add(bool.toString());
	}
	
	public <T> OperationParameterValue(List<T> list, Class<T> listType) {
		if (listType.equals(Integer.class)) {
			this.parameterType = OperationParameterValueType.INTEGER_LIST;
			this.parameterValueSimple = new ArrayList<>();
			for (T integer : list) {
				this.parameterValueSimple.add(integer.toString());
			}
		}
		else if (listType.equals(Measure.class)) {
			this.parameterType = OperationParameterValueType.MEASURE_LIST;
			this.parameterValue = new ArrayList<>();
			this.parameterValue.addAll((List<Measure>)list);
		}
	}

	public OperationParameterValueType getParameterType() {
		return parameterType;
	}

	public void setParameterType(OperationParameterValueType parameterType) {
		this.parameterType = parameterType;
	}

	public List<Measure> getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(List<Measure> parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	public Object getValue() {
		switch (this.parameterType) {
			case MEASURE:
				if (this.parameterValue == null || this.parameterValue.isEmpty()) {
					return null; 
				}
				break;
			case MEASURE_LIST:
				if (this.parameterValue == null || this.parameterValue.isEmpty()) {
					return new ArrayList<>(); 
				}
				break;
			default:
				if (this.parameterValueSimple == null || this.parameterValueSimple.isEmpty()) {
					return null;
				}
		}
		
		switch (this.parameterType) {
			case STRING:
				return this.parameterValueSimple.get(0);
			case INTEGER:
				return Integer.parseInt(this.parameterValueSimple.get(0));
			case INTEGER_LIST:
				List<Integer> result = new ArrayList<>();
				for (String value : this.parameterValueSimple) {
					result.add(Integer.parseInt(value));
				}
				return result;
			case BOOLEAN:
				return Boolean.parseBoolean(this.parameterValueSimple.get(0));
			case FILE:
				return new File(this.parameterValueSimple.get(0));
			case MEASURE:
				return this.parameterValue.get(0);
			case MEASURE_LIST:
				return this.parameterValue;
			default:
				return null;
		}
	}

}
