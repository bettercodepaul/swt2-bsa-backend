package app.bogenliga.application.services.v2.hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import app.bogenliga.application.business.hello.Greeting;

@Controller
public class HelloWorldControllerV2 {

    private static final String template = "Hi, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("v2/hello-world")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
