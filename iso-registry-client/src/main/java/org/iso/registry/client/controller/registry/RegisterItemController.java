/**
 * 
 */
package org.iso.registry.client.controller.registry;

import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.iso.registry.client.RegisterItemViewBean;
import org.iso.registry.client.ViewBeanFactory;
import org.iso.registry.client.configuration.BasePathRedirectView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.geoinfoffm.registry.api.ItemNotFoundException;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO.ProposalType;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalFactory;
import de.geoinfoffm.registry.core.model.RegistryUserRepository;
import de.geoinfoffm.registry.core.model.iso19135.InvalidProposalException;
import de.geoinfoffm.registry.core.model.iso19135.ProposalManagementInformationRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.persistence.SubmittingOrganizationRepository;
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
	private ProposalManagementInformationRepository pmiRepository;
	
	@Autowired
	private SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private RegistryUserRepository userRepository;
	
	@Autowired
	private ProposalFactory proposalFactory;

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
			viewBean = ViewBeanFactory.getInstance().getViewBean(proposal);
		}
		else {
			viewBean = ViewBeanFactory.getInstance().getViewBean(item);
		}


		model.addAttribute("item", viewBean);
		model.addAttribute("pmiEvaluator", new ProposalManagementInformationEvaluator());
		
		return "registry/item";
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

	@RequestMapping(value = "/{uuid}/retire", method = RequestMethod.GET)
	@Transactional
	public View proposeItemRetirement(@PathVariable("uuid") UUID itemUuid,
													  @RequestParam(value = "justification", required = true) String justification) throws IllegalOperationException, ItemNotFoundException {
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
		
//		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		RE_SubmittingOrganization suborg = null;

		itemService.proposeRetirement(item, justification, suborg);

		return new BasePathRedirectView("/");
	}
	
	@RequestMapping(value = "/{uuid}/clarify", method = RequestMethod.GET)
	@Transactional
	public String createClarificationProposal(@PathVariable("uuid") UUID itemUuid, @ModelAttribute("proposal") RegisterItemProposalDTO proposal, final Model model) throws ItemNotFoundException {
		model.addAttribute("isNew", "true");
		
		RE_RegisterItem clarifiedItem = itemService.findOne(itemUuid);
		if (clarifiedItem == null) {
			throw new ItemNotFoundException(itemUuid);
		}
		proposal.setItemUuid(itemUuid);
		
//		RE_SubmittingOrganization suborg = RegistryUserUtils.getUserSponsor(userRepository);
		RE_SubmittingOrganization suborg = null;
		
		proposal.setProposalType(ProposalType.CLARIFICATION);
		proposal.setSponsorUuid(suborg.getUuid());
		
		proposal.setName(clarifiedItem.getName());
		proposal.setDefinition(clarifiedItem.getDefinition());
		proposal.setDescription(clarifiedItem.getDescription());
		
		return "registry/proposal/create_clarification";
	}

	@RequestMapping(value = "/{uuid}/clarify", method = RequestMethod.POST)
	@Transactional
	public View proposeItemClarification(
			@PathVariable("uuid") UUID itemUuid,
			@ModelAttribute("proposal") final RegisterItemProposalDTO proposal, 
			final BindingResult bindingResult, final Model model, final RedirectAttributes redirectAttributes) throws IllegalOperationException, ItemNotFoundException {
		
		RE_RegisterItem item = itemService.findOne(itemUuid);
		if (item == null) {
			throw new ItemNotFoundException(itemUuid);
		}
		
		if (!item.isValid()) {
			throw new IllegalOperationException(String.format("Cannot retire item with status %s", item.getStatus().name()));
		}
		
		RE_SubmittingOrganization suborg = suborgRepository.findOne(proposal.getSponsorUuid());

		itemService.proposeClarification(item, proposal.calculateProposedChanges(item), proposal.getJustification(), suborg);
		
		return new BasePathRedirectView("/");
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
}
