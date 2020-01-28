package org.iso.registry.client.boot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @ResponseBody
    @GetMapping("version")
    public String getVersion() {
        return "1.2.3";
    }
}
