package online.bogenliga.application.common.errorhandling.exception;

import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class EntityNotFoundException extends BusinessException {


    private static final long serialVersionUID = -8587612776541184396L;


    /**
     * EntityNotFoundException
     *
     * @param message error message
     */
    public EntityNotFoundException(final String message) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, message);
    }


    /**
     * EntityNotFoundException
     *
     * @param cause error cause
     */
    public EntityNotFoundException(final Throwable cause) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, cause);
    }


    /**
     * EntityNotFoundException
     *
     * @param message error message
     * @param cause   error cause
     */
    public EntityNotFoundException(final String message, final Throwable cause) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, message, cause);
    }


    /**
     * EntityNotFoundException
     *
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public EntityNotFoundException(final String message, final Throwable cause,
                                   final boolean enableSuppression, final boolean writableStackTrace) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * EntityNotFoundException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     */
    public EntityNotFoundException(final String message, final Object... param) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, message, param);
    }


    /**
     * EntityNotFoundException
     *
     * @param param error parameter to be logged and displayed with the error code
     * @param cause error cause
     */
    public EntityNotFoundException(final Throwable cause, final Object... param) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, cause, param);
    }


    /**
     * EntityNotFoundException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     * @param cause   error cause
     */
    public EntityNotFoundException(final String message, final Throwable cause,
                                   final Object... param) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, message, cause, param);
    }


    /**
     * EntityNotFoundException
     *
     * @param message            error message
     * @param param              error parameter to be logged and displayed with the error code
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public EntityNotFoundException(final String message, final Throwable cause,
                                   final boolean enableSuppression, final boolean writableStackTrace,
                                   final Object... param) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, message, cause, enableSuppression, writableStackTrace, param);
    }
}
