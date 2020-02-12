package org.iso.registry.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;

@Controller
public class HealthCheckController {

    @ResponseStatus(OK)
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public void ping() {
        System.out.println("Pinging me. I m OK.");
    }
}
