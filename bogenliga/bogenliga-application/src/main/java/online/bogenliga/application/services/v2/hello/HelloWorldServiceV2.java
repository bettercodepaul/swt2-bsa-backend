package online.bogenliga.application.services.v2.hello;

import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import online.bogenliga.application.common.service.ServiceFacade;

/**
 * Example REST service
 *
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @deprecated Remove REST service version dummy
 */
@Deprecated
@RestController
public class HelloWorldServiceV2 implements ServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServiceV2.class);

    private static final String TEMPLATE = "Hi, %s!";
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("v2/hello-world")
    @ResponseBody
    public Greeting sayHello(
            @RequestParam(name = "name", required = false, defaultValue = "Stranger") final String name) {
        logger.info("HelloWorldServiceV2#sayHello() invoked with name '{}'", name);

        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

}
