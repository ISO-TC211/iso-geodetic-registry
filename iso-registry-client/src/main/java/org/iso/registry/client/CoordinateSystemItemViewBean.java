package org.iso.registry.client;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.cs.CoordinateSystemItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateSystemItemViewBean extends IdentifiedItemViewBean
{
	private List<CoordinateSystemAxisItemViewBean> axes;

	public CoordinateSystemItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public CoordinateSystemItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public CoordinateSystemItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public CoordinateSystemItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public CoordinateSystemItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public CoordinateSystemItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof CoordinateSystemItem)) {
			return;
		}
		
		CoordinateSystemItem item = (CoordinateSystemItem)registerItem;
		
		if (item.getAxes() != null) {
			for (CoordinateSystemAxisItem axis : item.getAxes()) {
				this.addAxis(new CoordinateSystemAxisItemViewBean(axis, false));
			}
		}
	}

	public List<CoordinateSystemAxisItemViewBean> getAxes() {
		return axes;
	}

	public void setAxes(List<CoordinateSystemAxisItemViewBean> axes) {
		this.axes = axes;
	}

	public void addAxis(CoordinateSystemAxisItemViewBean axis) {
		if (this.axes == null) {
			this.axes = new ArrayList<>();
		}
		this.axes.add(axis);
	}
}
