package org.iso.registry.api.forum;

import java.util.List;

import org.iso.registry.core.forum.Post;
import org.iso.registry.core.forum.ProposalDiscussion;

import de.geoinfoffm.registry.api.ApplicationService;
import de.geoinfoffm.registry.core.model.RegistryUser;

public interface ProposalDiscussionService extends ApplicationService<ProposalDiscussion>
{
	ProposalDiscussion startDiscussion(ProposalDiscussionDTO proposalDiscussion);

	void invite(ProposalDiscussion proposalDiscussion, String inviteeMailAddresses);
	List<String> invite(ProposalDiscussion proposalDiscussion, List<String> inviteesMailAddresses);
	
	Post post(PostDTO post);

	boolean isManager(ProposalDiscussion discussion, RegistryUser user);
	boolean mayPost(ProposalDiscussion discussion, RegistryUser user);
	boolean mayPost(ProposalDiscussion discussion, String author, String token);
}
