package org.iso.registry.core.model.cs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.iso.registry.core.model.IdentifiedItem;

import de.geoinfoffm.registry.core.ItemClass;

@Access(AccessType.FIELD)
@Table(name = "CoordinateSystem")
@Audited @Entity
public abstract class CoordinateSystemItem extends IdentifiedItem
{
	/**
	 * Ordered sequence of associations to the coordinate system axes included in a coordinate system.  
	 */
	@ManyToMany
	private List<CoordinateSystemAxisItem> axes = new ArrayList<CoordinateSystemAxisItem>();

	public List<CoordinateSystemAxisItem> getAxes() {
		return axes;
	}

	public void setAxes(List<CoordinateSystemAxisItem> axes) {
		this.axes = axes;
	}

	public void addAxis(CoordinateSystemAxisItem axis) {
		if (this.axes == null) {
			this.axes = new ArrayList<CoordinateSystemAxisItem>();
		}
		this.axes.add(axis);
	}

}
