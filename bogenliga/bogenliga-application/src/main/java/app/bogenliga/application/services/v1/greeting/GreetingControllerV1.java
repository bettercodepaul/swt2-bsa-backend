package app.bogenliga.application.services.v1.greeting;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import app.bogenliga.application.business.greeting.Greeting;

@RestController
@RequestMapping("v1/greeting")
public class GreetingControllerV1 {

    private static final String templateHello = "Hello, %s!";
    private static final String templateBye = "Bye, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(templateHello, name));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Greeting greetingDelete(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(templateBye, name));
    }
}
