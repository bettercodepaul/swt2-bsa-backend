package online.bogenliga.application.common.database.tx;

import java.sql.Connection;

/**
 * Created by ajost on 29.10.2015.
 */
public interface TransactionManager {
    boolean isActive();

    void begin();

    void rollback();

    void commit();

    void release();

    Connection getConnection();
}