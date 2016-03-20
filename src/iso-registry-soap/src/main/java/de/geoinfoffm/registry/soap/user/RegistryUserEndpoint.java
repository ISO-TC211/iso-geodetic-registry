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
package de.geoinfoffm.registry.soap.user;

import static de.geoinfoffm.registry.core.security.RegistrySecurity.*;

import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBElement;

import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.hibernate.SessionFactory;
import org.isotc211.iso19135.RE_SubmittingOrganization_PropertyType;
import org.isotc211.iso19139.common.CharacterString_PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import de.geoinfoffm.registry.api.EntityNotFoundException;
import de.geoinfoffm.registry.api.OrganizationService;
import de.geoinfoffm.registry.api.Organization_PropertyType;
import de.geoinfoffm.registry.api.Organization_Type;
import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserRequest;
import de.geoinfoffm.registry.api.soap.CreateRegistryUserResponse;
import de.geoinfoffm.registry.api.soap.DelegateRoleRequest;
import de.geoinfoffm.registry.api.soap.DelegateRoleResponse;
import de.geoinfoffm.registry.api.soap.GetOrganizationDescriptionsRequest;
import de.geoinfoffm.registry.api.soap.GetOrganizationDescriptionsResponse;
import de.geoinfoffm.registry.api.soap.GetOrganizationRequest;
import de.geoinfoffm.registry.api.soap.GetOrganizationResponse;
import de.geoinfoffm.registry.api.soap.ObjectFactory;
import de.geoinfoffm.registry.api.soap.OrganizationDescription;
import de.geoinfoffm.registry.core.IllegalOperationException;
import de.geoinfoffm.registry.core.model.Delegation;
import de.geoinfoffm.registry.core.model.Organization;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.core.model.RoleRepository;
import de.geoinfoffm.registry.core.model.iso19135.RE_SubmittingOrganization;
import de.geoinfoffm.registry.core.security.RegistrySecurity;
import de.geoinfoffm.registry.soap.AbstractEndpoint;

/**
 * The class RegistryUserEndpoint.
 *
 * @author Florian Esser
 */
@Endpoint
public class RegistryUserEndpoint extends AbstractEndpoint
{
	private static final String NAMESPACE_URI = "http://registry.gdi-de.org/api/soap";

	@Autowired
	private RegistrySecurity security;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RegistryUserService userService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private ObjectFactory objectFactory = new ObjectFactory();
	private de.geoinfoffm.registry.api.ObjectFactory regObjectFactory = new de.geoinfoffm.registry.api.ObjectFactory();
	private org.isotc211.iso19139.common.ObjectFactory gcoObjectFactory = new org.isotc211.iso19139.common.ObjectFactory();
	private org.isotc211.iso19135.ObjectFactory grgObjectFactory = new org.isotc211.iso19135.ObjectFactory();
	
	@Transactional(readOnly = true)
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOrganizationDescriptionsRequest")
	public @ResponsePayload JAXBElement<GetOrganizationDescriptionsResponse> handleGetOrganizationDescriptionsRequest(@RequestPayload GetOrganizationDescriptionsRequest req) throws Exception {
		GetOrganizationDescriptionsResponse result = objectFactory.createGetOrganizationDescriptionsResponse();

		Map<UUID, Object[]> orgs = organizationService.getDescriptions();
		for (UUID id : orgs.keySet()) {
			Object[] data = orgs.get(id);
			OrganizationDescription desc = objectFactory.createOrganizationDescription();
			desc.setUuid(id.toString());
			desc.setName(data[1].toString());
			Object suborg = data[2];
			if (suborg != null && suborg instanceof RE_SubmittingOrganization) {
				desc.setSubmittingOrganizationUuid(((RE_SubmittingOrganization)suborg).getUuid().toString());
			}
			result.getOrganizationDescription().add(desc);
		}
		
		return objectFactory.createGetOrganizationDescriptionsResponse(result);
	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateGdiRegistryUserRequest")
	public @ResponsePayload JAXBElement<CreateRegistryUserResponse> handleCreateRegisterUserRequest(@RequestPayload CreateRegistryUserRequest req) throws Exception {
		security.assertHasRole(RegistrySecurity.ADMIN_ROLE);
	
		CreateRegistryUserResponse result = objectFactory.createCreateRegistryUserResponse();
		
		RegistryUser user = userService.registerUser(req);
		result.setUuid(user.getUuid().toString());
		return objectFactory.createCreateRegistryUserResponse(result);
	}

	@Transactional(readOnly = true)
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetOrganizationRequest")
	public @ResponsePayload JAXBElement<GetOrganizationResponse> handleGetOrganizationRequest(@RequestPayload GetOrganizationRequest req) throws Exception {
		GetOrganizationResponse result = objectFactory.createGetOrganizationResponse();
		
		Organization org = organizationService.findOne(UUID.fromString(req.getUuid()));
		if (org == null) {
			throw new EntityNotFoundException(String.format("Organization with UUID '%s' does not exist", req.getUuid()));
		}
		result.setOrganization(toType(org));
		
		return objectFactory.createGetOrganizationResponse(result);
	}
	
//	@Transactional
//	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateOrganizationRolesRequest")
//	public @ResponsePayload JAXBElement<UpdateOrganizationRolesResponse> handleUpdateOrganizationRolesRequest(@RequestPayload UpdateOrganizationRolesRequest req) throws Exception {
//		UpdateOrganizationRolesResponse result = objectFactory.createUpdateOrganizationRolesResponse();
//		
//		security.assertIsAdmin();
//		
//		Organization org = organizationService.findOne(UUID.fromString(req.getOrganizationUuid()));
//		if (org == null) {
//			throw new EntityNotFoundException(String.format("Organization with UUID '%s' does not exist", req.getOrganizationUuid()));
//		}
//		
//		OrganizationDTO dto = new OrganizationDTO(org);
//		
//		if (req.getNewRole() != null) {
//			for (String newRole : req.getNewRole()) {
//				dto.getRoles().add(newRole);
//			}
//		}
//		
//		if (req.getRevokeRole() != null) {
//			for (String revokedRole : req.getRevokeRole()) {
//				dto.getRoles().remove(revokedRole);
//			}
//		}
//		
//		org = organizationService.updateOrganization(dto);
//		result.setOrganization(toType(org));
//		
//		return objectFactory.createUpdateOrganizationRolesResponse(result);
//	}

//	@Transactional(readOnly = true)
//	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetRegistrUserDescriptionRequest")
//	public @ResponsePayload JAXBElement<GetRegistrUserDescriptionResponse> handleGetGdiRegistryUserDescriptionRequest(@RequestPayload GetRegistrUserDescriptionRequest req) throws Exception {
//		GetRegistrUserDescriptionResponse result = objectFactory.createGetRegistrUserDescriptionResponse();
//
//		RegistryUser currentUser = null;
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof UsernamePasswordAuthenticationToken) {
//			WSUsernameTokenPrincipal principal = (WSUsernameTokenPrincipal)((UsernamePasswordAuthenticationToken)authentication).getPrincipal();
//			currentUser = userService.findByEmailAddress(principal.getName());
//		}
//
//		GdiDeRegistryUser describedUser;
//		if (!security.isAdmin()) {
//			if (!StringUtils.isEmpty(req.getEmailAddress()) && !req.getEmailAddress().equalsIgnoreCase(currentUser.getEmailAddress())) {
//				throw new IllegalOperationException("You are not authorized to request the details of another user");
//			}
//			
//			describedUser = (GdiDeRegistryUser)currentUser;
//		}
//		else {
//			if (StringUtils.isEmpty(req.getEmailAddress())) {
//				describedUser = (GdiDeRegistryUser)currentUser;
//			}
//			else {
//				describedUser = userService.findByEmailAddress(req.getEmailAddress());
//				if (describedUser == null) {
//					throw new EntityNotFoundException(String.format("Not user exists with e-mail address '%s'", req.getEmailAddress()));
//				}
//			}
//		}
//
//		result.setUuid(describedUser.getUuid().toString());
//		result.setEmailAddress(describedUser.getEmailAddress());
//		result.setName(describedUser.getName());
//		result.setOrganizationUuid(describedUser.getOrganization().getUuid().toString());
//		result.setOrganizationName(describedUser.getOrganization().getName());
//		result.setPreferredLanguage(describedUser.getPreferredLanguage());
//		if (describedUser.getTelephone() != null && describedUser.getTelephone().getVoice() != null && !describedUser.getTelephone().getVoice().isEmpty()) {
//			result.setTelephone(describedUser.getTelephone().getVoice().get(0));
//		}
//		result.setPublishTelephone(describedUser.isPublishTelephone());
//		
//		for (GrantedAuthority auth : describedUser.getAuthorities()) {
//			result.getRole().add(auth.getAuthority());
//		}
//		
//		return objectFactory.createGetGdiRegistrUserDescriptionResponse(result);
//	}

	@Transactional
	@Namespace(prefix = "reg", uri = NAMESPACE_URI)
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "DelegateRoleRequest")
	public @ResponsePayload JAXBElement<DelegateRoleResponse> handleDelegateRoleRequest(@RequestPayload DelegateRoleRequest req) throws Exception {
		security.assertIsTrue(security.isAdmin() || security.hasEntityRelatedRole(POINTOFCONTACT_ROLE_PREFIX, UUID.fromString(req.getOrganizationUuid())));

		DelegateRoleResponse result = objectFactory.createDelegateRoleResponse();
		result.setResult("failed");

		RegistryUser user = userService.findOne(UUID.fromString(req.getUserUuid()));
		Organization organization = organizationService.findOne(UUID.fromString(req.getOrganizationUuid()));
		Role role = roleRepository.findByName(req.getRole());
		if (role == null) {
			result.setResult(String.format("Role '%s' does not exist", req.getRole()));
		}
		else {
			try {
				Delegation delegation = organizationService.delegate(user, role, organization);
				if (delegation != null) {
					result.setResult("success");
				}
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		return objectFactory.createDelegateRoleResponse(result);
	}

	private Organization_Type toType(Organization organization) {
		Organization_Type result = new Organization_Type();
		
		result.setName(toPropertyType(organization.getName()));
		result.setShortName(toPropertyType(organization.getShortName()));
//		result.setOnlineResourceLogo(toPropertyType(organization.getOnlineResourceLogo()));
//		result.setOnlineResourceWebsite(toPropertyType(organization.getOnlineResourceWebsite()));

		RE_SubmittingOrganization_PropertyType suborg = grgObjectFactory.createRE_SubmittingOrganization_PropertyType();
		suborg.setUuidref(organization.getSubmittingOrganization().getUuid().toString());
		result.setSubmittingOrganization(suborg);

		return result;
	}

	private Organization_PropertyType toPropertyType(Organization organization) {
		Organization_PropertyType result = regObjectFactory.createOrganization_PropertyType();
		result.setOrganization(toType(organization));
		
		return result;
	}
	
	private CharacterString_PropertyType toPropertyType(String characterString) {
		CharacterString_PropertyType result = gcoObjectFactory.createCharacterString_PropertyType();
		result.setCharacterString(gcoObjectFactory.createCharacterString(characterString));
		
		return result;
	}


}
