/**
 * 
 */
package org.iso.registry.client.controller.registry;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.iso.registry.client.controller.DatatablesResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.EntityNotFoundException;
import de.geoinfoffm.registry.api.ItemNotFoundException;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.client.web.BasePathRedirectView;
import de.geoinfoffm.registry.client.web.ProposalDtoFactory;
import de.geoinfoffm.registry.client.web.RegisterItemViewBean;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.ItemClassConfiguration;
import de.geoinfoffm.registry.core.ItemClassRegistry;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.ProposalType;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemClass;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.persistence.ItemClassRepository;
import de.geoinfoffm.registry.persistence.ProposalRepository;
import de.geoinfoffm.registry.persistence.RegisterItemRepository;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import de.geoinfoffm.registry.persistence.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.persistence.SupersessionRepository;
import de.geoinfoffm.registry.persistence.xml.exceptions.XmlSerializationException;

/**
 * Controller for the register item proposals.
 * 
 * @author Florian Esser
 *
 */
@Controller
@RequestMapping("/register")
public class RegisterController
{
	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	private RegisterItemService itemService;
	
	@Autowired
	private RegisterItemRepository itemRepository;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private RegisterRepository registerRepository;

	@Autowired
	private ProposalRepository proposalRepository;

	@Autowired
	private ProposalManagementInformationRepository pmiRepository;

	@Autowired
	private SupersessionRepository supersessionRepository;

	@Autowired
	private SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private ItemClassRepository itemClassRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private ViewResolver viewResolver;
	
	@Autowired
	private ItemClassRegistry itemClassRegistry;
	
	@Autowired
	private ProposalDtoFactory proposalDtoFactory;
	
	@Autowired
	private ConversionService conversionService;
	
	@Autowired
	private MessageSource messageSource;
	
	private Map<String, RE_ItemClass> itemClassCache = new HashMap<>();
	
//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(new ProposalValidator(itemService));
//	}

	@RequestMapping(value = "/{register}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String registerOverview(@PathVariable("register") String registerName,
							       final Model model, final RedirectAttributes redirectAttributes, Pageable pageable) {
		return registerOverview(registerName, (String)null, model, redirectAttributes, pageable);
	}

	@RequestMapping(value = "/{register}/containedItems", method = RequestMethod.GET)
	public @ResponseBody DatatablesResult getRegisterContainedItems(@PathVariable("register") String registerName, 
																	@RequestParam Map<String, String> parameters) {
		RE_Register register = findRegister(registerName);
		
		String sEcho = parameters.get("sEcho");
		String iDisplayStart = parameters.get("iDisplayStart");
		String iDisplayLength = parameters.get("iDisplayLength");
		String iColumns = parameters.get("iColumns");
		String iSortingsCols = parameters.get("iSortingCols");
		String sSearch = parameters.get("sSearch");
		
		Map<Integer, String> columns = new HashMap<Integer, String>();
		if (iColumns != null) {
			int columnCount = Integer.parseInt(iColumns);
			for (int i = 0; i < columnCount; i++) {
				columns.put(i, parameters.get("mDataProp_" + Integer.toString(i)));
			}
		}

		Map<String, String> sortingColumns = new HashMap<String, String>();
		if (iSortingsCols != null) {
			int sortingCols = Integer.parseInt(iSortingsCols);
			for (int i = 0; i < sortingCols; i++) {
				String sortingCol = parameters.get("iSortCol_" + Integer.toString(i));
				String direction = parameters.get("sSortDir_" + Integer.toString(i));
				sortingColumns.put(columns.get(Integer.parseInt(sortingCol)), direction);
			}
		}
		
		Pageable pageable;
		if (iDisplayStart != null && iDisplayLength != null) {
			int startAt = Integer.parseInt(iDisplayStart);
			int length = Integer.parseInt(iDisplayLength);
			int pageNo = startAt / length;
			
			Sort sort;
			if (!sortingColumns.isEmpty()) {
				List<Order> orders = new ArrayList<Order>();
				for (String property : sortingColumns.keySet()) {
					Order order = new Order(Direction.fromString(sortingColumns.get(property).toString().toUpperCase()), property);
					orders.add(order);
				}
				sort = new Sort(orders);
			}
			else {
				sort = new Sort(new Order("itemIdentifier"));
			}
			
			pageable = new PageRequest(pageNo, length, sort);
		}
		else {
			pageable = new PageRequest(0, 10);
		}
		
		List<RE_ItemClass> itemClasses = new ArrayList<>();
		if (parameters.containsKey("itemClass") && !StringUtils.isEmpty(parameters.get("itemClass")) && !"null".equalsIgnoreCase(parameters.get("itemClass")) && !parameters.get("itemClass").contains("[[")) {
			UUID itemClassUuid = UUID.fromString(parameters.get("itemClass"));
			itemClasses.add(itemClassRepository.findOne(itemClassUuid));
		}
		else if (parameters.containsKey("itemClassFilter") && !StringUtils.isEmpty(parameters.get("itemClassFilter")) && !"null".equalsIgnoreCase(parameters.get("itemClassFilter"))) {
			String itemClassParam = parameters.get("itemClassFilter");
			if (itemClassParam.equalsIgnoreCase("crs")) {
				addItemClassToList("GeodeticCRS", itemClasses);
				addItemClassToList("EngineeringCRS", itemClasses);
				addItemClassToList("CompoundCRS", itemClasses);
				addItemClassToList("VerticalCRS", itemClasses);
				addItemClassToList("ProjectedCRS", itemClasses);
			}
			if (itemClassParam.equalsIgnoreCase("cs")) {
				addItemClassToList("CartesianCS", itemClasses);
				addItemClassToList("EllipsoidalCS", itemClasses);
				addItemClassToList("VerticalCS", itemClasses);
			}
			if (itemClassParam.equalsIgnoreCase("datums")) {
				addItemClassToList("GeodeticDatum", itemClasses);
				addItemClassToList("VerticalDatum", itemClasses);
			}
			if (itemClassParam.equalsIgnoreCase("operations")) {
				addItemClassToList("ConcatenatedOperation", itemClasses);
				addItemClassToList("Conversion", itemClasses);
				addItemClassToList("Transformation", itemClasses);
			}
		}
		
		Page<RE_RegisterItem> items;
		if (itemClasses.isEmpty()) { 
			if (!StringUtils.isEmpty(sSearch)) {
				items = itemRepository.findByRegisterAndStatusInFiltered(register, Arrays.asList(RE_ItemStatus.VALID, RE_ItemStatus.SUPERSEDED, RE_ItemStatus.RETIRED), "%" + sSearch + "%", pageable);
			}
			else {
				items = itemRepository.findByRegisterAndStatusIn(register, Arrays.asList(RE_ItemStatus.VALID, RE_ItemStatus.SUPERSEDED, RE_ItemStatus.RETIRED), pageable);
			}
		}
		else {
			if (!StringUtils.isEmpty(sSearch)) {
				items = itemRepository.findByRegisterAndItemClassInAndStatusInFiltered(register, itemClasses, Arrays.asList(RE_ItemStatus.VALID, RE_ItemStatus.SUPERSEDED, RE_ItemStatus.RETIRED), "%" + sSearch + "%", pageable);
			}
			else {
				items = itemRepository.findByRegisterAndItemClassInAndStatusIn(register, itemClasses, Arrays.asList(RE_ItemStatus.VALID, RE_ItemStatus.SUPERSEDED, RE_ItemStatus.RETIRED), pageable);				
			}
		}
		List<RegisterItemViewBean> viewBeans = new ArrayList<RegisterItemViewBean>();
		for (RE_RegisterItem item : items.getContent()) {
			RegisterItemViewBean rvb = new RegisterItemViewBean(item, false);
			rvb.setStatus(messageSource.getMessage(item.getStatus().toString(), null, LocaleContextHolder.getLocale()));
			rvb.setItemClassName(messageSource.getMessage(item.getItemClass().getName().toString(), null, LocaleContextHolder.getLocale()));
			viewBeans.add(rvb);
		}
		
		return new DatatablesResult(items.getTotalElements(), items.getTotalElements(), sEcho, viewBeans);
	}

	public void addItemClassToList(String name, List<RE_ItemClass> itemClasses) {
		if (itemClassCache.containsKey(name)) {
			itemClasses.add(itemClassCache.get(name));
		}
		else {
			RE_ItemClass ic = itemClassRepository.findByName(name);
			if (ic != null) {
				itemClassCache.put(name, ic);
				itemClasses.add(ic);
			}
		}
	}

	@RequestMapping(value = "/{register}/{itemClassText}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public String registerOverview(@PathVariable("register") String registerName, 
								   @PathVariable("itemClassText") String itemClassFilter,
								   final Model model, final RedirectAttributes redirectAttributes, final Pageable pageable) {
		RE_Register register = findRegister(registerName);
		if (register == null) {
			redirectAttributes.addFlashAttribute("registerName", registerName);
			return "registry/register_notfound";
		}
		model.addAttribute("register", register);
		
		RE_ItemClass itemClass = null;
		if (itemClassFilter != null) {
			if (itemClassFilter.contains("-")) {
				UUID itemClassUuid = UUID.fromString(itemClassFilter);
				itemClass = itemClassRepository.findOne(itemClassUuid);
				model.addAttribute("itemClass", itemClass);
				model.addAttribute("pageTitle", messageSource.getMessage(itemClass.getName(), new Object[] { }, LocaleContextHolder.getLocale()));
			}
			else {
				model.addAttribute("itemClassFilter", itemClassFilter);
				if (itemClassFilter.equalsIgnoreCase("crs")) {
					model.addAttribute("pageTitle", "CRS");
				}
				else if (itemClassFilter.equalsIgnoreCase("cs")) {
					model.addAttribute("pageTitle", "Coordinate Systems");
				}
				else if (itemClassFilter.equalsIgnoreCase("datums")) {
					model.addAttribute("pageTitle", "Geodetic Datums");
				}
				else if (itemClassFilter.equalsIgnoreCase("operations")) {
					model.addAttribute("pageTitle", "Coordinate Operations");
				}
			}
		}
		
		
//		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
//		RE_SubmittingOrganization suborg = null;
//		Collection<Proposal> proposals = proposalRepository.findByDateSubmittedIsNotNull();
		if (security.isLoggedIn()) {
//			Collection<Proposal> proposals = proposalRepository.findByDateSubmittedIsNotNull();
//			List<RegisterItemViewBean> proposedItemViewBeans = new ArrayList<RegisterItemViewBean>();
//			for (Proposal proposal : proposals) {
//				if(!security.may(BasePermission.READ, proposal)) {
//					continue;
//				}
//
//				if (proposal.getStatus().equals(RE_DecisionStatus.FINAL)) continue;
//				if (proposal.hasGroup()) continue;
//				
//				if (proposal.isContainedIn(register)) {
//					Appeal appeal = proposalService.findAppeal(proposal);
//					if (appeal != null) {
//						proposedItemViewBeans.add(new RegisterItemViewBean(appeal));
//					}
//					else {
//						proposedItemViewBeans.add(new RegisterItemViewBean(proposal));
//					}
//				}
//			}
//			
//			model.addAttribute("proposedItems", proposedItemViewBeans);
		}

		if (!model.containsAttribute("viewMode")) {
			model.addAttribute("viewMode", "contents");
		}
		return "registry/register";
	}

	private RE_Register findRegister(String registerName) {
		RE_Register register = registerRepository.findByName(registerName);
		
		if (register == null) {
			try {
				register = registerRepository.findOne(UUID.fromString(registerName));
			}
			catch (IllegalArgumentException e) {
				// Ignore
			}
		}
		return register;
	}

	@RequestMapping(value = "/{register}/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> registerOverviewAsXml(@PathVariable("register") String registerName, final Model model) throws XmlSerializationException {
		RE_Register register = findRegister(registerName);
		if (register == null) {
			throw new EntityNotFoundException(String.format("Register %s does not exist", registerName));
		}
		
		StringWriter sw = new StringWriter();
		registerService.toXml(register, sw);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/xml; charset=utf-8");
		return new ResponseEntity<String>(sw.toString(), responseHeaders, HttpStatus.OK);
	}

	@RequestMapping(value = "/{register}/proposals", method = RequestMethod.GET)
	@Transactional
	public String registerOverviewProposals(@PathVariable("register") String registerName, final Model model, final RedirectAttributes redirectAttributes, final Pageable pageable) {
		model.addAttribute("viewMode", "proposals");
		return registerOverview(registerName, (String)null, model, redirectAttributes, pageable);
	}


	/**
	 * Displays an empty view and allows for the creation of a new user.
	 * 
	 * @param user The view-backing {@link RegisterItemProposalDTO} object
	 * @param model The view model
	 * @return The name of the view used
	 * @throws UnauthorizedException 
	 */
	@RequestMapping(value = "/{register}/proposal/addition", method = RequestMethod.GET)
	@Transactional
	public String createProposal(@PathVariable("register") String registerName,
								 @RequestParam(value = "itemClass", required = false) String itemClassUuid,
								 @ModelAttribute("proposal") RegisterItemProposalDTO proposal, 
								 final Model model,
								 final RedirectAttributes redirectAttributes) throws UnauthorizedException {

		RE_Register register = findRegister(registerName);
		if (register == null) {
			redirectAttributes.addFlashAttribute("registerName", registerName);
			return "registry/register_notfound";
		}
		
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
				throw new IllegalArgumentException(String.format("Register %s does not contain item class %s", registerName, itemClassUuid)); 
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

		RE_SubmittingOrganization suborg = suborgRepository.findAll().get(0);
		
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
	
	@RequestMapping(value = "/{register}/proposal/supersession", method = RequestMethod.GET)
	@Transactional
	public String createSupersessionProposal(WebRequest request,
			@PathVariable("register") String registerName, 
			final Model model,
			final RedirectAttributes redirectAttributes) throws ItemNotFoundException, UnauthorizedException {

		RE_Register register = findRegister(registerName);
		if (register == null) {
			redirectAttributes.addFlashAttribute("registerName", registerName);
			return "registry/register_notfound";
		}
		
		security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, register);
		
//		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		RE_SubmittingOrganization suborg = suborgRepository.findAll().get(0);
		SupersessionState state = new SupersessionState(register, suborg, itemService);
		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);

		model.addAttribute("state", state);

		Collection<RE_ItemClass> itemClasses = register.getContainedItemClasses();
		itemClasses.size();
		model.addAttribute("itemClasses", itemClasses);

		return "registry/proposal/create_supersession";
	}

	@RequestMapping(value = "/{register}/proposal/supersession", params={ "addSupersededItems" })
	@Transactional
	public String addSupersededItemsToSupersession(WebRequest request,
			@PathVariable("register") String registerName,
			final Model model, 
			@RequestParam Map<String, String> supersededItems,
			final RedirectAttributes redirectAttributes) throws InvalidProposalException, IllegalOperationException {

		RE_Register register = findRegister(registerName);
		if (register == null) {
			redirectAttributes.addFlashAttribute("registerName", registerName);
			return "registry/register_notfound";
		}

		addSupersededItemsToSupersession(request, model, supersededItems, itemService);

		Collection<RE_ItemClass> itemClasses = register.getContainedItemClasses();
		itemClasses.size();
		model.addAttribute("itemClasses", itemClasses);

		return "registry/proposal/create_supersession";
	}

	public static void addSupersededItemsToSupersession(WebRequest request,
			final Model model, Map<String, String> supersededItems, RegisterItemService itemService)
			throws InvalidProposalException {
		
		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
		
		state.removeAllSupersededItems();

		for (String supersededItemUuid : supersededItems.keySet()) {
			if (!supersededItemUuid.startsWith("supersede_")) continue;
			
			RE_RegisterItem supersededItem = itemService.findOne(UUID.fromString(supersededItemUuid.substring(10)));
			if (supersededItem == null) {
				throw new InvalidProposalException(String.format("Superseded item %s does not exist", supersededItemUuid));
			}
			state.addSupersededItem(supersededItem);
		}
		
		if (state.getSupersededItems().isEmpty()) {
			model.addAttribute("noSupersededItems", "true");
		}
		else {
			state.setStep("supersedingItems");
		}

		request.setAttribute("supersession", state, WebRequest.SCOPE_SESSION);
		model.addAttribute("state", state);
	}

	@RequestMapping(value = "/{register}/proposal/supersession", method = RequestMethod.POST, params={ "addNew" })
	@Transactional
	public String addNewItemToProposalPost(WebRequest request,
			@PathVariable("register") String registerName,
			@RequestParam(value = "itemClass", required = false) String itemClassUuid,
			final Model model,
			final RedirectAttributes redirectAttributes) throws UnauthorizedException {
		
		return addNewItemToProposal(request, registerName, itemClassUuid, model, redirectAttributes);
	}

	@RequestMapping(value = "/{register}/proposal/supersession", params={ "addNew" })
	@Transactional
	public String addNewItemToProposal(WebRequest request,
			@PathVariable("register") String registerName,
			@RequestParam(value = "itemClass", required = false) String itemClassUuid,
			final Model model,
			final RedirectAttributes redirectAttributes) throws UnauthorizedException { 
		
		SupersessionState state = (SupersessionState)request.getAttribute("supersession", WebRequest.SCOPE_SESSION);
		if (state == null) {
			throw new IllegalStateException("State not initialized");
		}
		
		RE_Register targetRegister = findRegister(registerName);
		if (targetRegister == null) {
			redirectAttributes.addFlashAttribute("registerName", registerName);
			return "registry/register_notfound";
		}
		model.addAttribute("register", targetRegister);
		
//		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		RE_SubmittingOrganization suborg = suborgRepository.findAll().get(0);

		model.addAttribute("isNew", "true");

		RegisterItemProposalDTO newItem = new RegisterItemProposalDTO();
		newItem.setProposalType(ProposalType.ADDITION);		
		newItem.setSponsorUuid(suborg.getUuid());
		newItem.setTargetRegisterUuid(targetRegister.getUuid());

		model.addAttribute("proposal", newItem);
		model.addAttribute("partOfSupersession", "true");
		
		return createProposal(registerName, itemClassUuid, newItem, model, redirectAttributes);
	}

	@RequestMapping(value = "/{register}/proposal/supersession", params={ "save" })
	@Transactional
	public String saveNewItem(WebRequest request, HttpServletRequest servletRequest,
			@PathVariable("register") String registerName,
			@ModelAttribute("proposal") RegisterItemProposalDTO proposal,
			final Model model) {

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

	@RequestMapping(value = "/{register}/proposal/supersession", params={ "saveSupersedingItems" })
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
			

	@RequestMapping(value = "/{register}/proposal/supersession", params={ "saveAdditionalData" })
	@Transactional
	public String saveAdditionalDataSupersessionProposal(WebRequest request,
			@PathVariable("register") String registerName, 
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

	@RequestMapping(value = "/{register}/proposal/supersession", params={ "submit" })
	@Transactional
	public View submitSupersessionProposal(WebRequest request,
			@PathVariable("register") String registerName,
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
		
		proposalService.proposeSupersession(supersededItems, state.getNewSupersedingItems(), 
				state.getJustification(), state.getRegisterManagerNotes(), state.getControlBodyNotes(), state.getSponsor());

		return new BasePathRedirectView("/management/submitter");
//		return new BasePathRedirectView("/register/" + registerName + "/proposals");
//		return "redirect:/register/" + registerName + "/proposals";
	}
	
	@RequestMapping(value = "/{register}/proposal/addition", method = RequestMethod.POST)
	public String submitProposal(WebRequest request, ServletRequest servletRequest, @PathVariable("register") String registerName, 
			@Valid @ModelAttribute("proposal") RegisterItemProposalDTO proposal,
			@RequestParam Map<String, String> allParams,
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) throws Exception {

		boolean submitProposal = !allParams.containsKey("saveProposal");

		RE_Register register = findRegister(registerName);
		if (register == null) {
			redirectAttributes.addFlashAttribute("registerName", registerName);
			return "registry/register_notfound";
		}
		
		security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, register);

		model.addAttribute("register", register);

		if (bindingResult.hasErrors()) {
			model.addAttribute("isNew", "true");
			
			Collection<RE_ItemClass> itemClasses = register.getContainedItemClasses();
			model.addAttribute("itemClasses", itemClasses);
			return "proposal";
		}
		
		proposal = bindAdditionalAttributes(proposal, servletRequest);
		
		Addition addition = proposalService.createAdditionProposal(proposal);
		
		if (submitProposal) {
			proposalService.submitProposal(addition);
		}
		
		redirectAttributes.addFlashAttribute("createdItem", addition.getItem().getUuid().toString());
		
		return "redirect:/management/submitter";
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

	
	public static class SupersessionState {
		private String step;
//		private Set<RegisterItemViewBean> validItems;
		private RE_SubmittingOrganization sponsor;
		private final Set<RegisterItemProposalDTO> newSupersedingItems = new HashSet<RegisterItemProposalDTO>();
		private final Set<RegisterItemViewBean> existingSupersedingItems = new HashSet<RegisterItemViewBean>();
		private final Set<RegisterItemViewBean> supersededItems = new HashSet<RegisterItemViewBean>();
		private String justification;
		private String registerManagerNotes;
		private String controlBodyNotes;
		
		public SupersessionState(RE_Register register, RE_SubmittingOrganization sponsor, RegisterItemService itemService) {
			this.sponsor = sponsor;
			
//			step = "supersededItems";
			step = "supersedingItems";
			
//			validItems = new HashSet<RegisterItemViewBean>();			
//			Set<RE_RegisterItem> validItemsDb = itemService.findByRegisterAndStatus(register, RE_ItemStatus.VALID);
//			for (RE_RegisterItem validItem : validItemsDb) {
//				validItems.add(new RegisterItemViewBean(validItem));
//			}
		}
		
		public SupersessionState(Supersession proposal, RegisterItemService itemService) {
			this.sponsor = proposal.getSponsor();
//			step = "supersededItems";
			step = "supersedingItems";
		
//			validItems = new HashSet<RegisterItemViewBean>();			
//			Set<RE_RegisterItem> validItemsDb = itemService.findByRegisterAndStatus(proposal.getTargetRegister(), RE_ItemStatus.VALID);
//			for (RE_RegisterItem validItem : validItemsDb) {
//				validItems.add(new RegisterItemViewBean(validItem));
//			}
			
			for (RE_RegisterItem supersededItem : proposal.getSupersededItems()) {
				this.addSupersededItem(supersededItem);
			}
			for (RE_RegisterItem supersedingItem : proposal.getSupersedingItems()) {
				this.addExistingSupersedingItem(supersedingItem);
			}
			
			this.justification = proposal.getJustification();
			this.registerManagerNotes = proposal.getRegisterManagerNotes();
			this.controlBodyNotes = proposal.getControlBodyNotes();
		}

		/**
		 * @return the step
		 */
		public String getStep() {
			return step;
		}

		/**
		 * @param step the step to set
		 */
		public void setStep(String step) {
			this.step = step;
		}

//		/**
//		 * @return the validItems
//		 */
//		public Set<RegisterItemViewBean> getValidItems() {
//			return validItems;
//		}
//
//		/**
//		 * @param validItems the validItems to set
//		 */
//		public void setValidItems(Set<RegisterItemViewBean> validItems) {
//			this.validItems = validItems;
//		}

		/**
		 * @return the sponsor
		 */
		public RE_SubmittingOrganization getSponsor() {
			return sponsor;
		}

		/**
		 * @param sponsor the sponsor to set
		 */
		public void setSponsor(RE_SubmittingOrganization sponsor) {
			this.sponsor = sponsor;
		}

		/**
		 * @return the newSupersedingItems
		 */
		public Set<RegisterItemProposalDTO> getNewSupersedingItems() {
			return Collections.unmodifiableSet(newSupersedingItems);
		}
		
		public void addNewSupersedingItem(RegisterItemProposalDTO newItem) {
			this.newSupersedingItems.add(newItem);
		}
		
		public Set<RegisterItemViewBean> getExistingSupersedingItems() {
			return Collections.unmodifiableSet(existingSupersedingItems);
		}

		public void addExistingSupersedingItem(RE_RegisterItem existingItem) {
			this.existingSupersedingItems.add(new RegisterItemViewBean(existingItem));
		}
		
		public void removeSupersedingItem(UUID uuid) {
			for (RegisterItemProposalDTO newItem : this.newSupersedingItems) {
				if (newItem.getItemUuid().equals(uuid)) {
					this.newSupersedingItems.remove(newItem);
					return;
				}
			}
			
			for (RegisterItemViewBean existingItem : this.existingSupersedingItems) {
				if (existingItem.getUuid().equals(uuid)) {
					this.existingSupersedingItems.remove(existingItem);
					return;
				}
			}
		}
		
		public void removeAllSupersededItems() {
			this.supersededItems.clear();
		}
		
		/**
		 * @return the supersededItems
		 */
		public Set<RegisterItemViewBean> getSupersededItems() {
			return Collections.unmodifiableSet(supersededItems);
		}
		
		public List<UUID> getSupersededItemUuids() {
			List<UUID> result = new ArrayList<UUID>();
			for (RegisterItemViewBean rvb : supersededItems) {
				result.add(rvb.getUuid());
			}
			
			return Collections.unmodifiableList(result);
		}
		
		public void addSupersededItem(RE_RegisterItem supersededItem) {
			if (!this.getSupersededItemUuids().contains(supersededItem.getUuid())) {
				supersededItems.add(new RegisterItemViewBean(supersededItem));
			}
		}
		
		public void removeSupersededItem(UUID uuid) {
			supersededItems.remove(uuid);
		}

		/**
		 * @return the justification
		 */
		public String getJustification() {
			return justification;
		}

		/**
		 * @param justification the justification to set
		 */
		public void setJustification(String justification) {
			this.justification = justification;
		}

		/**
		 * @return the registerManagerNotes
		 */
		public String getRegisterManagerNotes() {
			return registerManagerNotes;
		}

		/**
		 * @param registerManagerNotes the registerManagerNotes to set
		 */
		public void setRegisterManagerNotes(String registerManagerNotes) {
			this.registerManagerNotes = registerManagerNotes;
		}

		/**
		 * @return the controlBodyNotes
		 */
		public String getControlBodyNotes() {
			return controlBodyNotes;
		}

		/**
		 * @param controlBodyNotes the controlBodyNotes to set
		 */
		public void setControlBodyNotes(String controlBodyNotes) {
			this.controlBodyNotes = controlBodyNotes;
		}
	}
}
