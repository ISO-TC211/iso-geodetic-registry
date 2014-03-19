/**
 * 
 */
package org.iso.registry.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.geoinfoffm.registry.api.RegistryUserUpdateDTO;
import de.geoinfoffm.registry.core.model.RegistryUser;
import de.geoinfoffm.registry.core.model.Role;
import de.geoinfoffm.registry.soap.CreateRegistryUserRequest;

/**
 * @author Florian Esser
 *
 */
public class RegistryUserFormBean
{
	private UUID uuid;
	private String name;
	private String emailAddress;
	private String password;
	private String confirmedPassword;
	private String preferredLanguage;
	private List<String> roles = new ArrayList<String>();
	private boolean isActive = true;
	
	public RegistryUserFormBean() {
		
	}
	
	public RegistryUserFormBean(RegistryUser userData) {
		this.uuid = userData.getUuid();
		this.name = userData.getName().toString();
		this.emailAddress = userData.getEmailAddress().toString();
		this.preferredLanguage = userData.getPreferredLanguage().toString();

		for (Role role : userData.getRoles()) {
			this.roles.add(role.getName());
		}
		
		this.isActive = userData.isActive();
	}
	
	/**
	 * @return
	 */
	public CreateRegistryUserRequest toRegistrationDTO() {
		CreateRegistryUserRequest result = new CreateRegistryUserRequest();
		result.setName(this.name);
		result.setEmailAddress(this.emailAddress);
		result.setPassword(this.password);
		result.setPreferredLanguage(this.preferredLanguage);
		result.setActive(this.isActive);
		
		return result;
	}
	
	public RegistryUserUpdateDTO toUpdateDTO(UUID uuid) {
		RegistryUserUpdateDTO result = new RegistryUserUpdateDTO();
		result.setUuid(uuid);
		result.setName(this.name);
		result.setEmailAddress(this.emailAddress);
		result.setPassword(this.password);
		result.setPreferredLanguage(this.preferredLanguage);
		result.setActive(this.isActive);
		result.setRoles(this.roles);
		
		return result;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
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
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the confirmedPassword
	 */
	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	/**
	 * @param confirmedPassword the confirmedPassword to set
	 */
	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	/**
	 * @return the preferredLanguage
	 */
	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	/**
	 * @param preferredLanguage the preferredLanguage to set
	 */
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public List<String> getRoles() {
		return this.roles;
	}
	
	public void addRole(String role) {
		this.roles.add(role);
	}
	
	public void removeRole(String role) {
		this.roles.remove(role);
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isRegistryPointOfContact the isRegistryPointOfContact to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
