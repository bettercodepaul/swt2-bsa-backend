package online.bogenliga.application.common.database.tx;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * I save session information for the current thread.
 *
 * Handle database connection and support the transaction management.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
final class SessionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SessionHandler.class);

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    private static final String CONNECTION = "CONNECTION";
    private static final String IS_ACTIVE = "IS_ACTIVE";
    private static final String UNEXPECTED_EMPTY_THREAD_LOCAL_FOR_CONNECTION =
            "unexpected empty ThreadLocal for Connection";


    /**
     * Constructor
     */
    private SessionHandler() {
        // empty private constructor
    }


    /**
     * Return the one connection which is associated with the calling thread.
     *
     * @return the connection bound types the caller thread.
     */
    static Connection getConnection() {
        LOG.debug("Get connection from ThreadLocal.");

        final Connection connection;

        if (THREAD_LOCAL.get().get(CONNECTION) == null) {
            throw new IllegalStateException(UNEXPECTED_EMPTY_THREAD_LOCAL_FOR_CONNECTION);
        }

        connection = (Connection) THREAD_LOCAL.get().get(CONNECTION);

        return connection;
    }


    /**
     * Set the one connection which should be associated with the calling
     * thread.
     *
     * @param connection the connection types set in ThreadLocal
     */
    static void setConnection(final Connection connection) {
        LOG.debug("Set new connection in ThreadLocal.");

        if (THREAD_LOCAL.get().containsKey(CONNECTION)) {
            LOG.warn("Found unexpected connection in ThreadLocal, will be removed and overwritten by new one.");
            THREAD_LOCAL.get().remove(CONNECTION);
        }

        THREAD_LOCAL.get().put(CONNECTION, connection);
    }


    /**
     * Remove the connection from ThreadLocal.
     */
    static void removeConnection() {
        LOG.debug("Remove connection from ThreadLocal.");

        THREAD_LOCAL.get().remove(CONNECTION);
        THREAD_LOCAL.get().put(IS_ACTIVE, false);
    }


    /**
     * Reflects the transaction status for connection in ThreadLocal.
     *
     * @return true, if transaction on connection is active, false else
     */
    static Boolean isActive() {
        if (THREAD_LOCAL.get().get(IS_ACTIVE) == null) {
            return false;
        }

        return (Boolean) THREAD_LOCAL.get().get(IS_ACTIVE);
    }


    /**
     * Set transaction status for connection in ThreadLocal.
     *
     * @param active true, if transaction on connection is active, false else
     */
    static void setIsActive(final Boolean active) {
        THREAD_LOCAL.get().put(IS_ACTIVE, active);
    }
}
