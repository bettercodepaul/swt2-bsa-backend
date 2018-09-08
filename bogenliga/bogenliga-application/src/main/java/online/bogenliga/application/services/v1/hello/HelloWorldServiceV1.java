package online.bogenliga.application.services.v1.hello;

import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import online.bogenliga.application.common.service.ServiceFacade;
import online.bogenliga.application.services.v2.hello.HelloWorldServiceV2;

/**
 * Example REST service
 *
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @deprecated Remove REST service version dummy
 */
@Deprecated
@RestController
public class HelloWorldServiceV1 implements ServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServiceV2.class);

    private static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("v1/hello-world")
    public Greeting sayHello(
            @RequestParam(name = "name", required = false, defaultValue = "Stranger") final String name) {
        logger.info("HelloWorldServiceV1#sayHello() invoked with name '{}'", name);

        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

}
