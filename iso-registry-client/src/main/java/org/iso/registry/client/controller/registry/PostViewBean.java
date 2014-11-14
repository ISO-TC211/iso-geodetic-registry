package org.iso.registry.client.controller.registry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.iso.registry.core.forum.Post;

public class PostViewBean
{
	private UUID postUuid;
	private String authorEmailAddress;
	private String authorRole;
	private Date timePosted;
	private Date timeEdited;
	private UUID parentPostUuid;
//	private List<UUID> replies = new ArrayList<UUID>();
	private String subject;
	private String text;
	
	public PostViewBean(Post post) {
		this.postUuid = post.getUuid();
		this.authorEmailAddress = post.getAuthor();
		this.authorRole = "N/A";
		this.timePosted = post.getTimePosted();
		this.timeEdited = post.getTimeEdited();
		if (post.getParentPost() != null) {
			this.parentPostUuid = post.getParentPost().getUuid();
		}
//		if (post.getReplies() != null) {
//			for (Post reply : post.getReplies()) {
//				this.replies.add(reply.getUuid());
//			}
//		}
		this.subject = post.getSubject();
		this.text = post.getText();
	}

	public UUID getPostUuid() {
		return postUuid;
	}

	public void setPostUuid(UUID postUuid) {
		this.postUuid = postUuid;
	}

	public String getAuthorEmailAddress() {
		return authorEmailAddress;
	}

	public void setAuthorEmailAddress(String authorEmailAddress) {
		this.authorEmailAddress = authorEmailAddress;
	}

	public String getAuthorRole() {
		return authorRole;
	}

	public void setAuthorRole(String authorRole) {
		this.authorRole = authorRole;
	}

	public Date getTimePosted() {
		return timePosted;
	}
	
	public String getTimePostedGerman() {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
		
		return df.format(timePosted);
	}
	
	public void setTimePosted(Date timePosted) {
		this.timePosted = timePosted;
	}

	public Date getTimeEdited() {
		return timeEdited;
	}

	public void setTimeEdited(Date timeEdited) {
		this.timeEdited = timeEdited;
	}

	public UUID getParentPostUuid() {
		return parentPostUuid;
	}

	public void setParentPostUuid(UUID parentPostUuid) {
		this.parentPostUuid = parentPostUuid;
	}

//	public List<UUID> getReplies() {
//		return replies;
//	}
//
//	public void setReplies(List<UUID> replies) {
//		this.replies = replies;
//	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
