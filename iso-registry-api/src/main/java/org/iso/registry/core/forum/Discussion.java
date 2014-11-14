package org.iso.registry.core.forum;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.FIELD)
@Audited @Entity
public abstract class Discussion extends de.geoinfoffm.registry.core.Entity
{
	@OneToMany(mappedBy = "discussion")
	private List<Post> posts;
	
	public Discussion() { }

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public void addPost(Post post) {
		if (this.posts == null) {
			this.posts = new ArrayList<Post>();
		}
		
		this.posts.add(post);
	}
}
