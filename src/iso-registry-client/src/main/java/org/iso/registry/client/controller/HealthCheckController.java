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
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/lrt", method = RequestMethod.GET)
    public void logRuntime() {
        Runtime runtime = Runtime.getRuntime();
        // Get current size of heap in bytes
        long heapSize = runtime.totalMemory();
        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
        long heapMaxSize = runtime.maxMemory();
        // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
        long heapFreeSize = runtime.freeMemory();
        logger.info("heapSize= {}", heapSize);
        logger.info("heapMaxSize= {}", heapMaxSize);
        logger.info("heapFreeSize= {}", heapFreeSize);
        logger.info("totalMemory= {}", runtime.totalMemory());
        logger.info("availableProcessors= {}", runtime.availableProcessors());
    }
}
