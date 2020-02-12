package org.iso.registry.client.controller;

import de.geoinfoffm.registry.api.RegistryUserService;
import de.geoinfoffm.registry.api.ex.EmptyPasswordException;
import de.geoinfoffm.registry.api.ex.ResetPasswordException;
import org.iso.registry.client.dto.PasswordResetDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/reset-password")
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    private final RegistryUserService userService;

    public PasswordResetController(RegistryUserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordReset() {
        return new PasswordResetDto();
    }

    @GetMapping
    public String displayResetPasswordPage(
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String mail,
            Model model
    ) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(mail)) {
            model.addAttribute("error", "Token and email are required, please request a new password reset.");
            return "reset-password";
        }

        try {
            this.userService.resetPassword(mail, token, null);
        }
        catch (ResetPasswordException e) {
            model.addAttribute("error", "Token has expired or email incorrect, please request a new password reset.");
        }
        catch (EmptyPasswordException ignored) {
            logger.debug("expected exception to check token validity");
            model.addAttribute("mail", mail);
            model.addAttribute("token", token);
        }

        return "reset-password";
    }

    @PostMapping
    @Transactional
    public String handlePasswordReset(
            @ModelAttribute("passwordResetForm") @Valid PasswordResetDto form,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", form);
            return "redirect:/reset-password?token=" + form.getToken() + "&mail=" + form.getMail();
        }

        try {
            this.userService.resetPassword(form.getMail(), form.getToken(), form.getPassword());
        }
        catch (ResetPasswordException | EmptyPasswordException e) {
            logger.warn("reset password error, email= {}, token= {}", form.getMail(), form.getToken());
            return "redirect:/reset-password?token=" + form.getToken() + "&mail=" + form.getMail();
        }

        return "redirect:/login?resetSuccess";
    }

}