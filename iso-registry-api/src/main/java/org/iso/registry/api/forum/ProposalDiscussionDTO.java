package org.iso.registry.api.forum;

import java.util.List;
import java.util.UUID;

import org.iso.registry.core.forum.ProposalDiscussion.DiscussionType;
import org.springframework.util.StringUtils;

import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.Proposal;

public class ProposalDiscussionDTO
{
	private UUID ownerUuid;
	private UUID discussedProposalUuid;
	private DiscussionType discussionType;
	private String invitees; // List of e-mail addresses separated comma or \r\n
	private String message;

	protected ProposalDiscussionDTO() { }
	
	public ProposalDiscussionDTO(Actor owner, Proposal discussedProposal, DiscussionType discussionType, List<String> invitees, String message) {
		if (owner == null || discussedProposal == null) {
			throw new NullPointerException(String.format("owner or discussedProposal null [owner=%s; discussedProposal=%s]", owner, discussedProposal));
		}
		this.ownerUuid = owner.getUuid();
		this.discussedProposalUuid = discussedProposal.getUuid();
		this.discussionType = discussionType;
		this.invitees = StringUtils.arrayToCommaDelimitedString(invitees.toArray());
		this.message = message;
	}
	
	public ProposalDiscussionDTO(UUID ownerUuid, UUID discussedProposalUuid, DiscussionType discussionType, List<String> invitees, String message) {
		this.ownerUuid = ownerUuid;
		this.discussedProposalUuid = discussedProposalUuid;
		this.discussionType = discussionType;
		this.invitees = StringUtils.arrayToCommaDelimitedString(invitees.toArray());
		this.message = message;
	}

	public UUID getOwnerUuid() {
		return ownerUuid;
	}

	public void setOwnerUuid(UUID ownerUuid) {
		this.ownerUuid = ownerUuid;
	}

	public UUID getDiscussedProposalUuid() {
		return discussedProposalUuid;
	}

	public void setDiscussedProposalUuid(UUID discussedProposalUuid) {
		this.discussedProposalUuid = discussedProposalUuid;
	}

	public DiscussionType getDiscussionType() {
		return discussionType;
	}

	public void setDiscussionType(DiscussionType discussionType) {
		this.discussionType = discussionType;
	}

	public String getInvitees() {
		return invitees;
	}

	public void setInvitees(String invitees) {
		this.invitees = invitees;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
