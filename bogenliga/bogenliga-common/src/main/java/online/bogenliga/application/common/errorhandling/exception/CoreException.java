package online.bogenliga.application.common.errorhandling.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

/**
 * Basisklasse für COSY Exceptions.
 *
 * @author Alexander Jost
 */
abstract class CoreException extends RuntimeException implements
        Serializable {

    private static final long serialVersionUID = 4518207150194368697L;


    CoreException() {
    }


    CoreException(final String message) {
        super(message);
    }


    CoreException(final Throwable cause) {
        this(stacktrace2String(cause));
    }


    CoreException(final String message, final Throwable cause) {
        this(message + System.lineSeparator() + stacktrace2String(cause));
    }


    CoreException(final String message, final Throwable cause,
                  final boolean enableSuppression, final boolean writableStackTrace) {
        super(message + System.lineSeparator() + stacktrace2String(cause),
                null, enableSuppression, writableStackTrace);
    }


    private static String stacktrace2String(final Throwable e) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(stream));
        return new String(stream.toByteArray());
    }
}
