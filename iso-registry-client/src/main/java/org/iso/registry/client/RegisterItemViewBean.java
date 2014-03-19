/**
 * 
 */
package org.iso.registry.client;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import de.geoinfoffm.registry.api.RegisterItemProposalDTO.ProposalType;
import de.geoinfoffm.registry.core.model.Addition;
import de.geoinfoffm.registry.core.model.Appeal;
import de.geoinfoffm.registry.core.model.Appeal.AppealDisposition;
import de.geoinfoffm.registry.core.model.Clarification;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalGroup;
import de.geoinfoffm.registry.core.model.Retirement;
import de.geoinfoffm.registry.core.model.SimpleProposal;
import de.geoinfoffm.registry.core.model.Supersession;
import de.geoinfoffm.registry.core.model.iso19103.CharacterString;
import de.geoinfoffm.registry.core.model.iso19135.RE_AdditionInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_AmendmentType;
import de.geoinfoffm.registry.core.model.iso19135.RE_ClarificationInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_DecisionStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_Disposition;
import de.geoinfoffm.registry.core.model.iso19135.RE_ItemStatus;
import de.geoinfoffm.registry.core.model.iso19135.RE_ProposalManagementInformation;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.core.model.iso19135.RE_RegisterItem;

/**
 * @author Florian.Esser
 *
 */
public class RegisterItemViewBean
{
	public enum ProposalStatus {
		UNDER_REVIEW,
		IN_APPROVAL_PROCESS,
		APPEALABLE,
		APPEALED,
		FINISHED
	}
	
	private UUID uuid;
	private Class<?> itemClass;
	
	private BigInteger itemIdentifier;
	private String name;
	private String definition;
	private String description;
	private RE_ItemStatus status;
	
	private boolean isEditable;

	private UUID itemClassUuid;
	private String itemClassName;

	private UUID registerUuid;
	private String registerName;

	private boolean isProposal;
	private ProposalType proposalType;
	private String justification;
	private String registerManagerNotes;
	private String controlBodyNotes;
	private Map<String, String> proposedChange;
	private Date proposalDate;
	private RE_Disposition disposition;
	private RE_DecisionStatus decisionStatus;
	private boolean isAppealed;
	
	private UUID proposalUuid;
	private Class<?> proposalClass;
	private ProposalStatus proposalStatus;
	
	private AppealDisposition appealDisposition;
	
	private UUID sponsorUuid;
	private String sponsorName;
	
	private final Set<RegisterItemViewBean> supersededItems = new HashSet<RegisterItemViewBean>(); 
	private final Set<RegisterItemViewBean> supersedingItems = new HashSet<RegisterItemViewBean>(); 
	
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	private final Set<RE_AdditionInformation> additionInformations = new LinkedHashSet<RE_AdditionInformation>();
	private final Set<RE_AmendmentInformation> amendmentInformations = new LinkedHashSet<RE_AmendmentInformation>();
	private final Set<RE_ClarificationInformation> clarificationInformations = new LinkedHashSet<RE_ClarificationInformation>();

	private final Map<UUID, String> predecessors = new LinkedHashMap<UUID, String>(); 
	private final Map<UUID, String> successors = new LinkedHashMap<UUID, String>(); 

	public RegisterItemViewBean(RE_RegisterItem item) {
		initializeFromItem(item);
	}

	public RegisterItemViewBean(Proposal proposal) {
		this.setEditable(proposal.isEditable());
		
		if (proposal instanceof SimpleProposal) {
			initializeFromSimpleProposal((SimpleProposal)proposal);
		}
		else if (proposal instanceof Supersession) {
			initializeFromSupersession((Supersession)proposal);
		}
		else if (proposal instanceof ProposalGroup) {
			throw new RuntimeException("Not yet implemented");
		}
	}

	public RegisterItemViewBean(SimpleProposal proposal) {
		initializeFromSimpleProposal(proposal);
	}
	
	public RegisterItemViewBean(Appeal appeal) {
		initializeFromAppeal(appeal);
	}
	
	public RegisterItemViewBean(Supersession supersession) {
		initializeFromSupersession(supersession);
	}
	
	/**
	 * May be overwritten by extending classes
	 */
	protected void addAdditionalProperties(RE_RegisterItem item) {
		// does nothing here
	}

	/**
	 * @param item
	 */
	private void initializeFromItem(RE_RegisterItem item) {
		this.uuid = item.getUuid();
		this.itemClass = item.getClass();
		this.setItemIdentifier(item.getItemIdentifier());
		this.name = item.getName();
		this.definition = item.getDefinition();
		this.description = item.getDescription();
		this.setStatus(item.getStatus());
		
		this.itemClassUuid = item.getItemClass().getUuid();
		this.itemClassName = item.getItemClass().getName();

		if (item.getRegister() != null) {
			this.registerUuid = item.getRegister().getUuid();
			this.registerName = item.getRegister().getName();
		}
		
		this.isProposal = false;
		this.setAppealed(false);
		
		for (RE_AdditionInformation ai : item.getAdditionInformation()) {
			if (ai.isFinal()) {
				initializeFromProposalManagementInformation(ai);
				break;
			}
		}

		this.additionInformations.addAll(item.getAdditionInformation());
		this.clarificationInformations.addAll(item.getClarificationInformation());
		this.amendmentInformations.addAll(item.getAmendmentInformation());
		
		for (RE_RegisterItem predecessor : item.getPredecessors()) {
			this.predecessors.put(predecessor.getUuid(), predecessor.getName());
		}

		for (RE_RegisterItem successor : item.getSuccessors()) {
			this.successors.put(successor.getUuid(), successor.getName());
		}
		
		this.addAdditionalProperties(item);
}
	
	private void initializeFromProposalManagementInformation(RE_ProposalManagementInformation proposal) {
		if (!proposal.isFinal()) {
			this.isProposal = true;
		}

		this.proposalDate = proposal.getDateProposed();
		if (proposal instanceof RE_AdditionInformation) {
			this.proposalType = ProposalType.ADDITION;
		}
		else if (proposal instanceof RE_ClarificationInformation) {
			this.proposalType = ProposalType.CLARIFICATION;
			this.setProposedChange(RE_ClarificationInformation.fromJson(CharacterString.asString(((RE_ClarificationInformation)proposal).getProposedChange())));
		}	
		else if (proposal instanceof RE_AmendmentInformation && ((RE_AmendmentInformation)proposal).getAmendmentType().equals(RE_AmendmentType.RETIREMENT)) {
			this.proposalType = ProposalType.RETIREMENT;
		}

		this.setProposalStatus(ProposalStatus.UNDER_REVIEW);
		if (proposal.isPending()) {
			this.setProposalStatus(ProposalStatus.IN_APPROVAL_PROCESS);
		}
		if (proposal.isTentative()) {
			this.setProposalStatus(ProposalStatus.APPEALABLE);
		}
		if (proposal.isFinal()) {
			this.setProposalStatus(ProposalStatus.FINISHED);
		}
		
		this.justification = proposal.getJustification();
		this.sponsorUuid = proposal.getSponsor().getUuid();
		this.sponsorName = proposal.getSponsor().getName(); 
		
		this.setRegisterManagerNotes(proposal.getRegisterManagerNotes());
		this.setControlBodyNotes(proposal.getControlBodyNotes());

		this.setDisposition(proposal.getDisposition());
		this.setDecisionStatus(proposal.getStatus());		
	}
	
	private void initializeFromSimpleProposal(SimpleProposal proposal) {
		initializeFromItem(proposal.getItem());
		initializeFromProposalManagementInformation(proposal.getProposalManagementInformation());
		this.setProposalUuid(proposal.getUuid());
		this.setProposalClass(proposal.getClass());
	}
	
	private void initializeFromSupersession(Supersession supersession) {
		initializeFromProposalManagementInformation(supersession.getProposalManagementInformations().get(0));
		
		this.setProposalUuid(supersession.getUuid());
		this.proposalClass = supersession.getClass();
		this.setName(supersession.getName());
		this.setRegisterUuid(supersession.getTargetRegister().getId());
		
		if (!supersession.getSupersessionParts().isEmpty()) {
			this.setRegisterManagerNotes(supersession.getSupersessionParts().get(0).getRegisterManagerNotes());
			this.setControlBodyNotes(supersession.getSupersessionParts().get(0).getControlBodyNotes());
		}
		
		for (RE_RegisterItem si : supersession.getSupersededItems()) {
			this.supersededItems.add(new RegisterItemViewBean(si));
		}
		for (RE_RegisterItem si : supersession.getSupersedingItems()) {
			this.supersedingItems.add(new RegisterItemViewBean(si));
		}
		
		this.setProposalType(ProposalType.SUPERSESSION);
	}

	private void initializeFromAppeal(Appeal appeal) {
		Proposal appealedProposal = appeal.getAppealedProposal();
		if (appealedProposal instanceof SimpleProposal) {
			initializeFromSimpleProposal((SimpleProposal)appealedProposal);
		}
		else if (appealedProposal instanceof Supersession) {
			initializeFromSupersession((Supersession)appealedProposal);
		}
		
		this.isAppealed = true;
		if (appeal.isDecided()) {
			this.setProposalStatus(ProposalStatus.FINISHED);
		}
		else {
			this.setProposalStatus(ProposalStatus.APPEALED);			
		}
		this.appealDisposition = appeal.getDisposition();
	}
	
	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Class<?> getItemClass() {
		return itemClass;
	}

	public void setItemClass(Class<?> itemClass) {
		this.itemClass = itemClass;
	}

	/**
	 * @return the itemIdentifier
	 */
	public BigInteger getItemIdentifier() {
		return itemIdentifier;
	}

	/**
	 * @param itemIdentifier the itemIdentifier to set
	 */
	public void setItemIdentifier(BigInteger itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public RE_ItemStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(RE_ItemStatus status) {
		this.status = status;
	}

	/**
	 * @return the disposition
	 */
	public RE_Disposition getDisposition() {
		return disposition;
	}

	/**
	 * @param disposition the disposition to set
	 */
	public void setDisposition(RE_Disposition disposition) {
		this.disposition = disposition;
	}

	/**
	 * @return the proposalType
	 */
	public ProposalType getProposalType() {
		return proposalType;
	}

	/**
	 * @param proposalType the proposalType to set
	 */
	public void setProposalType(ProposalType proposalType) {
		this.proposalType = proposalType;
	}

	/**
	 * @return the proposalDate
	 */
	public Date getProposalDate() {
		return proposalDate;
	}

	/**
	 * @param proposalDate the proposalDate to set
	 */
	public void setProposalDate(Date proposalDate) {
		this.proposalDate = proposalDate;
	}

	/**
	 * @return the decisionStatus
	 */
	public RE_DecisionStatus getDecisionStatus() {
		return decisionStatus;
	}

	/**
	 * @param decisionStatus the decisionStatus to set
	 */
	public void setDecisionStatus(RE_DecisionStatus decisionStatus) {
		this.decisionStatus = decisionStatus;
	}

	/**
	 * @return the itemClassUuid
	 */
	public UUID getItemClassUuid() {
		return itemClassUuid;
	}

	/**
	 * @param itemClassUuid the itemClassUuid to set
	 */
	public void setItemClassUuid(UUID itemClassUuid) {
		this.itemClassUuid = itemClassUuid;
	}

	/**
	 * @return the itemClassName
	 */
	public String getItemClassName() {
		return itemClassName;
	}

	/**
	 * @param itemClassName the itemClassName to set
	 */
	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}

	/**
	 * @return the isProposal
	 */
	public boolean isProposal() {
		return isProposal;
	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * @param isProposal the isProposal to set
	 */
	public void setProposal(boolean isProposal) {
		this.isProposal = isProposal;
	}

	/**
	 * @return the sponsorUuid
	 */
	public UUID getSponsorUuid() {
		return sponsorUuid;
	}

	/**
	 * @param sponsorUuid the sponsorUuid to set
	 */
	public void setSponsorUuid(UUID sponsorUuid) {
		this.sponsorUuid = sponsorUuid;
	}

	/**
	 * @return the sponsorName
	 */
	public String getSponsorName() {
		return sponsorName;
	}

	/**
	 * @param sponsorName the sponsorName to set
	 */
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	/**
	 * @return the isAppealed
	 */
	public boolean isAppealed() {
		return isAppealed;
	}

	/**
	 * @param isAppealed the isAppealed to set
	 */
	public void setAppealed(boolean isAppealed) {
		this.isAppealed = isAppealed;
	}

	/**
	 * @return the proposalUuid
	 */
	public UUID getProposalUuid() {
		return proposalUuid;
	}

	/**
	 * @param proposalUuid the proposalUuid to set
	 */
	public void setProposalUuid(UUID proposalUuid) {
		this.proposalUuid = proposalUuid;
	}

	public Class<?> getProposalClass() {
		return proposalClass;
	}

	public void setProposalClass(Class<?> proposalClass) {
		this.proposalClass = proposalClass;
	}

	/**
	 * @return the proposalStatus
	 */
	public ProposalStatus getProposalStatus() {
		return proposalStatus;
	}

	/**
	 * @param proposalStatus the proposalStatus to set
	 */
	public void setProposalStatus(ProposalStatus proposalStatus) {
		this.proposalStatus = proposalStatus;
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

	/**
	 * @return the proposedChange
	 */
	public Map<String, String> getProposedChange() {
		return proposedChange;
	}

	/**
	 * @param proposedChange the proposedChange to set
	 */
	public void setProposedChange(Map<String, String> proposedChange) {
		this.proposedChange = proposedChange;
	}

	/**
	 * @return the appealDisposition
	 */
	public AppealDisposition getAppealDisposition() {
		return appealDisposition;
	}

	/**
	 * @param appealDisposition the appealDisposition to set
	 */
	public void setAppealDisposition(AppealDisposition appealDisposition) {
		this.appealDisposition = appealDisposition;
	}

	/**
	 * @return the registers
	 */
	public UUID getRegisterUuid() {
		return registerUuid;
	}

	/**
	 * @return the targetRegister
	 */
	public String getRegisterName() {
		return registerName;
	}

	/**
	 * @param targetRegister the targetRegister to set
	 */
	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	/**
	 * @return the supersededItems
	 */
	public Set<RegisterItemViewBean> getSupersededItems() {
		return supersededItems;
	}

	/**
	 * @return the supersedingItems
	 */
	public Set<RegisterItemViewBean> getSupersedingItems() {
		return supersedingItems;
	}

	/**
	 * @return the additionalProperties
	 */
	public Map<String, Object> getAdditionalProperties() {
		return Collections.unmodifiableMap(additionalProperties);
	}
	
	public void addAdditionalProperty(String propertyName, Object value) {
		this.additionalProperties.put(propertyName, value);
	}
	
	/**
	 * Returns all proposal management information records sorted by date.
	 * 
	 * @return
	 */
	public SortedSet<RE_ProposalManagementInformation> getSortedProposalManagementInformations() {
		SortedSet<RE_ProposalManagementInformation> result = new TreeSet<RE_ProposalManagementInformation>(new Comparator<RE_ProposalManagementInformation>() {

			@Override
			public int compare(RE_ProposalManagementInformation o1, RE_ProposalManagementInformation o2) {
				int result = getRelevantDate(o1).compareTo(getRelevantDate(o2));
				if (result == 0) {
					result = 1;
				}
				
				return result;
			}

			public Date getRelevantDate(RE_ProposalManagementInformation pmi) {
				Date result;
				if (pmi.getDateDisposed() != null) {
					result = pmi.getDateDisposed();
				}
				else if (pmi.getDateProposed() != null) {
					result = pmi.getDateProposed();
				}
				else {
					result = Calendar.getInstance().getTime();
				}
				
				return result;
			}
		});
		
		result.addAll(additionInformations);
		result.addAll(clarificationInformations);
		result.addAll(amendmentInformations);
		
		return result;
	}

	/**
	 * @return the additionInformations
	 */
	public Set<RE_AdditionInformation> getAdditionInformations() {
		return additionInformations;
	}

	/**
	 * @return the amendmentInformations
	 */
	public Set<RE_AmendmentInformation> getAmendmentInformations() {
		return amendmentInformations;
	}

	/**
	 * @return the clarificationInformations
	 */
	public Set<RE_ClarificationInformation> getClarificationInformations() {
		return clarificationInformations;
	}

	/**
	 * @param registerUuid the registerUuid to set
	 */
	public void setRegisterUuid(UUID registerUuid) {
		this.registerUuid = registerUuid;
	}

	/**
	 * @return the predecessors
	 */
	public Map<UUID, String> getPredecessors() {
		return predecessors;
	}

	/**
	 * @return the successors
	 */
	public Map<UUID, String> getSuccessors() {
		return successors;
	}

	public boolean isUnderReview() {
		return this.getProposalStatus().equals(ProposalStatus.UNDER_REVIEW);
	}

	public boolean isPending() {
		return this.getProposalStatus().equals(ProposalStatus.IN_APPROVAL_PROCESS);
	}
	
	public boolean isAppealable() {
		return this.getProposalStatus().equals(ProposalStatus.APPEALABLE) && !this.isAppealed();
	}
	
	public boolean isWithdrawable() {
		return isPending() || isUnderReview();
	}

	public boolean isFinished() {
		return this.getProposalStatus().equals(ProposalStatus.FINISHED);
	}

	public boolean isWithdrawn() {
		return this.getDisposition().equals(RE_Disposition.WITHDRAWN);
	}

	public boolean isValid() {
		return this.getStatus().equals(RE_ItemStatus.VALID);
	}
	
	public boolean isAddition() {
		return this.getProposalType().equals(ProposalType.ADDITION);
	}

	public boolean isClarification() {
		return this.getProposalType().equals(ProposalType.CLARIFICATION);
	}
	
	public boolean isSupersession() {
		return this.getProposalType().equals(ProposalType.SUPERSESSION);
	}

	public boolean isRetirement() {
		return this.getProposalType().equals(ProposalType.RETIREMENT);
	}
}
