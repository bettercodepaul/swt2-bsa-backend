package online.bogenliga.application.business.common.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import online.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import online.bogenliga.application.common.component.dao.DataAccessObject;
import online.bogenliga.application.common.database.SQL;
import online.bogenliga.application.common.database.tx.PostgresqlTransactionManager;
import online.bogenliga.application.common.database.tx.TransactionManager;
import online.bogenliga.application.common.errorhandling.exception.TechnicalException;


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
        final SQL.SQLWithParameter sql = SQL.insertSQL(insertBusinessEntity, businessEntityConfiguration.getTable(),
                businessEntityConfiguration.getColumnToFieldMapping());

        T businessEntityAfterInsert = null;
        try {
            transactionManager.begin();

            businessEntityAfterInsert = run.insert(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sql.getSql(), sql.getParameter()),
                    new BasicBeanHandler<>(
                            businessEntityConfiguration.getBusinessEntity(),
                            businessEntityConfiguration.getColumnToFieldMapping()),
                    sql.getParameter());

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
        try {
            transactionManager.begin();

            run.update(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sql.getSql(), sql.getParameter()),
                    sql.getParameter());

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
