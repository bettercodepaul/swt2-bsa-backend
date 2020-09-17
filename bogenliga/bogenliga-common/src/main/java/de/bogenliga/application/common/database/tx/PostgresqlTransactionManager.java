package de.bogenliga.application.common.database.tx;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import de.bogenliga.application.common.configuration.DatabaseConfiguration;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/properties-with-spring">Properties with Spring and Spring Boot</a>
 */
@Profile({"LOCAL", "INT", "DEV", "PROD", "DOCKER"})
@Service("TransactionManager")
public class PostgresqlTransactionManager implements TransactionManager {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresqlTransactionManager.class);
    private DataSource ds;
    private DatabaseConfiguration databaseConfiguration;


    /**
     * Constructor
     *
     * Initialize and test database connection
     */
    @Autowired
    public PostgresqlTransactionManager(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }


    /**
     * Package-private constructor with all dependencies
     *
     * @param dataSource with the database connection
     */
    PostgresqlTransactionManager(DataSource dataSource) {
        ds = dataSource;
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
        Connection connection;

        try {
            connection = getDataSource().getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            LOG.debug("Created new connection from Datasource.");
        } catch (SQLException e) {
            throw new TechnicalException(ErrorCode.DATABASE_TRANSACTION_ERROR, e);
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
            } catch (SQLException e) {
                throw new TechnicalException(ErrorCode.DATABASE_TRANSACTION_ERROR, e);
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
            } catch (SQLException e) {
                throw new TechnicalException(ErrorCode.DATABASE_TRANSACTION_ERROR, e);
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
        } catch (SQLException e) {
            throw new TechnicalException(ErrorCode.DATABASE_TRANSACTION_ERROR, e);
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


    public DataSource getDataSource() {
        if (ds == null) {
            try {
                LOG.debug("Database connection: jdbc:postgresql://{}:{}/{} with user '{}' and password length '{}'",
                        databaseConfiguration.getHost(), databaseConfiguration.getPort(),
                        databaseConfiguration.getDatabaseName(), databaseConfiguration.getUser(),
                        databaseConfiguration.getPassword().length());

                PGSimpleDataSource postgresqlDatasource = new PGSimpleDataSource();  // Empty instance.
                // The value `localhost` means the Postgres cluster running locally on the same machine.
                postgresqlDatasource.setServerName(databaseConfiguration.getHost());
                postgresqlDatasource.setPortNumber(databaseConfiguration.getPort());
                // A connection to Postgres must be made to a specific database rather than to the server as a whole.
                // You likely have an initial database created named `public`.
                postgresqlDatasource.setDatabaseName(databaseConfiguration.getDatabaseName());
                // Or use the super-user 'postgres' for user name if you installed Postgres with defaults and
                // have not yet created user(s) for your application.
                postgresqlDatasource.setUser(databaseConfiguration.getUser());
                postgresqlDatasource.setPassword(databaseConfiguration.getPassword());

                ds = postgresqlDatasource;

                testConnection();
            } catch (SQLException | NullPointerException e) {
                throw new TechnicalException(ErrorCode.DATABASE_CONNECTION_ERROR, e);
            }
        }

        return ds;
    }


    /**
     * Initial database connection check
     *
     * Access database information
     *
     * @throws SQLException if the database information could not be accessed
     */
    void testConnection() throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            LOG.info("Datasource {} {} found and registered.", databaseMetaData.getDatabaseProductName(),
                    databaseMetaData.getDatabaseProductVersion());
        }
    }
}
