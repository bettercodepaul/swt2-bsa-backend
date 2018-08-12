package app.bogenliga.application.services.v1.hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import app.bogenliga.application.common.service.ServiceFacade;

@Deprecated
@RestController
public class HelloWorldServiceV1 implements ServiceFacade {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("v1/hello-world")
    @ResponseBody
    public Greeting sayHello(
            @RequestParam(name = "name", required = false, defaultValue = "Stranger") final String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
