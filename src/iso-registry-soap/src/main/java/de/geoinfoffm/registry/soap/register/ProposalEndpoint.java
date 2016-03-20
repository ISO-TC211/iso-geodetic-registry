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

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hibernate.SessionFactory;
import org.isotc211.iso19135.RE_RegisterItem_PropertyType;
import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.isotc211.iso19139.common.CharacterString_PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.Namespaces;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import de.geoinfoffm.registry.api.ProposalDtoFactory;
import de.geoinfoffm.registry.api.ProposalService;
import de.geoinfoffm.registry.api.RegisterItemProposalDTO;
import de.geoinfoffm.registry.api.RegisterItemService;
import de.geoinfoffm.registry.api.RegisterService;
import de.geoinfoffm.registry.api.soap.AbstractProposal_Type;
import de.geoinfoffm.registry.api.soap.AbstractRegisterItemProposal_Type;
import de.geoinfoffm.registry.api.soap.AcceptAppealRequest;
import de.geoinfoffm.registry.api.soap.AcceptAppealResponse;
import de.geoinfoffm.registry.api.soap.AcceptProposalRequest;
import de.geoinfoffm.registry.api.soap.AcceptProposalResponse;
import de.geoinfoffm.registry.api.soap.Addition_Type;
import de.geoinfoffm.registry.api.soap.AppealDisposition_PropertyType;
import de.geoinfoffm.registry.api.soap.AppealDisposition_Type;
import de.geoinfoffm.registry.api.soap.AppealProposalRequest;
import de.geoinfoffm.registry.api.soap.AppealProposalResponse;
import de.geoinfoffm.registry.api.soap.Appeal_PropertyType;
import de.geoinfoffm.registry.api.soap.Appeal_Type;
import de.geoinfoffm.registry.api.soap.Clarification_Type;
import de.geoinfoffm.registry.api.soap.CreateProposalRequest;
import de.geoinfoffm.registry.api.soap.CreateProposalResponse;
import de.geoinfoffm.registry.api.soap.GetProposalDescriptionsRequest;
import de.geoinfoffm.registry.api.soap.GetProposalDescriptionsResponse;
import de.geoinfoffm.registry.api.soap.GetProposalRequest;
import de.geoinfoffm.registry.api.soap.GetProposalResponse;
import de.geoinfoffm.registry.api.soap.ObjectFactory;
import de.geoinfoffm.registry.api.soap.ProposalDescription;
import de.geoinfoffm.registry.api.soap.Proposal_PropertyType;
import de.geoinfoffm.registry.api.soap.ProposedChange_PropertyType;
import de.geoinfoffm.registry.api.soap.RegisterItemProposal_PropertyType;
import de.geoinfoffm.registry.api.soap.RejectAppealRequest;
import de.geoinfoffm.registry.api.soap.RejectAppealResponse;
import de.geoinfoffm.registry.api.soap.RejectProposalRequest;
import de.geoinfoffm.registry.api.soap.RejectProposalResponse;
import de.geoinfoffm.registry.api.soap.Retirement_Type;
import de.geoinfoffm.registry.api.soap.ReviewProposalRequest;
import de.geoinfoffm.registry.api.soap.ReviewProposalResponse;
import de.geoinfoffm.registry.api.soap.SubmitProposalRequest;
import de.geoinfoffm.registry.api.soap.SubmitProposalResponse;
import de.geoinfoffm.registry.api.soap.Supersession_Type;
import de.geoinfoffm.registry.api.soap.UpdateProposalRequest;
import de.geoinfoffm.registry.api.soap.UpdateProposalResponse;
import de.geoinfoffm.registry.api.soap.WithdrawProposalRequest;
import de.geoinfoffm.registry.api.soap.WithdrawProposalResponse;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.AppealDisposition;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.SupersessionPart;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.model.iso19135.SubmittingOrganizationRepository;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.soap.AbstractEndpoint;

/**
 * The class ProposalEndpoint.
 *
 * @author Florian Esser
 */
@Endpoint
public class ProposalEndpoint extends AbstractEndpoint
{
	private static final String NAMESPACE_URI = "http://www.geoinfoffm.de/registry/soap";
	private static final String GDI_NAMESPACE_URI = "http://registry.gdi-de.org/api/soap";
	
	private static final Logger logger = LoggerFactory.getLogger(ProposalEndpoint.class);
	
	@Autowired
	private RegistrySecurity security;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RegisterItemService itemService;
	
	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private SubmittingOrganizationRepository suborgRepository;
	
	@Autowired
	private ProposalDtoFactory dtoFactory;

	private ObjectFactory objectFactory = new ObjectFactory();
	private org.isotc211.iso19135.ObjectFactory iso19135ObjectFactory = new org.isotc211.iso19135.ObjectFactory();

	@Transactional(readOnly = true)
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProposalDescriptionsRequest")
	public @ResponsePayload JAXBElement<GetProposalDescriptionsResponse> handleGetProposalDescriptionsRequest(@RequestPayload GetProposalDescriptionsRequest req) throws Exception {
		if (!req.isSetRegisterUuid()) {
			throw new IllegalArgumentException("Illegal request: Must provide register UUID");
		}

		try {
			UUID registerUuid = UUID.fromString(req.getRegisterUuid());
			RE_Register register = registerService.findOne(registerUuid);
			if (register == null) {
				throw new IllegalArgumentException("Illegal request: Register not found");
			}
			
			security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, registerUuid);
	
			Collection<Proposal> proposals = proposalService.findProposals(register, RE_DecisionStatus.PENDING);
			
			GetProposalDescriptionsResponse result = objectFactory.createGetProposalDescriptionsResponse();
			
			for (Proposal proposal : proposals) {
				ProposalDescription desc = objectFactory.createProposalDescription();
				desc.setUuid(proposal.getUuid().toString());
				desc.setProposalType(proposal.getClass().getName());
				desc.setSponsor(proposal.getSponsor().getUuid().toString());
				desc.setStatus(proposal.getStatus().toString());
				if (proposal.hasParent()) {
					desc.setBelongsToGroup(proposal.getParent().getUuid().toString());
				}
				
				result.getProposalDescription().add(desc);
			}
			
			return objectFactory.createGetProposalDescriptionsResponse(result);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		}
	}

	@Transactional(readOnly = true)
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProposalRequest")
	public @ResponsePayload JAXBElement<GetProposalResponse> handleGetProposalRequest(@RequestPayload GetProposalRequest req) throws Exception {
		Proposal proposal = proposalService.findProposal(UUID.fromString(req.getUuid()));
		
		security.assertHasAnyEntityRelatedRoleForAll(Arrays.asList(SUBMITTER_ROLE_PREFIX, MANAGER_ROLE_PREFIX, CONTROLBODY_ROLE_PREFIX), proposal.getAffectedRegisters());
		security.assertMayRead(proposal);

		GetProposalResponse result = new GetProposalResponse();
		result.setProposal(toPropertyType(proposal));
		
		return objectFactory.createGetProposalResponse(result);
	}

	private void setCommonProperties(SimpleProposal simpleProposal, AbstractProposal_Type proposal) {
		proposal.setUuid(simpleProposal.getUuid().toString());
		proposal.setJustification(simpleProposal.getJustification());
		proposal.setRegisterManagerNotes(simpleProposal.getRegisterManagerNotes());
		proposal.setControlBodyNotes(simpleProposal.getControlBodyNotes());
		
		RE_SubmittingOrganization_PropertyType sponsor = new RE_SubmittingOrganization_PropertyType();
		sponsor.setUuidref(simpleProposal.getSponsor().getUuid().toString());
		proposal.setSponsor(sponsor);
	}
	
	private AppealDisposition_PropertyType toPropertyType(AppealDisposition disposition) {
		AppealDisposition_PropertyType result = new AppealDisposition_PropertyType();
		
		switch (disposition) {
			case PENDING:
				result.setAppealDisposition(AppealDisposition_Type.PENDING);
				break;
			case ACCEPTED:
				result.setAppealDisposition(AppealDisposition_Type.ACCEPTED);
				break;
			case NOT_ACCEPTED:
				result.setAppealDisposition(AppealDisposition_Type.NOT_ACCEPTED);
				break;
			default:
				throw new IllegalArgumentException(String.format("Appeal disposition %s not supported", disposition.name()));
		}
		
		return result;
	}

	private Appeal_PropertyType toPropertyType(Appeal appeal) {
		Appeal_Type appealType = new Appeal_Type();
		
		appealType.setJustification(appeal.getJustification());
		appealType.setImpact(appeal.getImpact());
		appealType.setSituation(appeal.getSituation());
		
		Proposal_PropertyType proposal = new Proposal_PropertyType();
		proposal.setUuidref(appeal.getAppealedProposal().getUuid().toString());
		appealType.setAppealedProposal(proposal);
		
		appealType.setAppealDisposition(toPropertyType(appeal.getDisposition()));
		appealType.setAppealDate(toXmlDate(appeal.getAppealDate()));
		appealType.setDispositionDate(toXmlDate(appeal.getDispositionDate()));
		
		Appeal_PropertyType result = new Appeal_PropertyType();
		result.setAppeal(appealType);
		
		return result;
	}

	private Proposal_PropertyType toPropertyType(Proposal proposal) {
		Proposal_PropertyType result = new Proposal_PropertyType();
		if (proposal instanceof SimpleProposal) {
			SimpleProposal sp = (SimpleProposal)proposal;

			if (proposal instanceof Addition) {
				Addition_Type addition = new Addition_Type();
				addition.setTargetRegisterUuid(sp.getRegister().getUuid().toString());
				RegisterItemProposal_PropertyType itemDetails = new RegisterItemProposal_PropertyType();
				addition.setItemDetails(itemDetails);

				RE_RegisterItem_PropertyType item = new RE_RegisterItem_PropertyType();
				item.setUuidref(sp.getItem().getUuid().toString());
				addition.setCreatedItem(item);
				
				setCommonProperties(sp, addition);
				
				result.setAbstractProposal(objectFactory.createAddition(addition));
			}
			else if (proposal instanceof Clarification) {
				Clarification_Type clarification = new Clarification_Type();
				clarification.setClarifiedItemUuid(sp.getItem().getUuid().toString());

				setCommonProperties(sp, clarification);

				result.setAbstractProposal(objectFactory.createClarification(clarification));
			}
			else if (proposal instanceof Retirement) {
				Retirement_Type retirement = new Retirement_Type();
				retirement.setRetiredItemUuid(sp.getItem().getUuid().toString());

				setCommonProperties(sp, retirement);

				result.setAbstractProposal(objectFactory.createRetirement(retirement));
			}
			else if (proposal instanceof SupersessionPart) {
				throw new IllegalArgumentException(String.format("This proposal is part of a supersession. Please retrieve proposal %s instead.", proposal.getParent().getUuid()));
			}
			else {
				throw new IllegalArgumentException("This proposal cannot be retrieved by this request.");
			}
		}
		else if (proposal instanceof Supersession) {
			Supersession_Type supersession = new Supersession_Type();
			
			Supersession s = (Supersession)proposal;
			
			for (RE_RegisterItem supersededItem : s.getSupersededItems()) {
				supersession.getSupersededItemUuid().add(supersededItem.getUuid().toString());
			}
			
			for (RE_RegisterItem supersedingItem : s.getSupersedingItems()) {
				RegisterItemProposal_PropertyType itemDetails = objectFactory.createRegisterItemProposal_PropertyType();
				itemDetails.setUuidref(supersedingItem.getUuid().toString());
				supersession.getSupersedingItem().add(itemDetails);
			}
			
			supersession.setUuid(s.getUuid().toString());
			supersession.setJustification(s.getJustification());
			supersession.setRegisterManagerNotes(s.getRegisterManagerNotes());
			supersession.setControlBodyNotes(s.getControlBodyNotes());
			
			RE_SubmittingOrganization_PropertyType sponsor = new RE_SubmittingOrganization_PropertyType();
			sponsor.setUuidref(s.getSponsor().getUuid().toString());
			supersession.setSponsor(sponsor);
			
			result.setAbstractProposal(objectFactory.createSupersession(supersession));
		}
		else if (proposal instanceof ProposalGroup) {
			ProposalGroup group = (ProposalGroup)proposal;
			result.setUuidref(group.getUuid().toString());
		}
		else {
			throw new IllegalArgumentException("This proposal cannot be retrieved by this request.");			
		}
		
		return result;
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ReviewProposalRequest")
	public @ResponsePayload JAXBElement<ReviewProposalResponse> handleReviewProposalRequest(@RequestPayload ReviewProposalRequest req) throws Exception {
		Proposal proposal = proposalService.findProposal(UUID.fromString(req.getUuid()));
		
		security.assertHasEntityRelatedRoleForAll(MANAGER_ROLE_PREFIX, proposal.getAffectedRegisters());
		
		proposal = proposalService.reviewProposal(proposal);
		
		ReviewProposalResponse result = objectFactory.createReviewProposalResponse();
		result.setProposal(toPropertyType(proposal));
		
		return objectFactory.createReviewProposalResponse(result);
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "AppealProposalRequest")
	public @ResponsePayload JAXBElement<AppealProposalResponse> handleAppealProposalRequest(@RequestPayload AppealProposalRequest req) throws Exception {
		Proposal proposal = proposalService.findProposal(UUID.fromString(req.getAppealedProposalUuid()));
		
		security.assertHasEntityRelatedRoleForAll(SUBMITTER_ROLE_PREFIX, proposal.getAffectedRegisters());
		security.assertMayWrite(proposal);
		
		Appeal appeal = proposalService.appealProposal(proposal, req.getJustification(), req.getSituation(), req.getImpact());
		
		AppealProposalResponse result = objectFactory.createAppealProposalResponse();
		result.setAppeal(toPropertyType(appeal));
		
		return objectFactory.createAppealProposalResponse(result);
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "AcceptAppealRequest")
	public @ResponsePayload JAXBElement<AcceptAppealResponse> handleAcceptAppealRequest(@RequestPayload AcceptAppealRequest req) throws Exception {
		Appeal appeal = proposalService.findAppeal(UUID.fromString(req.getUuid()));
		if (appeal == null) {
			throw new IllegalArgumentException(String.format("Appeal %s does not exist.", req.getUuid()));
		}

		security.assertHasEntityRelatedRoleForAll(OWNER_ROLE_PREFIX, appeal.getAppealedProposal().getAffectedRegisters());

		appeal = proposalService.acceptAppeal(appeal);
		
		AcceptAppealResponse result = new AcceptAppealResponse();
		result.setAppeal(toPropertyType(appeal));
		
		return objectFactory.createAcceptAppealResponse(result);
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "RejectAppealRequest")
	public @ResponsePayload JAXBElement<RejectAppealResponse> handleRejectAppealRequest(@RequestPayload RejectAppealRequest req) throws Exception {
		Appeal appeal = proposalService.findAppeal(UUID.fromString(req.getUuid()));
		if (appeal == null) {
			throw new IllegalArgumentException(String.format("Appeal %s does not exist.", req.getUuid()));
		}

		security.assertHasEntityRelatedRoleForAll(OWNER_ROLE_PREFIX, appeal.getAppealedProposal().getAffectedRegisters());

		appeal = proposalService.rejectAppeal(appeal);
		
		RejectAppealResponse result = new RejectAppealResponse();
		result.setAppeal(toPropertyType(appeal));
		
		return objectFactory.createRejectAppealResponse(result);		
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateProposalRequest")
	public @ResponsePayload JAXBElement<UpdateProposalResponse> handleUpdateProposalRequest(@RequestPayload UpdateProposalRequest req) throws Exception {
		if (!req.getProposal().isSetAbstractProposal() || req.getProposal().getAbstractProposal().getValue() == null) {
			throw new IllegalArgumentException("Must provide changed proposal");
		}
		
		UUID proposalUuid = UUID.fromString(req.getProposal().getAbstractProposal().getValue().getUuid());
		Proposal proposal = proposalService.findProposal(proposalUuid);
		if (proposal == null) {
			throw new IllegalArgumentException(String.format("Proposal %s does not exist.", proposalUuid));
		}
		
		security.assertHasAnyEntityRelatedRoleForAll(Arrays.asList(SUBMITTER_ROLE_PREFIX, MANAGER_ROLE_PREFIX), proposal.getAffectedRegisters());
		security.assertMayWrite(proposal);

		if (proposal instanceof Supersession) {
			Supersession_Type s = (Supersession_Type)req.getProposal().getAbstractProposal().getValue();
			
			UUID targetRegisterUuid = null;
			Set<RE_RegisterItem> supersededItems = new HashSet<RE_RegisterItem>();
			for (String supersededItemUuid : s.getSupersededItemUuid()) {
				UUID uuid = UUID.fromString(supersededItemUuid);
				RE_RegisterItem supersededItem = itemService.findOne(uuid);
				
				if (supersededItem == null) {
					throw new IllegalArgumentException(String.format("Superseded item %s does not exist", supersededItemUuid));
				}
				
				if (targetRegisterUuid == null) {
					targetRegisterUuid = supersededItem.getRegister().getUuid(); 
				}
				else if (!targetRegisterUuid.equals(supersededItem.getRegister().getUuid())) {
					throw new IllegalArgumentException("Cannot supersede items that belong to different registers.");
				}
				
				supersededItems.add(supersededItem);
			}
			
			Set<RegisterItemProposalDTO> newSuccessors = new HashSet<RegisterItemProposalDTO>();
			Set<RE_RegisterItem> existingSuccessors = new HashSet<RE_RegisterItem>();
			for (RegisterItemProposal_PropertyType successor : s.getSupersedingItem()) {
				if (successor.isSetUuidref()) {
					RE_RegisterItem item = itemService.findOne(UUID.fromString(successor.getUuidref()));
					existingSuccessors.add(item);
				}
				else if (successor.isSetAbstractRegisterItemProposal()) {
					// new item
					AbstractRegisterItemProposal_Type itemDetails = successor.getAbstractRegisterItemProposal().getValue();
//					RE_RegisterItem_Type item = successor.getRE_RegisterItem().getValue();
					RegisterItemProposalDTO dto = new RegisterItemProposalDTO(itemDetails);
					dto.setTargetRegisterUuid(targetRegisterUuid);
					dto.setJustification(s.getJustification());
					newSuccessors.add(dto);
				}
			}
			
			proposalService.updateSupersession((Supersession)proposal, supersededItems, existingSuccessors, newSuccessors, s.getJustification(), s.getRegisterManagerNotes(), s.getControlBodyNotes());
		}
		else {
			proposal = proposalService.updateProposal(proposalUuid, req.getProposal().getAbstractProposal().getValue());
		}
		
		UpdateProposalResponse result = new UpdateProposalResponse();
		result.setProposal(toPropertyType(proposal));
		
		return objectFactory.createUpdateProposalResponse(result);
	}


	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "WithdrawProposalRequest")
	public @ResponsePayload JAXBElement<WithdrawProposalResponse> handleWithdrawProposalRequest(@RequestPayload WithdrawProposalRequest req) throws Exception {
		Proposal proposal = proposalService.findProposal(UUID.fromString(req.getUuid()));
		if (proposal == null) {
			throw new IllegalArgumentException(String.format("Proposal %s does not exist.", req.getUuid()));
		}
		
		security.assertHasEntityRelatedRoleForAll(SUBMITTER_ROLE_PREFIX, proposal.getAffectedRegisters());
		security.assertMayDelete(proposal);
		
		proposal = proposalService.withdrawProposal(proposal);
		
		WithdrawProposalResponse result = objectFactory.createWithdrawProposalResponse();
		result.setProposal(toPropertyType(proposal));
		
		return objectFactory.createWithdrawProposalResponse(result);
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "AcceptProposalRequest")
	public @ResponsePayload JAXBElement<AcceptProposalResponse> handleAcceptProposalRequest(@RequestPayload AcceptProposalRequest req) throws Exception {
		Proposal proposal = proposalService.findProposal(UUID.fromString(req.getUuid()));
		if (proposal == null) {
			throw new IllegalArgumentException(String.format("Proposal %s does not exist.", req.getUuid()));
		}
		
		security.assertHasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());
		
		proposal = proposalService.acceptProposal(proposal, req.getControlBodyDecisionEvent());
		
		AcceptProposalResponse result = objectFactory.createAcceptProposalResponse();
		result.setProposal(toPropertyType(proposal));
		
		return objectFactory.createAcceptProposalResponse(result);
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "RejectProposalRequest")
	public @ResponsePayload JAXBElement<RejectProposalResponse> handleRejectProposalRequest(@RequestPayload RejectProposalRequest req) throws Exception {
		Proposal proposal = proposalService.findProposal(UUID.fromString(req.getUuid()));
		if (proposal == null) {
			throw new IllegalArgumentException(String.format("Proposal %s does not exist.", req.getUuid()));
		}
		
		security.assertHasEntityRelatedRoleForAll(CONTROLBODY_ROLE_PREFIX, proposal.getAffectedRegisters());
		
		proposal = proposalService.acceptProposal(proposal, req.getControlBodyDecisionEvent());
		
		RejectProposalResponse result = objectFactory.createRejectProposalResponse();
		result.setProposal(toPropertyType(proposal));
		
		return objectFactory.createRejectProposalResponse(result);
	}

	@Transactional
	@Namespaces({
		@Namespace(prefix = "reg", uri = NAMESPACE_URI),
		@Namespace(prefix = "gdireg", uri = GDI_NAMESPACE_URI)
	})
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateProposalRequest")
	public @ResponsePayload JAXBElement<CreateProposalResponse> handleCreateProposalRequest(@RequestPayload CreateProposalRequest req) throws Exception {
		if (req.getProposal() == null || req.getProposal().isEmpty()) {
			throw new IllegalArgumentException("Invalid request");
		}

		// Simple security check here, to reject all non-submitters.
		// Detailed checks in handle methods.
		security.assertHasRoleWith(SUBMITTER_ROLE_PREFIX);

		CreateProposalResponse result = objectFactory.createCreateProposalResponse();
		
		try {
			ProposalGroup group = null;
			boolean firstProposal = true;
			for (Proposal_PropertyType proposalProperty : req.getProposal()) {
				if (!proposalProperty.isSetAbstractProposal()) continue;
				if (firstProposal && req.isCreateGroup()) {
					String groupName;
					if (req.isSetGroupName() && !StringUtils.isEmpty(req.getGroupName())) {
						groupName = req.getGroupName();
					}
					else {
						DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
						groupName = "Group" + df.format(Calendar.getInstance().getTime()); 
					}
					
					RE_SubmittingOrganization sponsor = extractSponsorFromProposal(proposalProperty.getAbstractProposal().getValue());
					group = proposalService.createProposalGroup(groupName, new ArrayList<Proposal>(), sponsor);
				}
				else if (firstProposal && req.isSetAddToGroup()) {
					group = (ProposalGroup)proposalService.findOne(UUID.fromString(req.getAddToGroup()));
				}
				final AbstractProposal_Type proposal = proposalProperty.getAbstractProposal().getValue();
//				final String transportUuid = proposal.getTransportUuid();
//				if (!StringUtils.isEmpty(transportUuid)) {
//					final Proposal proposalWithTransportUuid = proposalService.findByTransportUuid(transportUuid);
//					if (proposalWithTransportUuid != null) {
//						// proposal with same transport ID exists, skip it
//						logger.debug("Skipping proposal with transport ID '{}': proposal with this transport ID already exists.", transportUuid);
//						continue;
//					}
//				}
				if (proposalProperty.getAbstractProposal().getValue() instanceof Addition_Type) {
					logger.debug("ADDITION proposal received...");
					Addition_Type addition = handleAddition((Addition_Type)proposalProperty.getAbstractProposal().getValue(), group);
					result.getProposal().add(createProposalProperty(addition));
				}
				else if (proposalProperty.getAbstractProposal().getValue() instanceof Retirement_Type) {
					Retirement_Type retirement = handleRetirement((Retirement_Type)proposalProperty.getAbstractProposal().getValue(), group);
					result.getProposal().add(createProposalProperty(retirement));
				}
				else if (proposalProperty.getAbstractProposal().getValue() instanceof Clarification_Type) {
					Clarification_Type clarification = handleClarification((Clarification_Type)proposalProperty.getAbstractProposal().getValue(), group);
					result.getProposal().add(createProposalProperty(clarification));
				}
				else if (proposalProperty.getAbstractProposal().getValue() instanceof Supersession_Type) {
					Supersession_Type supersession = handleSupersession((Supersession_Type)proposalProperty.getAbstractProposal().getValue(), group);
					result.getProposal().add(createProposalProperty(supersession));
				}
				else {
	//				throw new RuntimeException("Proposal type not yet implemented");
				}
	
				firstProposal = false;
			}
			
			if (group != null && !group.getProposals().isEmpty()) {
				proposalService.saveProposal(group);
				result.setGroupUuid(group.getUuid().toString());
			}
			
			return objectFactory.createCreateProposalResponse(result);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw t;
		}
	}
	

	private Proposal_PropertyType createProposalProperty(AbstractProposal_Type proposal) {
		Proposal_PropertyType pp = objectFactory.createProposal_PropertyType();
		pp.setAbstractProposal(objectFactory.createAbstractProposal(proposal));
		return pp;
	}

	private Addition_Type handleAddition(Addition_Type addition, ProposalGroup group) throws Exception {
		if (addition.getItemDetails() == null || addition.getItemDetails().getAbstractRegisterItemProposal() == null) {
			throw new IllegalArgumentException("Invalid request");
		}
		
		security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, addition.getTargetRegisterUuid());
				
		RegisterItemProposalDTO dto = dtoFactory.getProposalDto(addition);
		logger.debug("DTO factory created object of type {}", dto.getClass().getCanonicalName());
		
		Addition newAddition; 
		try {
			newAddition = proposalService.createAdditionProposal(dto);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (group != null) {
			newAddition.setParent(group);
			group.addProposal(newAddition);
			newAddition = proposalService.saveProposal(newAddition);
		}

		addition.setUuid(newAddition.getUuid().toString());
	
		RE_RegisterItem_PropertyType createdItemPropertyType = iso19135ObjectFactory.createRE_RegisterItem_PropertyType();
		createdItemPropertyType.setUuidref(newAddition.getItem().getUuid().toString());
		addition.setCreatedItem(createdItemPropertyType);
		
		return addition;
	}

	private Retirement_Type handleRetirement(Retirement_Type retirement, ProposalGroup group) throws Exception {
		RE_RegisterItem retiree = itemService.findOne(UUID.fromString(retirement.getRetiredItemUuid()));
		
		if (retiree == null) {
			throw new IllegalArgumentException("Item to be retired does not exist");
		}
		
		security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, retiree.getRegister());
		
		Retirement retirementProposal;
		try {
			retirementProposal = proposalService.createRetirement(retiree, 
					retirement.getJustification(), retirement.getRegisterManagerNotes(), retirement.getControlBodyNotes(),
					extractSponsorFromProposal(retirement));
			
//			retirementProposal.setTransportUuid(retirement.getTransportUuid());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		if (group != null) {
			retirementProposal.setParent(group);
			group.addProposal(retirementProposal);
			retirementProposal = proposalService.saveProposal(retirementProposal);
			// group = proposalService.saveProposal(group);
		}

		retirement.setUuid(retirementProposal.getUuid().toString());
		retirement.setRetiredItemUuid(retirementProposal.getItem().getUuid().toString());
		
		return retirement;
	}
	
	private Clarification_Type handleClarification(Clarification_Type clarification, ProposalGroup group) throws Exception {
		if (clarification.getProposedChange() == null || clarification.getProposedChange().isEmpty()) {
			throw new IllegalArgumentException("At least one proposed change must be provided");
		}

		String clarifiedItemUuid = clarification.getClarifiedItemUuid();
		if (StringUtils.isEmpty(clarifiedItemUuid)) {
			throw new IllegalArgumentException("Clarified item must be specified");
		}
		RE_RegisterItem clarifiedItem = itemService.findOne(UUID.fromString(clarifiedItemUuid));
		if (clarifiedItem == null) {
			throw new IllegalArgumentException("Clarified item does not exist.");
		}

		security.assertMaySubmitTo(clarifiedItem.getRegister());

		Map<String, List<String>> proposedChanges = new HashMap<String, List<String>>();
		for (ProposedChange_PropertyType change : clarification.getProposedChange()) {
			List<String> newValues = new ArrayList<String>();

			for (CharacterString_PropertyType newValue : change.getProposedChange().getNewValue()) {
				if (newValue == null || !newValue.isSetCharacterString() || newValue.getCharacterString().getValue() == null) {
					newValue = null;
				}
				else {
					newValues.add(newValue.getCharacterString().getValue().toString());
				}
			}

			proposedChanges.put(change.getProposedChange().getProperty(), newValues);
		}

		Clarification clarificationProposal;
		try {
			clarificationProposal = proposalService.createClarification(clarifiedItem, proposedChanges, 
					clarification.getJustification(), clarification.getRegisterManagerNotes(), clarification.getControlBodyNotes(),
					extractSponsorFromProposal(clarification));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		if (group != null) {
			clarificationProposal.setParent(group);
			group.addProposal(clarificationProposal);
			clarificationProposal = proposalService.saveProposal(clarificationProposal);
			group = proposalService.saveProposal(group);
		}

		clarification.setUuid(clarificationProposal.getUuid().toString());
		clarification.setClarifiedItemUuid(clarificationProposal.getItem().getUuid().toString());

		return clarification;
	}

	private Supersession_Type handleSupersession(Supersession_Type supersession, ProposalGroup group) throws Exception {
		if (supersession.getSupersededItemUuid() == null || supersession.getSupersededItemUuid().isEmpty()) {
			throw new IllegalArgumentException("Must provide at least one superseded item UUID");
		}
		
		UUID targetRegisterUuid = null;
		
		Set<RE_RegisterItem> supersededItems = new HashSet<RE_RegisterItem>();
		for (String supersededItemUuid : supersession.getSupersededItemUuid()) {
			UUID uuid = UUID.fromString(supersededItemUuid);
			RE_RegisterItem supersededItem = itemService.findOne(uuid);
			
			if (supersededItem == null) {
				throw new IllegalArgumentException(String.format("Superseded item %s does not exist", supersededItemUuid));
			}

			security.assertHasEntityRelatedRole(SUBMITTER_ROLE_PREFIX, supersededItem.getRegister());

			if (targetRegisterUuid == null) {
				targetRegisterUuid = supersededItem.getRegister().getUuid(); 
			}
			else if (!targetRegisterUuid.equals(supersededItem.getRegister().getUuid())) {
				throw new IllegalArgumentException("Cannot supersede items that belong to different registers.");
			}
			
			supersededItems.add(supersededItem);
		}
		
		Set<RegisterItemProposalDTO> successors = new HashSet<RegisterItemProposalDTO>();
		for (RegisterItemProposal_PropertyType successor : supersession.getSupersedingItem()) {
			if (successor.isSetUuidref()) {
				throw new RuntimeException("Support for already existing items as successors is not yet implemented");
//				RE_RegisterItem existingSuccessor = itemService.findOne(UUID.fromString(successor.getUuidref()));
//				if (existingSuccessor == null) {
//					throw new IllegalArgumentException(String.format("Referenced item %s does not exist", successor.getUuidref()));
//				}
			}
			else if (successor.isSetAbstractRegisterItemProposal()) {
				// new item
				AbstractRegisterItemProposal_Type itemDetails = successor.getAbstractRegisterItemProposal().getValue();
				RegisterItemProposalDTO dto = dtoFactory.getProposalDto(itemDetails);
				dto.setTargetRegisterUuid(targetRegisterUuid);
				dto.setJustification(supersession.getJustification());
				successors.add(dto);
			}
		}
		
		Supersession supersessionProposal;
		try {
			supersessionProposal = proposalService.createSupersession(supersededItems, successors, supersession.getJustification(), supersession.getRegisterManagerNotes(), supersession.getControlBodyNotes(), extractSponsorFromProposal(supersession));
//			supersessionProposal.setTransportUuid(supersession.getTransportUuid());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (group != null) {
			supersessionProposal.setParent(group);
			group.addProposal(supersessionProposal);
			supersessionProposal = proposalService.saveProposal(supersessionProposal);
			// group = proposalService.saveProposal(group);
		}

		supersession.setUuid(supersessionProposal.getUuid().toString());

		return supersession;
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "SubmitProposalRequest")
	public @ResponsePayload JAXBElement<SubmitProposalResponse> handleSubmitProposalRequest(@RequestPayload SubmitProposalRequest req) throws Exception {		
		Proposal proposal = proposalService.findProposal(UUID.fromString(req.getUuid()));
		if (proposal == null) {
			throw new IllegalArgumentException(String.format("Proposal %s does not exist.", req.getUuid()));
		}
		
		security.assertHasEntityRelatedRoleForAll(SUBMITTER_ROLE_PREFIX, proposal.getAffectedRegisters());
		
		proposal = proposalService.submitProposal(proposal);
		
		SubmitProposalResponse result = objectFactory.createSubmitProposalResponse();
		result.setProposal(toPropertyType(proposal));
		
		return objectFactory.createSubmitProposalResponse(result);
	}

	private RE_SubmittingOrganization extractSponsorFromProposal(AbstractProposal_Type proposal) {
		UUID sponsorUuid;
		
		if (!StringUtils.isEmpty(proposal.getSponsor().getUuidref())) {
			// Submitter is referenced by uuid
			sponsorUuid = UUID.fromString(proposal.getSponsor().getUuidref());
		}
		else if (proposal.getSponsor().getRE_SubmittingOrganization() != null) {
			// Item class is described
			sponsorUuid = UUID.fromString(proposal.getSponsor().getRE_SubmittingOrganization().getUuid());
		}
		else {
			throw new IllegalArgumentException("Proposal must reference a sponsor or contain sponsor description");
		}
		
		RE_SubmittingOrganization sponsor = suborgRepository.findOne(sponsorUuid);
		if (sponsor == null) {
			throw new IllegalArgumentException("Proposal sponsor does not exist");			
		}

		return sponsor;
	}

	private XMLGregorianCalendar toXmlDate(Date date) {
		if (date == null) {
			return null;
		}
		
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(date);		
		XMLGregorianCalendar xmlcal;
		try {
			xmlcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		}
		catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		return xmlcal;
	}
}
