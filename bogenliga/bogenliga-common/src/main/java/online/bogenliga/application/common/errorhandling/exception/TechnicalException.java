package online.bogenliga.application.common.errorhandling.exception;

/**
 * Technische Fehler werden über die Facade durch diese CoreException transportiert.
 * Enthaltenede Exceptions werden als Text übertragen, um keine
 * Code-Abhängigkeiten im Aufrufer zu erzeugen.
 *
 * @author Alexander Jost
 */
public class TechnicalException extends CoreException {

    private static final long serialVersionUID = -2121356581287150071L;


    public TechnicalException() {
        super();
    }


    public TechnicalException(final String message, final Throwable cause,
                              final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public TechnicalException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public TechnicalException(final String message) {
        super(message);
    }


    public TechnicalException(final Throwable cause) {
        super(cause);
    }
}
