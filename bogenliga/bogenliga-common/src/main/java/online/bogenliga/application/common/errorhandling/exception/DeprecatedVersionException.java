package online.bogenliga.application.common.errorhandling.exception;

import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class DeprecatedVersionException extends TechnicalException {


    /**
     * DeprecatedVersionException
     *
     * @param message error message
     */
    public DeprecatedVersionException(final String message) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, message);
    }


    /**
     * DeprecatedVersionException
     *
     * @param cause error cause
     */
    public DeprecatedVersionException(final Throwable cause) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, cause);
    }


    /**
     * DeprecatedVersionException
     *
     * @param message error message
     * @param cause   error cause
     */
    public DeprecatedVersionException(final String message, final Throwable cause) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, message, cause);
    }


    /**
     * DeprecatedVersionException
     *
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public DeprecatedVersionException(final String message, final Throwable cause,
                                      final boolean enableSuppression, final boolean writableStackTrace) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * DeprecatedVersionException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     */
    public DeprecatedVersionException(final String message, final Object... param) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, message, param);
    }


    /**
     * DeprecatedVersionException
     *
     * @param param error parameter to be logged and displayed with the error code
     * @param cause error cause
     */
    public DeprecatedVersionException(final Throwable cause, final Object... param) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, cause, param);
    }


    /**
     * DeprecatedVersionException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     * @param cause   error cause
     */
    public DeprecatedVersionException(final String message, final Throwable cause,
                                      final Object... param) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, message, cause, param);
    }


    /**
     * DeprecatedVersionException
     *
     * @param message            error message
     * @param param              error parameter to be logged and displayed with the error code
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public DeprecatedVersionException(final String message, final Throwable cause,
                                      final boolean enableSuppression, final boolean writableStackTrace,
                                      final Object... param) {
        super(ErrorCode.DEPRECATED_VERSION_ERROR, message, cause, enableSuppression, writableStackTrace, param);
    }
}
