package org.iso.registry.api.registry.registers.gcp.operation;

import java.util.ArrayList;
import java.util.List;

import org.iso.registry.core.model.operation.ConcatenatedOperationItem;
import org.isotc211.iso19135.RE_RegisterItem_Type;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.soap.Addition_Type;

public class ConcatenatedOperationItemProposalDTO extends CoordinateOperationItemProposalDTO
{
	private List<CoordinateOperationItemProposalDTO> coordOperation;

	public ConcatenatedOperationItemProposalDTO() {
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(String itemClassName) {
		super(itemClassName);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(ConcatenatedOperationItem item) {
		super(item);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(Addition_Type proposal, RE_SubmittingOrganization sponsor) {
		super(proposal, sponsor);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(Proposal proposal) {
		super(proposal);
		// TODO Auto-generated constructor stub
	}

	public ConcatenatedOperationItemProposalDTO(RE_RegisterItem_Type item, RE_SubmittingOrganization sponsor) {
		super(item, sponsor);
		// TODO Auto-generated constructor stub
	}

	public List<CoordinateOperationItemProposalDTO> getCoordOperation() {
		return coordOperation;
	}

	public void setCoordOperation(List<CoordinateOperationItemProposalDTO> coordOperation) {
		this.coordOperation = coordOperation;
	}

	public void addCoordOperation(CoordinateOperationItemProposalDTO coordOperation) {
		if (this.coordOperation != null) {
			this.coordOperation = new ArrayList<>();
		}
		this.coordOperation.add(coordOperation);
	}

}
