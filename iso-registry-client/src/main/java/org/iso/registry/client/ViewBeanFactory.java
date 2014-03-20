package org.iso.registry.client;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

public class ViewBeanFactory
{
	@Autowired
	private ItemClassRegistry itemClassRegistry;

	public ViewBeanFactory() {
	}
	
	private Constructor<?> findConstructor(RE_ItemClass itemClass, Class<?> argumentType) {
		ItemClassConfiguration config = itemClassRegistry.getConfiguration(itemClass.getName());
		
		Constructor<RegisterItemViewBean> defaultConstructor;
		try {
			defaultConstructor = RegisterItemViewBean.class.getConstructor(argumentType);
		}
		catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		if (config == null) {
			return defaultConstructor;
		}

		String viewBeanClassName = config.getViewBeanClass();
		Class<?> viewBeanClass;		
		try {
			viewBeanClass = this.getClass().getClassLoader().loadClass(viewBeanClassName);
			if (!RegisterItemViewBean.class.isAssignableFrom(viewBeanClass)) {
				throw new RuntimeException(String.format("The configured view bean class %s is not derived from RegisterItemViewBean", viewBeanClassName));
			}
		}
		catch (ClassNotFoundException e) {
			return defaultConstructor;
		}

		Constructor<?> ctor;
		try {
			ctor = viewBeanClass.getConstructor(argumentType);
		}
		catch (NoSuchMethodException ex) {
			throw new RuntimeException(String.format("The configured view bean class is missing the constructor %s(%s)", viewBeanClass.getCanonicalName(), argumentType.getCanonicalName()));
		}
		
		return ctor;
	}
	
	public RegisterItemViewBean getViewBean(RE_RegisterItem item) {
		RE_ItemClass itemClass = item.getItemClass();
		return (RegisterItemViewBean)BeanUtils.instantiateClass(findConstructor(itemClass, RE_RegisterItem.class), item);
	}
	
	public RegisterItemViewBean getViewBean(Proposal proposal) {
		if (proposal instanceof SimpleProposal) {
			return getViewBean((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			return getViewBean((Supersession)proposal);
		}
		else if (proposal instanceof ProposalGroup) {
			throw new RuntimeException("Not yet implemented");
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
	}
	
	public RegisterItemViewBean getViewBean(SimpleProposal proposal) {
		RE_ItemClass itemClass = proposal.getItem().getItemClass();
		return (RegisterItemViewBean)BeanUtils.instantiateClass(findConstructor(itemClass, Proposal.class), proposal);		
	}
	
	public RegisterItemViewBean getViewBean(Supersession supersession) {
		return new RegisterItemViewBean(supersession);
	}

}
