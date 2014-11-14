package org.iso.registry.api.forum;

import java.util.UUID;

public class PostDTO
{
	private String author;
	private String token;
	private UUID discussionUuid; // null if new discussion
	private UUID parentPostUuid; // this post is a reply to the parent post; may be null
	private String subject;
	private String text;
	
	protected PostDTO() { }
	
	public PostDTO(UUID discussionUuid, String author, String token, String subject, String text) {
		this.discussionUuid = discussionUuid;
		this.author = author;
		this.token = token;
		this.subject = subject;
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UUID getDiscussionUuid() {
		return discussionUuid;
	}

	public void setDiscussionUuid(UUID discussionUuid) {
		this.discussionUuid = discussionUuid;
	}

	public UUID getParentPostUuid() {
		return parentPostUuid;
	}

	public void setParentPostUuid(UUID parentPostUuid) {
		this.parentPostUuid = parentPostUuid;
	}

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
