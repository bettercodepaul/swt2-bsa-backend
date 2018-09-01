package online.bogenliga.application.common.database.tx;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SessionHandler.class);

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    private static final String CONNECTION = "CONNECTION";
    private static final String IS_ACTIVE = "IS_ACTIVE";
    private static final String USER_ID = "USER_ID";
    private static final String USER_INFO = "USER_INFO";
    private static final String UNEXPECTED_EMPTY_THREAD_LOCAL_FOR_CONNECTION =
            "unexpected empty ThreadLocal for Connection";
    private static final String THREAD_LOCAL_NOT_INITIALIZED = "ThreadLocal not initialized.";


    /**
     * Return the one connection which is associated with the calling thread.
     *
     * @return the connection bound types the caller thread.
     */
    static Connection getConnection() {
        LOG.debug("Get connection from ThreadLocal.");

        final Connection connection;

        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(UNEXPECTED_EMPTY_THREAD_LOCAL_FOR_CONNECTION);
        } else if (THREAD_LOCAL.get().get(CONNECTION) == null) {
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
    public static void setConnection(final Connection connection) {
        LOG.debug("Set new connection in ThreadLocal.");

        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException("ThreadLocal not initialized.");
        } else if (THREAD_LOCAL.get().containsKey(CONNECTION)) {
            LOG.warn("Found unexpected connection in ThreadLocal, will be removed and overwritten by new one.");
            THREAD_LOCAL.get().remove(CONNECTION);
        }

        THREAD_LOCAL.get().put(CONNECTION, connection);
    }


    /**
     * Remove the connection from ThreadLocal.
     */
    public static void removeConnection() {
        LOG.debug("Remove connection from ThreadLocal.");

        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(THREAD_LOCAL_NOT_INITIALIZED);
        } else {
            THREAD_LOCAL.get().remove(CONNECTION);
        }

        THREAD_LOCAL.get().put(IS_ACTIVE, false);
    }


    /**
     * Reflects the transaction status for connection in ThreadLocal.
     *
     * @return true, if transaction on connection is active, false else
     */
    public static Boolean isActive() {
        if (THREAD_LOCAL.get() == null
                || THREAD_LOCAL.get().get(IS_ACTIVE) == null) {
            return false;
        }

        return (Boolean) THREAD_LOCAL.get().get(IS_ACTIVE);
    }


    /**
     * Set transaction status for connection in ThreadLocal.
     *
     * @param active true, if transaction on connection is active, false else
     */
    public static void setIsActive(final Boolean active) {
        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(THREAD_LOCAL_NOT_INITIALIZED);
        }

        THREAD_LOCAL.get().put(IS_ACTIVE, active);
    }


    /**
     * Return the caller userId which is associated with the thread.
     *
     * @return caller userId.
     */
    public static Long getUserId() {
        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(
                    "unexpected empty ThreadLocal for userId");
        } else if (THREAD_LOCAL.get().get(USER_ID) == null) {
            throw new IllegalStateException(
                    "unexpected empty ThreadLocal for userId");
        }

        return (Long) THREAD_LOCAL.get().get(USER_ID);
    }


    /**
     * Set client caller userId in ThreadLocal.
     *
     * @param userId the caller userId
     */
    public static void setUserId(final Long userId) {
        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(THREAD_LOCAL_NOT_INITIALIZED);
        }

        THREAD_LOCAL.get().put(USER_ID, userId);
    }


    /**
     * Return the caller UserInfos which is associated with the thread.
     *
     * @return caller UserInfos or null.
     */
    public static Object getUserInfo() {
        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(
                    "unexpected empty ThreadLocal for UserInfos");
        }

        // could be null!
        return THREAD_LOCAL.get().get(USER_INFO);
    }


    /**
     * Set client caller UserInfos in ThreadLocal.
     *
     * @param userInfos the caller UserInfos
     */
    public static void setUserInfo(final Object userInfos) {
        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(THREAD_LOCAL_NOT_INITIALIZED);
        }

        THREAD_LOCAL.get().put(USER_INFO, userInfos);
    }


    /**
     * Remove the UserID and UserInfos from ThreadLocal.
     */
    public static void releaseUserInfos() {
        if (THREAD_LOCAL.get() == null) {
            throw new IllegalStateException(
                    "unexpected empty ThreadLocal on releaseUserInfos");
        }

        THREAD_LOCAL.get().remove(USER_ID);
        THREAD_LOCAL.get().remove(USER_INFO);
    }
}
