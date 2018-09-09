package online.bogenliga.application.common.database.tx;

import java.sql.Connection;

/**
 * I´m a common interface for all database {@link TransactionManager}
 *
 * Created by ajost on 29.10.2015.
 */
public interface TransactionManager {
    /**
     * @return true, if a transaction is active
     */
    boolean isActive();

    /**
     * Start a transaction
     */
    void begin();

    /**
     * Undo all changes while transaction (begin)
     */
    void rollback();

    /**
     * Persist all changes while transaction
     */
    void commit();

    /**
     * Stop transaction and unlock resources
     */
    void release();

    /**
     * @return database connection
     */
    Connection getConnection();
}