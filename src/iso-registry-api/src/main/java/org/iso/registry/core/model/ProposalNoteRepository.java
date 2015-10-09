package org.iso.registry.core.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.Proposal;

@Repository
public interface ProposalNoteRepository extends EntityRepository<ProposalNote>
{
	List<ProposalNote> findByProposal(Proposal proposal);
	
	@Query("SELECT n FROM ProposalNote n WHERE n.proposal.uuid = :uuid")
	List<ProposalNote> findByProposalUuid(@Param("uuid") UUID uuid);
}
