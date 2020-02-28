package org.iso.registry.api;

import java.util.UUID;

import org.iso.registry.core.model.ProposalNote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProposalNoteViewBean
{
	private ProposalNote note;

	public ProposalNoteViewBean(ProposalNote note) {
		this.note = note;
	}

	@JsonProperty
	public UUID getProposalUuid() {
		return note.getProposal().getUuid();
	}

	@JsonProperty
	public String getProposalTitle() {
		return note.getProposal().getTitle();
	}

	@JsonProperty
	public String getNote() {
		return note.getNote();
	}

	@JsonProperty
	public String getAuthorEmail() {
		return note.getAuthor().getEmailAddress();
	}

	@JsonProperty
	public String getAuthorName() {
		return note.getAuthor().getName();
	}
}
