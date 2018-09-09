package online.bogenliga.application.common.errorhandling.exception;

import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class InvalidArgumentException extends BusinessException {

    private static final long serialVersionUID = 114845712016113073L;


    /**
     * InvalidArgumentException
     *
     * @param message error message
     */
    public InvalidArgumentException(final String message) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, message);
    }


    /**
     * InvalidArgumentException
     *
     * @param cause error cause
     */
    public InvalidArgumentException(final Throwable cause) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, cause);
    }


    /**
     * InvalidArgumentException
     *
     * @param message error message
     * @param cause   error cause
     */
    public InvalidArgumentException(final String message, final Throwable cause) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, message, cause);
    }


    /**
     * InvalidArgumentException
     *
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public InvalidArgumentException(final String message, final Throwable cause,
                                    final boolean enableSuppression, final boolean writableStackTrace) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * InvalidArgumentException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     */
    public InvalidArgumentException(final String message, final Object... param) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, message, param);
    }


    /**
     * InvalidArgumentException
     *
     * @param param error parameter to be logged and displayed with the error code
     * @param cause error cause
     */
    public InvalidArgumentException(final Throwable cause, final Object... param) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, cause, param);
    }


    /**
     * InvalidArgumentException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     * @param cause   error cause
     */
    public InvalidArgumentException(final String message, final Throwable cause,
                                    final Object... param) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, message, cause, param);
    }


    /**
     * InvalidArgumentException
     *
     * @param message            error message
     * @param param              error parameter to be logged and displayed with the error code
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public InvalidArgumentException(final String message, final Throwable cause,
                                    final boolean enableSuppression, final boolean writableStackTrace,
                                    final Object... param) {
        super(ErrorCode.INVALID_ARGUMENT_ERROR, message, cause, enableSuppression, writableStackTrace, param);
    }
}
