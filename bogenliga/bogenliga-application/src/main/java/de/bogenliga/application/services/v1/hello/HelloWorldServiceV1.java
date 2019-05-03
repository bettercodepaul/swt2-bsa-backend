package de.bogenliga.application.services.v1.hello;

import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.services.v2.hello.HelloWorldServiceV2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Example REST service
 *
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @deprecated Remove REST service version dummy
 */
@Api("HelloWorldService")
@Deprecated
@RestController
public class HelloWorldServiceV1 implements ServiceFacade {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServiceV2.class);

    private static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("v1/hello-world")
    @ApiOperation(value = "HelloWorld Resource",
            httpMethod = "GET",
            notes = "Say 'Hello'")
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "Greeting for {name}",
                    response = Greeting.class),
    })
    public Greeting sayHello(
            @ApiParam(value = "Your name", defaultValue = "Stranger", allowEmptyValue = true, type = "String")
            @RequestParam(name = "name", required = false, defaultValue = "Stranger")
                    String name) {
        logger.info("HelloWorldServiceV1#sayHello() invoked with name '{}'", name);

        return new Greeting(counter.incrementAndGet(), String.format(TEMPLATE, name));
    }

}
