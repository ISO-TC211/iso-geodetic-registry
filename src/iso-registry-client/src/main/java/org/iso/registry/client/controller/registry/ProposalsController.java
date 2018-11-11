/**
 * 
 */
package org.iso.registry.client.controller.registry;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.CONTROLBODY_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.MANAGER_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.OWNER_ROLE_PREFIX;
import static de.geoinfoffm.registry.core.security.RegistrySecurity.SUBMITTER_ROLE_PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.iso.registry.api.registry.IsoProposalService;
import org.iso.registry.client.controller.registry.RegisterController.SupersessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.ItemNotFoundException;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.ProposalListItem;
import de.geoinfoffm.registry.api.ProposalListItemImpl;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.ViewBeanFactory;
import de.geoinfoffm.registry.client.web.AbstractController;
import de.geoinfoffm.registry.client.web.BasePathRedirectView;
import de.geoinfoffm.registry.client.web.DatatableParameters;
import de.geoinfoffm.registry.client.web.DatatablesResult;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.PropertyConfiguration;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalChangeRequestRepository;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.ProposalRepository;
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
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.core.security.RegistryUserUtils;
import de.geoinfoffm.registry.core.workflow.ProposalWorkflowManager;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.SupersessionRepository;

/**
 * @author Florian.Esser
 *
 */
@Controller
@RequestMapping("/proposals")
public class ProposalsController extends AbstractController
{
	private static final Logger logger = LoggerFactory.getLogger(ProposalsController.class);
	
	@Autowired
	private RegisterItemService itemService;

	@Autowired
	private IsoProposalService proposalService;

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

	@Autowired
	private ProposalChangeRequestRepository pcrRepository;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private ProposalWorkflowManager workflowManager;

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
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	@Transactional
	public String viewProposal(WebRequest request, 
							   @PathVariable("uuid") UUID proposalUuid, 
							   @RequestParam(value = "edit", required = false) String forceEdit,
							   @RequestParam(value = "role", required = false) String role,
							   final Model model) throws ProposalNotFoundException, UnauthorizedException {

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		
		RegisterItemProposalDTO dto = proposalDtoFactory.getProposalDto(proposal);
		model.addAttribute("proposal", dto);
		model.addAttribute("itemClass", dto.getItemClassUuid());
		
		if (!StringUtils.isEmpty("role")) {
			model.addAttribute("_userRole", role);
		}
		else {
			model.addAttribute("_userRole", "submitter");
		}
		
		if (!dto.getSupersededItems().isEmpty()) {
			Set<RegisterItemViewBean> supersededItems = new HashSet<RegisterItemViewBean>();
			for (UUID uuid : dto.getSupersededItems()) {
				RE_RegisterItem supersededItem = itemService.findOne(uuid);
				RegisterItemViewBean rvb = viewBeanFactory.getViewBean(supersededItem);
				supersededItems.add(rvb);
			}
			model.addAttribute("supersededItems", supersededItems);
		}
		
		if (!dto.getNewSupersedingItems().isEmpty() || !dto.getExistingSupersedingItems().isEmpty()) {
			Set<RegisterItemViewBean> supersedingItems = new HashSet<RegisterItemViewBean>();
			for (UUID uuid : dto.getExistingSupersedingItems()) {
				RE_RegisterItem supersedingItem = itemService.findOne(uuid);
				supersedingItems.add(viewBeanFactory.getViewBean(supersedingItem));
			}
			
			for (RegisterItemProposalDTO newItem : dto.getNewSupersedingItems()) {
				RE_RegisterItem supersedingItem = itemService.findOne(newItem.getItemUuid());
				supersedingItems.add(viewBeanFactory.getViewBean(supersedingItem));
			}
			
			model.addAttribute("supersedingItems", supersedingItems);
		}

		ItemClassConfiguration itemClassConfiguration = null;
		RegisterItemViewBean rvb = null;
		if (proposal instanceof SimpleProposal) {
			itemClassConfiguration = itemClassRegistry.getConfiguration(((SimpleProposal)proposal).getItem().getItemClass().getName());
			model.addAttribute("itemClassConfiguration", itemClassConfiguration);
			rvb = viewBeanFactory.getViewBean(proposal);
		}
//		else if (proposal instanceof HierarchicalProposal && ((HierarchicalProposal)proposal).getPrimaryProposal() instanceof SimpleProposal) {
//			HierarchicalProposal group = (HierarchicalProposal)proposal;
//			if (group.getPrimaryProposal() != null) {
//				SimpleProposal primaryProposal = (SimpleProposal)group.getPrimaryProposal();
//				itemClassConfiguration = itemClassRegistry.getConfiguration(primaryProposal.getItem().getItemClass().getName());
//				model.addAttribute("itemClassConfiguration", itemClassConfiguration);
//				rvb = viewBeanFactory.getViewBean(primaryProposal);
//			}
//		}
		else if (proposal instanceof Supersession) {
			rvb = RegisterItemViewBean.forProposal((Supersession)proposal, workflowManager);
		}
		else if (proposal instanceof ProposalGroup) {
			rvb = RegisterItemViewBean.forProposal((ProposalGroup)proposal, workflowManager);
		}
		
		if (rvb == null) {
			throw new RuntimeException(String.format("No view bean for proposal '%s' [class: %s]", proposal.getTitle(), proposal.getClass().getCanonicalName()));
		}

		model.addAttribute("rvb", rvb);
//		if (proposal instanceof SimpleProposal) {
//			itemClassConfiguration = itemClassRegistry.getConfiguration(((SimpleProposal)proposal).getItem().getItemClass().getName());
//			model.addAttribute("itemClassConfiguration", itemClassConfiguration);
//		}

		model.addAttribute("isProposal", "true");
		
		String viewName;
		if (itemClassConfiguration != null && !StringUtils.isEmpty(itemClassConfiguration.getViewProposalTemplate())) {
			viewName = itemClassConfiguration.getViewProposalTemplate();
		}
		else {
			StringBuilder viewNameBuilder = new StringBuilder("registry/proposal/");
			if (workflowManager.isEditable(proposal) || ("true".equals(forceEdit) && workflowManager.isWithdrawable(proposal))) {
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
		
		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		
		model.addAttribute("isNew", "true");
		model.addAttribute("partOfSupersession", "true");

		RegisterItemProposalDTO newItem = new RegisterItemProposalDTO();
		newItem.setProposalType(ProposalType.ADDITION);		
		newItem.setSponsorUuid(suborg.getUuid());
		newItem.setTargetRegisterUuid(targetRegister.getUuid());

		model.addAttribute("proposal", newItem);
		model.addAttribute("register", targetRegister);
		
		Collection<RE_ItemClass> itemClasses = targetRegister.getContainedItemClasses();
		itemClasses.size();
		model.addAttribute("itemClasses", itemClasses);

		return "registry/proposal/create_addition";
	}

	@RequestMapping(value = "/{uuid}", params={ "saveProposal" })
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
		
		String role = null;
		if (allParams.containsKey("_userRole")) {
			role = (String)allParams.get("_userRole");
		}
		
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
			proposalDto = bindAdditionalAttributes(proposalDto, servletRequest, itemClassRepository, itemClassRegistry, conversionService);
			
			proposalService.updateProposal(proposalDto);
			if (role != null) {
				return "redirect:/management/" + role;
			}
			else {
				return "redirect:/";
			}
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

	@RequestMapping(value = "/{uuid}", params={ "submitProposal" })
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
	public Object updateProposal(
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
			return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		RE_ItemClass itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());
		RegisterItemProposalDTO dto = proposalDtoFactory.getProposalDto(itemClass);
		dto = bindAdditionalAttributes(dto, servletRequest, itemClassRepository, itemClassRegistry, conversionService);

		proposalService.updateProposal(dto);
		
		return "redirect:/management/submitter";
	}

	@RequestMapping(value = "/{uuid}/withdraw", method = RequestMethod.POST)
	@Transactional 
	public ResponseEntity<Void> withdrawProposal(@PathVariable("uuid") UUID proposalUuid) throws InvalidProposalException, IllegalOperationException, UnauthorizedException {
		logger.debug("POST /proposal/{}/withdraw", proposalUuid);
		
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
//		security.assertMayDelete(proposal);
		
		proposalService.withdrawProposal(proposal);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/{uuid}/submit", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<Void> submitProposal(@PathVariable("uuid") UUID proposalUuid) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposal/{}/submit", proposalUuid);
		
		Proposal proposal = proposalService.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

//		security.assertMaySubmit(proposal);

		proposalService.submitProposal(proposal);
		
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

		security.assertHasAnyEntityRelatedRoleForAll(Arrays.asList(MANAGER_ROLE_PREFIX, CONTROLBODY_ROLE_PREFIX), proposal.getAffectedRegisters());

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
											   @RequestParam("controlBodyAcceptDecisionEvent") String controlBodyDecisionEvent) 
	throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposal/{}/accept", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		security.assertHasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());

		proposalService.acceptProposal(proposal, controlBodyDecisionEvent);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/{uuid}/return", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> returnProposal(@PathVariable("uuid") UUID proposalUuid,
											@RequestParam("noteToSubmitter") String noteToSubmitter) throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposal/{}/return", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		if (workflowManager.isUnderReview(proposal)) {
			security.assertHasEntityRelatedRoleForAll(MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());
			proposalService.returnProposal(proposal, "[Register Manager] " + noteToSubmitter);
		}
		else if (workflowManager.isPending(proposal)) {
			security.assertHasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());
			proposalService.returnProposal(proposal, "[Control Body] " + noteToSubmitter);			
		}
		else {
			// cannot be rejected: wrong status
			return new ResponseEntity<String>(String.format("The proposal %s cannot be rejected: wrong status", proposal.getUuid().toString()), HttpStatus.FORBIDDEN);			
		}

		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity<?> deleteProposal(@PathVariable("uuid") UUID proposalUuid) throws IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("DELETE /proposal/{}", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}

		if (proposal.isSubmitted()) {
			throw new IllegalOperationException("Cannot delete submitted proposal: use withdraw");
		}
		
		proposalService.deleteProposal(proposal);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@RequestMapping(value = "/{uuid}/reject", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> rejectProposal(@PathVariable("uuid") UUID proposalUuid,
											   @RequestParam("controlBodyRejectDecisionEvent") String controlBodyDecisionEvent) 
	throws InvalidProposalException, IllegalOperationException, ProposalNotFoundException, UnauthorizedException {
		logger.debug("POST /proposals/{}/reject", proposalUuid);

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		
		if (workflowManager.isPending(proposal)) {
			security.assertHasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());
			proposalService.rejectProposal(proposal, controlBodyDecisionEvent);			
		}
		else {
			// cannot be rejected: wrong status
			return new ResponseEntity<String>(String.format("The proposal %s cannot be rejected: wrong status", proposal.getUuid().toString()), HttpStatus.FORBIDDEN);			
		}
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/{uuid}/appeal", method = RequestMethod.GET)
	@Transactional
	public String appealProposal(@PathVariable("uuid") UUID proposalUuid,
			final Model model) throws ProposalNotFoundException, UnauthorizedException {
		
		logger.debug("GET /proposals/{}/appeal", proposalUuid);

		security.assertIsLoggedIn();
		
		Proposal proposal = proposalRepository.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		
		Appeal appeal = proposalService.findAppeal(proposal);
		if (appeal != null) {
			model.addAttribute("appeal", appeal);
		}

		RegisterItemViewBean rvb = RegisterItemViewBean.forProposal(proposal, workflowManager);		
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

	@RequestMapping(value = "/{uuid}/addproposal", method = RequestMethod.POST)
	@Transactional 
	public ResponseEntity<Void> addProposalToGroup(@PathVariable("uuid") UUID proposalUuid, @RequestParam("addedProposalUuid") UUID addedProposalUuid) throws InvalidProposalException, IllegalOperationException, UnauthorizedException, ProposalNotFoundException {
		logger.debug("POST /proposal/{}/addproposal", proposalUuid);
		
		security.assertHasRoleWith(SUBMITTER_ROLE_PREFIX);
		RegistryUser currentUser = security.getCurrentUser();

		Proposal proposal = proposalService.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		if (!(proposal instanceof ProposalGroup)) {
			throw new IllegalOperationException("Cannot add proposal to non-group");
		}
		if (proposalUuid.equals(addedProposalUuid)) {
			throw new IllegalOperationException("Cannot add group to itself");			
		}
		
		Proposal addedProposal = proposalService.findOne(addedProposalUuid);

		ProposalGroup group = (ProposalGroup)proposal;
		group.addProposal(addedProposal);
		proposalService.saveProposal(group);
		
		return new ResponseEntity<Void>(HttpStatus.OK);		
	}

	@RequestMapping(value = "/{uuid}/removefromgroup", method = RequestMethod.POST)
	@Transactional 
	public ResponseEntity<Void> removeProposalFromGroup(@PathVariable("uuid") UUID proposalUuid) throws InvalidProposalException, IllegalOperationException, UnauthorizedException, ProposalNotFoundException {
		logger.debug("POST /proposal/{}/removefromgroup", proposalUuid);
		
		security.assertHasRoleWith(SUBMITTER_ROLE_PREFIX);
		RegistryUser currentUser = security.getCurrentUser();

		Proposal proposal = proposalService.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		if (!proposal.hasParent() || !(proposal.getParent() instanceof ProposalGroup) || proposal.getParent() instanceof Supersession) {
			throw new IllegalOperationException("Proposal is not part of a group");
		}
		
		ProposalGroup group = (ProposalGroup)proposal.getParent();
		group.removeProposal(proposal);
		proposalService.saveProposal(group);
		
		return new ResponseEntity<Void>(HttpStatus.OK);				
	}

	@RequestMapping(value = "/{uuid}/dissolve", method = RequestMethod.POST)
	@Transactional 
	public ResponseEntity<Void> dissolveProposalGroup(@PathVariable("uuid") UUID proposalUuid) throws InvalidProposalException, IllegalOperationException, UnauthorizedException, ProposalNotFoundException {
		logger.debug("POST /proposal/{}/dissolve", proposalUuid);
		
		security.assertHasRoleWith(SUBMITTER_ROLE_PREFIX);
		RegistryUser currentUser = security.getCurrentUser();

		Proposal proposal = proposalService.findOne(proposalUuid);
		if (proposal == null) {
			throw new ProposalNotFoundException(proposalUuid);
		}
		if (!(proposal instanceof ProposalGroup) || (proposal instanceof Supersession)) {
			throw new IllegalOperationException("Cannot dissolve non-group or supersessions");
		}
		
		ProposalGroup group = (ProposalGroup)proposal;
		while (!group.getProposals().isEmpty()) {
			group.removeProposal(group.getProposals().get(0));
		}
		
		group = proposalService.saveProposal(group);
		proposalService.delete(group.getUuid());
		
		return new ResponseEntity<Void>(HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/by-group/{uuid}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody DatatablesResult getAdminProposals(@PathVariable("uuid") UUID groupUuid,
														    @RequestParam Map<String, String> parameters) throws UnauthorizedException {

		DatatableParameters dtParameters = new DatatableParameters(parameters);
		Pageable pageable = dtParameters.createPageable();

		Page<Proposal> proposals;
		if (!StringUtils.isEmpty(dtParameters.sSearch)) {
			proposals = proposalRepository.findByParentAndIsConcludedIsFalse(groupUuid, "%" + dtParameters.sSearch + "%", pageable);
		}
		else {
			proposals = proposalRepository.findByParentAndIsConcludedIsFalse(groupUuid, pageable);
		}

		Locale locale = LocaleContextHolder.getLocale();
		
		List<ProposalListItem> proposalViewBeans = new ArrayList<>();

		for (Proposal proposal : proposals) {
			ProposalListItem rvb = new ProposalListItemImpl(proposal, messageSource, locale, workflowManager, pcrRepository);
			proposalViewBeans.add(rvb);
		}
		DatatablesResult result = new DatatablesResult(proposals.getTotalElements(), proposals.getTotalElements(), dtParameters.sEcho, proposalViewBeans);
		
		return result;
	}
}
