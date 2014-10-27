package org.iso.registry.core.forum;

import org.iso.registry.core.forum.ProposalDiscussion.DiscussionType;
import org.springframework.stereotype.Repository;

import de.geoinfoffm.registry.core.EntityRepository;
import de.geoinfoffm.registry.core.model.Proposal;

@Repository
public interface ProposalDiscussionRepository extends EntityRepository<ProposalDiscussion>
{
	ProposalDiscussion findByDiscussedProposalAndDiscussionType(Proposal proposal, DiscussionType type);
}
