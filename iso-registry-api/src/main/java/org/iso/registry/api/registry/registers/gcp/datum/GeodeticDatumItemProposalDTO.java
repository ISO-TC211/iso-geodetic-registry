package org.iso.registry.api.registry.registers.gcp.datum;

import org.iso.registry.core.model.datum.GeodeticDatumItem;

public class GeodeticDatumItemProposalDTO extends DatumItemProposalDTO
{
	/**
	 * Association to the ellipsoid used by this geodetic datum. 
	 */
	private EllipsoidItemProposalDTO ellipsoid;
	/**
	 * Association to the prime meridian used by this geodetic datum. 
	 */
	private PrimeMeridianItemProposalDTO primeMeridian;

	public GeodeticDatumItemProposalDTO() { }

	public GeodeticDatumItemProposalDTO(GeodeticDatumItem item) {
		super(item);
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
