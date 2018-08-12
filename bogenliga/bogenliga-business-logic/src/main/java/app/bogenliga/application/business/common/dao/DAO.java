package app.bogenliga.application.business.common.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import app.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import app.bogenliga.application.common.component.dao.DataAccessObject;
import app.bogenliga.application.common.database.tx.PostgresqlTransactionManager;
import app.bogenliga.application.common.database.tx.TransactionManager;
import app.bogenliga.application.common.errorhandling.exception.TechnicalException;
import app.bogenliga.application.common.utils.SQL;


/**
 * Basic data access object implementation for CRUD operations.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/apache-commons-dbutils">A Guide to Apache Commons DbUtils</a>
 */
public abstract class DAO implements DataAccessObject {

    protected final QueryRunner run = new QueryRunner();
    private final Logger __logger;
    private final Map<String, String> __columnToFieldMapping;
    private final TransactionManager transactionManager;


    /**
     * Initialize the transaction manager to provide a database connection
     */
    public DAO(final Logger logger) {
        transactionManager = new PostgresqlTransactionManager();
        __logger = logger;
        __columnToFieldMapping = Collections.emptyMap();
    }


    /**
     * Initialize the transaction manager to provide a database connection
     */
    public DAO(final Logger logger, final Map<String, String> columnToFieldMapping) {
        transactionManager = new PostgresqlTransactionManager();
        __logger = logger;
        __columnToFieldMapping = columnToFieldMapping;
    }


    protected final Connection getConnection() {
        return transactionManager.getConnection();
    }


    protected <T> T selectSingleEntity(final Class<T> businessEnityClass, final String sqlQuery,
                                       final Object... params) {
        try {
            return run.query(getConnection(), logSQL(__logger, sqlQuery, params),
                    new BasicBeanHandler<>(businessEnityClass, __columnToFieldMapping), params);
        } catch (final SQLException e) {
            throw new TechnicalException(e);
        }
    }


    protected <T> List<T> selectEntityList(final Class<T> businessEnityClass, final String sqlQuery,
                                           final Object... params) {
        List<T> businessEntityList = null;
        try {
            businessEntityList = run.query(getConnection(), logSQL(__logger, sqlQuery, params),
                    new BasicBeanListHandler<>(businessEnityClass, __columnToFieldMapping), params);
        } catch (final SQLException e) {
            throw new TechnicalException(e);
        }

        return businessEntityList == null ? Collections.emptyList() : businessEntityList;

    }


    protected <T> ConfigurationBE insertEntity(final T insertBusinessEntity) {
        return insertEntity(insertBusinessEntity, null);
    }


    protected <T> ConfigurationBE insertEntity(final T insertBusinessEntity, final String tableName) {
        final QueryRunner run = new QueryRunner();
        final SQL.SQLWithParameter sql = SQL.insertSQL(insertBusinessEntity, tableName, __columnToFieldMapping);
        final Object[] id;

        ConfigurationBE businessEntityAfterInsert = null;
        try {
            transactionManager.begin();

            businessEntityAfterInsert = run.insert(getConnection(), logSQL(__logger, sql.sql, sql.parameter),
                    new BasicBeanHandler<>(
                            ConfigurationBE.class, __columnToFieldMapping), sql.parameter);

            transactionManager.commit();
        } catch (final SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }


        return businessEntityAfterInsert;
    }


    public <T> void updateEntity(final T updateBusinessEntity) {
        updateEntity(updateBusinessEntity, null, null);
    }


    public <T> void updateEntity(final T updateBusinessEntity, final String tableName, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.updateSQL(updateBusinessEntity, tableName, fieldSelector,
                __columnToFieldMapping);
        runUpdate(sql);
    }


    public <T> void deleteEntity(final T deleteBusinessEntity) {
        deleteEntity(deleteBusinessEntity, null, null);
    }


    public <T> void deleteEntity(final T deleteBusinessEntity, final String tableName, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.deleteSQL(deleteBusinessEntity, tableName, fieldSelector,
                __columnToFieldMapping);
        runUpdate(sql);
    }


    private void runUpdate(final SQL.SQLWithParameter sql) {
        final QueryRunner run = new QueryRunner();

        try {
            transactionManager.begin();

            run.update(getConnection(), logSQL(__logger, sql.sql, sql.parameter), sql.parameter);

            transactionManager.commit();

        } catch (final SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }
    }


    protected final String logSQL(final Logger logger, final String sql) {
        if (logger.isInfoEnabled()) {
            logger.info(sql);
        }
        return sql;
    }


    protected final String logSQL(final Logger logger, final String sql, final Object... para) {
        if (logger.isInfoEnabled()) {
            final String formatString = sql.replace("?", "%s");
            logger.info(String.format(formatString, para));
        }
        return sql;
    }
}
