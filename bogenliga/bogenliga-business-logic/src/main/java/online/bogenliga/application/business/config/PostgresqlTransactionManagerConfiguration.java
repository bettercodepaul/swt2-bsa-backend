package online.bogenliga.application.business.config;


import online.bogenliga.application.common.database.tx.PostgresqlTransactionManager;
import online.bogenliga.application.common.database.tx.TransactionManager;
import online.bogenliga.application.common.database.tx.TransactionManagerConfiguration;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class PostgresqlTransactionManagerConfiguration implements
        TransactionManagerConfiguration {

    private TransactionManager transactionManager = null;


    /**
     *
     */
    public PostgresqlTransactionManagerConfiguration() {
        transactionManager = new PostgresqlTransactionManager();
    }


    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
}
