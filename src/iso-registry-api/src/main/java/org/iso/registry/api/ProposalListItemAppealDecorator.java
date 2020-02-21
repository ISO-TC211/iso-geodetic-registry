package org.iso.registry.api;

import de.geoinfoffm.registry.api.ProposalListItem;
import de.geoinfoffm.registry.api.ProposalListItemDecorator;
import de.geoinfoffm.registry.core.model.Appeal;

public class ProposalListItemAppealDecorator extends ProposalListItemDecorator
{
	private final ProposalListItem decoratee;
	private final Appeal appeal;

	public ProposalListItemAppealDecorator(ProposalListItem decoratee, Appeal appeal) {
		super(decoratee);

		this.decoratee = decoratee;
		this.appeal = appeal;
	}

	public ProposalListItem getAppealedProposal() {
		return decoratee;
	}

	public String getAppealDisposition() {
		return (appeal.getDisposition() != null) ? appeal.getDisposition().toString() : "UNKNOWN";
	}
}
