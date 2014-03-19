package org.iso.registry.client.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

public abstract class AbstractController implements ApplicationEventPublisherAware
{
	private ApplicationEventPublisher eventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	protected ApplicationEventPublisher eventPublisher() {
		return this.eventPublisher;
	}
}
