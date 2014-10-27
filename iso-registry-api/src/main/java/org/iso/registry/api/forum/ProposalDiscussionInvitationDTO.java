package org.iso.registry.api.forum;

import java.util.UUID;

public class ProposalDiscussionInvitationDTO
{
	private UUID discussionUuid;
	private String invitees;
	
	protected ProposalDiscussionInvitationDTO() { }
	
	public ProposalDiscussionInvitationDTO(UUID discussionUuid, String invitees) {
		this.discussionUuid = discussionUuid;
		this.setInvitees(invitees);
	}

	public UUID getDiscussionUuid() {
		return discussionUuid;
	}

	public void setDiscussionUuid(UUID discussionUuid) {
		this.discussionUuid = discussionUuid;
	}

	public String getInvitees() {
		return invitees;
	}

	public void setInvitees(String invitees) {
		this.invitees = invitees;
	}
}
