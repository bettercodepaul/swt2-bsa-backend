package online.bogenliga.application.common.errorhandling;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class ErrorCodeTest {

    @Test
    public void checkErrorCodes() {
        assertThat(ErrorCode.fromValue("EXTERNAL_SERVICE_ERROR"))
                .isEqualTo(ErrorCode.EXTERNAL_SERVICE_ERROR);
        assertThat(ErrorCode.fromValue("SQL_ERROR"))
                .isEqualTo(ErrorCode.SQL_ERROR);
        assertThat(ErrorCode.fromValue("INTERNAL_ERROR"))
                .isEqualTo(ErrorCode.INTERNAL_ERROR);
        assertThat(ErrorCode.fromValue("UNEXPECTED_ERROR"))
                .isEqualTo(ErrorCode.UNEXPECTED_ERROR);
        assertThat(ErrorCode.fromValue("DEPRECATED_VERSION_ERROR"))
                .isEqualTo(ErrorCode.DEPRECATED_VERSION_ERROR);

        assertThat(ErrorCode.fromValue("INVALID_ARGUMENT_ERROR"))
                .isEqualTo(ErrorCode.INVALID_ARGUMENT_ERROR);
        assertThat(ErrorCode.fromValue("ENTITY_NOT_FOUND_ERROR"))
                .isEqualTo(ErrorCode.ENTITY_NOT_FOUND_ERROR);
        assertThat(ErrorCode.fromValue("ENTITY_CONFLICT_ERROR"))
                .isEqualTo(ErrorCode.ENTITY_CONFLICT_ERROR);
        assertThat(ErrorCode.fromValue("NO_SESSION_ERROR"))
                .isEqualTo(ErrorCode.NO_SESSION_ERROR);
        assertThat(ErrorCode.fromValue("NO_PERMISSION_ERROR"))
                .isEqualTo(ErrorCode.NO_PERMISSION_ERROR);

        assertThat(ErrorCode.fromValue("UNDEFINED"))
                .isEqualTo(ErrorCode.UNDEFINED);
    }


    @Test
    public void fromValue() {
        final ErrorCode actual = ErrorCode.fromValue("ENTITY_CONFLICT_ERROR");
        assertThat(actual).isEqualTo(ErrorCode.ENTITY_CONFLICT_ERROR);
    }


    @Test
    public void fromValue_withInvalidValue() {
        final ErrorCode actual = ErrorCode.fromValue("ABC");
        assertThat(actual).isNull();
    }


    @Test
    public void getErrorCategory() {
        assertThat(ErrorCode.ENTITY_NOT_FOUND_ERROR.getErrorCategory()).isEqualTo(ErrorCategory.BUSINESS);
    }


    @Test
    public void getValue() {
        assertThat(ErrorCode.ENTITY_NOT_FOUND_ERROR.getValue()).isEqualTo("ENTITY_NOT_FOUND_ERROR");
    }


    @Test
    public void assertToString() {
        assertThat(ErrorCode.ENTITY_NOT_FOUND_ERROR.toString()).isEqualTo("ENTITY_NOT_FOUND_ERROR");
    }
}