package org.iso.registry.client.controller;

import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.ex.ResetPasswordException;
import org.iso.registry.client.dto.PasswordForgotDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    private final RegistryUserService userService;

    public ForgotPasswordController(RegistryUserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }

    @GetMapping
    public String displayForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping
    public String processForgotPasswordForm
            (@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
             BindingResult result) {
        if (result.hasErrors()) {
            return "forgot-password";
        }

        try {
            this.userService.requestPasswordReset(form.getEmail());
        }
        catch (ResetPasswordException e) {
            logger.warn("Request reset password for email failed {}, reason: {}", form.getEmail(), e.getMessage());
        }

        return "redirect:/forgot-password?success";

    }
}
