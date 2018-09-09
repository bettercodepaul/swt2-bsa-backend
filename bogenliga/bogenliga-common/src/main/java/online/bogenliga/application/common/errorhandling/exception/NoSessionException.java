package online.bogenliga.application.common.errorhandling.exception;

import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class NoSessionException extends BusinessException {


    private static final long serialVersionUID = 2267233112145982663L;


    /**
     * NoSessionException
     *
     * @param message error message
     */
    public NoSessionException(final String message) {
        super(ErrorCode.NO_SESSION_ERROR, message);
    }


    /**
     * NoSessionException
     *
     * @param cause error cause
     */
    public NoSessionException(final Throwable cause) {
        super(ErrorCode.NO_SESSION_ERROR, cause);
    }


    /**
     * NoSessionException
     *
     * @param message error message
     * @param cause   error cause
     */
    public NoSessionException(final String message, final Throwable cause) {
        super(ErrorCode.NO_SESSION_ERROR, message, cause);
    }


    /**
     * NoSessionException
     *
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public NoSessionException(final String message, final Throwable cause,
                              final boolean enableSuppression, final boolean writableStackTrace) {
        super(ErrorCode.NO_SESSION_ERROR, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * NoSessionException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     */
    public NoSessionException(final String message, final Object... param) {
        super(ErrorCode.NO_SESSION_ERROR, message, param);
    }


    /**
     * NoSessionException
     *
     * @param param error parameter to be logged and displayed with the error code
     * @param cause error cause
     */
    public NoSessionException(final Throwable cause, final Object... param) {
        super(ErrorCode.NO_SESSION_ERROR, cause, param);
    }


    /**
     * NoSessionException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     * @param cause   error cause
     */
    public NoSessionException(final String message, final Throwable cause,
                              final Object... param) {
        super(ErrorCode.NO_SESSION_ERROR, message, cause, param);
    }


    /**
     * NoSessionException
     *
     * @param message            error message
     * @param param              error parameter to be logged and displayed with the error code
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public NoSessionException(final String message, final Throwable cause,
                              final boolean enableSuppression, final boolean writableStackTrace,
                              final Object... param) {
        super(ErrorCode.NO_SESSION_ERROR, message, cause, enableSuppression, writableStackTrace, param);
    }
}
