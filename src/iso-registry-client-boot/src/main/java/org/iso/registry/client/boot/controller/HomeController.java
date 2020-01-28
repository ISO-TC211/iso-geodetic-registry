package org.iso.registry.client.boot.controller;

import org.iso.registry.client.boot.dto.HomePage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
public class HomeController {

//    @CrossOrigin(origins = "http://localhost:9000")
    @RequestMapping(path = "/version", method = RequestMethod.GET)
    public HomePage getVersion() {
        return HomePage.builder().version("1.2.3").build();
    }
}
