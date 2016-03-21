package org.iso.registry.api.registry.registers.gcp.datum;

import org.iso.registry.api.GcoConverter;
import org.iso.registry.api.PrimeMeridianItem_Type;
import org.iso.registry.api.UnitOfMeasureItem_PropertyType;
import org.iso.registry.api.registry.registers.gcp.IdentifiedItemViewBean;
import org.iso.registry.api.registry.registers.gcp.UnitOfMeasureItemViewBean;
import org.iso.registry.core.model.datum.PrimeMeridianItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

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
	public void setXmlValues(RE_RegisterItem_Type result) {
		super.setXmlValues(result);
		if (result instanceof PrimeMeridianItem_Type) {
			final PrimeMeridianItem_Type xmlBean = (PrimeMeridianItem_Type)result;
			final PrimeMeridianItemViewBean viewBean = this;
			xmlBean.setGreenwichLongitude(GcoConverter.convertToGcoDouble(viewBean.getGreenwichLongitude()));
			if (viewBean.getGreenwichLongitudeUom() != null && viewBean.getGreenwichLongitudeUom().getUuid() != null) {
				final UnitOfMeasureItem_PropertyType xmlBeanProp= new UnitOfMeasureItem_PropertyType(); 
				xmlBeanProp.setUuidref(viewBean.getGreenwichLongitudeUom().getUuid().toString());
				xmlBean.setGreenwichLongitudeUom(xmlBeanProp);
			}
		}
	}

	@Override
	protected void addAdditionalProperties(RE_RegisterItem registerItem, boolean loadDetails) {
		super.addAdditionalProperties(registerItem, false);
		
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
