package online.bogenliga.application.common.errorhandling.exception;

import org.junit.Test;
import online.bogenliga.application.common.errorhandling.ErrorCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class BusinessExceptionTest {

    private static final String MESSAGE = "message";
    private static final String PARAM1 = "param1";
    private static final int PARAM2 = 123;


    @Test
    public void testExceptionConstructor() {
        assertException(new BusinessException(ErrorCode.UNDEFINED, MESSAGE));
    }


    @Test
    public void testExceptionConstructor_withCause() {
        assertException(new BusinessException(ErrorCode.UNDEFINED, MESSAGE, new NullPointerException()));
    }


    @Test
    public void testExceptionConstructor_withParam() {
        assertException(new BusinessException(ErrorCode.UNDEFINED, MESSAGE, PARAM1, PARAM2));
    }


    @Test
    public void testExceptionConstructor_withCause_withParam() {
        assertException(
                new BusinessException(ErrorCode.UNDEFINED, MESSAGE, new NullPointerException(), PARAM1, PARAM2));
    }


    @Test
    public void testExceptionConstructor_withCause_withConfigs() {
        assertException(
                new BusinessException(ErrorCode.UNDEFINED, MESSAGE, new NullPointerException(), true, true));
    }


    private void assertException(final CoreException e) {
        assertThatExceptionOfType(e.getClass())
                .isThrownBy(
                        () -> assertThrowException(e))
                .withMessageContaining(MESSAGE)
                .withMessageContaining(ErrorCode.UNDEFINED.toString())
                .withNoCause();
    }


    private void assertThrowException(final CoreException e) {
        throw e;
    }
}