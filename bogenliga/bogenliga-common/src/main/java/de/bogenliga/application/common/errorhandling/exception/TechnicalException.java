package de.bogenliga.application.common.errorhandling.exception;

import de.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * Technische Fehler werden über die Facade durch diese CoreException transportiert.
 * Enthaltenede Exceptions werden als Text übertragen, um keine
 * Code-Abhängigkeiten im Aufrufer zu erzeugen.
 *
 * @author Alexander Jost
 */
public class TechnicalException extends CoreException {

    private static final long serialVersionUID = -2121356581287150071L;


    /**
     * TechnicalException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     */
    public TechnicalException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }


    /**
     * TechnicalException
     *
     * @param errorCode specifies the detailed error
     * @param cause     error cause
     */
    public TechnicalException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }


    /**
     * TechnicalException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param cause     error cause
     */
    public TechnicalException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(errorCode, message, cause);
    }


    /**
     * TechnicalException
     *
     * @param errorCode          specifies the detailed error
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public TechnicalException(final ErrorCode errorCode, final String message, final Throwable cause,
                              final boolean enableSuppression, final boolean writableStackTrace) {
        super(errorCode, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * TechnicalException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param param     error parameter to be logged and displayed with the error code
     */
    public TechnicalException(final ErrorCode errorCode, final String message, final Object... param) {
        super(errorCode, message, param);
    }


    /**
     * TechnicalException
     *
     * @param errorCode specifies the detailed error
     * @param param     error parameter to be logged and displayed with the error code
     * @param cause     error cause
     */
    public TechnicalException(final ErrorCode errorCode, final Throwable cause, final Object... param) {
        super(errorCode, cause, param);
    }


    /**
     * TechnicalException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param param     error parameter to be logged and displayed with the error code
     * @param cause     error cause
     */
    public TechnicalException(final ErrorCode errorCode, final String message, final Throwable cause,
                              final Object... param) {
        super(errorCode, message, cause, param);
    }
}
