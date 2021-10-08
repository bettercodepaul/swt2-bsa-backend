package de.bogenliga.application.common.errorhandling;

/**
 * I represent the defined error codes.
 *
 * Errors are handled and mapped to an error code.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public enum ErrorCode {
    /*
     * technical errors
     */
    // external systems
    EXTERNAL_SERVICE_ERROR(ErrorCategory.TECHNICAL, "EXTERNAL_SERVICE_ERROR"),
    DATABASE_CONNECTION_ERROR(ErrorCategory.TECHNICAL, "DATABASE_CONNECTION_ERROR"),
    DATABASE_TRANSACTION_ERROR(ErrorCategory.TECHNICAL, "DATABASE_TRANSACTION_ERROR"),
    DATABASE_ERROR(ErrorCategory.TECHNICAL, "DATABASE_ERROR"),
    // server error
    INTERNAL_ERROR(ErrorCategory.TECHNICAL, "INTERNAL_ERROR"),
    UNEXPECTED_ERROR(ErrorCategory.TECHNICAL, "UNEXPECTED_ERROR"),
    // backward compatibility
    DEPRECATED_VERSION_ERROR(ErrorCategory.TECHNICAL, "DEPRECATED_VERSION_ERROR"),

    /*
     * business errors
     */
    // precondition checks
    INVALID_ARGUMENT_ERROR(ErrorCategory.BUSINESS, "INVALID_ARGUMENT_ERROR"),
    // database
    ENTITY_NOT_FOUND_ERROR(ErrorCategory.BUSINESS, "ENTITY_NOT_FOUND_ERROR"),
    ENTITY_CONFLICT_ERROR(ErrorCategory.BUSINESS, "ENTITY_CONFLICT_ERROR"),
    // user management
    NO_SESSION_ERROR(ErrorCategory.BUSINESS, "NO_SESSION_ERROR"),
    NO_PERMISSION_ERROR(ErrorCategory.BUSINESS, "NO_PERMISSION_ERROR"),
    INVALID_SIGN_IN_CREDENTIALS(ErrorCategory.BUSINESS, "INVALID_SIGN_IN_CREDENTIALS"),
    PRECONDITION_MSG_RESET_PW_EQUAL_IDS(ErrorCategory.BUSINESS, "PRECONDITION_MSG_USER_PW_RESET_FAIL"),
    TOO_MANY_INCORRECT_LOGIN_ATTEMPTS(ErrorCategory.BUSINESS, "TOO_MANY_INCORRECT_LOGIN_ATTEMPTS"),
    INSUFFICIENT_CREDENTIALS(ErrorCategory.BUSINESS, "INSUFFICIENT_CREDENTIALS"),
    DUBLICATE_USERNAME(ErrorCategory.BUSINESS, "DUBLICATE_USERNAME"),


    UNDEFINED(ErrorCategory.UNDEFINED, "UNDEFINED");

    private final ErrorCategory errorCategory;
    private final String value;


    /**
     * Constructor
     */
    ErrorCode(final ErrorCategory errorCategory, final String value) {
        this.errorCategory = errorCategory;
        this.value = value;
    }


    /**
     * I return the enum to the corresponding value string.
     *
     * @param value to identify the enum
     * @return enum with the given value or {@code null}
     */
    public static ErrorCode fromValue(final String value) {
        for (final ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getValue().equalsIgnoreCase(value)) {
                return errorCode;
            }
        }
        return null;
    }


    /**
     * @return {@link ErrorCategory} of the error code
     */
    public ErrorCategory getErrorCategory() {
        return errorCategory;
    }


    /**
     * @return {@code value} of the enum
     */
    public String getValue() {
        return value;
    }


    /**
     * @return {@code value} of the enum
     */
    @Override
    public String toString() {
        return value;
    }
}
