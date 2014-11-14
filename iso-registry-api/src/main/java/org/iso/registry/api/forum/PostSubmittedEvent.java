package org.iso.registry.api.forum;

import org.iso.registry.core.forum.Post;

import de.geoinfoffm.registry.core.EntityRelatedEvent;

public class PostSubmittedEvent extends EntityRelatedEvent<Post>
{

	public PostSubmittedEvent(Post entity) {
		super(entity);
	}

}
