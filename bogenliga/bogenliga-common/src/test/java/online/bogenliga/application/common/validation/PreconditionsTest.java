package online.bogenliga.application.common.validation;

import java.util.List;
import org.junit.Test;
import online.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThatCode;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class PreconditionsTest {
    private static final String MESSAGE = "errorMessage";


    @Test
    public void checkNotNull() {
        assertThatCode(() -> Preconditions.checkNotNull("", MESSAGE)).doesNotThrowAnyException();
    }


    @Test
    public void checkNotNull_withLong() {
        assertThatCode(() -> Preconditions.checkNotNull(new Long(123L), MESSAGE)).doesNotThrowAnyException();
    }


    @Test
    public void checkNotNull_withNull_shouldThrowException() {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> Preconditions.checkNotNull(null, MESSAGE))
                .withMessageContaining(MESSAGE)
                .withNoCause();
    }


    @Test
    public void checkNotNullOrEmpty() {
        assertThatCode(() -> Preconditions.checkNotNullOrEmpty("notEmpty", MESSAGE)).doesNotThrowAnyException();
    }


    @Test
    public void checkNotNull_withNullList_shouldThrowException() {
        final List<String> input = null;
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> Preconditions.checkNotNull(input, MESSAGE))
                .withMessageContaining(MESSAGE)
                .withNoCause();
    }


    @Test
    public void checkNotNullOrEmpty_withEmpty_shouldThrowException() {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> Preconditions.checkNotNullOrEmpty("", MESSAGE))
                .withMessageContaining(MESSAGE)
                .withNoCause();
    }


    @Test
    public void checkNotNullOrEmpty_withNull_shouldThrowException() {
        final String input = null;
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> Preconditions.checkNotNullOrEmpty(input, MESSAGE))
                .withMessageContaining(MESSAGE)
                .withNoCause();
    }


    @Test
    public void checkArgument_withFalse_shouldThrowException() {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> Preconditions.checkArgument(1 > 2, MESSAGE))
                .withMessageContaining(MESSAGE)
                .withNoCause();
    }


    @Test
    public void checkArgument_withTrue() {
        assertThatCode(() -> Preconditions.checkArgument(1 < 2, MESSAGE)).doesNotThrowAnyException();
    }
}