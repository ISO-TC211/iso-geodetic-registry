package org.iso.registry.core.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;

@Access(AccessType.FIELD)
@Audited @Entity
public class ProposalNote extends de.geoinfoffm.registry.core.Entity
{
	@ManyToOne
	private Proposal proposal;
	
	@ManyToOne
	private RegistryUser author;
	
	@Column(columnDefinition = "text")
	private String note;
	
	protected ProposalNote() {
	}
	
	public ProposalNote(Proposal proposal, RegistryUser author, String note) {
		this.proposal = proposal;
		this.author = author;
		this.note = note;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public RegistryUser getAuthor() {
		return author;
	}

	public void setAuthor(RegistryUser author) {
		this.author = author;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
