package de.bogenliga.application.common.errorhandling.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * Base exception class
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
abstract class CoreException extends RuntimeException implements
        Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CoreException.class);

    private static final long serialVersionUID = 4518207150194368697L;
    private final ErrorCode errorCode;
    private final String[] parameters;


    /**
     * CoreException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     */
    CoreException(final ErrorCode errorCode, final String message) {
        super(errorCode.getValue() + ": " + message);
        this.errorCode = errorCode;
        this.parameters = new String[0];

        if (logger.isDebugEnabled()) {
            logger.debug("{}: {}", errorCode.getValue(), message.replaceAll("[\n\r\t]", "_"));

        }
    }

    /**
     * CoreException
     *
     * @param errorCode specifies the detailed error
     * @param cause     error cause
     */
    CoreException(final ErrorCode errorCode, final Throwable cause) {
        this(errorCode, stacktrace2String(cause));
    }


    /**
     * CoreException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param cause     error cause
     */
    CoreException(final ErrorCode errorCode, final String message, final Throwable cause) {
        this(errorCode, message + System.lineSeparator() + stacktrace2String(cause));
    }


    /**
     * CoreException
     *
     * @param errorCode          specifies the detailed error
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    CoreException(final ErrorCode errorCode, final String message, final Throwable cause,
                  final boolean enableSuppression, final boolean writableStackTrace) {
        super(errorCode.getValue() + ": " + message + System.lineSeparator() + stacktrace2String(cause),
                null, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.parameters = new String[0];

        if (logger.isDebugEnabled()) {
            logger.debug("{}: {}{}{}", errorCode.getValue(), message, System.lineSeparator(), stacktrace2String(cause));
        }
    }


    /**
     * CoreException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param param     error parameter to be logged and displayed with the error code
     */
    CoreException(final ErrorCode errorCode, final String message, final Object... param) {
        super(errorCode.getValue() + ": " + message);
        this.errorCode = errorCode;
        this.parameters = objectParametersToStringArray(param);

        if (logger.isDebugEnabled()) {
            logger.debug("{} with Parameter [{}]: {}", errorCode.getValue(), String.join(",", this.parameters),
                    message);
        }
    }


    /**
     * CoreException
     *
     * @param errorCode specifies the detailed error
     * @param param     error parameter to be logged and displayed with the error code
     * @param cause     error cause
     */
    CoreException(final ErrorCode errorCode, final Throwable cause, final Object... param) {
        this(errorCode, stacktrace2String(cause), param);
    }


    /**
     * CoreException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param param     error parameter to be logged and displayed with the error code
     * @param cause     error cause
     */
    CoreException(final ErrorCode errorCode, final String message, final Throwable cause, final Object... param) {
        this(errorCode, message + System.lineSeparator() + stacktrace2String(cause), param);
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }


    public String[] getParameters() {
        return parameters;
    }


    private static String[] objectParametersToStringArray(final Object... param) {
        return Arrays.stream(param).map(String::valueOf).toArray(String[]::new);
    }


    private static String stacktrace2String(final Throwable e) {
        if (e != null) {
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(stream));
            return new String(stream.toByteArray());
        } else {
            return "[no stacktrace found]";
        }
    }
}
