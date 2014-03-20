package org.iso.registry.client;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeanUtils;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;

public class ProposalDtoFactory
{
	private ItemClassRegistry itemClassRegistry;

	private ProposalDtoFactory() {}
	
	public ProposalDtoFactory(ItemClassRegistry registry) {
		this.itemClassRegistry = registry;
	}
	
	public RegisterItemProposalDTO getProposalDto(RE_ItemClass itemClass) {
		return (RegisterItemProposalDTO)BeanUtils.instantiateClass(findConstructor(itemClass));		
	}
	
	public RegisterItemProposalDTO getProposalDto(Proposal proposal) {
		if (proposal instanceof SimpleProposal) {
			return getProposalDto((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			return getProposalDto((Supersession)proposal);
		}
		else if (proposal instanceof ProposalGroup) {
			throw new RuntimeException("Not yet implemented");
		}
		else {
			throw new RuntimeException("Not yet implemented");
		}
	}

	public RegisterItemProposalDTO getProposalDto(SimpleProposal proposal) {
		RE_ItemClass itemClass = proposal.getItem().getItemClass();
		return (RegisterItemProposalDTO)BeanUtils.instantiateClass(findConstructor(itemClass, Proposal.class), proposal);		
	}
	
	public RegisterItemProposalDTO getProposalDto(Supersession supersession) {
		return new RegisterItemProposalDTO(supersession);
	}

	private Constructor<?> findConstructor(RE_ItemClass itemClass) {
		return findConstructor(itemClass, null);
	}

	private Constructor<?> findConstructor(RE_ItemClass itemClass, Class<?> argumentType) {
		ItemClassConfiguration config = itemClassRegistry.getConfiguration(itemClass.getName());
		
		Constructor<RegisterItemProposalDTO> defaultConstructor;
		try {
			if (argumentType == null || Void.class.equals(argumentType)) {
				defaultConstructor = RegisterItemProposalDTO.class.getConstructor();
			}
			else {
				defaultConstructor = RegisterItemProposalDTO.class.getConstructor(argumentType);
			}
		}
		catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		if (config == null) {
			return defaultConstructor;
		}

		String proposalDtoClassName = config.getDtoClass();
		Class<?> proposalDtoClass;		
		try {
			proposalDtoClass = this.getClass().getClassLoader().loadClass(proposalDtoClassName);
			if (!RegisterItemProposalDTO.class.isAssignableFrom(proposalDtoClass)) {
				throw new RuntimeException(String.format("The configured view bean class %s is not derived from RegisterItemProposalDTO", proposalDtoClassName));
			}
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		Constructor<?> ctor;
		try {
			if (argumentType == null || argumentType.equals(Void.class)) {
				ctor = proposalDtoClass.getDeclaredConstructor();
			}
			else {
				ctor = proposalDtoClass.getConstructor(argumentType);
			}
		}
		catch (NoSuchMethodException ex) {
			throw new RuntimeException(String.format("The configured view bean class is missing the constructor %s(%s)", proposalDtoClass.getCanonicalName(), argumentType.getCanonicalName()));
		}
		
		return ctor;
	}
}
