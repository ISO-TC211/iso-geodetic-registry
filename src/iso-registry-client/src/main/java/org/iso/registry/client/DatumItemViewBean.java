package org.iso.registry.client;

import java.util.Date;

import org.iso.registry.api.registry.registers.gcp.ExtentDTO;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.GeodeticDatumItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class DatumItemViewBean extends IdentifiedItemViewBean
{
	private String anchorDefinition;
	private ExtentDTO domainOfValidity;
	private Date realizationEpoch;
	private String scope;
	private Date coordinateReferenceEpoch;
	private EllipsoidItemViewBean ellipsoid;
	private PrimeMeridianItemViewBean primeMeridian;
	
	public DatumItemViewBean(Appeal appeal) {
		super(appeal);
	}

	public DatumItemViewBean(Proposal proposal) {
		super(proposal);
	}

	public DatumItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
	}

	public DatumItemViewBean(RE_RegisterItem item) {
		super(item);
	}

	public DatumItemViewBean(SimpleProposal proposal) {
		super(proposal);
	}

	public DatumItemViewBean(Supersession supersession) {
		super(supersession);
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, loadDetails);
		
		if (!(registerItem instanceof DatumItem)) {
			return;
		}
		
		DatumItem item = (DatumItem)registerItem;

		this.setAnchorDefinition(item.getAnchorDefinition());
		if (item.getDomainOfValidity() != null) {
			this.setDomainOfValidity(new ExtentDTO(item.getDomainOfValidity()));
		}
		this.setRealizationEpoch(item.getRealizationEpoch());
		this.setScope(item.getScope());
		this.setCoordinateReferenceEpoch(item.getCoordinateReferenceEpoch());
		
		if (registerItem instanceof GeodeticDatumItem) {
			GeodeticDatumItem geodeticDatum = (GeodeticDatumItem)registerItem;
			if (geodeticDatum.getEllipsoid() != null) {
				this.setEllipsoid(new EllipsoidItemViewBean(geodeticDatum.getEllipsoid()));
			}
			if (geodeticDatum.getPrimeMeridian() != null) {
				this.setPrimeMeridian(new PrimeMeridianItemViewBean(geodeticDatum.getPrimeMeridian()));
			}
		}
	}

	public String getAnchorDefinition() {
		return anchorDefinition;
	}

	public void setAnchorDefinition(String anchorDefinition) {
		this.anchorDefinition = anchorDefinition;
	}

	public ExtentDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(ExtentDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

	public Date getRealizationEpoch() {
		return realizationEpoch;
	}

	public void setRealizationEpoch(Date realizationEpoch) {
		this.realizationEpoch = realizationEpoch;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Date getCoordinateReferenceEpoch() {
		return coordinateReferenceEpoch;
	}

	public void setCoordinateReferenceEpoch(Date coordinateReferenceEpoch) {
		this.coordinateReferenceEpoch = coordinateReferenceEpoch;
	}

	public EllipsoidItemViewBean getEllipsoid() {
		return ellipsoid;
	}

	public void setEllipsoid(EllipsoidItemViewBean ellipsoid) {
		this.ellipsoid = ellipsoid;
	}

	public PrimeMeridianItemViewBean getPrimeMeridian() {
		return primeMeridian;
	}

	public void setPrimeMeridian(PrimeMeridianItemViewBean primeMeridian) {
		this.primeMeridian = primeMeridian;
	}

}
