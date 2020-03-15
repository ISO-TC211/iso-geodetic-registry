package org.iso.registry.api.registry.registers.gcp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.iso.registry.core.model.iso19115.extent.EX_GeographicBoundingBox;
import org.iso.registry.core.model.iso19115.extent.ExtentItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;

public class ExtentItemProposalDTO extends RegisterItemProposalDTO
{
	private String description;
	private List<EX_GeographicBoundingBox> geographicBoundingBoxes;

	public ExtentItemProposalDTO() {
		super("Extent");
	}

	// TODO Activate after extending isogcp-registry.xsd
//	public ExtentItemProposalDTO(ExtentItemProposal_Type itemDetails) {
//		super(itemDetails);
//	}

	public ExtentItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
	}

	public ExtentItemProposalDTO(ExtentItem item) {
		super(item);
	}

	public ExtentItemProposalDTO(Proposal proposal) {
		super(proposal);
	}

	public ExtentItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
	}

	public ExtentItemProposalDTO(String itemClassName) {
		super(itemClassName);
	}

	@Override
	public void setAdditionalValues(RE_RegisterItem registerItem, EntityManager entityManager) {
		super.setAdditionalValues(registerItem, entityManager);

		if (registerItem instanceof ExtentItem) {
			ExtentItem item = (ExtentItem)registerItem;

			ExtentDTO dto = new ExtentDTO();
			dto.setDescription(this.description);
			for (EX_GeographicBoundingBox box : this.getGeographicBoundingBoxes()) {
				dto.getGeographicBoundingBoxes().add(box);
			}

			if (item.getExtent() == null) {
				item.setExtent(dto.getNewExtent());
			}
			else {
				dto.copyValues(item.getExtent());
			}
		}
	}

	@Override
	public void loadAdditionalValues(RE_RegisterItem registerItem) {
		super.loadAdditionalValues(registerItem);

		if (registerItem instanceof ExtentItem) {
			ExtentItem item = (ExtentItem)registerItem;

			this.description = item.getExtent().getDescription();

			ExtentDTO dto = new ExtentDTO(item.getExtent());
			this.getGeographicBoundingBoxes().addAll(dto.getGeographicBoundingBoxes());
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<EX_GeographicBoundingBox> getGeographicBoundingBoxes() {
		if (geographicBoundingBoxes == null) {
			geographicBoundingBoxes = new ArrayList<>();
		}
		return geographicBoundingBoxes;
	}

	public void setGeographicBoundingBoxes(List<EX_GeographicBoundingBox> geographicBoundingBoxes) {
		this.geographicBoundingBoxes = geographicBoundingBoxes;
	}

}
