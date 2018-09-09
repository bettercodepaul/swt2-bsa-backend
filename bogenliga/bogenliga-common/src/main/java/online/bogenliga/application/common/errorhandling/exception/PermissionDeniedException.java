package online.bogenliga.application.common.errorhandling.exception;

import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PermissionDeniedException extends BusinessException {


    private static final long serialVersionUID = 5675123714198939324L;


    /**
     * PermissionDeniedException
     *
     * @param message error message
     */
    public PermissionDeniedException(final String message) {
        super(ErrorCode.NO_PERMISSION_ERROR, message);
    }


    /**
     * PermissionDeniedException
     *
     * @param cause error cause
     */
    public PermissionDeniedException(final Throwable cause) {
        super(ErrorCode.NO_PERMISSION_ERROR, cause);
    }


    /**
     * PermissionDeniedException
     *
     * @param message error message
     * @param cause   error cause
     */
    public PermissionDeniedException(final String message, final Throwable cause) {
        super(ErrorCode.NO_PERMISSION_ERROR, message, cause);
    }


    /**
     * PermissionDeniedException
     *
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public PermissionDeniedException(final String message, final Throwable cause,
                                     final boolean enableSuppression, final boolean writableStackTrace) {
        super(ErrorCode.NO_PERMISSION_ERROR, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * PermissionDeniedException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     */
    public PermissionDeniedException(final String message, final Object... param) {
        super(ErrorCode.NO_PERMISSION_ERROR, message, param);
    }


    /**
     * PermissionDeniedException
     *
     * @param param error parameter to be logged and displayed with the error code
     * @param cause error cause
     */
    public PermissionDeniedException(final Throwable cause, final Object... param) {
        super(ErrorCode.NO_PERMISSION_ERROR, cause, param);
    }


    /**
     * PermissionDeniedException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     * @param cause   error cause
     */
    public PermissionDeniedException(final String message, final Throwable cause,
                                     final Object... param) {
        super(ErrorCode.NO_PERMISSION_ERROR, message, cause, param);
    }


    /**
     * PermissionDeniedException
     *
     * @param message            error message
     * @param param              error parameter to be logged and displayed with the error code
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public PermissionDeniedException(final String message, final Throwable cause,
                                     final boolean enableSuppression, final boolean writableStackTrace,
                                     final Object... param) {
        super(ErrorCode.NO_PERMISSION_ERROR, message, cause, enableSuppression, writableStackTrace, param);
    }
}
