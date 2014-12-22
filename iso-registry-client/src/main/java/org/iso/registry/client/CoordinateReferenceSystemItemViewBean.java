package org.iso.registry.client;

import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.api.registry.registers.gcp.crs.CoordinateReferenceSystemItemProposalDTO;
import org.iso.registry.core.model.CoordinateSystemType;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.GeodeticCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;
import org.iso.registry.core.model.iso19115.extent.EX_Extent;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateReferenceSystemItemViewBean extends IdentifiedItemViewBean
{
	private CoordinateSystemType type;
	private String scope;
	private ExtentDTO domainOfValidity;
	private DatumItemViewBean datum;
	private CoordinateSystemItemViewBean coordinateSystem;
	private CoordinateReferenceSystemItemViewBean horizontalCrs;
	private CoordinateReferenceSystemItemViewBean verticalCrs;

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
			if (((SingleCoordinateReferenceSystemItem)crsItem).getCoordinateSystem() != null) {
				this.setCoordinateSystem(new CoordinateSystemItemViewBean(((SingleCoordinateReferenceSystemItem<?>)crsItem).getCoordinateSystem()));
			}
			if (((SingleCoordinateReferenceSystemItem)crsItem).getDatum() != null) {
				this.setDatum(new DatumItemViewBean(((SingleCoordinateReferenceSystemItem<?>)crsItem).getDatum()));
			}
		}
		else if (crsItem instanceof CompoundCoordinateReferenceSystemItem) {
			CompoundCoordinateReferenceSystemItem ccrsItem = (CompoundCoordinateReferenceSystemItem)crsItem;
			// By convention, the first CRS is the horizontal component
			this.setHorizontalCrs(new CoordinateReferenceSystemItemViewBean(ccrsItem.getComponentReferenceSystem().get(0)));
			// By convention, the second CRS is the vertical component
			this.setVerticalCrs(new CoordinateReferenceSystemItemViewBean(ccrsItem.getComponentReferenceSystem().get(1)));
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
		
//		this.setAreaName(crsItem.getDomainOfValidity().getName());
//		this.setDomainOfValidity(new AreaItemViewBean(crsItem.getDomainOfValidity()));
		this.setDomainOfValidity(new ExtentDTO(crsItem.getDomainOfValidity()));
		this.setScope(crsItem.getScope());
		
//		this.setCode(crsItem.getCode());
//		this.setType(crsItem.getType());
//		this.setAreaName(crsItem.getAreaOfUse().getName());
//		
//		this.addAdditionalProperty("code", crsItem.getCode());
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

	public ExtentDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentDTO domainOfValidity) {
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

	public CoordinateReferenceSystemItemViewBean getHorizontalCrs() {
		return horizontalCrs;
	}

	public void setHorizontalCrs(CoordinateReferenceSystemItemViewBean horizontalCrs) {
		this.horizontalCrs = horizontalCrs;
	}

	public CoordinateReferenceSystemItemViewBean getVerticalCrs() {
		return verticalCrs;
	}

	public void setVerticalCrs(CoordinateReferenceSystemItemViewBean verticalCrs) {
		this.verticalCrs = verticalCrs;
	}
}
