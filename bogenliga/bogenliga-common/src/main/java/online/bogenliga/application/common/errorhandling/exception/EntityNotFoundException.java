package online.bogenliga.application.common.errorhandling.exception;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException() {
        super();
    }


    public EntityNotFoundException(final String message, final Throwable cause,
                                   final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public EntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public EntityNotFoundException(final String message) {
        super(message);
    }


    public EntityNotFoundException(final Throwable cause) {
        super(cause);
    }
}
