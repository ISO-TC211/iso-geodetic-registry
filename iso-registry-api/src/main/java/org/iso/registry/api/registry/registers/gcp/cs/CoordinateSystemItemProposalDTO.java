package org.iso.registry.api.registry.registers.gcp.cs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemProposalDTO;
import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.cs.CartesianCoordinateSystemItem;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;
import org.iso.registry.core.model.cs.EllipsoidalCoordinateSystemItem;
import org.iso.registry.core.model.cs.SphericalCoordinateSystemItem;

import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

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
	
	public void setType(CoordinateSystemType type) {
		this.type = type;
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
	
	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof CoordinateSystemItem) {
			CoordinateSystemItem cs = (CoordinateSystemItem)item;

			for (CoordinateSystemAxisProposalDTO axisDto : this.getAxes()) {
				CoordinateSystemAxisItem axis = entityManager.find(CoordinateSystemAxisItem.class, axisDto.getReferencedItemUuid());
				cs.addAxis(axis);
			}
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof CoordinateSystemItem) {
			CoordinateSystemItem cs = (CoordinateSystemItem)item;
			if (item instanceof CartesianCoordinateSystemItem) {
				this.setType(CoordinateSystemType.CARTESIAN);
			}
			else if (item instanceof EllipsoidalCoordinateSystemItem) {
				this.setType(CoordinateSystemType.ELLIPSOIDAL);
			}
			else if (item instanceof SphericalCoordinateSystemItem) {
				this.setType(CoordinateSystemType.SPHERICAL);
			}
			else {
				this.setType(CoordinateSystemType.USER_DEFINED);
			}
		}
	}

}