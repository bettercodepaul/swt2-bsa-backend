package app.bogenliga.application.business.common.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import app.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
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
@Repository
public abstract class DAO implements DataAccessObject {

    protected final QueryRunner run = new QueryRunner();
    private final TransactionManager transactionManager;


    /**
     * Initialize the transaction manager to provide a database connection
     */
    public DAO() {
        transactionManager = new PostgresqlTransactionManager();
    }


    protected final Connection getConnection() {
        return transactionManager.getConnection();
    }


    protected <T> T selectSingleEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                       final String sqlQuery,
                                       final Object... params) {
        try {
            return run.query(getConnection(), logSQL(businessEntityConfiguration.getLogger(), sqlQuery, params),
                    new BasicBeanHandler<>(businessEntityConfiguration.getBusinessEntity(),
                            businessEntityConfiguration.getColumnToFieldMapping()), params);
        } catch (final SQLException e) {
            throw new TechnicalException(e);
        }
    }


    protected <T> List<T> selectEntityList(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                           final String sqlQuery,
                                           final Object... params) {
        List<T> businessEntityList = null;
        try {
            businessEntityList = run.query(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sqlQuery, params),
                    new BasicBeanListHandler<>(businessEntityConfiguration.getBusinessEntity(),
                            businessEntityConfiguration.getColumnToFieldMapping()), params);
        } catch (final SQLException e) {
            throw new TechnicalException(e);
        }

        return businessEntityList == null ? Collections.emptyList() : businessEntityList;

    }


    protected <T> T insertEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                 final T insertBusinessEntity) {
        final QueryRunner run = new QueryRunner();
        final SQL.SQLWithParameter sql = SQL.insertSQL(insertBusinessEntity, businessEntityConfiguration.getTable(),
                businessEntityConfiguration.getColumnToFieldMapping());
        final Object[] id;

        T businessEntityAfterInsert = null;
        try {
            transactionManager.begin();

            businessEntityAfterInsert = run.insert(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sql.sql, sql.parameter),
                    new BasicBeanHandler<>(
                            businessEntityConfiguration.getBusinessEntity(),
                            businessEntityConfiguration.getColumnToFieldMapping()),
                    sql.parameter);

            transactionManager.commit();
        } catch (final SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }

        return businessEntityAfterInsert;
    }


    public <T> void updateEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                 final T updateBusinessEntity, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.updateSQL(updateBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());
        runUpdate(businessEntityConfiguration, sql);
    }


    public <T> void deleteEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                 final T deleteBusinessEntity, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.deleteSQL(deleteBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());
        runUpdate(businessEntityConfiguration, sql);
    }


    private <T> void runUpdate(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                               final SQL.SQLWithParameter sql) {
        final QueryRunner run = new QueryRunner();

        try {
            transactionManager.begin();

            run.update(getConnection(), logSQL(businessEntityConfiguration.getLogger(), sql.sql, sql.parameter),
                    sql.parameter);

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
