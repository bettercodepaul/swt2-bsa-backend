package online.bogenliga.application.integrationtests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import online.bogenliga.application.services.v1.hello.Greeting;
import static org.assertj.core.api.BDDAssertions.then;


/**
 * Integration test
 *
 * Use Spring Boot TEST profile to mock the database connection.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html">
 * Spring Boot - Testing</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
@ActiveProfiles("TEST")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldServiceV1IT {

    private static final String NAME = "name";
    private static final String MESSAGE_FORMAT = "Hello, %s!";
    /*
     * Configure REST endpoint
     */
    private final String baseUrl = "http://localhost";
    private final String resourceUrl = "/v1/hello-world";
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    public void requestGreeting_withoutName_shouldReturnDefaultName() {
        final String expected = String.format(MESSAGE_FORMAT, "Stranger"); // default value
        final String serviceUrl = baseUrl + ":" + this.port + resourceUrl;

        assertGetGreetingRequest(serviceUrl, expected);
    }


    @Test
    public void requestGreeting_withName_shouldReturnName() {
        final String expected = String.format(MESSAGE_FORMAT, NAME);
        final String serviceUrl = baseUrl + ":" + this.port + resourceUrl + "?name=" + NAME;

        assertGetGreetingRequest(serviceUrl, expected);
    }


    private void assertGetGreetingRequest(final String serviceUrl, final String expected) {
        final ResponseEntity<Greeting> entity = this.testRestTemplate.getForEntity(serviceUrl, Greeting.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(entity.getBody())
                .isNotNull()
                .isInstanceOf(Greeting.class);
        then(entity.getBody().getId())
                .isPositive();
        then(entity.getBody().getContent())
                .isEqualTo(expected);
    }
}
