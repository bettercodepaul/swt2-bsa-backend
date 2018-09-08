package online.bogenliga.application.services.v2.hello;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class HelloWorldServiceV2Test {
    private static final String NAME = "name";
    private static final String MESSAGE_FORMAT = "Hi, %s!";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private HelloWorldServiceV2 underTest;


    @Test
    public void sayHello_withName() {
        // prepare test data
        final String expected = String.format(MESSAGE_FORMAT, NAME);

        // configure mocks

        // call test method
        final Greeting actual = underTest.sayHello(NAME);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getContent()).isEqualTo(expected);

        // verify invocations
    }


    @Test
    public void sayHello_withoutName() {
        // prepare test data
        final String expected = String.format(MESSAGE_FORMAT, "null"); // no spring annotation

        // configure mocks

        // call test method
        final Greeting actual = underTest.sayHello(null);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getContent()).isEqualTo(expected);

        // verify invocations
    }


    @Test
    public void sayHello_withAdditionalRequests() {
        // prepare test data
        final String expected = String.format(MESSAGE_FORMAT, NAME);

        // configure mocks

        // call test method
        for (int numberOfInvocations = 1; numberOfInvocations <= 5; numberOfInvocations++) {
            final Greeting actual = underTest.sayHello(NAME);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(numberOfInvocations);
            assertThat(actual.getContent()).isEqualTo(expected);
        }

        // verify invocations
    }
}