package org.iso.registry.client;

import javax.persistence.ManyToOne;

import org.iso.registry.core.model.UnitOfMeasureItem;
import org.iso.registry.core.model.datum.DatumItem;
import org.iso.registry.core.model.datum.PrimeMeridianItem;

import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class PrimeMeridianItemViewBean extends IdentifiedItemViewBean
{
	private Double greenwichLongitude;
	private UnitOfMeasureItemViewBean greenwichLongitudeUom;

	public PrimeMeridianItemViewBean(RE_RegisterItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemViewBean(RE_RegisterItem item, boolean loadDetails) {
		super(item, loadDetails);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemViewBean(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemViewBean(SimpleProposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemViewBean(Appeal appeal) {
		super(appeal);
		// TODO Auto-generated constructor stub
	}

	public PrimeMeridianItemViewBean(Supersession supersession) {
		super(supersession);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem) {
		super.addAdditionalProperties(registerItem);
		
		if (!(registerItem instanceof PrimeMeridianItem)) {
			return;
		}
		
		PrimeMeridianItem item = (PrimeMeridianItem)registerItem;

		this.setGreenwichLongitude(item.getGreenwichLongitude());
		if (item.getGreenwichLongitudeUom() != null) {
			this.setGreenwichLongitudeUom(new UnitOfMeasureItemViewBean(item.getGreenwichLongitudeUom()));
		}
	}

	/**
	 * @return the greenwichLongitude
	 */
	public Double getGreenwichLongitude() {
		return greenwichLongitude;
	}

	/**
	 * @param greenwichLongitude the greenwichLongitude to set
	 */
	public void setGreenwichLongitude(Double greenwichLongitude) {
		this.greenwichLongitude = greenwichLongitude;
	}

	/**
	 * @return the greenwichLongitudeUom
	 */
	public UnitOfMeasureItemViewBean getGreenwichLongitudeUom() {
		return greenwichLongitudeUom;
	}

	/**
	 * @param greenwichLongitudeUom the greenwichLongitudeUom to set
	 */
	public void setGreenwichLongitudeUom(UnitOfMeasureItemViewBean greenwichLongitudeUom) {
		this.greenwichLongitudeUom = greenwichLongitudeUom;
	}

}
