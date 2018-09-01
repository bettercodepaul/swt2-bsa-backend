package online.bogenliga.application.common.errorhandling.exception;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class InvalidArgumentException extends BusinessException {

    public InvalidArgumentException() {
        super();
    }


    public InvalidArgumentException(final String message, final Throwable cause,
                                    final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public InvalidArgumentException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public InvalidArgumentException(final String message) {
        super(message);
    }


    public InvalidArgumentException(final Throwable cause) {
        super(cause);
    }
}
