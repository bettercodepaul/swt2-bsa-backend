package de.bogenliga.application.services.v1.errortesting;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class ErrorTestingServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @InjectMocks
    private ErrorTestingService underTest;


    @Test
    public void generateErrorCode_withBusinessError() {
        assertException(BusinessException.class, ErrorCode.NO_SESSION_ERROR.name());
    }


    @Test
    public void generateErrorCode_withTechnicalError() {
        assertException(TechnicalException.class, ErrorCode.DATABASE_CONNECTION_ERROR.name());
    }


    @Test
    public void generateErrorCode_withRuntimeError() {
        assertException(RuntimeException.class, ErrorCode.UNDEFINED.name());
    }


    @Test
    public void generateErrorCode_withNullpointerError() {
        assertException(NullPointerException.class, "unknown");
    }


    private void assertException(final Class<? extends RuntimeException> expectedClass, final String errorCode) {
        assertThatExceptionOfType(expectedClass)
                .isThrownBy(() -> underTest.generateErrorCode(errorCode))
                .withNoCause();
    }
}