package org.iso.registry.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.iso.registry.client.controller.registry.PostViewBean;
import org.springframework.util.Assert;

import de.geoinfoffm.registry.core.forum.Post;
import de.geoinfoffm.registry.core.forum.ProposalDiscussion;
import de.geoinfoffm.registry.core.forum.ProposalDiscussion.DiscussionType;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.SimpleProposal;

public class ProposalDiscussionViewBean
{
	private UUID discussionUuid;
	private UUID proposalUuid;
	private String proposalType;
	private String proposalStatus;
	private String proposalTitle;
	private DiscussionType discussionType;
	private UUID ownerUuid;
	private Map<String, String> invitees = new HashMap<>();
	private List<UUID> postsUuids = new ArrayList<UUID>();
	private List<PostViewBean> posts = new ArrayList<PostViewBean>();
	
	public ProposalDiscussionViewBean(ProposalDiscussion discussion) {
		Assert.notNull(discussion);
		
		this.discussionUuid = discussion.getUuid();	
		this.proposalUuid = discussion.getDiscussedProposal().getUuid();
		this.proposalType = discussion.getDiscussedProposal().getClass().getSimpleName();
		this.proposalStatus = discussion.getDiscussedProposal().getStatus();
		
		if (discussion.getDiscussedProposal() instanceof SimpleProposal) {
			SimpleProposal sp = (SimpleProposal)discussion.getDiscussedProposal();
			if (sp.getItem() != null) {
				this.proposalTitle = sp.getItem().getName();
			}
		}
		else if (discussion.getDiscussedProposal() instanceof ProposalGroup) {
			ProposalGroup pg = (ProposalGroup)discussion.getDiscussedProposal();
			this.proposalTitle = pg.getTitle();
		}
		
		this.discussionType = discussion.getDiscussionType();
		this.ownerUuid = discussion.getOwner().getUuid();
		if (discussion.getInvitees() != null) {
			invitees.putAll(discussion.getInvitees());
		}
		if (discussion.getPosts() != null) {
			for (Post post : discussion.getPosts()) {
				if (post.getParentPost() != null) continue;
				this.postsUuids.add(post.getUuid());
				this.posts.add(new PostViewBean(post));
				for (Post reply : traverse(post)) {
					this.postsUuids.add(reply.getUuid());
					this.posts.add(new PostViewBean(reply));
				}
			}			
		}
	}
	
	private List<Post> traverse(Post post) {
		List<Post> result = new ArrayList<Post>();
		for (Post reply : post.getReplies()) {
			this.postsUuids.add(reply.getUuid());
			this.posts.add(new PostViewBean(reply));
			result.addAll(traverse(reply));
		}
		
		return result;
	}

	public UUID getDiscussionUuid() {
		return discussionUuid;
	}

	public void setDiscussionUuid(UUID discussionUuid) {
		this.discussionUuid = discussionUuid;
	}

	public UUID getProposalUuid() {
		return proposalUuid;
	}

	public void setProposalUuid(UUID proposalUuid) {
		this.proposalUuid = proposalUuid;
	}

	public String getProposalType() {
		return proposalType;
	}

	public void setProposalType(String proposalType) {
		this.proposalType = proposalType;
	}

	public String getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(String proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public String getProposalTitle() {
		return proposalTitle;
	}

	public void setProposalTitle(String proposalTitle) {
		this.proposalTitle = proposalTitle;
	}

	public DiscussionType getDiscussionType() {
		return discussionType;
	}

	public void setDiscussionType(DiscussionType discussionType) {
		this.discussionType = discussionType;
	}

	public UUID getOwnerUuid() {
		return ownerUuid;
	}

	public void setOwnerUuid(UUID ownerUuid) {
		this.ownerUuid = ownerUuid;
	}

	public Map<String, String> getInvitees() {
//		return StringUtils.join(this.invitees, "\r\n");
		return this.invitees;
	}

	public void setInvitees(Map<String, String> invitees) {
		this.invitees = invitees;
	}

	public List<UUID> getPostsUuids() {
		return postsUuids;
	}

	public void setPostsUuids(List<UUID> postsUuids) {
		this.postsUuids = postsUuids;
	}

	public List<PostViewBean> getPosts() {
		return posts;
	}

	public void setPosts(List<PostViewBean> posts) {
		this.posts = posts;
	}
}
