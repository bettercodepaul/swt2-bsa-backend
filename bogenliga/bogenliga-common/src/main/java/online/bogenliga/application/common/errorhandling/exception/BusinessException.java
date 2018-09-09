package online.bogenliga.application.common.errorhandling.exception;

import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * Fachliche Fehler werden über die Facade durch diese CoreException transportiert.
 * Enthaltenede Exceptions werden als Text übertragen, um keine
 * Code-Abhängigkeiten im Aufrufer zu erzeugen.
 *
 * @author Alexander Jost
 */
public class BusinessException extends CoreException {

    private static final long serialVersionUID = -5176421778850723950L;


    /**
     * BusinessException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     */
    public BusinessException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }


    /**
     * BusinessException
     *
     * @param errorCode specifies the detailed error
     * @param cause     error cause
     */
    public BusinessException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }


    /**
     * BusinessException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param cause     error cause
     */
    public BusinessException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(errorCode, message, cause);
    }


    /**
     * BusinessException
     *
     * @param errorCode          specifies the detailed error
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public BusinessException(final ErrorCode errorCode, final String message, final Throwable cause,
                             final boolean enableSuppression, final boolean writableStackTrace) {
        super(errorCode, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * BusinessException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param param     error parameter to be logged and displayed with the error code
     */
    public BusinessException(final ErrorCode errorCode, final String message, final Object... param) {
        super(errorCode, message, param);
    }


    /**
     * BusinessException
     *
     * @param errorCode specifies the detailed error
     * @param param     error parameter to be logged and displayed with the error code
     * @param cause     error cause
     */
    public BusinessException(final ErrorCode errorCode, final Throwable cause, final Object... param) {
        super(errorCode, cause, param);
    }


    /**
     * BusinessException
     *
     * @param errorCode specifies the detailed error
     * @param message   error message
     * @param param     error parameter to be logged and displayed with the error code
     * @param cause     error cause
     */
    public BusinessException(final ErrorCode errorCode, final String message, final Throwable cause,
                             final Object... param) {
        super(errorCode, message, cause, param);
    }


    /**
     * BusinessException
     *
     * @param errorCode          specifies the detailed error
     * @param message            error message
     * @param param              error parameter to be logged and displayed with the error code
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public BusinessException(final ErrorCode errorCode, final String message, final Throwable cause,
                             final boolean enableSuppression, final boolean writableStackTrace, final Object... param) {
        super(errorCode, message, cause, enableSuppression, writableStackTrace, param);
    }
}
