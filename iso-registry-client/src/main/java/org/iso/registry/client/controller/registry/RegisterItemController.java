/**
 * 
 */
package org.iso.registry.client.controller.registry;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.SUBMITTER_ROLE_PREFIX;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.iso.registry.client.controller.registry.RegisterController.SupersessionState;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.ItemNotFoundException;
import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterItemViewBean;
import de.geoinfoffm.registry.api.ViewBeanFactory;
import de.geoinfoffm.registry.client.web.BasePathRedirectView;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalFactory;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.core.security.RegistryUserUtils;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * @author Florian.Esser
 *
 */
@Controller
@RequestMapping("/item")
public class RegisterItemController
{
	@Autowired
	private RegisterItemService itemService;

	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalManagementInformationRepository pmiRepository;

	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private ProposalFactory proposalFactory;
	
	@Autowired
	private ViewBeanFactory viewBeanFactory;
	
	@Autowired
	private ItemClassRegistry itemClassRegistry;
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private ProposalDtoFactory proposalDtoFactory;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private VelocityContext velocityContext;

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String viewItem(@PathVariable("uuid") UUID itemUuid, final Model model) throws ItemNotFoundException, InvalidProposalException {
		RE_RegisterItem item = itemService.findOne(itemUuid);
		
		if (item == null) {
			throw new ItemNotFoundException(itemUuid);
		}
		
		RegisterItemViewBean viewBean;
		if (item.getStatus().equals(RE_ItemStatus.NOT_VALID)) {
			List<RE_ProposalManagementInformation> pmis = pmiRepository.findByItem(item);
			if (pmis.size() != 1) {
				throw new InvalidProposalException("Item with status 'not valid' must not have more than one pending proposal.");
			}
			else if (pmis.size() == 0) {
				throw new RuntimeException("Support for non-pending non-valid proposals not yet implemented");
			}
		
			Proposal proposal = proposalFactory.createProposal(pmis.get(0));
			viewBean = viewBeanFactory.getViewBean(proposal);
		}
		else {
			viewBean = viewBeanFactory.getViewBean(item);
		}

		model.addAttribute("item", viewBean);
		model.addAttribute("pmiEvaluator", new ProposalManagementInformationEvaluator());

		ItemClassConfiguration itemClassConfiguration = null;
		itemClassConfiguration = itemClassRegistry.getConfiguration(item.getItemClass().getName());
		model.addAttribute("itemClassConfiguration", itemClassConfiguration);
		model.addAttribute("itemClassName", item.getItemClass().getName());

		String viewName;
		if (itemClassConfiguration != null && !StringUtils.isEmpty(itemClassConfiguration.getViewItemTemplate())) {
			viewName = itemClassConfiguration.getViewItemTemplate();
		}
		else {
			viewName = "registry/item";
		}
		
		return viewName;
	}

	@RequestMapping(value = "/{uuid}/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> viewItemAsXml(@PathVariable("uuid") UUID itemUuid, final Model model, HttpServletResponse response) 
	throws ItemNotFoundException, InvalidProposalException, XmlSerializationException {
		
		RE_RegisterItem item = itemService.findOne(itemUuid);
		
		if (item == null) {
			throw new ItemNotFoundException(itemUuid);
		}

		StringWriter sw = new StringWriter();
		itemService.toXml(item, sw);
		
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
//		return sw.toString();
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/xml; charset=utf-8");
		return new ResponseEntity<String>(sw.toString(), responseHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{uuid}/pdf", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public void viewItemAsPdf(@PathVariable("uuid") UUID itemUuid, final Model model, HttpServletResponse response) throws ItemNotFoundException, InvalidProposalException, IOException, FOPException, TransformerFactoryConfigurationError, TransformerException {
		RE_RegisterItem item = itemService.findOne(itemUuid);
		RegisterItemViewBean vb = viewBeanFactory.getViewBean(item);
		
		model.addAttribute("item", vb);
		
		String templateLocation = "pdftemplates/" + item.getItemClass().getName().toLowerCase() + ".fo";

		StringWriter sw = new StringWriter();
		velocityEngine.mergeTemplate(templateLocation, "UTF-8", new VelocityContext(model.asMap(), velocityContext), sw);
		String sourceXml = sw.toString();

		ServletOutputStream out = response.getOutputStream();
		
		final Fop fop = FopFactory.newInstance().newFop(MimeConstants.MIME_PDF, out);
		final Transformer xf = TransformerFactory.newInstance().newTransformer();
		xf.setParameter("versionParam", "2.0");
		final Source src = new StreamSource(new StringReader(sourceXml));
		final Result result = new SAXResult(fop.getDefaultHandler());
		xf.transform(src, result);

		out.flush();
		out.close();
		response.flushBuffer();
		
//		return new ModelAndView(new IdentifiedItemPdfView(), model.asMap());
	}


	@RequestMapping(value = "/{uuid}/retire", method = RequestMethod.GET)
	@Transactional
	public View proposeItemRetirement(@PathVariable("uuid") UUID itemUuid,
									  @RequestParam(value = "justification", required = true) String justification,
									  @RequestParam(value = "registerManagerNotes", required = false) String registerManagerNotes,
									  @RequestParam(value = "controlBodyNotes", required = false) String controlBodyNotes) throws IllegalOperationException, ItemNotFoundException {
		if (StringUtils.isEmpty(justification)) {
			throw new IllegalOperationException("Cannot accept empty justification.");
		}
		
		RE_RegisterItem item = itemService.findOne(itemUuid);
		if (item == null) {
			throw new ItemNotFoundException(itemUuid);
		}
		
		if (!item.isValid()) {
			throw new IllegalOperationException(String.format("Cannot retire item with status %s", item.getStatus().name()));
		}
		
		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);

		proposalService.createRetirement(item, justification, registerManagerNotes, controlBodyNotes, suborg);

		return new BasePathRedirectView("/management/submitter");
	}

	@RequestMapping(value = "/{uuid}/supersede", method = RequestMethod.GET)
	@Transactional
	public String createSupersessionProposal(final WebRequest request, 
											 @PathVariable("uuid") UUID itemUuid, 
											 @ModelAttribute("proposal") RegisterItemProposalDTO proposal, 
											 final Model model) throws ItemNotFoundException, UnauthorizedException {

		model.addAttribute("isNew", "true");
	
		RE_RegisterItem item = itemService.findOne(itemUuid);
		if (item == null) {
			throw new ItemNotFoundException(itemUuid);
		}
		
		if (!item.isValid()) {
			throw new IllegalOperationException(String.format("Cannot supersede item with status %s", item.getStatus().name()));
		}

		security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, item.getRegister());

		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		SupersessionState state = new SupersessionState(item.getRegister(), suborg, itemService);
		state.addSupersededItem(item);
		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);

		model.addAttribute("state", state);

		Collection<RE_ItemClass> itemClasses = item.getRegister().getContainedItemClasses();
		itemClasses.size();
		model.addAttribute("itemClasses", itemClasses);

		return "registry/proposal/create_supersession";
	}
	
	@RequestMapping(value = "/{uuid}/supersede", method = RequestMethod.POST, params={ "addNew" })
	@Transactional
	public String addNewItemToProposalPost(WebRequest request,
			@PathVariable("uuid") UUID itemUuid, 
			@RequestParam(value = "itemClass", required = false) String itemClassUuid,
			final Model model,
			final RedirectAttributes redirectAttributes) throws UnauthorizedException {
		
		return addNewItemToProposal(request, itemUuid, itemClassUuid, model, redirectAttributes);
	}

	@RequestMapping(value = "/{uuid}/supersede", params={ "addNew" })
	@Transactional
	public String addNewItemToProposal(WebRequest request,
			@PathVariable("uuid") UUID itemUuid, 
			@RequestParam(value = "itemClass", required = false) String itemClassUuid,
			final Model model,
			final RedirectAttributes redirectAttributes) throws UnauthorizedException { 
		
		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}

		RE_RegisterItem item = itemService.findOne(itemUuid);
		if (item == null) {
			throw new ItemNotFoundException(itemUuid);
		}

		RE_Register targetRegister = item.getRegister();
		if (targetRegister == null) {
			redirectAttributes.addFlashAttribute("registerName", targetRegister.getName());
			return "registry/register_notfound";
		}
		model.addAttribute("register", targetRegister);
		
		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);

		model.addAttribute("isNew", "true");

		RegisterItemProposalDTO newItem = new RegisterItemProposalDTO();
		newItem.setProposalType(ProposalType.ADDITION);		
		newItem.setSponsorUuid(suborg.getUuid());
		newItem.setTargetRegisterUuid(targetRegister.getUuid());

		model.addAttribute("proposal", newItem);
		model.addAttribute("partOfSupersession", "true");
		
		return createProposal(targetRegister, itemClassUuid, newItem, model, redirectAttributes);
	}

	@RequestMapping(value = "/{uuid}/supersede", params={ "saveProposal" })
	@Transactional
	public String saveNewItem(WebRequest request, HttpServletRequest servletRequest,
			@PathVariable("uuid") UUID itemUuid, 
			@ModelAttribute("proposal") RegisterItemProposalDTO proposal,
			final Model model) {
		
		RE_ItemClass itemClass = itemClassRepository.findOne(proposal.getItemClassUuid());

		proposal = proposalDtoFactory.getProposalDto(itemClass);
		if (proposal.getClass().getCanonicalName().equals(RegisterItemProposalDTO.class.getCanonicalName())) {
			model.addAttribute("itemClassNotConfigured", "true");
		}

		proposal = bindAdditionalAttributes(proposal, servletRequest);

		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
		state.addNewSupersedingItem(proposal);
		
		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
		model.addAttribute("state", state);
		
		return "registry/proposal/create_supersession";
	}

	@RequestMapping(value = "/{uuid}/supersede", params={ "saveSupersedingItems" })
	@Transactional
	public String saveSupersedingItemsSupersessionProposal(WebRequest request,
			final Model model) {
		
		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
						
		if (state.getNewSupersedingItems().isEmpty()) {
			model.addAttribute("noSupersedingItems", "true");
		}
		else {
			state.setStep("additionalData");
		}

		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
		model.addAttribute("state", state);

		return "registry/proposal/create_supersession";
	}
			
	@RequestMapping(value = "/{uuid}/supersede", params={ "saveAdditionalData" })
	@Transactional
	public String saveAdditionalDataSupersessionProposal(WebRequest request,
			@PathVariable("uuid") UUID itemUuid, 
			final Model model,
			@RequestParam Map<String, String> additionalData) {

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
		
		return "registry/proposal/create_supersession";
		
	}

	@RequestMapping(value = "/{uuid}/supersede", params={ "submit" })
	@Transactional
	public View submitSupersessionProposal(WebRequest request,
			@PathVariable("uuid") UUID itemUuid, 
			final Model model) throws InvalidProposalException, IllegalOperationException {

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
		
		proposalService.createSupersession(supersededItems, state.getNewSupersedingItems(), 
				state.getJustification(), state.getRegisterManagerNotes(), state.getControlBodyNotes(), state.getSponsor());

		return new BasePathRedirectView("/management/submitter");
	}
	

	@RequestMapping(value = "/{uuid}/clarify", method = RequestMethod.GET)
	@Transactional
	public String createClarificationProposal(@PathVariable("uuid") UUID itemUuid, final Model model) throws ItemNotFoundException, UnauthorizedException {
		security.assertHasRoleWith(SUBMITTER_ROLE_PREFIX);
		
		model.addAttribute("isNew", "true");
		model.addAttribute("isProposal", "true");

		RE_RegisterItem clarifiedItem = itemService.findOne(itemUuid);
		if (clarifiedItem == null) {
			throw new ItemNotFoundException(itemUuid);
		}
		RE_ItemClass itemClass = clarifiedItem.getItemClass();
		
		RegisterItemProposalDTO proposal = proposalDtoFactory.getProposalDto(itemClass);
		if (proposal.getClass().getCanonicalName().equals(RegisterItemProposalDTO.class.getCanonicalName())) {
			model.addAttribute("itemClassNotConfigured", "true");
		}

		proposal.setItemUuid(itemUuid);
		proposal.setItemClassUuid(itemClass.getUuid());

		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		
		proposal.setProposalType(ProposalType.CLARIFICATION);
		proposal.setSponsorUuid(suborg.getUuid());
		
		proposal.setName(clarifiedItem.getName());
		proposal.setDefinition(clarifiedItem.getDefinition());
		proposal.setDescription(clarifiedItem.getDescription());
		
		proposal.loadAdditionalValues(clarifiedItem);
		
		ItemClassConfiguration itemClassConfiguration = itemClassRegistry.getConfiguration(itemClass.getName());
		if (itemClassConfiguration != null) {
			model.addAttribute("itemClassConfiguration", itemClassConfiguration);
		}

		model.addAttribute("itemClass", itemClass.getUuid().toString());
		model.addAttribute("itemClassName", itemClass.getName());
		model.addAttribute("proposal", proposal);
		model.addAttribute("register", clarifiedItem.getRegister());

		String viewName;
		if (itemClassConfiguration != null && !StringUtils.isEmpty(itemClassConfiguration.getCreateProposalTemplate())) {
			viewName = itemClassConfiguration.getCreateProposalTemplate();
		}
		else {
			viewName = "registry/proposal/create_clarification";
		}

		return viewName;
	}

	@RequestMapping(value = "/{uuid}/clarify", method = RequestMethod.POST)
	@Transactional
	public View proposeItemClarification(final ServletRequest servletRequest,
			@PathVariable("uuid") UUID itemUuid,
			@ModelAttribute("proposal") final RegisterItemProposalDTO proposal, 
			@RequestParam Map<String, String> allParams,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) throws IllegalOperationException, ItemNotFoundException {

		boolean submitProposal = !allParams.containsKey("saveProposal");

		RE_RegisterItem item = itemService.findOne(itemUuid);
		if (item == null) {
			throw new ItemNotFoundException(itemUuid);
		}
		
		if (!item.isValid()) {
			throw new IllegalOperationException(String.format("Cannot retire item with status %s", item.getStatus().name()));
		}
		
		RE_SubmittingOrganization suborg = suborgRepository.findOne(proposal.getSponsorUuid());
		
		RegisterItemProposalDTO dto = proposalDtoFactory.getProposalDto(item.getItemClass());
		dto = bindAdditionalAttributes(dto, servletRequest); new Object();

		Clarification clarification = proposalService.createClarification(item, dto.calculateProposedChanges(item), dto.getJustification(), 
				dto.getRegisterManagerNotes(), dto.getControlBodyNotes(), suborg);
		if (submitProposal) {
			proposalService.submitProposal(clarification);
		}
		
		return new BasePathRedirectView("/management/submitter");
	}

	public static class ProposalManagementInformationEvaluator {
		public ProposalType evaluate(RE_ProposalManagementInformation pmi) {
			if (RE_AdditionInformation.class.isAssignableFrom(pmi.getClass())) {
				return ProposalType.ADDITION;
			}
			else if (RE_ClarificationInformation.class.isAssignableFrom(pmi.getClass())) {
				return ProposalType.CLARIFICATION;
			}
			else if (RE_AmendmentInformation.class.isAssignableFrom(pmi.getClass())) {
				switch (((RE_AmendmentInformation)pmi).getAmendmentType()) {
					case SUPERSESSION:
						return ProposalType.SUPERSESSION;
					case RETIREMENT:
						return ProposalType.RETIREMENT;
					default:
						return null;
				}
			}
			else {
				return null;
			}
		}
	}
	
	protected RegisterItemProposalDTO bindAdditionalAttributes(RegisterItemProposalDTO proposal, ServletRequest servletRequest) {
		RE_ItemClass selectedItemClass = itemClassRepository.findOne(proposal.getItemClassUuid());
		ItemClassConfiguration itemClassConfiguration = itemClassRegistry.getConfiguration(selectedItemClass.getName());
		
		if (itemClassConfiguration != null) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends RegisterItemProposalDTO> dtoClass = 
						(Class<? extends RegisterItemProposalDTO>)this.getClass().getClassLoader().loadClass(itemClassConfiguration.getDtoClass());
				proposal = BeanUtils.instantiateClass(dtoClass);
				ServletRequestDataBinder binder = new ServletRequestDataBinder(proposal); 
				binder.setConversionService(conversionService);
				binder.bind(servletRequest);
				
//				proposal.setItemClassUuid(UUID.fromString(itemClassUuid));
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return proposal;
	}	

	public String createProposal(RE_Register register,
			String itemClassUuid,
			RegisterItemProposalDTO proposal, final Model model,
			final RedirectAttributes redirectAttributes) throws UnauthorizedException {

		security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, register);

		model.addAttribute("register", register);

		Collection<RE_ItemClass> itemClasses = register.getContainedItemClasses();
		if (itemClasses.size() == 1) {
			itemClassUuid = itemClasses.toArray(new RE_ItemClass[] {})[0].getUuid().toString();
			model.addAttribute("itemClassUuid", itemClassUuid);
		}
		model.addAttribute("itemClasses", itemClasses);

		ItemClassConfiguration itemClassConfiguration = null;
		if (!StringUtils.isEmpty(itemClassUuid)) {
			RE_ItemClass selectedItemClass = null;
			for (RE_ItemClass itemClass : itemClasses) {
				if (itemClass.getUuid().toString().toLowerCase().equals(itemClassUuid.toLowerCase())) {
					selectedItemClass = itemClass;
					break;
				}
			}

			if (selectedItemClass == null) {
				throw new IllegalArgumentException(String.format("Register %s does not contain item class %s",
						register.getName(), itemClassUuid));
			}

			itemClassConfiguration = itemClassRegistry.getConfiguration(selectedItemClass.getName());
			if (itemClassConfiguration != null) {
				model.addAttribute("itemClassConfiguration", itemClassConfiguration);
			}

			proposal = proposalDtoFactory.getProposalDto(selectedItemClass);
			if (proposal.getClass().getCanonicalName().equals(RegisterItemProposalDTO.class.getCanonicalName())) {
				model.addAttribute("itemClassNotConfigured", "true");
			}

			proposal.setItemClassUuid(UUID.fromString(itemClassUuid));
			model.addAttribute("itemClass", selectedItemClass.getUuid().toString());
			model.addAttribute("itemClassName", selectedItemClass.getName());
		}

		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);

		proposal.setProposalType(ProposalType.ADDITION);
		proposal.setSponsorUuid(suborg.getUuid());
		proposal.setTargetRegisterUuid(register.getUuid());

		model.addAttribute("proposal", proposal);
		model.addAttribute("isProposal", "true");

		String viewName;
		if (itemClassConfiguration != null && !StringUtils.isEmpty(itemClassConfiguration.getCreateProposalTemplate())) {
			viewName = itemClassConfiguration.getCreateProposalTemplate();
		}
		else {
			viewName = "registry/proposal/create_addition";
		}

		return viewName;
	}
}
