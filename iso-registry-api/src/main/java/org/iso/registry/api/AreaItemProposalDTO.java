package org.iso.registry.api;

import javax.persistence.EntityManager;

import org.iso.registry.core.model.AreaItem;
import org.iso.registry.core.model.CoordinateReferenceSystemItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class AreaItemProposalDTO extends RegisterItemProposalDTO
{
	private Integer code;

	public AreaItemProposalDTO() {
		super();
	}

	public AreaItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public AreaItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public AreaItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof AreaItem) {
			((AreaItem)item).setCode(this.code);
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem item) {
		super.loadAdditionalValues(item);
		
		if (item instanceof CoordinateReferenceSystemItem) {
			this.code = ((CoordinateReferenceSystemItem)item).getCode();
		}
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
