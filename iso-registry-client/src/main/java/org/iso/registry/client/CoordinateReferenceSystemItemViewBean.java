package org.iso.registry.client;

import org.iso.registry.core.model.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.CoordinateSystemType;

import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateReferenceSystemItemViewBean extends RegisterItemViewBean
{
	private Integer code;
	private CoordinateSystemType type;
	private String scope;
	private String areName;

	public CoordinateReferenceSystemItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public CoordinateReferenceSystemItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem item) {
		if (!(item instanceof CoordinateReferenceSystemItem)) {
			return;
		}
		
		CoordinateReferenceSystemItem crsItem = (CoordinateReferenceSystemItem)item;
		
		this.setCode(crsItem.getCode());
		this.setType(crsItem.getType());
		this.setAreaName(crsItem.getAreaOfUse().getName());
		this.setScope(crsItem.getScope());
		
		this.addAdditionalProperty("code", crsItem.getCode());
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public CoordinateSystemType getType() {
		return type;
	}

	public void setType(CoordinateSystemType type) {
		this.type = type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAreaName() {
		return areName;
	}

	public void setAreaName(String areaName) {
		this.areName = areaName;
	}
}
