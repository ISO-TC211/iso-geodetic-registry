package org.iso.registry.api.registry.registers.gcp.cs;

import org.iso.registry.api.registry.registers.gcp.IdentifiedItemViewBean;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemViewBean;
import org.iso.registry.core.model.cs.CoordinateSystemAxisItem;
import org.iso.registry.core.model.iso19111.cs.CS_AxisDirection;
import org.iso.registry.core.model.iso19111.cs.CS_RangeMeaning;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateSystemAxisItemViewBean extends IdentifiedItemViewBean
{

	private String axisAbbreviation;
	private CS_AxisDirection axisDirection;
	private UnitOfMeasureItemViewBean axisUnit;
	private Number maximumValue;
	private Number minimumValue;
	private CS_RangeMeaning rangeMeaning;

	public CoordinateSystemAxisItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateSystemAxisItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof CoordinateSystemAxisItem)) {
			return;
		}
		
		CoordinateSystemAxisItem item = (CoordinateSystemAxisItem)registerItem;
		
		this.axisAbbreviation = item.getAxisAbbreviation();
		this.axisDirection = item.getAxisDirection();
		if (item.getAxisUnit() != null) {
			this.setAxisUnit(new UnitOfMeasureItemViewBean(item.getAxisUnit(), false));
		}
		this.maximumValue = item.getMaximumValue();
		this.minimumValue = item.getMinimumValue();
		this.rangeMeaning = item.getRangeMeaning();
	}

	public String getAxisAbbreviation() {
		return axisAbbreviation;
	}

	public void setAxisAbbreviation(String axisAbbreviation) {
		this.axisAbbreviation = axisAbbreviation;
	}

	public CS_AxisDirection getAxisDirection() {
		return axisDirection;
	}

	public void setAxisDirection(CS_AxisDirection axisDirection) {
		this.axisDirection = axisDirection;
	}

	public UnitOfMeasureItemViewBean getAxisUnit() {
		return axisUnit;
	}

	public void setAxisUnit(UnitOfMeasureItemViewBean axisUnit) {
		this.axisUnit = axisUnit;
	}

	public Number getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(Number maximumValue) {
		this.maximumValue = maximumValue;
	}

	public Number getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Number minimumValue) {
		this.minimumValue = minimumValue;
	}

	public CS_RangeMeaning getRangeMeaning() {
		return rangeMeaning;
	}

	public void setRangeMeaning(CS_RangeMeaning rangeMeaning) {
		this.rangeMeaning = rangeMeaning;
	}

}
