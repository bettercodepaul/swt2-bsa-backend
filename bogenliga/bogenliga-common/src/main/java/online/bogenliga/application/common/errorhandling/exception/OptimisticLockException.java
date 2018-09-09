package online.bogenliga.application.common.errorhandling.exception;


import online.bogenliga.application.common.errorhandling.ErrorCode;

/**
 * Optimistische Fehler werden über die Facade durch diese CoreException
 * transportiert. Enthaltenede Exceptions werden als Text übertragen, um keine
 * Code-Abhängigkeiten im Aufrufer zu erzeugen.
 *
 * @author Alexander Jost
 */
public class OptimisticLockException extends BusinessException {


    private static final long serialVersionUID = -4567997291579738636L;


    /**
     * OptimisticLockException
     *
     * @param message error message
     */
    public OptimisticLockException(final String message) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, message);
    }


    /**
     * OptimisticLockException
     *
     * @param cause error cause
     */
    public OptimisticLockException(final Throwable cause) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, cause);
    }


    /**
     * OptimisticLockException
     *
     * @param message error message
     * @param cause   error cause
     */
    public OptimisticLockException(final String message, final Throwable cause) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, message, cause);
    }


    /**
     * OptimisticLockException
     *
     * @param message            error message
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public OptimisticLockException(final String message, final Throwable cause,
                                   final boolean enableSuppression, final boolean writableStackTrace) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, message, cause, enableSuppression, writableStackTrace);
    }


    /**
     * OptimisticLockException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     */
    public OptimisticLockException(final String message, final Object... param) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, message, param);
    }


    /**
     * OptimisticLockException
     *
     * @param param error parameter to be logged and displayed with the error code
     * @param cause error cause
     */
    public OptimisticLockException(final Throwable cause, final Object... param) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, cause, param);
    }


    /**
     * OptimisticLockException
     *
     * @param message error message
     * @param param   error parameter to be logged and displayed with the error code
     * @param cause   error cause
     */
    public OptimisticLockException(final String message, final Throwable cause,
                                   final Object... param) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, message, cause, param);
    }


    /**
     * OptimisticLockException
     *
     * @param message            error message
     * @param param              error parameter to be logged and displayed with the error code
     * @param cause              error cause
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    public OptimisticLockException(final String message, final Throwable cause,
                                   final boolean enableSuppression, final boolean writableStackTrace,
                                   final Object... param) {
        super(ErrorCode.ENTITY_CONFLICT_ERROR, message, cause, enableSuppression, writableStackTrace, param);
    }
}
