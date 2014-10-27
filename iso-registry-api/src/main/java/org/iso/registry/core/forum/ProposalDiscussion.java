package org.iso.registry.core.forum;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.Proposal;

@Access(AccessType.FIELD)
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "discussedProposal", "discussedStatus" }))
@Audited @Entity
public class ProposalDiscussion extends Discussion
{
	public enum DiscussionType {
		SUBMITTER,
		CONTROLBODY
	}
	
	@ManyToOne
	private Proposal discussedProposal;
	
	@Enumerated(EnumType.STRING)
	private DiscussionType discussionType;
	
	@ManyToOne
	private Actor owner;

	// Maps an invitee's e-mail address to his access secret
	@ElementCollection
	private Map<String, String> invitees;
	
	protected ProposalDiscussion() { }
	
	public ProposalDiscussion(Actor owner, Proposal discussedProposal, DiscussionType discussionType) {
		this.owner = owner;
		this.discussedProposal = discussedProposal;
		this.discussionType = discussionType;
	}

	public Proposal getDiscussedProposal() {
		return discussedProposal;
	}

	public void setDiscussedProposal(Proposal discussedProposal) {
		this.discussedProposal = discussedProposal;
	}

	public Actor getOwner() {
		return owner;
	}

	public void setOwner(Actor owner) {
		this.owner = owner;
	}

	public Map<String, String> getInvitees() {
		return invitees;
	}

	protected void setInvitees(Map<String, String> invitees) {
		this.invitees = invitees;
	}
	
	public String addInvitee(String emailAddress) {
		if (this.invitees == null) {
			this.invitees = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		}
		
		if (this.invitees.containsKey(emailAddress)) {
			return this.invitees.get(emailAddress);
		}
		else {
			String secret = RandomStringUtils.random(10, true, true);
			this.invitees.put(emailAddress, secret);
			
			return secret;
		}
	}
	
	public boolean isInvited(String emailAddress) {
		return this.invitees.containsKey(emailAddress);
	}
	
	public String getInviteeByToken(String token) {
		for (String mail : this.invitees.keySet()) {
			String inviteeSecret = this.invitees.get(mail);
			if (inviteeSecret.equalsIgnoreCase(token)) {
				return mail;
			}
		}
		
		return null;
	}
	
	public String getTokenByInvitee(String invitee) {
		return this.invitees.get(invitee);
	}

	public DiscussionType getDiscussionType() {
		return discussionType;
	}

	public void setDiscussionType(DiscussionType discussionType) {
		this.discussionType = discussionType;
	}
}
