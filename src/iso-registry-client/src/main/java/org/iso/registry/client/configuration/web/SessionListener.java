package org.iso.registry.client.configuration.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session listener
 * 
 * @author Florian Esser
 *
 */
public class SessionListener implements HttpSessionListener
{
	private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		logger.info("==== Session created ====");
		event.getSession().setMaxInactiveInterval(5 * 60 * 60); /* in seconds */
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		logger.info("==== Session destroyed ====");
	}
}
