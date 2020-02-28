package org.iso.registry.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iso.registry.core.model.ProposalNote;
import org.iso.registry.core.model.ProposalNoteRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.geoinfoffm.registry.api.ProposalListItem;
import de.geoinfoffm.registry.api.ProposalListItemDecorator;

public class ProposalListItemNotesDecorator extends ProposalListItemDecorator
{
	private final ProposalNoteRepository noteRepository;

	public ProposalListItemNotesDecorator(ProposalListItem decoratee, ProposalNoteRepository noteRepository) {
		super(decoratee);

		this.noteRepository = noteRepository;
	}

	@JsonIgnore
	public List<ProposalNote> getProposalNotes() {
		return noteRepository.findByProposal(this.getProposal());
	}

	@Override
	public Map<String, Object> getAdditionalData() {
		Map<String, Object> result = super.getAdditionalData();

		List<ProposalNoteViewBean> notes = new ArrayList<>();
		for (ProposalNote note : this.getProposalNotes()) {
			notes.add(new ProposalNoteViewBean(note));
		}
		result.put("notes", notes);

		return result;
	}

	@JsonProperty
	public boolean hasNotes() {
		return !getProposalNotes().isEmpty();
	}
}
