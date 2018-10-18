package de.bogenliga.application.services.common.errorhandling;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class CustomizedResponseEntityExceptionHandlerTest {

    private static final ErrorCode ERROR_CODE = ErrorCode.ENTITY_NOT_FOUND_ERROR;
    private static final String MESSAGE = "message";
    private static final int PARAM1 = 123;
    private static final String PARAM2 = "param2";

    private CustomizedResponseEntityExceptionHandler underTest;


    @Test
    public void handleMethodArgumentNotValid() {

    }


    @Test
    public void handleBusinessException_withMessage_withParameter() {
        // prepare test data
        final BusinessException exception = new BusinessException(ERROR_CODE, MESSAGE, PARAM1,
                PARAM2);
        // configure mocks

        // call test method
        underTest = new CustomizedResponseEntityExceptionHandler();
        final ResponseEntity<ErrorDTO> actual = underTest.handleBusinessException(exception, mock(WebRequest.class));

        // assert result
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isNotNull();

        final ErrorDTO actualError = actual.getBody();

        assertThat(actualError.getErrorCode()).isEqualTo(ERROR_CODE);
        assertThat(actualError.getErrorMessage())
                .contains(ERROR_CODE.getValue())
                .contains(MESSAGE);
        assertThat(actualError.getParam())
                .contains(String.valueOf(PARAM1))
                .contains(PARAM2);

        // verify invocations
    }


    @Test
    public void handleBusinessException_withMessage() {
        assertHandleBusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, HttpStatus.NOT_FOUND);
        assertHandleBusinessException(ErrorCode.INVALID_ARGUMENT_ERROR, HttpStatus.BAD_REQUEST);
        assertHandleBusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, HttpStatus.CONFLICT);
        assertHandleBusinessException(ErrorCode.NO_SESSION_ERROR, HttpStatus.UNAUTHORIZED);
        assertHandleBusinessException(ErrorCode.NO_PERMISSION_ERROR, HttpStatus.FORBIDDEN);
        assertHandleBusinessException(ErrorCode.UNDEFINED, HttpStatus.BAD_REQUEST);

    }


    @Test
    public void handleTechnicalException_withMessage_withParam() {
        // prepare test data
        final TechnicalException exception = new TechnicalException(ERROR_CODE, MESSAGE, PARAM1, PARAM2);
        // configure mocks

        // call test method
        underTest = new CustomizedResponseEntityExceptionHandler();
        final ResponseEntity<ErrorDTO> actual = underTest.handleTechnicalException(exception, mock(WebRequest.class));

        // assert result
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(actual.getBody()).isNotNull();

        final ErrorDTO actualError = actual.getBody();

        assertThat(actualError.getErrorCode()).isEqualTo(ERROR_CODE);
        assertThat(actualError.getErrorMessage())
                .contains(ERROR_CODE.getValue())
                .contains(MESSAGE);
        assertThat(actualError.getParam())
                .contains(String.valueOf(PARAM1))
                .contains(PARAM2);

        // verify invocations
    }


    @Test
    public void handleTechnicalException_withMessage() {
        // prepare test data
        final TechnicalException exception = new TechnicalException(ERROR_CODE, MESSAGE);
        // configure mocks

        // call test method
        underTest = new CustomizedResponseEntityExceptionHandler();
        final ResponseEntity<ErrorDTO> actual = underTest.handleTechnicalException(exception, mock(WebRequest.class));

        // assert result
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(actual.getBody()).isNotNull();

        final ErrorDTO actualError = actual.getBody();

        assertThat(actualError.getErrorCode()).isEqualTo(ERROR_CODE);
        assertThat(actualError.getErrorMessage())
                .contains(ERROR_CODE.getValue())
                .contains(MESSAGE);
        assertThat(actualError.getParam()).isEmpty();

        // verify invocations
    }


    @Test
    public void handleTechnicalException_withDeprecatedVersionError() {
        // prepare test data
        final TechnicalException exception = new TechnicalException(ErrorCode.DEPRECATED_VERSION_ERROR, MESSAGE);
        // configure mocks

        // call test method
        underTest = new CustomizedResponseEntityExceptionHandler();
        final ResponseEntity<ErrorDTO> actual = underTest.handleTechnicalException(exception, mock(WebRequest.class));

        // assert result
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(actual.getBody()).isNotNull();

        final ErrorDTO actualError = actual.getBody();

        assertThat(actualError.getErrorCode()).isEqualTo(ErrorCode.DEPRECATED_VERSION_ERROR);
        assertThat(actualError.getErrorMessage())
                .contains(ErrorCode.DEPRECATED_VERSION_ERROR.getValue())
                .contains(MESSAGE);
        assertThat(actualError.getParam()).isEmpty();

        // verify invocations
    }


    @Test
    public void handleNullPointerException() {
        // prepare test data
        final NullPointerException exception = new NullPointerException(MESSAGE);
        // configure mocks

        // call test method
        underTest = new CustomizedResponseEntityExceptionHandler();
        final ResponseEntity<ErrorDTO> actual = underTest.handleNullPointerException(exception, mock(WebRequest.class));

        // assert result
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(actual.getBody()).isNotNull();

        final ErrorDTO actualError = actual.getBody();

        assertThat(actualError.getErrorCode()).isEqualTo(ErrorCode.UNEXPECTED_ERROR);
        assertThat(actualError.getErrorMessage())
                .contains(MESSAGE);
        assertThat(actualError.getParam()).isEmpty();

        // verify invocations
    }


    @Test
    public void handleRuntimeException() {
        // prepare test data
        final RuntimeException exception = new RuntimeException(MESSAGE);
        // configure mocks

        // call test method
        underTest = new CustomizedResponseEntityExceptionHandler();
        final ResponseEntity<ErrorDTO> actual = underTest.handleRuntimeException(exception, mock(WebRequest.class));

        // assert result
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(actual.getBody()).isNotNull();

        final ErrorDTO actualError = actual.getBody();

        assertThat(actualError.getErrorCode()).isEqualTo(ErrorCode.UNEXPECTED_ERROR);
        assertThat(actualError.getErrorMessage())
                .contains(MESSAGE);
        assertThat(actualError.getParam()).isEmpty();

        // verify invocations
    }


    private void assertHandleBusinessException(final ErrorCode errorCode, final HttpStatus expectedStatus) {
        // configure mocks
        final BusinessException expectedException = new BusinessException(errorCode, MESSAGE);
        // call test method
        underTest = new CustomizedResponseEntityExceptionHandler();
        final ResponseEntity<ErrorDTO> actual = underTest.handleBusinessException(expectedException,
                mock(WebRequest.class));

        // assert result
        assertThat(actual.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(actual.getBody()).isNotNull();

        final ErrorDTO actualError = actual.getBody();

        assertThat(actualError.getErrorCode()).isEqualTo(errorCode);
        assertThat(actualError.getErrorMessage())
                .contains(errorCode.getValue())
                .contains(MESSAGE);
        assertThat(actualError.getParam()).isEmpty();

        // verify invocations
    }
}