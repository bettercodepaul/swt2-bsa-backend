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
public class BasicDAO implements DataAccessObject {

    private QueryRunner run = new QueryRunner();
    private final TransactionManager transactionManager;


    @Autowired
    public BasicDAO(final TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    /**
     * Package-protected constructor with all dependencies
     */
    BasicDAO(final TransactionManager transactionManager, final QueryRunner queryRunner) {
        this.transactionManager = transactionManager;
        this.run = queryRunner;
    }


    /**
     * Provide database connection
     *
     * @return {@link Connection}
     */
    private Connection getConnection() {
        return transactionManager.getConnection();
    }


    /**
     * I return a single {@link online.bogenliga.application.common.component.entity.BusinessEntity} for the given
     * sql SELECT query.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the "
     *                                    object-relational" mapping between the business entity and the database table.
     * @param sqlQuery                    to request the business entity
     * @param params                      The parameter(s) are used to identify the single business entity
     *                                    in the WHERE clause.
     * @return instance of the business entity which is defined in the {@code businessEntityConfiguration}
     */
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


    /**
     * I return a list of {@link online.bogenliga.application.common.component.entity.BusinessEntity} for the given
     * sql SELECT query.
     *
     * The instance of the business entity is defined in the {@code businessEntityConfiguration}
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param sqlQuery                    to request the list of business entities
     * @param params                      The parameter(s) are used to select the business entities in the WHERE clause
     * @return list of business entities
     */
    public <T> List<T> selectEntityList(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                        final String sqlQuery,
                                        final Object... params) {
        final List<T> businessEntityList;
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


    /**
     * I persist a single {@link online.bogenliga.application.common.component.entity.BusinessEntity}
     *
     * Encapsulate the INSERT query into a transaction.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param insertBusinessEntity        business entity to persist
     *                                    the INSERT sql query is automatically generated
     * @return instance of the persisted business entity
     */
    public <T> T insertEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                              final T insertBusinessEntity) {
        final SQL.SQLWithParameter sql = SQL.insertSQL(insertBusinessEntity, businessEntityConfiguration.getTable(),
                businessEntityConfiguration.getColumnToFieldMapping());

        T businessEntityAfterInsert;
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


    /**
     * I update one or more {@link online.bogenliga.application.common.component.entity.BusinessEntity} objects
     * in the database.
     *
     * Encapsulate the UPDATE query into a transaction.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param updateBusinessEntity        business entity to persist
     *                                    the UPDATE sql query is automatically generated
     * @param fieldSelector               to identify the target table rows in the WHERE clause
     * @return number of modified table rows
     */
    public <T> int updateEntities(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                  final T updateBusinessEntity, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.updateSQL(updateBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());
        try {
            transactionManager.begin();

            final int affectedRows = runUpdate(businessEntityConfiguration, sql);

            transactionManager.commit();

            return affectedRows;
        } catch (final SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }
    }


    /**
     * I update a single {@link online.bogenliga.application.common.component.entity.BusinessEntity} object
     * in the database.
     *
     * Encapsulate the UPDATE and SELECT query into a transaction.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param updateBusinessEntity        business entity to persist
     *                                    the UPDATE sql query is automatically generated
     * @param fieldSelector               to identify the target table row in the WHERE clause
     * @param singleSelectSql             to select the modified business entity after the update
     * @return instance of the updated business entity
     * @throws BusinessException if no or more than 1 row is affected by the update
     */
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
                // last parameter := identifier for the update query
                final Object fieldSelectorValue = sql.getParameter()[sql.getParameter().length - 1];

                businessEntityAfterUpdate = selectSingleEntity(businessEntityConfiguration, singleSelectSql,
                        fieldSelectorValue);

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

        } catch (final SQLException | TechnicalException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }

        return businessEntityAfterUpdate;
    }


    /**
     * I delete a single {@link online.bogenliga.application.common.component.entity.BusinessEntity} object
     * from the database.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param deleteBusinessEntity        business entity to delete
     *                                    the DELETE sql query is automatically generated
     * @param fieldSelector               to identify the target table row in the WHERE clause
     * @throws BusinessException if no or more than 1 row is affected by the delete.
     */
    public <T> void deleteEntity(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                                 final T deleteBusinessEntity, final String fieldSelector) {
        final SQL.SQLWithParameter sql = SQL.deleteSQL(deleteBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());
        try {
            transactionManager.begin();

            final int affectedRows = runUpdate(businessEntityConfiguration, sql);

            if (affectedRows == 1) {
                transactionManager.commit();
            } else {
                transactionManager.rollback();

                throw new BusinessException(String.format("Deletion of business entity '%s' does affect %d rows",
                        deleteBusinessEntity.toString(), affectedRows));
            }
        } catch (final SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(e);
        } finally {
            transactionManager.release();
        }
    }


    private <T> int runUpdate(final BusinessEntityConfiguration<T> businessEntityConfiguration,
                              final SQL.SQLWithParameter sql) throws SQLException {
        int affectedRows = 0;

        affectedRows = run.update(getConnection(),
                logSQL(businessEntityConfiguration.getLogger(), sql.getSql(), sql.getParameter()),
                sql.getParameter());

        return affectedRows;
    }


    /**
     * I log the sql query with the given logger instance and return the query to the query runner.
     *
     * @param logger specific {@link DataAccessObject} logger to log the logging message source in the log output
     * @param sql    query to log
     * @return sql query
     */
    protected final String logSQL(final Logger logger, final String sql) {
        if (logger.isInfoEnabled()) {
            logger.info(sql);
        }
        return sql;
    }


    /**
     * I log the sql query with the given logger instance and return the query to the query runner.
     *
     * @param logger            specific {@link DataAccessObject} logger to log the logging message source in the log output
     * @param sql               query with ?-parameters to log
     * @param sqlQueryParameter the ?-parameters are replaced with the {@code sqlQueryParameter}
     * @return sql query with parameters
     */
    protected final String logSQL(final Logger logger, final String sql, final Object... sqlQueryParameter) {
        if (logger.isInfoEnabled()) {
            final String formatString = sql.replace("?", "%s");
            logger.info(String.format(formatString, sqlQueryParameter));
        }
        return sql;
    }
}
