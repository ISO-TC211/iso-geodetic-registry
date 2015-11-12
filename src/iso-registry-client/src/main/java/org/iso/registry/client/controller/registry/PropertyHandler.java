package org.iso.registry.client.controller.registry;

import org.springframework.ui.Model;

import de.geoinfoffm.registry.core.Entity;

public interface PropertyHandler
{
	public void injectData(Object value, final Model model) throws Exception;
	
	public String viewName();
}
