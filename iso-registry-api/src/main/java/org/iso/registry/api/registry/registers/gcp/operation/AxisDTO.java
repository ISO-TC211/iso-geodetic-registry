package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.UUID;

import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;

public class AxisDTO
{
	private UUID axisUuid;
	private String axisName;
	
	public AxisDTO() { }

	public AxisDTO(CoordinateSystemAxisItem axis) {
		this.axisUuid = axis.getUuid();
		this.axisName = axis.getName();
	}
	public AxisDTO(UUID axisUuid, String axisName) {
		this.axisUuid = axisUuid;
		this.axisName = axisName;
	}

	public UUID getAxisUuid() {
		return axisUuid;
	}

	public void setAxisUuid(UUID axisUuid) {
		this.axisUuid = axisUuid;
	}

	public String getAxisName() {
		return axisName;
	}

	public void setAxisName(String axisName) {
		this.axisName = axisName;
	}
}
