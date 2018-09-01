package online.bogenliga.application.common.errorhandling.exception;

/**
 * Fachliche Fehler werden über die Facade durch diese CoreException transportiert.
 * Enthaltenede Exceptions werden als Text übertragen, um keine
 * Code-Abhängigkeiten im Aufrufer zu erzeugen.
 *
 * @author Alexander Jost
 */
public class BusinessException extends CoreException {

    private static final long serialVersionUID = -5176421778850723950L;


    public BusinessException() {
        super();
    }


    public BusinessException(final String message, final Throwable cause,
                             final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public BusinessException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public BusinessException(final String message) {
        super(message);
    }


    public BusinessException(final Throwable cause) {
        super(cause);
    }
}
