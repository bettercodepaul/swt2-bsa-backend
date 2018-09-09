package online.bogenliga.application.common.database.tx;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import online.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/properties-with-spring">Properties with Spring and Spring Boot</a>
 */
@Profile({"LOCAL", "INT", "PROD"})
@Service("TransactionManager")
public class PostgresqlTransactionManager implements TransactionManager {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresqlTransactionManager.class);
    private final DataSource ds;


    /**
     * Constructor
     *
     * Initialize and test database connection
     */
    public PostgresqlTransactionManager() {
        try {
            final PGSimpleDataSource postgresqlDatasource = new PGSimpleDataSource();  // Empty instance.
            // The value `localhost` means the Postgres cluster running locally on the same machine.
            postgresqlDatasource.setServerName("localhost");
            // A connection to Postgres must be made to a specific database rather than to the server as a whole.
            // You likely have an initial database created named `public`.
            postgresqlDatasource.setDatabaseName("swt2");
            // Or use the super-user 'postgres' for user name if you installed Postgres with defaults and
            // have not yet created user(s) for your application.
            postgresqlDatasource.setUser("swt2");
            postgresqlDatasource.setPassword("swt2");

            ds = postgresqlDatasource;

            testConnection();
        } catch (final SQLException | NullPointerException e) {
            throw new TechnicalException(e);
        }
    }


    /**
     * Package-private constructor with all dependencies
     *
     * @param dataSource with the database connection
     */
    PostgresqlTransactionManager(final DataSource dataSource) {
        this.ds = dataSource;
    }


    /**
     * Initial database connection check
     *
     * Access database information
     *
     * @throws SQLException if the database information could not be accessed
     */
    void testConnection() throws SQLException {
        try (final Connection connection = ds.getConnection()) {
            final DatabaseMetaData databaseMetaData = connection.getMetaData();
            final String databaseInfo = databaseMetaData.getDatabaseProductName()
                    + databaseMetaData.getDatabaseProductVersion();
            LOG.info("Datasource {} found and registered.", databaseInfo);
        }
    }


    @Override
    public boolean isActive() {
        return SessionHandler.isActive();
    }


    /**
     * Start transaction
     */
    @Override
    public void begin() {
        LOG.debug("Starting transaction.");
        final Connection connection;

        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            LOG.debug("Created new connection from Datasource.");
        } catch (final SQLException e) {
            throw new TechnicalException(e);
        }

        SessionHandler.setConnection(connection);
        SessionHandler.setIsActive(true);
    }


    @Override
    public void rollback() {
        if (isActive()) {
            try {
                SessionHandler.getConnection().rollback();
                LOG.debug("Rollback transaction.");
            } catch (final SQLException e) {
                throw new TechnicalException(e);
            } finally {
                SessionHandler.setIsActive(false);
            }
        } else {
            LOG.warn("Trying types rollback inactive transaction.");
        }
    }


    /**
     * Commit all changes and close transaction
     */
    @Override
    public void commit() {
        if (isActive()) {
            try {
                SessionHandler.getConnection().commit();
                LOG.debug("Commit transaction.");
            } catch (final SQLException e) {
                throw new TechnicalException(e);
            } finally {
                SessionHandler.setIsActive(false);
            }
        } else {
            LOG.warn("Trying types commit inactive transaction.");
        }
    }


    /**
     * Release database connection and rollback all uncommited changes
     */
    @Override
    public void release() {
        try {
            if (isActive()) {
                LOG.warn("Trying types release active transaction. Rollback command will be executed.");
                rollback();
            }

            SessionHandler.getConnection().close();
            LOG.debug("Release connection.");
        } catch (final SQLException e) {
            throw new TechnicalException(e);
        } finally {
            SessionHandler.removeConnection();
        }
    }


    /**
     * @return connection with active transaction
     */
    @Override
    public Connection getConnection() {
        if (!isActive()) {
            LOG.debug("Call types getConnection() without active transaction. Start transaction automatically...");
            begin();
        }

        return SessionHandler.getConnection();
    }
}
