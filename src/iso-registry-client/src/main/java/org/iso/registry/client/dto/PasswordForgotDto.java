package org.iso.registry.client.dto;

import de.geoinfoffm.registry.client.web.recaptcha.ValidReCaptcha;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class PasswordForgotDto {

    @Email
    @NotEmpty
    private String email;

    @ValidReCaptcha
    private String reCaptchaResponse;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReCaptchaResponse() {
        return reCaptchaResponse;
    }

    public void setReCaptchaResponse(String reCaptchaResponse) {
        this.reCaptchaResponse = reCaptchaResponse;
    }
}
