package org.iso.registry.client.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.iso.registry.client.controller.registry.ProposalNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import de.geoinfoffm.registry.api.forum.PostDTO;
import de.geoinfoffm.registry.api.forum.ProposalDiscussionDTO;
import de.geoinfoffm.registry.api.forum.ProposalDiscussionInvitationDTO;
import de.geoinfoffm.registry.api.forum.ProposalDiscussionService;
import de.geoinfoffm.registry.core.UnauthorizedException;
import de.geoinfoffm.registry.core.forum.Discussion;
import de.geoinfoffm.registry.core.forum.Post;
import de.geoinfoffm.registry.core.forum.ProposalDiscussion;
import de.geoinfoffm.registry.core.forum.ProposalDiscussion.DiscussionType;
import de.geoinfoffm.registry.core.forum.ProposalDiscussionRepository;
import de.geoinfoffm.registry.core.model.Proposal;
import de.geoinfoffm.registry.core.model.ProposalRepository;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.security.RegistrySecurity;

/**
 * @author Florian.Esser
 *
 */
@Controller
@RequestMapping("/discussion")
public class DiscussionController
{
	private static final Logger logger = LoggerFactory.getLogger(DiscussionController.class);

	@Autowired
	private ProposalRepository proposalRepository;
	
	@Autowired
	private ProposalDiscussionRepository discussionRepository;

	@Autowired
	private ProposalDiscussionService discussionService;

	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	/*
	 * View discussion
	 */
	@RequestMapping(value = "/{uuid}", method = GET)
	@Transactional
	public String viewDiscussion(WebRequest request, 
							     @PathVariable("uuid") UUID discussionUuid,
							     @RequestParam(value = "token", required = false) String token,
							     final Model model) throws ProposalNotFoundException, UnauthorizedException {

		RegistryUser user = null;
		if (StringUtils.isEmpty(token)) {
			security.assertIsLoggedIn();
			user = security.getCurrentUser();
		}
		
		ProposalDiscussion discussion = discussionRepository.findOne(discussionUuid);
		Assert.notNull(discussion, "Discussion does not exist");

		String author;
		if (user == null) {
			author = discussion.getInviteeByToken(token);
			if (author == null) {
				throw new UnauthorizedException("Invalid access token");
			}
		}
		else {
			author = user.getEmailAddress();			
		}

		if (user != null && discussionService.isManager(discussion, user)) {
			model.addAttribute("isManager", "true");
			model.addAttribute("invitations", new ProposalDiscussionInvitationDTO(discussionUuid, ""));
		}
		model.addAttribute("discussion", new ProposalDiscussionViewBean(discussion));
		model.addAttribute("newPost", new PostDTO(discussion.getUuid(), author, token, "", ""));

		return "registry/proposal/discussion";
	}

	
	/*
	 * View discussion
	 */
	@RequestMapping(value = "/{uuid}", method = POST)
	@Transactional
	public String addPost(WebRequest request, 
						  @PathVariable("uuid") UUID discussionUuid, 
						  @ModelAttribute("newPost") PostDTO newPost,
						  final Model model) throws ProposalNotFoundException, UnauthorizedException {
		
		
		ProposalDiscussion discussion = discussionRepository.findOne(discussionUuid);	
		Assert.notNull(discussion, "Discussion does not exist");

		if (!security.isLoggedIn()) {
			Assert.isTrue(discussionService.mayPost(discussion, newPost.getAuthor(), newPost.getToken()), "You are not authorized to post to this discussion");
		}
		else {
			Assert.isTrue(discussionService.mayPost(discussion, security.getCurrentUser()), "You are not authorized to post to this discussion");
		}
		
		Post post = discussionService.post(newPost);
		
		if (!security.isLoggedIn()) { 
			return "redirect:/discussion/" + discussion.getUuid() + "?token=" + newPost.getToken();
		}
		else {
			return "redirect:/discussion/" + discussion.getUuid();
		}
	}

	@RequestMapping(value = "/{uuid}/invite", method = POST)
	@Transactional
	public String inviteParticipants(WebRequest request, 
			  @PathVariable("uuid") UUID discussionUuid,
			  @ModelAttribute("invitations") ProposalDiscussionInvitationDTO invitations,
			  final Model model) throws ProposalNotFoundException, UnauthorizedException {

		ProposalDiscussion discussion = discussionRepository.findOne(discussionUuid);	
		Assert.notNull(discussion, "Discussion does not exist");
		
		if (!StringUtils.isEmpty(invitations.getInvitees())) {
			discussionService.invite(discussion, invitations.getInvitees());
		}
		
		return "redirect:/discussion/" + discussion.getUuid();
	}

	/*
	 * View discussion
	 */
	@RequestMapping(value = "/{type}/{proposalUuid}", method = GET)
	@Transactional
	public String viewDiscussion(WebRequest request, 
								 @PathVariable("type") DiscussionType discussionType,
							     @PathVariable("proposalUuid") UUID proposalUuid, 
							     final Model model) throws ProposalNotFoundException, UnauthorizedException {

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		Assert.notNull(proposal, "Proposal does not exist");
		
		ProposalDiscussion discussion = discussionRepository.findByDiscussedProposalAndDiscussionType(proposal, discussionType);
		if (discussion == null) {
//			GdiDeRegistryUser user = (GdiDeRegistryUser)security.getCurrentUser();
//			ProposalDiscussionDTO discussionDto = new ProposalDiscussionDTO(user, proposal, discussionType, new ArrayList<Actor>());
//			discussion = discussionService.startDiscussion(discussionDto);
			return "redirect:/discussion/new/" + proposal.getUuid();
		}
		
		return "redirect:/discussion/" + discussion.getUuid();
	}

	@RequestMapping(value = "/new/{proposalUuid}", method = GET)
	@Transactional
	public String createDisucssion(WebRequest request,
								   @PathVariable("proposalUuid") UUID proposalUuid,
								   @ModelAttribute("discussion") ProposalDiscussionDTO discussion,
								   final Model model) throws UnauthorizedException {
		
		Assert.notNull(proposalUuid);
		
		security.assertIsLoggedIn();
		RegistryUser user = security.getCurrentUser();

		Proposal proposal = proposalRepository.findOne(proposalUuid);
		Assert.notNull(proposal, "Proposal does not exist");

		String rolePrefix;
		DiscussionType discussionType;
		if (proposal.isSubmitted()) {
			rolePrefix = RegistrySecurity.CONTROLBODY_ROLE_PREFIX;
			discussionType = DiscussionType.CONTROLBODY;
			discussion.setOwnerUuid(user.getOrganization().getUuid());
		}
		else {
			rolePrefix = RegistrySecurity.SUBMITTER_ROLE_PREFIX;
			discussionType = DiscussionType.SUBMITTER;
			discussion.setOwnerUuid(user.getUuid());
		}

		security.assertHasEntityRelatedRoleForAll(rolePrefix, proposal.getAffectedRegisters());
		discussion.setDiscussionType(discussionType);
		discussion.setDiscussedProposalUuid(proposalUuid);
		
		Map<String, Object> msgModel = new HashMap<String, Object>();
		msgModel.put("user", user);
		msgModel.put("register", proposal.getAffectedRegisters().get(0));
		
		String message = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mailtemplates/discussion_invitation.vm", "UTF-8", msgModel);
		discussion.setMessage(message);
		
		return "registry/proposal/discussion_new";
	}
	
	@RequestMapping(value = "/new/{proposalUuid}", method = POST)
	@Transactional
	public String startDiscussion(WebRequest request,
							      @PathVariable("proposalUuid") UUID proposalUuid,
							      @ModelAttribute("discussion") ProposalDiscussionDTO discussionDto,
							      final Model model) throws UnauthorizedException {
		
		Discussion discussion = discussionService.startDiscussion(discussionDto);
		
		return "redirect:/discussion/" + discussion.getUuid();
	}
}
