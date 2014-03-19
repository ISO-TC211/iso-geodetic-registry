package org.iso.registry.api;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.iso.registry.core.model.CoordinateReferenceSystemItem;
import org.iso.registry.core.model.CoordinateSystemType;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class CoordinateReferenceSystemItemProposalDTO extends RegisterItemProposalDTO
{
	private Integer code;
	private CoordinateSystemType type;
	private String scope;
	private UUID areaUuid;

	public CoordinateReferenceSystemItemProposalDTO() {
		super();
	}

	public CoordinateReferenceSystemItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public CoordinateReferenceSystemItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public CoordinateReferenceSystemItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem item, EntityManager entityManager) {
		super.setAdditionalValues(item, entityManager);
		
		if (item instanceof CoordinateReferenceSystemItem) {
			((CoordinateReferenceSystemItem)item).setCode(this.code);
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

	public UUID getAreaUuid() {
		return areaUuid;
	}

	public void setAreaUuid(UUID areaUuid) {
		this.areaUuid = areaUuid;
	}
}
