package org.iso.registry.client.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.geoinfoffm.registry.core.model.iso19135.RE_Register;
import de.geoinfoffm.registry.persistence.RegisterRepository;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HomePageController {

    @Resource
    private RegisterRepository registerRepository;
    
    @GetMapping("/findAll")
    public List<RE_Register> findAll() {
        return registerRepository.findAll();
    }

    @GetMapping("/version")
    public String version() {
        return "beta";
    }

    @GetMapping("/test")
    public String working() {
        return "i m working";
    }
}
