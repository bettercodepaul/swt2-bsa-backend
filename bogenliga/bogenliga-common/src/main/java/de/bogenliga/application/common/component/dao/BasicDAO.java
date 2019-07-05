package de.bogenliga.application.common.component.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;
import de.bogenliga.application.common.database.SQL;
import de.bogenliga.application.common.database.tx.TransactionManager;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;
import de.bogenliga.application.common.time.DateProvider;


/**
 * Basic data access object implementation for CRUD operations.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/apache-commons-dbutils">A Guide to Apache Commons DbUtils</a>
 */
@Repository
public class BasicDAO implements DataAccessObject {

    private static final String DEFAULT_BE_CREATED_AT = "createdAtUtc";
    private static final String DEFAULT_BE_CREATED_BY = "createdByUserId";
    private static final String DEFAULT_BE_MODIFIED_AT = "lastModifiedAtUtc";
    private static final String DEFAULT_BE_MODIFIED_BY = "lastModifiedByUserId";
    private static final String DEFAULT_BE_VERSION = "version";
    private static final String DEFAULT_TABLE_CREATED_AT = "created_at_utc";
    private static final String DEFAULT_TABLE_CREATED_BY = "created_by";
    private static final String DEFAULT_TABLE_MODIFIED_AT = "last_modified_at_utc";
    private static final String DEFAULT_TABLE_MODIFIED_BY = "last_modified_by";
    private static final String DEFAULT_TABLE_VERSION = "version";
    private final TransactionManager transactionManager;
    private QueryRunner run = new QueryRunner();

    @Autowired
    public BasicDAO(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Package-protected constructor with all dependencies
     */
    BasicDAO(TransactionManager transactionManager, QueryRunner queryRunner) {
        this.transactionManager = transactionManager;
        run = queryRunner;
    }


    /**
     * I return a map with the table column to business entity parameter mapping.
     * <p>
     * The common business entities have these columns.
     *
     * @return map with the table column to business entity parameter mapping
     *
     * @see CommonBusinessEntity
     */
    public static Map<String, String> getTechnicalColumnsToFieldsMap() {
        Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(DEFAULT_TABLE_CREATED_AT, DEFAULT_BE_CREATED_AT);
        columnsToFieldsMap.put(DEFAULT_TABLE_CREATED_BY, DEFAULT_BE_CREATED_BY);
        columnsToFieldsMap.put(DEFAULT_TABLE_MODIFIED_AT, DEFAULT_BE_MODIFIED_AT);
        columnsToFieldsMap.put(DEFAULT_TABLE_MODIFIED_BY, DEFAULT_BE_MODIFIED_BY);
        columnsToFieldsMap.put(DEFAULT_TABLE_VERSION, DEFAULT_BE_VERSION);
        return columnsToFieldsMap;
    }


    /**
     * I set the creation parameter for the user and the timestamp.
     *
     * @param businessEntity with common technical paramater
     * @param currentUserId  current user
     *
     * @see CommonBusinessEntity
     */
    public <T extends CommonBusinessEntity> void setCreationAttributes(T businessEntity,
                                                                       long currentUserId) {
        businessEntity.setCreatedByUserId(currentUserId);
        businessEntity.setCreatedAtUtc(DateProvider.currentTimestampUtc());
    }




    /**
     * I set the modification parameter for the user and the timestamp.
     *
     * @param businessEntity with common technical paramater
     * @param currentUserId  current user
     *
     * @see CommonBusinessEntity
     */
    public <T extends CommonBusinessEntity> void setModificationAttributes(T businessEntity,
                                                                           long currentUserId) {
        businessEntity.setLastModifiedByUserId(currentUserId);
        businessEntity.setLastModifiedAtUtc(DateProvider.currentTimestampUtc());
    }


    /**
     * I return a single {@link BusinessEntity} for the given
     * sql SELECT query.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the "
     *                                    object-relational" mapping between the business entity and the database table.
     * @param sqlQuery                    to request the business entity
     * @param params                      The parameter(s) are used to identify the single business entity
     *                                    in the WHERE clause.
     * @return instance of the business entity which is defined in the {@code businessEntityConfiguration}
     */
    public <T> T selectSingleEntity(BusinessEntityConfiguration<T> businessEntityConfiguration,
                                    String sqlQuery,
                                    Object... params) {
        try {
            return run.query(getConnection(), logSQL(businessEntityConfiguration.getLogger(), sqlQuery, params),
                    new BasicBeanHandler<>(businessEntityConfiguration.getBusinessEntity(),
                            businessEntityConfiguration.getColumnToFieldMapping()), params);
        } catch (SQLException e) {
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        }
    }


    /**
     * I return a list of {@link BusinessEntity} for the given
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
    public <T> List<T> selectEntityList(BusinessEntityConfiguration<T> businessEntityConfiguration,
                                        String sqlQuery,
                                        Object... params) {
        List<T> businessEntityList;
        try {
            businessEntityList = run.query(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sqlQuery, params),
                    new BasicBeanListHandler<>(businessEntityConfiguration.getBusinessEntity(),
                            businessEntityConfiguration.getColumnToFieldMapping()), params);
        } catch (SQLException e) {
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        }

        return businessEntityList == null ? Collections.emptyList() : businessEntityList;

    }


    /**
     * I persist a single {@link BusinessEntity}
     *
     * Encapsulate the INSERT query into a transaction.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param insertBusinessEntity        business entity to persist
     *                                    the INSERT sql query is automatically generated
     * @return instance of the persisted business entity
     */
    public <T> T insertEntity(BusinessEntityConfiguration<T> businessEntityConfiguration,
                              T insertBusinessEntity) {
        SQL.SQLWithParameter sql = SQL.insertSQL(insertBusinessEntity, businessEntityConfiguration.getTable(),
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
        } catch (SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        } finally {
            transactionManager.release();
        }

        return businessEntityAfterInsert;
    }


    /**
     * I update one or more {@link BusinessEntity} objects
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
    <T> int updateEntities(BusinessEntityConfiguration<T> businessEntityConfiguration,
                           T updateBusinessEntity, String... fieldSelector) {
        SQL.SQLWithParameter sql = SQL.updateSQL(updateBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());
        try {
            transactionManager.begin();

            int affectedRows = runUpdate(businessEntityConfiguration, sql);

            transactionManager.commit();

            return affectedRows;
        } catch (SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        } finally {
            transactionManager.release();
        }
    }


    /**
     * I update a single {@link CommonBusinessEntity} object
     * in the database.
     *
     * I validate the version of the given object and detect concurrent modification conflicts.
     *
     * I encapsulate the UPDATE and SELECT query into a transaction.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param updateBusinessEntity        business entity with a version field to persist
     *                                    the UPDATE sql query is automatically generated
     * @param fieldSelector               to identify the target table row in the WHERE clause
     * @return instance of the updated business entity
     * @throws BusinessException if no or more than 1 row is affected by the update
     */
    <T extends CommonBusinessEntity> T updateVersionedEntity(BusinessEntityConfiguration<T>
                                                                     businessEntityConfiguration,
                                                             T updateBusinessEntity,
                                                             String... fieldSelector) {
        // check concurrent modification
        SQL.SQLWithParameter selectSql = SQL.selectSQL(updateBusinessEntity,
                businessEntityConfiguration.getTable(), fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());

        T objectBeforeUpdate = selectSingleEntity(businessEntityConfiguration, selectSql.getSql(),
                selectSql.getParameter());

        if (objectBeforeUpdate.getVersion() != updateBusinessEntity.getVersion()) {
            throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, "The business entity was modified by an other user.");
        } // else: do update

        return updateEntity(businessEntityConfiguration, updateBusinessEntity, fieldSelector);
    }


    /**
     * I update a single {@link BusinessEntity} object
     * in the database.
     *
     * Encapsulate the UPDATE and SELECT query into a transaction.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param updateBusinessEntity        business entity to persist
     *                                    the UPDATE sql query is automatically generated
     * @param fieldSelector               to identify the target table row in the WHERE clause
     * @return instance of the updated business entity
     * @throws BusinessException if no or more than 1 row is affected by the update
     */
    public <T> T updateEntity(BusinessEntityConfiguration<T> businessEntityConfiguration,
                              T updateBusinessEntity, String... fieldSelector) {
        SQL.SQLWithParameter sql = SQL.updateSQL(updateBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());

        T businessEntityAfterUpdate;

        try {
            transactionManager.begin();

            int affectedRows = run.update(getConnection(),
                    logSQL(businessEntityConfiguration.getLogger(), sql.getSql(), sql.getParameter()),
                    sql.getParameter());


            SQL.SQLWithParameter selectSql = SQL.selectSQL(updateBusinessEntity,
                    businessEntityConfiguration.getTable(), fieldSelector,
                    businessEntityConfiguration.getColumnToFieldMapping());

            if (affectedRows == 1) {

                businessEntityAfterUpdate = selectSingleEntity(businessEntityConfiguration, selectSql.getSql(),
                        selectSql.getParameter());

                transactionManager.commit();

            } else if (affectedRows == 0) {
                transactionManager.rollback();

                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                        String.format("Update of business entity '%s' does not affect any row",
                                updateBusinessEntity.toString()), selectSql.getParameter()[0]);
            } else {
                transactionManager.rollback();

                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                        String.format("Update of business entity '%s' affected %d rows",
                                updateBusinessEntity.toString(), affectedRows), selectSql.getParameter()[0]);
            }

        } catch (SQLException | TechnicalException | IndexOutOfBoundsException e) {
            transactionManager.rollback();
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        } finally {
            transactionManager.release();
        }

        return businessEntityAfterUpdate;
    }


    /**
     * I delete a single {@link BusinessEntity} object
     * from the database.
     *
     * @param businessEntityConfiguration The {@code businessEntityConfiguration} is used to process the
     *                                    "object-relational" mapping between the business entity and the database table
     * @param deleteBusinessEntity        business entity to delete
     *                                    the DELETE sql query is automatically generated
     * @param fieldSelector               to identify the target table row in the WHERE clause
     * @throws BusinessException if no or more than 1 row is affected by the delete.
     */
    public <T> void deleteEntity(BusinessEntityConfiguration<T> businessEntityConfiguration,
    T deleteBusinessEntity, String... fieldSelector) {
        SQL.SQLWithParameter sql = SQL.deleteSQL(deleteBusinessEntity, businessEntityConfiguration.getTable(),
                fieldSelector,
                businessEntityConfiguration.getColumnToFieldMapping());
        try {
            transactionManager.begin();

            int affectedRows = runUpdate(businessEntityConfiguration, sql);

            // last parameter := identifier for the update query
            Object fieldSelectorValue = sql.getParameter()[sql.getParameter().length - 1];

            if (affectedRows == 1) {
                transactionManager.commit();
            } else {
                transactionManager.rollback();

                throw new BusinessException(ErrorCode.INVALID_ARGUMENT_ERROR,
                        String.format("Deletion of business entity '%s' does affect %d rows",
                                deleteBusinessEntity.toString(), affectedRows), fieldSelectorValue);
            }
        } catch (SQLException e) {
            transactionManager.rollback();
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        } finally {
            transactionManager.release();
        }
    }


    /**
     * I log the sql query with the given logger instance and return the query to the query runner.
     *
     * @param logger            specific {@link DataAccessObject} logger to log the logging message source in the log output
     * @param sql               query with ?-parameters to log
     * @param sqlQueryParameter the ?-parameters are replaced with the {@code sqlQueryParameter}
     * @return sql query with parameters
     */
    public final String logSQL(Logger logger, String sql, Object... sqlQueryParameter) {
        if (logger.isInfoEnabled()) {
            String formatString = sql.replace("?", "%s");
            logger.info(String.format(formatString, sqlQueryParameter));
        }
        return sql;
    }


    /**
     * Provide database connection
     *
     * @return {@link Connection}
     */
    private Connection getConnection() {
        return transactionManager.getConnection();
    }


    private <T> int runUpdate(BusinessEntityConfiguration<T> businessEntityConfiguration,
                              SQL.SQLWithParameter sql) throws SQLException {
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
    final String logSQL(Logger logger, String sql) {
        if (logger.isInfoEnabled()) {
            logger.info(sql);
        }
        return sql;
    }
}
