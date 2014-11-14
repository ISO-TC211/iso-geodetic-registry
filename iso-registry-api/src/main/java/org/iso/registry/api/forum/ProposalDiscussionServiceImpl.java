package org.iso.registry.api.forum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iso.registry.core.forum.Post;
import org.iso.registry.core.forum.PostRepository;
import org.iso.registry.core.forum.ProposalDiscussion;
import org.iso.registry.core.forum.ProposalDiscussionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.geoinfoffm.registry.api.AbstractApplicationService;
import de.geoinfoffm.registry.core.model.Actor;
import de.geoinfoffm.registry.core.model.ActorRepository;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ProposalRepository;

@Service
public class ProposalDiscussionServiceImpl extends AbstractApplicationService<ProposalDiscussion, ProposalDiscussionRepository> implements ProposalDiscussionService
{
	@Autowired
	private ActorRepository actorRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private ProposalRepository proposalRepository;
	
	@Autowired
	private RegistrySecurity security; 
	
	@Autowired
	public ProposalDiscussionServiceImpl(ProposalDiscussionRepository repository) {
		super(repository);
	}

	@Override
	public ProposalDiscussion startDiscussion(ProposalDiscussionDTO proposalDiscussion) {
		Assert.notNull(proposalDiscussion);
		Assert.notNull(proposalDiscussion.getOwnerUuid());
		Assert.notNull(proposalDiscussion.getDiscussedProposalUuid());
		
		Actor owner = actorRepository.findOne(proposalDiscussion.getOwnerUuid());
		Proposal discussedProposal = proposalRepository.findOne(proposalDiscussion.getDiscussedProposalUuid());
		
		ProposalDiscussion result = new ProposalDiscussion(owner, discussedProposal, proposalDiscussion.getDiscussionType());
		Set<String> invitees = addInvitees(result, proposalDiscussion.getInvitees());
		
		result = repository().save(result);

		eventPublisher().publishEvent(new ProposalDiscussionInvitationsAddedEvent(result, invitees));

		return result;
	}
	
	private Set<String> addInvitees(ProposalDiscussion discussion, String inviteesList) {
		Set<String> result = new HashSet<String>();
		
		String[] invitees = StringUtils.tokenizeToStringArray(inviteesList, "\r\n,", true, true);
		for (String invitee : invitees) {
			// TODO check validity of mail address
			discussion.addInvitee(invitee);
			result.add(invitee);
		}
		
		return result;
	}
	
	@Override
	public void invite(ProposalDiscussion proposalDiscussion, String inviteeMailAddresses) throws UsernameNotFoundException {
		String[] invitees = StringUtils.tokenizeToStringArray(inviteeMailAddresses, "\r\n,", true, true);
		this.invite(proposalDiscussion, Arrays.asList(invitees));
	}

	@Override
	public List<String> invite(ProposalDiscussion proposalDiscussion, List<String> inviteesMailAddresses) {
		List<String> invitedAddresses = new ArrayList<String>();

		Assert.notNull(proposalDiscussion);
		
		for (String inviteeMailAddress : inviteesMailAddresses) {
			if (!proposalDiscussion.isInvited(inviteeMailAddress)) {
				proposalDiscussion.addInvitee(inviteeMailAddress);
				invitedAddresses.add(inviteeMailAddress);
			}
		}

		repository().save(proposalDiscussion);
		eventPublisher().publishEvent(new ProposalDiscussionInvitationsAddedEvent(proposalDiscussion, inviteesMailAddresses));

		return invitedAddresses;
	}

	@Override
	public Post post(PostDTO post) {
		Assert.notNull(post.getAuthor());
		
		ProposalDiscussion discussion = repository().findOne(post.getDiscussionUuid());
		Assert.notNull(discussion);
		
		Post parentPost = null;
		if (post.getParentPostUuid() != null) {
			parentPost = postRepository.findOne(post.getParentPostUuid());
		}

	
		if (security.isLoggedIn()) {
			Assert.isTrue(this.mayPost(discussion, security.getCurrentUser()), "You are not authorized to post to this dicussion");

			Assert.isTrue(post.getAuthor().equalsIgnoreCase(security.getCurrentUser().getEmailAddress()));
		}
		else {
			String token = discussion.getTokenByInvitee(post.getAuthor());
			Assert.isTrue(post.getToken().equals(token), "Invalid access token");
		}
		
		Date now = Calendar.getInstance().getTime();
		
		Post result = new Post(post.getAuthor(), now, discussion, post.getSubject(), post.getText());
		result.setParentPost(parentPost);
		result = postRepository.save(result);
		
		discussion.addPost(result);
		discussion = repository().save(discussion);
		
		return result;
	}

	@Override
	public boolean isManager(ProposalDiscussion discussion, RegistryUser user) {
		boolean mayManage;
		
		Actor owner = discussion.getOwner();

		if (owner instanceof RegistryUser) {
			mayManage = owner.equals(user);
		}
		else if (owner instanceof Organization) {
			switch (discussion.getDiscussionType()) {
				case SUBMITTER:
					mayManage = security.hasEntityRelatedRoleForAll(RegistrySecurity.SUBMITTER_ROLE_PREFIX, discussion.getDiscussedProposal().getAffectedRegisters());
					break;
				case CONTROLBODY:
					mayManage = security.hasEntityRelatedRoleForAll(RegistrySecurity.CONTROLBODY_ROLE_PREFIX, discussion.getDiscussedProposal().getAffectedRegisters());
					break;	
				default:
					mayManage = false;
			}
		}
		else {
			throw new RuntimeException("Invalid discussion owner type: " + owner.getClass().getCanonicalName());
		}

		return mayManage;
	}
		
	@Override
	public boolean mayPost(ProposalDiscussion discussion, RegistryUser user) {
		return isManager(discussion, user) || discussion.isInvited(user.getEmailAddress());
	}
	
	@Override
	public boolean mayPost(ProposalDiscussion discussion, String author, String token) {
		String tokenAuthor = discussion.getInviteeByToken(token);
		
		return !StringUtils.isEmpty(tokenAuthor) && tokenAuthor.equalsIgnoreCase(author);
	}

}
