package app.bogenliga.application.common.database.tx;


import app.bogenliga.application.common.config.Configuration;

/**
 * Created by ajost on 29.10.2015.
 */
public interface TransactionManagerConfiguration extends Configuration {
    /**
     * Access the TransactionManager facade <br>
     *
     * @return the one and only instance of an TransactionManager
     */
    TransactionManager getTransactionManager();
}
