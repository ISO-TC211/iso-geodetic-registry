package org.iso.registry.client.controller;

import de.geoinfoffm.registry.api.RegistryUserService;
import org.iso.registry.client.dto.PasswordForgotDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired
    private RegistryUserService userService;
//    @Autowired private PasswordResetTokenRepository tokenRepository;
//    @Autowired private EmailService emailService;

    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }

    @GetMapping
    public String displayForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping
    public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
                                            BindingResult result,
                                            HttpServletRequest request) {
        if (result.hasErrors()){
            return "forgot-password";
        }

//        User user = userService.findByEmail(form.getEmail());
//        if (user == null){
//            result.rejectValue("email", null, "We could not find an account for that e-mail address.");
//            return "forgot-password";
//        }
//
//        PasswordResetToken token = new PasswordResetToken();
//        token.setToken(UUID.randomUUID().toString());
//        token.setUser(user);
//        token.setExpiryDate(30);
//        tokenRepository.save(token);
//
//        Mail mail = new Mail();
//        mail.setFrom("no-reply@memorynotfound.com");
//        mail.setTo(user.getEmail());
//        mail.setSubject("Password reset request");
//
//        Map<String, Object> model = new HashMap<>();
//        model.put("token", token);
//        model.put("user", user);
//        model.put("signature", "https://memorynotfound.com");
//        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        model.put("resetUrl", url + "/reset-password?token=" + token.getToken());
//        mail.setModel(model);
//        emailService.sendEmail(mail);
        try {
            this.userService.requestPasswordReset(form.getEmail());
        }
        catch (Exception e) {
            logger.warn("Request reset password for email failed {}, reason: {}", form.getEmail(), e.getMessage());
        }


        return "redirect:/forgot-password?success";

    }
}
