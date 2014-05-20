package org.iso.registry.client;

import org.iso.registry.core.model.CoordinateSystemType;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.GeodeticCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateReferenceSystemItemViewBean extends IdentifiedItemViewBean
{
	private Integer code;
	private CoordinateSystemType type;
	private String scope;
	private String areaName;
	private AreaItemViewBean domainOfValidity;
	private DatumItemViewBean datum;
	private CoordinateSystemItemViewBean coordinateSystem;

	public CoordinateReferenceSystemItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public CoordinateReferenceSystemItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public CoordinateReferenceSystemItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public CoordinateReferenceSystemItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public CoordinateReferenceSystemItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem item) {
		super.addAdditionalProperties(item);
		
		if (!(item instanceof CoordinateReferenceSystemItem)) {
			return;
		}
		
		CoordinateReferenceSystemItem crsItem = (CoordinateReferenceSystemItem)item;
		if (crsItem instanceof SingleCoordinateReferenceSystemItem<?>) {
			this.setCoordinateSystem(new CoordinateSystemItemViewBean(((SingleCoordinateReferenceSystemItem<?>)crsItem).getCoordinateSystem()));
			this.setDatum(new DatumItemViewBean(((SingleCoordinateReferenceSystemItem<?>)crsItem).getDatum()));
		}
		if (crsItem instanceof GeodeticCoordinateReferenceSystemItem) {
			switch (((GeodeticCoordinateReferenceSystemItem)crsItem).getCoordinateSystem().getAxes().size()) {
				case 2:
					this.setType(CoordinateSystemType.GEOGRAPHIC_2D);
					break;
				case 3:
					this.setType(CoordinateSystemType.GEOGRAPHIC_3D);
					break;
			}
		}
		
		this.setAreaName(crsItem.getDomainOfValidity().getName());
		this.setDomainOfValidity(new AreaItemViewBean(crsItem.getDomainOfValidity()));
		this.setScope(crsItem.getScope());
		
//		this.setCode(crsItem.getCode());
//		this.setType(crsItem.getType());
//		this.setAreaName(crsItem.getAreaOfUse().getName());
//		
//		this.addAdditionalProperty("code", crsItem.getCode());
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
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public AreaItemViewBean getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(AreaItemViewBean domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

	public DatumItemViewBean getDatum() {
		return datum;
	}

	public void setDatum(DatumItemViewBean datum) {
		this.datum = datum;
	}

	public CoordinateSystemItemViewBean getCoordinateSystem() {
		return coordinateSystem;
	}

	public void setCoordinateSystem(CoordinateSystemItemViewBean coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}
}
