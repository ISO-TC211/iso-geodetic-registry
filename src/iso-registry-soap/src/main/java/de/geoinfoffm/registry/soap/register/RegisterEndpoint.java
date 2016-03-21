/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions 
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.
 
 *     * Redistributions in binary form must reproduce the above 
 *     	 copyright notice, this list of conditions and the following 
 *       disclaimer in the documentation and/or other materials 
 *       provided with the distribution.
 
 *     * The names "German Federal Agency for Cartography and Geodesy", 
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE", 
 *       "GDI-DE Registry" and the names of other contributors must not 
 *       be used to endorse or promote products derived from this 
 *       software without specific prior written permission.
 *       
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN 
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.geoinfoffm.registry.soap.register;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.hibernate.SessionFactory;
import org.isotc211.iso19135.RE_RegisterItem_Type;
import org.isotc211.iso19135.RE_Register_Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import de.geoinfoffm.registry.api.EntityNotFoundException;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.RoleService;
import de.geoinfoffm.registry.api.SubregisterDescriptionProposalDTO;
import de.geoinfoffm.registry.api.ViewBeanFactory;
import de.geoinfoffm.registry.api.iso.IsoXmlFactory;
import de.geoinfoffm.registry.api.soap.AddItemClassToRegisterRequest;
import de.geoinfoffm.registry.api.soap.AddItemClassToRegisterResponse;
import de.geoinfoffm.registry.api.soap.CreateRegisterRequest;
import de.geoinfoffm.registry.api.soap.CreateRegisterResponse;
import de.geoinfoffm.registry.api.soap.GetRegisterDescriptionsRequest;
import de.geoinfoffm.registry.api.soap.GetRegisterDescriptionsResponse;
import de.geoinfoffm.registry.api.soap.GetRegisterItemRequest;
import de.geoinfoffm.registry.api.soap.GetRegisterRequest;
import de.geoinfoffm.registry.api.soap.GetRegisterResponse;
import de.geoinfoffm.registry.api.soap.ItemClassDescription;
import de.geoinfoffm.registry.api.soap.ObjectFactory;
import de.geoinfoffm.registry.api.soap.RegisterDescription;
import de.geoinfoffm.registry.core.ParameterizedRunnable;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.soap.AbstractEndpoint;

/**
 * The class RegisterEndpoint.
 *
 * @author Florian Esser
 */
@Endpoint
public class RegisterEndpoint extends AbstractEndpoint
{
	private static final Logger logger = LoggerFactory.getLogger(RegisterEndpoint.class);
	private static final String NAMESPACE_URI = "http://www.geoinfoffm.de/registry/soap";
	
	private static final ObjectFactory objectFactory = new ObjectFactory();
	private static final org.isotc211.iso19135.ObjectFactory iso19135Factory = new org.isotc211.iso19135.ObjectFactory();
	
	@Autowired
	private ViewBeanFactory viewBeanFactory;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalDtoFactory proposalDtoFactory;

	 @Autowired
	 private IsoXmlFactory xmlFactory;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private RegisterRepository registerRepository;
	
	@Autowired
	private RegisterItemService registerItemService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private OrganizationService orgService;
	
	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private RegistrySecurity security;

	public RegisterEndpoint() {
	}

	@Transactional(readOnly = true)
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetRegisterDescriptionsRequest")
	public @ResponsePayload JAXBElement<GetRegisterDescriptionsResponse> handleGetRegistersRequest(@RequestPayload GetRegisterDescriptionsRequest req) throws Exception {
		if (req == null) { 
			throw new IllegalArgumentException("Invalid request");
		}
		
		List<RE_Register> registers = registerRepository.findAll();
		
		de.geoinfoffm.registry.api.soap.ObjectFactory of = new de.geoinfoffm.registry.api.soap.ObjectFactory(); 
		
		GetRegisterDescriptionsResponse result = of.createGetRegisterDescriptionsResponse();
		for (RE_Register register : registers) {
			RegisterDescription regDesc = of.createRegisterDescription();
			regDesc.setUuid(register.getUuid().toString());
			regDesc.setName(register.getName());
			for (UUID subregisterUuid : registerRepository.getSubregisters(register.getUuid())) {
				regDesc.getSubregisterUuid().add(subregisterUuid.toString());
			}
			for (RE_ItemClass itemClass : register.getContainedItemClasses()) {
				ItemClassDescription icDesc = of.createItemClassDescription();
				icDesc.setUuid(itemClass.getUuid().toString());
				icDesc.setName(itemClass.getName());
				regDesc.getItemClass().add(icDesc);
			}
			
			result.getRegisterDescription().add(regDesc);
		}
		
		return of.createGetRegisterDescriptionsResponse(result);
	}

	@Transactional(readOnly = true)
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetRegisterRequest")
	public @ResponsePayload JAXBElement<GetRegisterResponse> handleGetRegisterRequest(@RequestPayload GetRegisterRequest req) throws Exception {
		if (req == null || (req.isSetName() && req.isSetUuid())) { 
			throw new IllegalArgumentException("Invalid request");
		}
		
		RE_Register register;
		if (req.isSetName()) {
			String name = req.getName();
			register = registerService.findByName(name);
			if (register == null) {
				throw new EntityNotFoundException(String.format("No register with name '%s' exists.", name));
			}
		}
		else if (req.isSetUuid()) {
			UUID uuid = UUID.fromString(req.getUuid());
			register = registerService.findOne(uuid);
			if (register == null) {
				throw new EntityNotFoundException(String.format("No register with uuid '%s' exists.", uuid));
			}
		}
		else {
			throw new IllegalArgumentException("Invalid request");
		}

		RE_Register_Type resultRegister = xmlFactory.register(register, "http://localhost:8080/fad/%s");
		de.geoinfoffm.registry.api.soap.ObjectFactory of = new de.geoinfoffm.registry.api.soap.ObjectFactory(); 
		GetRegisterResponse result = new GetRegisterResponse();
		result.setRE_Register(resultRegister);
		return of.createGetRegisterResponse(result);
	}

	@Transactional(readOnly = true)
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetRegisterItemRequest")
	public @ResponsePayload JAXBElement<RE_RegisterItem_Type> handleGetRegisterItemRequest(@RequestPayload GetRegisterItemRequest req) throws Exception {
		if (req == null || StringUtils.isEmpty(req.getUuid())) { 
			throw new IllegalArgumentException("Invalid uuid");
		}
		UUID uuid = UUID.fromString(req.getUuid());
		
		RE_RegisterItem item = registerItemService.findOne(uuid);
		if (item == null) {
			throw new EntityNotFoundException(String.format("No register item with id '%s' exists.", uuid.toString()));
		}
//		return new StaxXmlSerializer.EntityContainer(result, sessionFactory);
		
		RegisterItemViewBean viewBean = viewBeanFactory.getViewBean(item);
		
//		RE_RegisterItem_Type result = of.createRE_RegisterItem_Type();
		
		return iso19135Factory.createRE_RegisterItem(viewBean.toXmlType(proposalDtoFactory));
	}
	
	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateRegisterRequest")
	public @ResponsePayload JAXBElement<CreateRegisterResponse> handleCreateRegisterRequest(@RequestPayload final CreateRegisterRequest req) throws Exception {
		security.assertIsAdmin();
		
		if (req == null) { 
			throw new IllegalArgumentException("Invalid request");
		}
		
		RE_Register existingRegister = registerService.findByName(req.getName());
		if (existingRegister != null) {
			throw new IllegalArgumentException(String.format("Register with name %s already exists (UUID: %s)", existingRegister.getName(), existingRegister.getUuid().toString()));
		}
		
		if (StringUtils.isEmpty(req.getName()) || StringUtils.isEmpty(req.getShortName())) {
			throw new IllegalArgumentException("Both name and shortName must be provided");
		}
		
		RE_Register parentRegister = null;
		if (req.isSetParentRegisterName()) {
			parentRegister = registerService.findByName(req.getParentRegisterName()); 
			if (parentRegister == null) {
				throw new IllegalArgumentException(String.format("Parent register %s does not exist", req.getParentRegisterName()));
			}
			
			security.assertHasEntityRelatedRole(RegistrySecurity.SUBMITTER_ROLE_PREFIX, parentRegister.getUuid());
		}

		Organization owner = orgService.findOne(UUID.fromString(req.getOwnerOrganizationUuid()));
		Organization manager = orgService.findOne(UUID.fromString(req.getManagerOrganizationUuid()));
		Organization controlBody = orgService.findOne(UUID.fromString(req.getControlBodyOrganizationUuid()));

		logger.debug("Creating register...");
		RE_Register register = registerService.createRegister(
				req.getName(),
				owner, manager, controlBody,
				roleService, RE_Register.class,
				new ParameterizedRunnable<RE_Register>() {
					@Override
					public void run(RE_Register parameter) {
					}
				}
		);
		
		for (String itemClassName : req.getContainedItemClassName()) {
			addItemClassToRegister(itemClassName, register);
		}

		CreateRegisterResponse result = new CreateRegisterResponse();
		result.setUuid(register.getUuid().toString());

		if (parentRegister != null) {
			logger.debug("Creating RE_SubregisterDescription proposal...");
			RE_ItemClass icSubregister = itemClassRepository.findByName("Subregister");
			Addition subregisterProposal = createSubregisterProposal(parentRegister, icSubregister, manager, register.getName());
			subregisterProposal = proposalService.submitProposal(subregisterProposal);
			logger.debug("Proposal for subregister '{}' has ID '{}'", register.getName(), subregisterProposal.getUuid().toString());
			result.setProposalUuid(subregisterProposal.getUuid().toString());
		}

		return objectFactory.createCreateRegisterResponse(result);
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddItemClassToRegisterRequest")
	public @ResponsePayload JAXBElement<AddItemClassToRegisterResponse> handleAddItemClassToRegisterRequest(@RequestPayload final AddItemClassToRegisterRequest req) throws Exception {
		RE_Register register = registerService.findByName(req.getRegisterName());
		if (register == null) {
			throw new IllegalArgumentException(String.format("Register with name %s does not exist", req.getRegisterName()));
		}
		
		AddItemClassToRegisterResponse result = new AddItemClassToRegisterResponse();
		result.setRegisterName(register.getName());
		
		for (String itemClassName : req.getItemClassName()) {
			addItemClassToRegister(itemClassName, register);
			result.getAddedItemClassName().add(itemClassName);
		}
		
		return objectFactory.createAddItemClassToRegisterResponse(result);
	}

	private RE_ItemClass addItemClassToRegister(String itemClassName, RE_Register register) {
		RE_ItemClass itemClass = null;
		for (RE_ItemClass containedItemClass : register.getContainedItemClasses()) {
			if (containedItemClass.getName().equals(itemClassName)) {
				itemClass = containedItemClass;
				break;
			}
		}
		
		if (itemClass == null) {
			logger.debug(String.format("> Adding item class to register '%s'...", register.getName()));
			
			itemClass = itemClassRepository.findByName(itemClassName);
			
			if (itemClass == null) {
				itemClass = new RE_ItemClass();
				itemClass.setName(itemClassName);
			}
			
			register.getContainedItemClasses().add(itemClass);
			itemClass = itemClassRepository.save(itemClass);
		}
		
		return itemClass;
	}

	public Addition createSubregisterProposal(RE_Register register, RE_ItemClass itemClass, Organization sponsor, String name) throws InvalidProposalException, UnauthorizedException {
		SubregisterDescriptionProposalDTO proposal = new SubregisterDescriptionProposalDTO();
		proposal.setName(name);
		proposal.setDefinition("Subregister in " + register.getName());
		proposal.setJustification("Example data");
		proposal.setSubregisterManager(register.getManager().getUuid());
		for (RE_ItemClass containedItemClass : register.getContainedItemClasses()) {
			proposal.getContainedItemClasses().add(containedItemClass.getUuid());
		}
		proposal.setOperatingLanguage("de");
		proposal.setUniformResourceIdentifier("urn:register:" + register.getUuid().toString());
		
		return createAdditionProposal(register, itemClass, sponsor, proposal);
	}

	public Addition createAdditionProposal(RE_Register register, RE_ItemClass itemClass, Organization sponsor, RegisterItemProposalDTO proposal) throws InvalidProposalException, UnauthorizedException {
		proposal.setItemClassUuid(itemClass.getUuid());
		proposal.setSponsorUuid(sponsor.getSubmittingOrganization().getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		Addition ai = proposalService.createAdditionProposal(proposal);
		
		return ai;
	}

}