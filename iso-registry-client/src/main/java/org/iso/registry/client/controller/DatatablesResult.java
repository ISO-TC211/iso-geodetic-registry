package org.iso.registry.client.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatatablesResult
{
	private long totalRecords;
	private long totalDisplayRecords;
	private int echo;
	private Object data;
	
	public DatatablesResult(long totalRecords, long totalDisplayRecords, String echo, Object data) {
		this.totalRecords = totalRecords;
		this.totalDisplayRecords = totalDisplayRecords;
		this.echo = Integer.parseInt(echo);
		this.data = data;
	}

	@JsonProperty("iTotalRecords")
	public long getTotalRecords() {
		return totalRecords;
	}

	@JsonProperty("iTotalDisplayRecords")
	public long getTotalDisplayRecords() {
		return totalDisplayRecords;
	}

	@JsonProperty("sEcho")
	public String getEcho() {
		return Integer.toString(echo);
	}

	@JsonProperty("aaData")
	public Object getData() {
		return data;
	}
}
