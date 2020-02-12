package org.iso.registry.client.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.client.web.signup.SignupFormBean;


@Deprecated
public class SignupValidator implements Validator
{
	private RegistryUserService userService;

	public SignupValidator(RegistryUserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(SignupFormBean.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SignupFormBean user = (SignupFormBean)target;
		
		if (user.getName().length() < 2) {
			errors.rejectValue("name", "form.validation.tooShort");
		}
		
		if (user.getPassword() == null || user.getConfirmedPassword() == null || !user.getPassword().equals(user.getConfirmedPassword())) {
			errors.rejectValue("confirmedPassword", "form.validation.dontmatch");
		}
		
		if (!userService.isEmailAddressAvailable(user.getEmailAddress())) {
			errors.rejectValue("emailAddress", "form.validation.unavailable");
		}
	}
}
