package de.bogenliga.application.integrationtests.mocks;

import java.sql.Connection;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import de.bogenliga.application.common.database.tx.TransactionManager;

/**
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Profile("TEST")
@Service("TransactionManager")
public class TransactionManagerMock implements TransactionManager {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionManagerMock.class);


    public TransactionManagerMock() {
    }


    @Override
    public boolean isActive() {
        return true;
    }


    @Override
    public void begin(boolean newDatabase) {
    }


    @Override
    public void rollback() {
    }


    @Override
    public void commit() {
    }


    @Override
    public void release() {
    }


    @Override
    public Connection getConnection() {
        return Mockito.mock(Connection.class);
    }
}
