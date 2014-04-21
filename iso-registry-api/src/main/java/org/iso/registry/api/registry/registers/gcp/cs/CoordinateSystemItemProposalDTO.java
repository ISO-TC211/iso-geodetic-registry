package org.iso.registry.api.registry.registers.gcp.cs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.core.model.cs.CoordinateSystemItem;

public class CoordinateSystemItemProposalDTO extends IdentifiedItemProposalDTO
{
	public enum CoordinateSystemType {
		AFFINE,
		CARTESIAN,
		CYLINDRICAL,
		ELLIPSOIDAL,
		ENGINEERING,
		IMAGE,
		LINEAR,
		POLAR,
		SPHERICAL,
		VERTICAL,
		USER_DEFINED
	}
	
	private CoordinateSystemType type;
	private List<CoordinateSystemAxisProposalDTO> axes = new ArrayList<CoordinateSystemAxisProposalDTO>();
	
	public CoordinateSystemItemProposalDTO() { }
	
	public CoordinateSystemItemProposalDTO(CoordinateSystemItem item) {
		super(item);
//		if (item instanceof EllipsoidalCoordinateSystemItem) {
//			this.type = CoordinateSystemType.ELLIPSOIDAL;
//		}
//		else {
//			throw new RuntimeException("Not yet implemented");
//		}
//		
//		for (CoordinateSystemAxisItem axis : item.getAxes()) {
//			this.addAxis(new CoordinateSystemAxisProposalDTO(axis));
//		}
	}
	
	public CoordinateSystemItemProposalDTO(CoordinateSystemType type, CoordinateSystemAxisProposalDTO firstAxis, CoordinateSystemAxisProposalDTO... otherAxes) {
		this.type = type;
		this.axes.add(firstAxis);
		for (CoordinateSystemAxisProposalDTO axis : otherAxes) {
			this.axes.add(axis);
		}
	}
	
	public CoordinateSystemType getType() {
		return this.type;
	}
	
	public List<CoordinateSystemAxisProposalDTO> getAxes() {
		return Collections.unmodifiableList(this.axes);
	}
	
	public void addAxis(CoordinateSystemAxisProposalDTO axis) {
		if (this.axes == null) {
			this.axes = new ArrayList<CoordinateSystemAxisProposalDTO>();
		}
		this.axes.add(axis);
	}
}