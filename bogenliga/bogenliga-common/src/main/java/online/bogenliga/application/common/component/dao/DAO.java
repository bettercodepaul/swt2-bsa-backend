package online.bogenliga.application.common.component.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import online.bogenliga.application.common.database.SQL;
import online.bogenliga.application.common.database.tx.TransactionManager;
import online.bogenliga.application.common.errorhandling.exception.BusinessException;
import online.bogenliga.application.common.errorhandling.exception.TechnicalException;


/**
 * Basic data access object implementation for CRUD operations.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/apache-commons-dbutils">A Guide to Apache Commons DbUtils</a>
 */
@Repository
public class DAO implements DataAccessObject {

    private final QueryRunner run = new QueryRunner();
    private final TransactionManager transactionManager;


    @Autowired
    public DAO(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    private Connection getConnection() {
        return transactionManager.getConnection();
    }


    public <T> T selectSingleEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
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


    public <T> List<T> selectEntityList(final BusinessEntityConfiguration<T> businessEntityConfiguration,
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


    public <T> T insertEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
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


    public <T> int updateEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                final T updateBusinessEntity, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.updateSQL(updateBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());

        return runUpdate(businessEntityConfiguration, sql);
    }


    public <T> T updateEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                              final T updateBusinessEntity, final String fieldSelector, final String singleSelectSql) {
        final SQL.SQLWithParameter sql = SQL.updateSQL(updateBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());

        T businessEntityAfterUpdate;

        try {
            transactionManager.begin();

            final int affectedRows = run.update(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sql.getSql(), sql.getParameter()),
                    sql.getParameter());

            if (affectedRows == 1) {
                businessEntityAfterUpdate = selectSingleEntity(businessEntityConfiguration, singleSelectSql,
                        fieldSelector);

                transactionManager.commit();

            } else if (affectedRows == 0) {
                transactionManager.rollback();

                throw new BusinessException(String.format("Update of business entity '%s' does not affect any row",
                        updateBusinessEntity.toString()));
            } else {
                transactionManager.rollback();

                throw new BusinessException(String.format("Update of business entity '%s' affected %d rows",
                        updateBusinessEntity.toString(), affectedRows));
            }

        } catch (final SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }

        return businessEntityAfterUpdate;
    }


    public <T> void deleteEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                 final T deleteBusinessEntity, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.deleteSQL(deleteBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());
        final int affectedRows = runUpdate(businessEntityConfiguration, sql);

        if (affectedRows != 1) {
            throw new BusinessException(String.format("Deletion of business entity '%s' does affect %d rows",
                    deleteBusinessEntity.toString(), affectedRows));
        }
    }


    private <T> int runUpdate(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                              final SQL.SQLWithParameter sql) {
        int affectedRows = 0;
        try {
            transactionManager.begin();

            affectedRows = run.update(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sql.getSql(), sql.getParameter()),
                    sql.getParameter());

            transactionManager.commit();

        } catch (final SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }

        return affectedRows;
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
