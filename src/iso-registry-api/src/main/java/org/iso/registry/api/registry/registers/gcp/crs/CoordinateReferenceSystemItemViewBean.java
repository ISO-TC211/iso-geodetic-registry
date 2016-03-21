package org.iso.registry.api.registry.registers.gcp.crs;

import org.iso.registry.api.AbstractCoordinateReferenceSystemItem_Type;
import org.iso.registry.api.AbstractSingleCoordinateReferenceSystemItem_Type;
import org.iso.registry.api.CoordinateSystemItem_PropertyType;
import org.iso.registry.api.DatumItem_PropertyType;
import org.iso.registry.api.GcoConverter;
import org.iso.registry.api.SingleCoordinateReferenceSystemItem_PropertyType;
import org.iso.registry.api.SingleOperationItem_PropertyType;
import org.iso.registry.api.registry.registers.gcp.cs.CoordinateSystemItemViewBean;
import org.iso.registry.api.registry.registers.gcp.datum.DatumItemViewBean;
import org.iso.registry.api.registry.registers.gcp.operation.SingleOperationItemViewBean;
import org.iso.registry.core.model.CoordinateSystemType;
import org.iso.registry.core.model.crs.CompoundCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.GeodeticCoordinateReferenceSystemItem;
import org.iso.registry.core.model.crs.SingleCoordinateReferenceSystemItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class CoordinateReferenceSystemItemViewBean extends ReferenceSystemItemViewBean
{
	private CoordinateSystemType type;
	private String scope;
	private DatumItemViewBean datum;
	private CoordinateSystemItemViewBean coordinateSystem;
	private CoordinateReferenceSystemItemViewBean horizontalCrs;
	private CoordinateReferenceSystemItemViewBean verticalCrs;
	private CoordinateReferenceSystemItemViewBean baseCrs;
	private SingleOperationItemViewBean operation;

	public CoordinateReferenceSystemItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public CoordinateReferenceSystemItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public CoordinateReferenceSystemItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public CoordinateReferenceSystemItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public CoordinateReferenceSystemItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public CoordinateReferenceSystemItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	public void setXmlValues(RE_RegisterItem_Type result) {
		super.setXmlValues(result);
		if (result instanceof AbstractCoordinateReferenceSystemItem_Type) {
			final AbstractCoordinateReferenceSystemItem_Type xmlBean = (AbstractCoordinateReferenceSystemItem_Type)result;
			final CoordinateReferenceSystemItemViewBean viewBean = this;
			xmlBean.setScope(GcoConverter.convertToGcoString(viewBean.getScope()));
		}
		
		if (result instanceof AbstractSingleCoordinateReferenceSystemItem_Type) {
			final AbstractSingleCoordinateReferenceSystemItem_Type xmlBean = (AbstractSingleCoordinateReferenceSystemItem_Type)result;
			final CoordinateReferenceSystemItemViewBean viewBean = this;
			if (viewBean.getDatum() != null && viewBean.getDatum().getUuid() != null) {
				final DatumItem_PropertyType xmlBeanProp= new DatumItem_PropertyType(); 
				xmlBeanProp.setUuidref(viewBean.getDatum().getUuid().toString());
				xmlBean.setDatum(xmlBeanProp);
			}
			if (viewBean.getCoordinateSystem() != null && viewBean.getCoordinateSystem().getUuid() != null) {
				final CoordinateSystemItem_PropertyType xmlBeanProp= new CoordinateSystemItem_PropertyType(); 
				xmlBeanProp.setUuidref(viewBean.getCoordinateSystem().getUuid().toString());
				xmlBean.setCoordinateSystem(xmlBeanProp);
			}
			if (viewBean.getBaseCrs() != null && viewBean.getBaseCrs().getUuid() != null) {
				final SingleCoordinateReferenceSystemItem_PropertyType xmlBeanProp= new SingleCoordinateReferenceSystemItem_PropertyType(); 
				xmlBeanProp.setUuidref(viewBean.getBaseCrs().getUuid().toString());
				xmlBean.setBaseCrs(xmlBeanProp);
			}
			if (viewBean.getOperation() != null && viewBean.getOperation().getUuid() != null) {
				final SingleOperationItem_PropertyType xmlBeanProp= new SingleOperationItem_PropertyType(); 
				xmlBeanProp.setUuidref(viewBean.getOperation().getUuid().toString());
				xmlBean.setOperation(xmlBeanProp);
			}
		}			
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem item, boolean loadDetails) {
		super.addAdditionalProperties(item, loadDetails);
		
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
		
		if (crsItem instanceof SingleCoordinateReferenceSystemItem) {
			SingleCoordinateReferenceSystemItem<?> derivedCrs = (SingleCoordinateReferenceSystemItem<?>)crsItem;
			if (derivedCrs.getBaseCrs() != null) {
				this.setBaseCrs(new CoordinateReferenceSystemItemViewBean(derivedCrs.getBaseCrs()));
			}
			if (derivedCrs.getOperation() != null) {
				this.setOperation(new SingleOperationItemViewBean(derivedCrs.getOperation()));
			}
		}
		
//		this.setAreaName(crsItem.getDomainOfValidity().getName());
//		this.setDomainOfValidity(new AreaItemViewBean(crsItem.getDomainOfValidity()));
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

	public CoordinateReferenceSystemItemViewBean getBaseCrs() {
		return baseCrs;
	}

	public void setBaseCrs(CoordinateReferenceSystemItemViewBean baseCrs) {
		this.baseCrs = baseCrs;
	}

	public SingleOperationItemViewBean getOperation() {
		return operation;
	}

	public void setOperation(SingleOperationItemViewBean operation) {
		this.operation = operation;
	}
}
