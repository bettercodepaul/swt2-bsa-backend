package de.bogenliga.application.services.common.errorhandling;

import org.junit.Test;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class ErrorDTOTest {

    private static final ErrorCode ERROR_CODE = ErrorCode.ENTITY_NOT_FOUND_ERROR;
    private static final String MESSAGE = "message";
    private static final String[] PARAM = {"param1", "param2"};


    @Test
    public void getErrorCode() {
        final ErrorDTO underTest = new ErrorDTO(ERROR_CODE);

        assertThat(underTest.getErrorCode()).isEqualTo(ERROR_CODE);
    }


    @Test
    public void getParam() {
        final ErrorDTO underTest = new ErrorDTO(ERROR_CODE, PARAM, MESSAGE);

        assertThat(underTest.getErrorCode()).isEqualTo(ERROR_CODE);
        assertThat(underTest.getErrorMessage()).isEqualTo(MESSAGE);
        assertThat(underTest.getParam()).isEqualTo(PARAM);
    }


    @Test
    public void getErrorMessage() {
        final ErrorDTO underTest = new ErrorDTO(ERROR_CODE, MESSAGE);

        assertThat(underTest.getErrorCode()).isEqualTo(ERROR_CODE);
        assertThat(underTest.getErrorMessage()).isEqualTo(MESSAGE);
    }
}