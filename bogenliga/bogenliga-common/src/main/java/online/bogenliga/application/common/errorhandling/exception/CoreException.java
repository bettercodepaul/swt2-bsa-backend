package online.bogenliga.application.common.errorhandling.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * Basisklasse für COSY Exceptions.
 *
 * @author Alexander Jost
 */
abstract class CoreException extends RuntimeException implements
        Serializable {

    private static final long serialVersionUID = 4518207150194368697L;
    private final ErrorCode errorCode;
    private final String[] parameters;


    CoreException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = new String[0];
    }


    CoreException(final ErrorCode errorCode, final Throwable cause) {
        this(errorCode, stacktrace2String(cause));
    }


    CoreException(final ErrorCode errorCode, final String message, final Throwable cause) {
        this(errorCode, message + System.lineSeparator() + stacktrace2String(cause));
    }


    CoreException(final ErrorCode errorCode, final String message, final Throwable cause,
                  final boolean enableSuppression, final boolean writableStackTrace) {
        super(message + System.lineSeparator() + stacktrace2String(cause),
                null, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.parameters = new String[0];
    }


    CoreException(final ErrorCode errorCode, final String message, final Object... param) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = objectParametersToStringArray(param);
    }


    CoreException(final ErrorCode errorCode, final Throwable cause, final Object... param) {
        this(errorCode, stacktrace2String(cause), param);
    }


    CoreException(final ErrorCode errorCode, final String message, final Throwable cause, final Object... param) {
        this(errorCode, message + System.lineSeparator() + stacktrace2String(cause), param);
    }


    CoreException(final ErrorCode errorCode, final String message, final Throwable cause,
                  final boolean enableSuppression, final boolean writableStackTrace, final Object... param) {
        super(message + System.lineSeparator() + stacktrace2String(cause),
                null, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.parameters = objectParametersToStringArray(param);
    }


    private static String[] objectParametersToStringArray(final Object[] param) {
        return Arrays.copyOf(param, param.length, String[].class);
    }


    private static String stacktrace2String(final Throwable e) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(stream));
        return new String(stream.toByteArray());
    }
}
