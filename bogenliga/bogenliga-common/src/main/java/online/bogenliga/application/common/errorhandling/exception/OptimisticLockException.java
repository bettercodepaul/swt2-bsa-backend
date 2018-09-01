package online.bogenliga.application.common.errorhandling.exception;


/**
 * Optimistische Fehler werden über die Facade durch diese CoreException
 * transportiert. Enthaltenede Exceptions werden als Text übertragen, um keine
 * Code-Abhängigkeiten im Aufrufer zu erzeugen.
 *
 * @author Alexander Jost
 */
public class OptimisticLockException extends TechnicalException {

    private static final long serialVersionUID = -2575881753310241814L;


    public OptimisticLockException() {
        super();
    }


    public OptimisticLockException(final String message, final Throwable cause,
                                   final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public OptimisticLockException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public OptimisticLockException(final String message) {
        super(message);
    }


    public OptimisticLockException(final Throwable cause) {
        super(cause);
    }
}
