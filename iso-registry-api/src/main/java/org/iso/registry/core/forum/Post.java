package org.iso.registry.core.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;

@Access(AccessType.FIELD)
@Audited @Entity
public class Post extends de.geoinfoffm.registry.core.Entity
{
	@Email
	private String author;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timePosted;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeEdited;
	
	@ManyToOne
	@JoinColumn(name = "discussion_id")
	private Discussion discussion;
	
	@ManyToOne
	private Post parentPost;
	
	@OneToMany(mappedBy = "parentPost")
	private List<Post> replies;
	
	private String subject;
	
	private String text;
	
	protected Post() { }
	
	public Post(String author, Date timePosted, Discussion discussion, String subject, String text) {
		this.author = author;
		this.timePosted = timePosted;
		this.discussion = discussion;
		this.subject = subject;
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getTimePosted() {
		return timePosted;
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

	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

	public Post getParentPost() {
		return parentPost;
	}

	public void setParentPost(Post parentPost) {
		this.parentPost = parentPost;
	}

	public List<Post> getReplies() {
		return replies;
	}

	public void setReplies(List<Post> replies) {
		this.replies = replies;
	}
	
	public void addReply(Post reply) {
		if (this.replies == null) {
			this.replies = new ArrayList<Post>();
		}
		
		this.replies.add(reply);
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
