//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package online.bogenliga.application.common.errorhandling;

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
    SQL_ERROR(ErrorCategory.TECHNICAL, "SQL_ERROR"),
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
