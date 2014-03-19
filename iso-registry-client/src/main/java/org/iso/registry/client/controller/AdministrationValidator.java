/**
 * 
 */
package org.iso.registry.client.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.core.model.RegistryUser;

/**
 * @author Florian.Esser
 *
 */
public class AdministrationValidator implements Validator
{
	private RegistryUserService userService;
	
	public AdministrationValidator(RegistryUserService userService) {
		this.userService = userService;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(RegistryUserFormBean.class);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		RegistryUserFormBean user = (RegistryUserFormBean)target;
		boolean isNew = (user.getUuid() == null); 
		
		if (isNew && user.getName().length() < 2) {
			errors.rejectValue("name", "form.validation.tooShort");
		}
		
		if (user.getPassword() == null || user.getConfirmedPassword() == null || !user.getPassword().equals(user.getConfirmedPassword())) {
			errors.rejectValue("confirmedPassword", "form.validation.dontmatch");
		}
		
		if (!userService.isEmailAddressAvailable(user.getEmailAddress())) {
			if (!isNew) {
				RegistryUser otherUser = userService.findByEmailAddress(user.getEmailAddress());
				if (!otherUser.getUuid().equals(user.getUuid())) {
					errors.rejectValue("emailAddress", "form.validation.unavailable");
				}
			}
			else {
				errors.rejectValue("emailAddress", "form.validation.unavailable");
			}
		}
	}

}
