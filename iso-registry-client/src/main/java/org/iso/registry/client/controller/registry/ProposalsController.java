/**
 * 
 */
package org.iso.registry.client.controller.registry;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.CONTROLBODY_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.MANAGER_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.OWNER_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.SUBMITTER_ROLE_PREFIX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;

import org.iso.registry.client.controller.registry.RegisterController.SupersessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.ItemNotFoundException;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.client.web.BasePathRedirectView;
import de.geoinfoffm.registry.client.web.ProposalDtoFactory;
import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.client.web.ViewBeanFactory;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.PropertyConfiguration;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.ProposalRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.persistence.SupersessionRepository;

/**
 * @author Florian.Esser
 *
 */
@Controller
@RequestMapping("/proposals")
public class ProposalsController
{
	private static final Logger logger = LoggerFactory.getLogger(ProposalsController.class);
	
	@Autowired
	private RegisterItemService itemService;

	@Autowired
	private ProposalService proposalService;

	@Autowired
	private SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private ItemClassRepository itemClassRepository;

	@Autowired
	private ProposalRepository proposalRepository;

	@Autowired
	private RegisterRepository registerRepository;

	@Autowired
	private SupersessionRepository supersessionRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private RegistryUserService userService;
	
	@Autowired
	private ViewResolver viewResolver;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private ItemClassRegistry itemClassRegistry;
	
	@Autowired
	private ViewBeanFactory viewBeanFactory;
	
	@Autowired
	private ProposalDtoFactory proposalDtoFactory;
	
	@RequestMapping(value = "/", method = RequestMethod.POST/*, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE*/)
	@Transactional
	public View createProposal(@ModelAttribute("proposal") RegisterItemProposalDTO proposal, final Model model) throws InvalidProposalException, ItemNotFoundException, IllegalOperationException, UnauthorizedException {
		security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, proposal.getTargetRegisterUuid());
		
		proposalService.propose(proposal);
		
		return new BasePathRedirectView("/management/submitter");
	}

	@RequestMapping(value = "/{uuid}/{property}", method = RequestMethod.GET)
	@Transactional
	public String viewProposalProperty(WebRequest request, 
			   @PathVariable("uuid") UUID proposalUuid, 
			   @PathVariable("property") String propertyName,
			   final Model model) throws Exception {

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertMayRead(proposal);

		ItemClassConfiguration itemClassConfiguration = null;
		if (proposal instanceof SimpleProposal) {
			itemClassConfiguration = itemClassRegistry.getConfiguration(((SimpleProposal)proposal).getItem().getItemClass().getName());
			model.addAttribute("itemClassConfiguration", itemClassConfiguration);
		}

		RegisterItemViewBean rvb = viewBeanFactory.getViewBean(proposal);

		if (itemClassConfiguration != null && rvb.getAdditionalProperties().keySet().contains(propertyName)) {
			PropertyConfiguration propertyConfiguration = itemClassConfiguration.getProperty(propertyName);
			if (propertyConfiguration == null) {
				throw new RuntimeException(String.format("Property %s of item class %s is not configured in item class configuration file", propertyName, itemClassConfiguration.getItemClassName()));
			}
			
			PropertyHandler handler = context.getBean(propertyConfiguration.getViewHandlerBeanName(), PropertyHandler.class);

			Object entity = rvb.getAdditionalProperties().get(propertyName);
			handler.injectData(entity, model);
			
			return handler.viewName();
			
		}
		else {
			throw new EntityExistsException(String.format("Proposal %s has no property %s", proposalUuid, propertyName));
		}
	}

//	@RequestMapping(value = "/{uuid}/{property}", method = RequestMethod.PUT)
//	@Transactional
//	public String updateProposalProperty(WebRequest request, 
//			   @PathVariable("uuid") UUID proposalUuid, 
//			   @PathVariable("property") String propertyName,
//			   final Model model) throws Exception {
//
//		Proposal proposal = proposalRepository.findOne(proposalUuid);
//		if (proposal == null) {
//			throw new ProposalNotFoundException(proposalUuid);
//		}
//
//		security.assertMayRead(proposal);
//
//		ItemClassConfiguration itemClassConfiguration = null;
//		if (proposal instanceof SimpleProposal) {
//			itemClassConfiguration = itemClassRegistry.getConfiguration(((SimpleProposal)proposal).getItem().getItemClass().getName());
//			model.addAttribute("itemClassConfiguration", itemClassConfiguration);
//		}
//
//		RegisterItemViewBean rvb = ViewBeanFactory.getInstance().getViewBean(proposal);
//
//		if (itemClassConfiguration != null && rvb.getAdditionalProperties().keySet().contains(propertyName)) {
//			PropertyConfiguration propertyConfiguration = itemClassConfiguration.getProperty(propertyName);
//			if (propertyConfiguration == null) {
//				throw new RuntimeException(String.format("Property %s of item class %s is not configured in item class configuration file", propertyName, itemClassConfiguration.getItemClassName()));
//			}
//			
//			PropertyHandler handler = context.getBean(propertyConfiguration.getViewHandlerBeanName(), PropertyHandler.class);
//
//			Object entity = rvb.getAdditionalProperties().get(propertyName);
//			handler.injectData(entity, model);
//			
//			return handler.viewName();
//			
//		}
//		else {
//			throw new EntityExistsException(String.format("Proposal %s has no property %s", proposalUuid, propertyName));
//		}
//	}

	/*
	 * View proposal
	 */
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	@Transactional
	public String viewProposal(WebRequest request, 
							   @PathVariable("uuid") UUID proposalUuid, 
							   @RequestParam(value = "edit", required = false) String forceEdit,
							   final Model model) throws ProposalNotFoundException, UnauthorizedException {

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertMayRead(proposal);
		if ("true".equals(forceEdit)) {
			security.assertMayWrite(proposal);
		}
		
		RegisterItemProposalDTO dto = proposalDtoFactory.getProposalDto(proposal);
		model.addAttribute("proposal", dto);
		
		ItemClassConfiguration itemClassConfiguration = null;
		if (proposal instanceof SimpleProposal) {
			itemClassConfiguration = itemClassRegistry.getConfiguration(((SimpleProposal)proposal).getItem().getItemClass().getName());
			model.addAttribute("itemClassConfiguration", itemClassConfiguration);
		}

		RegisterItemViewBean rvb = viewBeanFactory.getViewBean(proposal);
		model.addAttribute("rvb", rvb);
		
		RE_SubmittingOrganization sponsor = suborgRepository.findOne(rvb.getSponsorUuid());
//		if (rvb.getProposalType().equals(ProposalType.SUPERSESSION)) {
//			Supersession s = (Supersession)proposal;
//			
//			RE_Register register = registerRepository.findByName(new CharacterString(rvb.getRegisterName()));
//			SupersessionState state = new SupersessionState(register, sponsor, itemService);
//			request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
//			
//			state.setControlBodyNotes(s.getSupersessionParts().get(0).getControlBodyNotes());
//			state.setRegisterManagerNotes(s.getSupersessionParts().get(0).getRegisterManagerNotes());
//			for (RE_RegisterItem supersededItem : s.getSupersededItems()) {
//				state.addSupersededItem(supersededItem, itemService.findFinalAdditionInformation(supersededItem));
//			}
//			for (RE_RegisterItem supersedingItems : s.getSupersedingItems()) {
//
//			model.addAttribute("state", state);
//		}
		
		String viewName;
		if (itemClassConfiguration != null && !StringUtils.isEmpty(itemClassConfiguration.getViewProposalTemplate())) {
			viewName = itemClassConfiguration.getViewProposalTemplate();
		}
		else {
			StringBuilder viewNameBuilder = new StringBuilder("registry/proposal/");
			if (proposal.isEditable() || ("true".equals(forceEdit) && proposal.isWithdrawable())) {
				viewNameBuilder.append("edit_");
				
				if (proposal instanceof Supersession) {
					SupersessionState state = new SupersessionState((Supersession)proposal, itemService);
					request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
					model.addAttribute("state", state);
				}
			}
			else {
				viewNameBuilder.append("view_");
			}
	
			viewNameBuilder.append(dto.getProposalType().name().toLowerCase());
			viewName = viewNameBuilder.toString();
		}
		
		return viewName;
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.POST, params = { "removeSupersedingItem" })
	@Transactional
	public String removeSupersedingItemFromSupersession(WebRequest request,
			final Model model, 
			@RequestParam("item") UUID supersedingItem) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		
		security.assertIsLoggedIn();
		
		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
		
		state.setStep("supersedingItems");
		state.removeSupersedingItem(supersedingItem);
		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
		model.addAttribute("state", state);

		return "registry/proposal/edit_supersession";
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.POST, params={ "addSupersededItems" })
	@Transactional
	public String addSupersededItemsToSupersession(WebRequest request,
			final Model model, 
			@RequestParam Map<String, String> supersededItems) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		
		security.assertIsLoggedIn();
		
		RegisterController.addSupersededItemsToSupersession(request, model, supersededItems, itemService);
		
		return "registry/proposal/edit_supersession";
	}

	@RequestMapping(value = "/{uuid}", params={ "addNew" })
	@Transactional
	public String addNewItemToProposal(WebRequest request,
			   @PathVariable("uuid") UUID proposalUuid, 
			   final Model model) throws ProposalNotFoundException, UnauthorizedException {

		security.assertIsLoggedIn();

		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
		
		state.setStep("supersedingItems");
		
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		if (!(proposal instanceof Supersession)) {
			throw new IllegalStateException("This function must be used by SUPERSESSIONs only");
		}
		
		Supersession supersession = (Supersession)proposal;
		RE_Register targetRegister = supersession.getTargetRegister();
		
		model.addAttribute("register", targetRegister);
		
//		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		RE_SubmittingOrganization suborg = suborgRepository.findAll().get(0);

		model.addAttribute("isNew", "true");
		model.addAttribute("partOfSupersession", "true");

		RegisterItemProposalDTO newItem = new RegisterItemProposalDTO();
		newItem.setProposalType(ProposalType.ADDITION);		
		newItem.setSponsorUuid(suborg.getUuid());
		newItem.setTargetRegisterUuid(targetRegister.getUuid());

		model.addAttribute("proposal", newItem);
		model.addAttribute("register", targetRegister);
		
		Set<RE_ItemClass> itemClasses = targetRegister.getContainedItemClasses();
		itemClasses.size();
		model.addAttribute("itemClasses", itemClasses);

		return "registry/proposal/create_addition";
	}

	@RequestMapping(value = "/{uuid}", params={ "save" })
	@Transactional
	public String saveObject(WebRequest request, final HttpServletRequest servletRequest,
			@PathVariable("uuid") UUID proposalUuid, 
			@ModelAttribute("proposal") RegisterItemProposalDTO proposalDto,
			@RequestParam Map<String, Object> allParams,
			final Model model) throws Exception {

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		security.assertMayWrite(proposal);
		
		if (proposal instanceof Supersession) {
			SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
			if (state == null) {
				throw new IllegalStateException("State not initialized");
			}
			state.addNewSupersedingItem(proposalDto);
			
			request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
			model.addAttribute("state", state);
			
			return "redirect:/registry/proposal/edit_supersession"; 
		}
		else {
			proposalDto = proposalDtoFactory.getProposalDto(proposal);
			ServletRequestDataBinder binder = new ServletRequestDataBinder(proposalDto);
			binder.bind(servletRequest);
			
			proposalService.updateProposal(proposalDto);
			return "redirect:/management/submitter";
		}
	}

	@RequestMapping(value = "/{uuid}", params={ "saveSupersedingItems" })
	@Transactional
	public String saveSupersedingItemsSupersessionProposal(WebRequest request,
			@PathVariable("uuid") UUID proposalUuid, 
			final Model model) throws UnauthorizedException {

		security.assertIsLoggedIn();
		
		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
						
		if (state.getNewSupersedingItems().isEmpty() && state.getExistingSupersedingItems().isEmpty()) {
			model.addAttribute("noSupersedingItems", "true");
		}
		else {
			state.setStep("additionalData");
		}

		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
		model.addAttribute("state", state);

		return "registry/proposal/edit_supersession";
		
	}
			

	@RequestMapping(value = "/{uuid}", params={ "saveAdditionalData" })
	@Transactional
	public String saveAdditionalDataSupersessionProposal(WebRequest request,
			@PathVariable("uuid") UUID proposalUuid, 
			final Model model,
			@RequestParam Map<String, String> additionalData) throws UnauthorizedException {

		security.assertIsLoggedIn();

		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
		
		if (additionalData.containsKey("justification")) {
			state.setJustification(additionalData.get("justification"));
		}
		if (additionalData.containsKey("registerManagerNotes")) {
			state.setRegisterManagerNotes(additionalData.get("registerManagerNotes"));
		}
		if (additionalData.containsKey("controlBodyNotes")) {
			state.setControlBodyNotes(additionalData.get("controlBodyNotes"));
		}

		state.setStep("overview");

		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
		model.addAttribute("state", state);
		
		return "registry/proposal/edit_supersession";
		
	}

	@RequestMapping(value = "/{uuid}", params={ "submit" })
	@Transactional
	public View submitSupersessionProposal(WebRequest request,
			@PathVariable("uuid") UUID proposalUuid,
			final Model model) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {

		security.assertIsLoggedIn();

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		if (!(proposal instanceof Supersession)) {
			throw new IllegalStateException("This function must be used by SUPERSESSIONs only");
		}
		
		Supersession supersession = (Supersession)proposal;
		
		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}

		Set<RE_RegisterItem> supersededItems = new HashSet<RE_RegisterItem>();
		for (RegisterItemViewBean supersededItemViewBean : state.getSupersededItems()) {
			RE_RegisterItem supersededItem = itemService.findOne(supersededItemViewBean.getUuid());
			if (supersededItem == null) {
				throw new InvalidProposalException(String.format("Superseded item %s does not exist", supersededItemViewBean.getUuid()));
			}
			supersededItems.add(supersededItem);
		}

		request.removeAttribute("supersession", WebRequest.SCOPE_SESSION);

		Set<RE_RegisterItem> existingSuccessors = new HashSet<RE_RegisterItem>();
		for (RegisterItemViewBean supersedingItemViewBean : state.getExistingSupersedingItems()) {
			RE_RegisterItem supersedingItem = itemService.findOne(supersedingItemViewBean.getUuid());
			if (supersedingItem == null) {
				throw new InvalidProposalException(String.format("Superseding item %s does not exist", supersedingItemViewBean.getUuid()));
			}
			existingSuccessors.add(supersedingItem);			
		}
		
		proposalService.updateSupersession(supersession, supersededItems, existingSuccessors, state.getNewSupersedingItems(), state.getJustification(), state.getRegisterManagerNotes(), state.getControlBodyNotes());
		
		proposalRepository.saveAndFlush(supersession);
		
//		itemService.proposeSupersession(supersededItems, state.getNewSupersedingItems(), 
//				state.getJustification(), state.getRegisterManagerNotes(), state.getControlBodyNotes(), state.getSponsor());
		
		return new BasePathRedirectView("/management/submitter");
	}
	
	/*
	 * Update proposal
	 */
	@RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
	@Transactional
	public ResponseEntity<Void> updateProposal(
			@PathVariable("uuid") UUID proposalUuid,
			@RequestParam(value = "op", required = false) String operation,
			@RequestParam(value = "item", required = false) String itemUuid,
			@ModelAttribute("proposal") RegisterItemProposalDTO proposal,
			@RequestParam Map<String, String> allParams,
			final WebRequest request, final HttpServletRequest servletRequest, 
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) throws ProposalNotFoundException, InvalidProposalException, UnauthorizedException {

		Proposal pmi = proposalRepository.findOne(proposalUuid);
		if (pmi == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		
		security.assertMayWrite(pmi);
		
		if (!StringUtils.isEmpty(operation)) {
			proposal = new RegisterItemProposalDTO(pmi);
			UUID uuid = UUID.fromString(itemUuid);
			// perform special operation
			if ("removeSupersedingItem".equals(operation)) {
				SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
				if (state == null) {
					throw new IllegalStateException("State not initialized");
				}
				
				state.removeSupersedingItem(uuid);
				request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
			else if ("removeSupersededItem".equals(operation)) {
				SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
				if (state == null) {
					throw new IllegalStateException("State not initialized");
				}

				state.removeSupersededItem(uuid);
				request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		}
		
		if (bindingResult.hasErrors()) {
//			if (pmi instanceof Addition) {
//				RE_Register targetRegister = ((Addition)pmi).getRegister();
//
//				Set<RE_ItemClass> itemClasses = targetRegister.getContainedItemClasses();
//				itemClasses.size();
//				model.addAttribute("itemClasses", itemClasses);
//			}

//			return "registry/proposal";
			return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		RE_ItemClass itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());
		RegisterItemProposalDTO dto = proposalDtoFactory.getProposalDto(itemClass);
		ServletRequestDataBinder binder = new ServletRequestDataBinder(dto);
		binder.bind(servletRequest);

		proposalService.updateProposal(proposal);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{uuid}/withdraw", method = RequestMethod.POST)
	@Transactional 
	public ResponseEntity<Void> withdrawProposal(@PathVariable("uuid") UUID proposalUuid) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		logger.debug("POST /proposal/{}/withdraw", proposalUuid);
		
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
		security.assertMayDelete(proposal);
		
		proposalService.withdrawProposal(proposal);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/{uuid}/review", method = RequestMethod.GET)
	@Transactional
	public String reviewProposal(@PathVariable("uuid") UUID proposalUuid, final Model model) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("GET /proposal/{}/review", proposalUuid);
		
		RegisterItemViewBean rvb;
	
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertHasAnyEntityRelatedRoleForAll(Arrays.asList(MANAGER_ROLE_PREFIX, OWNER_ROLE_PREFIX), proposal.getAffectedRegisters());

//		rvb = new RegisterItemViewBean(proposal);
		rvb = viewBeanFactory.getViewBean(proposal);
		
		model.addAttribute("proposal", rvb);
		
		return "mgmt/review";
	}	

	@RequestMapping(value = "/{uuid}/review", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> reviewProposal(@PathVariable("uuid") UUID proposalUuid) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposal/{}/review", proposalUuid);
		
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertHasEntityRelatedRoleForAll(MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());

		proposalService.reviewProposal(proposal);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/{uuid}/accept", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> acceptProposal(@PathVariable("uuid") UUID proposalUuid,
											   @RequestParam("controlBodyDecisionEvent") String controlBodyDecisionEvent) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposal/{}/accept", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertHasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());

		proposalService.acceptProposal(proposal, controlBodyDecisionEvent);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/{uuid}/reject", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> rejectProposal(@PathVariable("uuid") UUID proposalUuid,
											   @RequestParam("controlBodyDecisionEvent") String controlBodyDecisionEvent) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposal/{}/reject", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertHasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());

		proposalService.rejectProposal(proposal, controlBodyDecisionEvent);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/{uuid}/appeal", method = RequestMethod.GET)
	@Transactional
	public String appealProposal(@PathVariable("uuid") UUID proposalUuid,
			final Model model) throws ProposalNotFoundException, UnauthorizedException {
		
		logger.debug("GET /proposal/{}/appeal", proposalUuid);

		security.assertIsLoggedIn();
		
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertMayRead(proposal);
		security.assertMayWrite(proposal);
		
		RegistryUser registryUser =  security.getCurrentUser();

//		if (!proposal.getSponsor().getUuid().equals(registryUser.getOrganizationName() .getOrganization().getSubmittingOrganization().getUuid())) {
//			throw new UnauthorizedException("You are not authorized to access this resource");
//		}

		RegisterItemViewBean rvb = new RegisterItemViewBean(proposal);		
		model.addAttribute("proposal", rvb);

		return "registry/appeal";
	}

	@RequestMapping(value = "/{uuid}/appeal", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> appealProposal(@PathVariable("uuid") UUID proposalUuid, 
			@RequestParam("justification") String justification,
			@RequestParam("situation") String situation,
			@RequestParam("impact") String impact) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposal/{}/reject", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertMayRead(proposal);
		security.assertMayWrite(proposal);
		
		RegistryUser registryUser =  security.getCurrentUser();

//		if (!proposal.getSponsor().getUuid().equals(((GdiDeRegistryUser)registryUser).getOrganization().getSubmittingOrganization().getUuid())) {
//			throw new UnauthorizedException("You are not authorized to access this resource");
//		}

		proposalService.appealProposal(proposal, justification, situation, impact);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{uuid}/acceptAppeal", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> acceptAppeal(@PathVariable("uuid") UUID proposalUuid) throws ProposalNotFoundException, IllegalOperationException, UnauthorizedException {
		logger.debug("POST /proposal/{}/acceptAppeal", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		
		security.assertHasEntityRelatedRoleForAll(OWNER_ROLE_PREFIX, proposal.getAffectedRegisters());

		Appeal appeal = proposalService.findAppeal(proposal);
		if (appeal == null) {
			throw new IllegalOperationException("The proposal is not appealed");
		}

		proposalService.acceptAppeal(appeal);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{uuid}/rejectAppeal", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> rejectAppeal(@PathVariable("uuid") UUID proposalUuid) throws ProposalNotFoundException, IllegalOperationException, UnauthorizedException {
		logger.debug("POST /proposal/{}/rejectAppeal", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertHasEntityRelatedRoleForAll(OWNER_ROLE_PREFIX, proposal.getAffectedRegisters());

		Appeal appeal = proposalService.findAppeal(proposal);
		if (appeal == null) {
			throw new IllegalOperationException("The proposal is not appealed");
		}

		proposalService.rejectAppeal(appeal);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{uuid}/conclude", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> concludeProposal(@PathVariable("uuid") UUID proposalUuid) throws ProposalNotFoundException, IllegalOperationException, InvalidProposalException, UnauthorizedException {
		logger.debug("POST /proposal/{}/conclude", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertHasEntityRelatedRoleForAll(MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());

		proposalService.concludeProposal(proposal);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
