package org.iso.registry.restapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}