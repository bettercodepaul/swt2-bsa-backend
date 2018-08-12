package app.bogenliga.application.services.v2.hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import app.bogenliga.application.common.service.ServiceFacade;

@RestController
public class HelloWorldServiceV2 implements ServiceFacade {

    private static final String template = "Hi, %s!";
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("v2/hello-world")
    @ResponseBody
    public Greeting sayHello(
            @RequestParam(name = "name", required = false, defaultValue = "Stranger") final String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
