package de.bogenliga.application.common.errorhandling.exception;

import org.junit.Test;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class TechnicalExceptionTest {

    private static final String MESSAGE = "message";
    private static final String PARAM1 = "param1";
    private static final int PARAM2 = 123;


    @Test
    public void testExceptionConstructor() {
        assertExceptionWithMessage(new TechnicalException(ErrorCode.UNDEFINED, MESSAGE));
    }


    @Test
    public void testExceptionConstructor_withCause() {
        assertExceptionWithMessage(new TechnicalException(ErrorCode.UNDEFINED, MESSAGE, new NullPointerException()));
    }


    @Test
    public void testExceptionConstructor_withoutMessage_withCause() {
        assertExceptionWithoutMessage(new TechnicalException(ErrorCode.UNDEFINED, new NullPointerException()));
    }


    @Test
    public void testExceptionConstructor_withParam() {
        assertExceptionWithMessage(new TechnicalException(ErrorCode.UNDEFINED, MESSAGE, PARAM1, PARAM2));
    }


    @Test
    public void testExceptionConstructor_withCause_withParam() {
        assertExceptionWithMessage(
                new TechnicalException(ErrorCode.UNDEFINED, MESSAGE, new NullPointerException(), PARAM1, PARAM2));
    }


    @Test
    public void testExceptionConstructor_withoutMessage_withCause_withParam() {
        assertExceptionWithoutMessage(
                new TechnicalException(ErrorCode.UNDEFINED, new NullPointerException(), PARAM1, PARAM2));
    }


    @Test
    public void testExceptionConstructor_withCause_withConfigs() {
        assertExceptionWithMessage(
                new TechnicalException(ErrorCode.UNDEFINED, MESSAGE, new NullPointerException(), true, true));
    }


    private void assertExceptionWithMessage(final CoreException e) {
        assertThatExceptionOfType(e.getClass())
                .isThrownBy(
                        () -> assertThrowException(e))
                .withMessageContaining(MESSAGE)
                .withMessageContaining(ErrorCode.UNDEFINED.toString())
                .withNoCause();

        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.UNDEFINED);
        assertThat(e.getParameters()).isNotNull();
    }


    private void assertExceptionWithoutMessage(final CoreException e) {
        assertThatExceptionOfType(e.getClass())
                .isThrownBy(
                        () -> assertThrowException(e))
                .withMessageContaining(ErrorCode.UNDEFINED.toString())
                .withNoCause();

        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.UNDEFINED);
        assertThat(e.getParameters()).isNotNull();
    }


    private void assertThrowException(final CoreException e) {
        throw e;
    }
}