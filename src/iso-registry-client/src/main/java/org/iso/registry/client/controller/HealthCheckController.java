package org.iso.registry.client.controller;

import de.bespire.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.OK;

@Controller
public class HealthCheckController {
    private static final Logger logger = LoggerFactory.make();

    @ResponseStatus(OK)
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public void ping(HttpServletResponse response) {
        logger.debug("Pinging me. I m OK.");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
