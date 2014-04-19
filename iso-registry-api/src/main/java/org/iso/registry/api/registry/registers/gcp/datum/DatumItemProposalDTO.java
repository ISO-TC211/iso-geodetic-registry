package org.iso.registry.api.registry.registers.gcp.datum;

import java.util.Date;

import org.iso.registry.api.IdentifiedItemProposalDTO;
import org.iso.registry.api.registry.registers.gcp.crs.AreaItemProposalDTO;
import org.iso.registry.core.model.datum.DatumItem;

public class DatumItemProposalDTO extends IdentifiedItemProposalDTO
{
	public enum DatumType {
		ENGINEERING,
		GEODETIC,
		IMAGE,
		VERTICAL
	}
	
	private DatumType type;
	
	private String anchorDefinition;
	private AreaItemProposalDTO domainOfValidity;
	private Date realizationEpoch;
	
	private EllipsoidItemProposalDTO ellipsoid;
	private PrimeMeridianItemProposalDTO primeMeridian;
	
	public DatumItemProposalDTO() { }
	
	public DatumItemProposalDTO(DatumItem item) {
		super(item);
	}
	
	public DatumType getType() {
		return this.type;
	}

	public void setType(DatumType type) {
		this.type = type;
	}

	public String getAnchorDefinition() {
		return anchorDefinition;
	}

	public void setAnchorDefinition(String anchorDefinition) {
		this.anchorDefinition = anchorDefinition;
	}

	public AreaItemProposalDTO getDomainOfValidity() {
		return domainOfValidity;
	}

	public void setDomainOfValidity(AreaItemProposalDTO domainOfValidity) {
		this.domainOfValidity = domainOfValidity;
	}

	public Date getRealizationEpoch() {
		return realizationEpoch;
	}

	public void setRealizationEpoch(Date realizationEpoch) {
		this.realizationEpoch = realizationEpoch;
	}

	public EllipsoidItemProposalDTO getEllipsoid() {
		return ellipsoid;
	}

	public void setEllipsoid(EllipsoidItemProposalDTO ellipsoid) {
		this.ellipsoid = ellipsoid;
	}

	public PrimeMeridianItemProposalDTO getPrimeMeridian() {
		return primeMeridian;
	}

	public void setPrimeMeridian(PrimeMeridianItemProposalDTO primeMeridian) {
		this.primeMeridian = primeMeridian;
	}

}
