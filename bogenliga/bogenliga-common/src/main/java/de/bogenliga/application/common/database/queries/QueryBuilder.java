package de.bogenliga.application.common.database.queries;

import java.util.HashMap;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * <p>
 * "Not so simple anymore" query builder utility class. Use it in case you
 * don't want those unhandy raw SQL strings inside your DAO.
 *
 * Does support:
 * Simple queries, subselects, simple joins with equality, aliases for table names and fields, grouping and ordering.
 *
 * Best practice:
 * Define constants for your table, table columns and required aliases and use them in the query builder api. -> DRY
 * If you don't know how something works, check out the QueryBuilderTest. Some common cases are shown there.
 *
 * <br>
 * Usage e.g.: new QueryBuilder()
 *                      .selectAll()
 *                      .from(TABLE)
 *                      .whereEquals(FIELD)
 *                      .orderByAsc(ANOTHER_FIELD)
 *                      .compose().toString();
 *
 * Advanced usage:
 *      new QueryBuilder()
 *             .selectFieldsWithAliases(TABLE_ALIAS, FIELD, OTHER_TABLE_ALIAS, OTHER_FIELD)
 *             .from(TABLE, TABLE_ALIAS)
 *                 .joinLeft(OTHER_TABLE, OTHER_TABLE_ALIAS)
 *                 .on(TABLE_ALIAS, FIELD, OTHER_TABLE_ALIAS, OTHER_FIELD)
 *             .whereEquals(
 *                  ANOTHER_FIELD,
 *                  new SubQueryBuilder()
 *                          .selectField(YET_ANOTHER_FIELD)
 *                          .from(YET_ANOTHER_TABLE)
 *                          .whereEquals(YET_ANOTHER_FIELD)
 *             )
 *             .groupBy(FIELD)
 *             .orderByAsc(FIELD)
 *             .compose().toString();
 *
 *      Result: SELECT t1.field, t2.other_field
 *              FROM table t1
 *              LEFT JOIN other_table t2
 *              ON t1.field = t2.other_field
 *              WHERE another_field = (
 *                  SELECT yet_another_field
 *                  FROM yet_another_table
 *                  WHERE yet_another_field = ?
 *              )
 *              GROUP BY field
 *              ORDER BY field ASC;
 * </p>
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class QueryBuilder {

    public static final String SQL_VALUE_PLACEHOLDER = "? ";
    public static final String SQL_QUERY_TERMINATOR = ";";
    public static final String SQL_FIELD_SEPARATOR = ", ";

    // Statements/Clauses
    public static final String SQL_SELECT = "SELECT ";
    public static final String SQL_SELECT_ALL = String.format("%s* ", SQL_SELECT);
    public static final String SQL_FROM = " FROM ";
    public static final String SQL_JOIN = " JOIN ";
    public static final String SQL_ON = " ON ";
    public static final String SQL_LEFT_JOIN = " LEFT " + SQL_JOIN;
    public static final String SQL_RIGHT_JOIN = " RIGHT " + SQL_JOIN;
    public static final String SQL_WHERE = " WHERE ";
    public static final String SQL_AND = " AND ";
    public static final String SQL_IN = " IN ";
    public static final String SQL_IN_VALUE = SQL_IN + SQL_VALUE_PLACEHOLDER;
    public static final String SQL_GROUP_BY = " GROUP BY ";
    public static final String SQL_HAVING = " HAVING ";
    public static final String SQL_ORDER_BY = " ORDER BY ";
    public static final String SQL_ORDER_ASC = " ASC ";
    public static final String SQL_ORDER_DESC = " DESC ";

    // Comparators
    public static final String SQL_EQUALS = " = ";
    public static final String SQL_GTE = " >= ";
    public static final String SQL_EQUAL_COMPARATOR = String.format(" %s? ", SQL_EQUALS);
    public static final String SQL_GT_COMPARATOR = String.format(" >%s ", SQL_VALUE_PLACEHOLDER);
    public static final String SQL_GTE_COMPARATOR = String.format(" %s%s ", SQL_GTE, SQL_VALUE_PLACEHOLDER);
    public static final String SQL_LT_COMPARATOR = String.format(" <%s ", SQL_VALUE_PLACEHOLDER);
    public static final String SQL_LTE_COMPARATOR = String.format(" <=%s ", SQL_VALUE_PLACEHOLDER);
    public static final String SQL_TRUE_COMPARATOR = SQL_EQUALS + "true ";
    public static final String SQL_FALSE_COMPARATOR = SQL_EQUALS + "false ";

    // functionName(parameter)
    private static final String SQL_FUNCTION_TEMPLATE = "%s(%s)";

    // value -> 'value'
    private static final String SQL_VALUE_QUOTE_TEMPLATE = "'%s'";

    // "value" -> " value "
    private static final String SQL_SPACED_VALUE_TEMPLATE = " %s ";

    // table, column -> "table.column"
    private static final String SQL_FIELD_ALIAS_TEMPLATE = "%s.%s";

    // t1.column, t2.column -> "ON t1.column = t2.column"
    private static final String SQL_JOIN_ON_TEMPLATE = SQL_ON + "%s" + SQL_EQUALS + "%s";

    // SELECT ... -> (SELECT ...)
    private static final String SQL_SUB_SELECT_TEMPLATE = "(%s)";

    protected String queryString;
    protected HashMap<String, String> aliases;
    protected QueryValidator queryValidator;


    public QueryBuilder() {
        this.queryString = "";
        this.queryValidator = new QueryValidator(this);
        this.aliases = new HashMap<>();
    }


    /**
     * SELECT operations
     */

    public QueryBuilder selectAll() {
        this.queryString += SQL_SELECT_ALL;
        return this;
    }


    public QueryBuilder selectField(String fieldName) {
        this.queryString += SQL_SELECT + fieldName;
        return this;
    }


    public QueryBuilder selectFields(String... fieldNames) {
        this.queryString += SQL_SELECT;
        this.queryString += getFieldList(fieldNames);
        return this;
    }


    public QueryBuilder selectFieldsWithTableNames(String... fieldNames) {
        Preconditions.checkArgument(
                fieldNames.length % 2 == 0,
                "When using aliases or explicit table names, " +
                        "make sure each column has its table alias/name (-> even arg count)."
        );

        StringBuilder builder = new StringBuilder(this.queryString);
        builder.append(SQL_SELECT);
        int maxIdx = fieldNames.length - 1;
        String lastItem = fieldNames[maxIdx];
        for (int i = 0; i < fieldNames.length; i += 2) {
            String fieldName = fieldNames[i];
            if (i + 1 < fieldNames.length) {
                String alias = fieldName;
                fieldName = fieldNames[i + 1];
                builder.append(withAlias(alias, fieldName));
            } else {
                builder.append(fieldName);
            }
            if (!fieldName.equals(lastItem)) {
                builder.append(SQL_FIELD_SEPARATOR);
            } else {
                builder.append(" ");
            }
        }
        this.queryString = builder.toString();
        return this;
    }


    public QueryBuilder selectFieldsWithAliases(String... fieldNames) {
        return this.selectFieldsWithTableNames(fieldNames);
    }


    /**
     * FROM operations
     */

    public QueryBuilder from(final String tableName) {
        this.addFrom(tableName);
        return this;
    }


    public QueryBuilder from(final String tableName, final String alias) {
        this.addFrom(tableName, alias);
        return this;
    }


    public QueryBuilder andFrom(final String tableName) {
        this.addAndFrom(tableName);
        return this;
    }


    public QueryBuilder andFrom(final String tableName, final String alias) {
        this.addAndFrom(tableName, alias);
        return this;
    }


    private void addFrom(final String tableName) {
        this.queryValidator.checkDuplicateFrom();
        this.queryValidator.validateFrom(tableName);

        this.queryString += SQL_FROM + spaceAround(tableName);
    }


    private void addFrom(final String tableName, final String alias) {
        this.queryValidator.checkDuplicateFrom();
        this.queryValidator.ensureValidString(alias);
        this.registerAlias(alias, tableName);

        this.addFrom(tableName);
        this.queryString += spaceAround(alias);
    }


    private void addAndFrom(final String tableName) {
        this.queryValidator.validateFrom(tableName);

        this.queryString += SQL_FIELD_SEPARATOR + spaceAround(tableName);
    }


    private void addAndFrom(final String tableName, final String alias) {
        this.queryValidator.ensureValidString(alias);
        this.registerAlias(alias, tableName);

        this.addAndFrom(tableName);
        this.queryString += spaceAround(alias);
    }


    /**
     * JOIN operations
     */

    public QueryBuilder join(String otherTableName) {
        this.addJoin(SQL_JOIN, otherTableName);
        return this;
    }


    public QueryBuilder join(String otherTableName, String otherTableAlias) {
        this.addJoinWithAlias(SQL_JOIN, otherTableName, otherTableAlias);
        return this;
    }


    public QueryBuilder joinLeft(String otherTableName) {
        this.addJoin(SQL_LEFT_JOIN, otherTableName);
        return this;
    }


    public QueryBuilder joinLeft(String otherTableName, String otherTableAlias) {
        this.addJoinWithAlias(SQL_LEFT_JOIN, otherTableName, otherTableAlias);
        return this;
    }


    public QueryBuilder joinRight(String otherTableName) {
        this.addJoin(SQL_RIGHT_JOIN, otherTableName);
        return this;
    }


    public QueryBuilder joinRight(String otherTableName, String otherTableAlias) {
        this.addJoinWithAlias(SQL_RIGHT_JOIN, otherTableName, otherTableAlias);
        return this;
    }


    public QueryBuilder on(String fieldName, String otherFieldName) {
        this.addOn(fieldName, otherFieldName);
        return this;
    }


    public QueryBuilder on(String tableAlias, String fieldName, String otherTableAlias, String otherFieldName) {
        this.queryValidator.checkHasAlias(tableAlias);
        this.queryValidator.checkHasAlias(otherTableAlias);

        this.addOn(withAlias(tableAlias, fieldName), withAlias(otherTableAlias, otherFieldName));
        return this;
    }


    private void addJoinWithAlias(String joinType, String otherTableName, String otherTableAlias) {
        this.registerAlias(otherTableAlias, otherTableName);

        this.addJoin(joinType, otherTableName);
        this.queryString += spaceAround(otherTableAlias);
    }


    private void addJoin(String joinType, String otherTableName) {
        this.queryValidator.validateJoin(otherTableName);

        this.queryString += joinType + spaceAround(otherTableName);
    }


    private void addOn(String fieldName, String otherFieldName) {
        this.queryValidator.validateOn(fieldName, otherFieldName);

        this.queryString += String.format(SQL_JOIN_ON_TEMPLATE, spaceAround(fieldName), spaceAround(otherFieldName));
    }


    /**
     * Functions
     */


    /**
     * Usage: [...].whereEquals(fieldName, QueryBuilder.applyFunction(otherFieldName, functionName)) In case a a
     * function must be applied to a fields value
     *
     * @param fieldName
     * @param functionName
     *
     * @return
     */
    public static String applyFunction(String fieldName, String functionName) {
        Preconditions.checkArgument(functionName != null, "Function name must not be emtpy!");
        Preconditions.checkArgument(functionName.length() > 0, "Function name must not be emtpy!");

        return String.format(SQL_FUNCTION_TEMPLATE, functionName.trim(), fieldName.trim());
    }


    /**
     * WHERE operations
     * <p>
     * gt=greater than, gte=gt or equal, lt=lesser than, lte=lt or equal
     */

    /**
     * Equality with added filled value
     *
     * @param fieldName
     *
     * @return
     */
    public QueryBuilder whereEquals(final String fieldName) {
        this.addWhere(fieldName, SQL_EQUAL_COMPARATOR);
        return this;
    }


    /**
     * Equality with fixed given, static value
     *
     * @param fieldName
     * @param value
     *
     * @return
     */
    public QueryBuilder whereEquals(final String fieldName, final String value) {
        this.addWhere(fieldName, SQL_EQUALS);
        this.queryString += spaceAround(quote(value));
        return this;
    }


    /**
     * Equality with raw value e.g. result of a function, so no quoting
     *
     * @param fieldName
     * @param value
     *
     * @return
     */
    public QueryBuilder whereEqualsRaw(final String fieldName, final String value) {
        this.addWhere(fieldName, SQL_EQUALS);
        this.queryString += spaceAround(value);
        return this;
    }


    /**
     * Equality with result of sub select, requires single column select in subquery i guess... (use .selectField(...))
     *
     * @param fieldName
     * @param subQuery
     *
     * @return
     */
    public QueryBuilder whereEquals(final String fieldName, final QueryBuilder subQuery) {
        this.addWhere(fieldName, SQL_EQUALS);
        addSubSelect(subQuery);
        return this;
    }


    /**
     * Equality with other given table column, table name is explicit
     *
     * @param tableName
     * @param fieldName
     * @param otherTableName
     * @param otherFieldName
     *
     * @return
     */
    public QueryBuilder whereEquals(final String tableName, final String fieldName, final String otherTableName,
                                    final String otherFieldName) {
        this.addWhere(withAlias(tableName, fieldName), SQL_EQUALS);
        this.queryString += spaceAround(withAlias(otherTableName, otherFieldName));
        return this;
    }


    /**
     * Equality with given table column, table is referenced via alias.
     *
     * @param tableAlias
     * @param fieldName
     * @param otherTableAlias
     * @param otherFieldName
     *
     * @return
     */
    public QueryBuilder whereEqualsWithAliases(final String tableAlias, final String fieldName,
                                               final String otherTableAlias,
                                               final String otherFieldName) {
        this.queryValidator.checkHasAlias(tableAlias);
        this.queryValidator.checkHasAlias(otherTableAlias);

        this.addWhere(withAlias(tableAlias, fieldName), SQL_EQUALS);
        this.queryString += spaceAround(withAlias(otherTableAlias, otherFieldName));
        return this;
    }


    public QueryBuilder whereGt(final String fieldName) {
        this.addWhere(fieldName, SQL_GT_COMPARATOR);
        return this;
    }


    public QueryBuilder whereGte(final String fieldName) {
        this.addWhere(fieldName, SQL_GTE_COMPARATOR);
        return this;
    }


    public QueryBuilder whereGteRaw(final String fieldName, final String value) {
        this.addWhere(fieldName, SQL_GTE);
        this.queryString += spaceAround(value);
        return this;
    }


    public QueryBuilder whereLt(final String fieldName) {
        this.addWhere(fieldName, SQL_LT_COMPARATOR);
        return this;
    }


    public QueryBuilder whereLte(final String fieldName) {
        this.addWhere(fieldName, SQL_LTE_COMPARATOR);
        return this;
    }


    public QueryBuilder whereTrue(final String fieldName) {
        this.addWhere(fieldName, SQL_TRUE_COMPARATOR);
        return this;
    }


    public QueryBuilder whereFalse(final String fieldName) {
        this.addWhere(fieldName, SQL_FALSE_COMPARATOR);
        return this;
    }


    private void addWhere(final String fieldName, final String comparator) {
        this.queryValidator.validateWhere(fieldName);

        this.queryString += SQL_WHERE + fieldName + comparator;
    }


    /**
     * AND operations
     * <p>
     * gt=greater than, gte=gt or equal, lt=lesser than, lte=lt or equal
     */

    public QueryBuilder andEquals(final String fieldName) {
        addAnd(fieldName, SQL_EQUAL_COMPARATOR);
        return this;
    }


    public QueryBuilder andEquals(final String fieldName, final String value) {
        addAnd(fieldName, SQL_EQUALS);
        this.queryString += spaceAround(quote(value));
        return this;
    }


    public QueryBuilder andEquals(final String fieldName, final QueryBuilder subQuery) {
        addAnd(fieldName, SQL_EQUALS);
        addSubSelect(subQuery);
        return this;
    }


    public QueryBuilder andEquals(final String tableName, final String fieldName, final String otherTableName,
                                  final String otherFieldName) {
        addAnd(withAlias(tableName, fieldName), SQL_EQUALS);
        this.queryString += spaceAround(withAlias(otherTableName, otherFieldName));
        return this;
    }


    public QueryBuilder andEqualsWithAliases(final String tableAlias, final String fieldName,
                                             final String otherTableAlias,
                                             final String otherFieldName) {
        this.queryValidator.checkHasAlias(tableAlias);
        this.queryValidator.checkHasAlias(otherTableAlias);

        addAnd(withAlias(tableAlias, fieldName), SQL_EQUALS);
        this.queryString += spaceAround(withAlias(otherTableAlias, otherFieldName));
        return this;
    }


    public QueryBuilder andGt(final String fieldName) {
        addAnd(fieldName, SQL_GT_COMPARATOR);
        return this;
    }


    public QueryBuilder andGte(final String fieldName) {
        addAnd(fieldName, SQL_GTE_COMPARATOR);
        return this;
    }


    public QueryBuilder andLt(final String fieldName) {
        addAnd(fieldName, SQL_LT_COMPARATOR);
        return this;
    }


    public QueryBuilder andLte(final String fieldName) {
        addAnd(fieldName, SQL_LTE_COMPARATOR);
        return this;
    }


    public QueryBuilder andTrue(final String fieldName) {
        addAnd(fieldName, SQL_TRUE_COMPARATOR);
        return this;
    }


    public QueryBuilder andFalse(final String fieldName) {
        addAnd(fieldName, SQL_FALSE_COMPARATOR);
        return this;
    }


    private void addAnd(final String fieldName, final String comparator) {
        this.queryValidator.validateAnd(fieldName);

        this.queryString += SQL_AND + fieldName + comparator;
    }


    /**
     * IN operations
     */


    public QueryBuilder whereIn(final String fieldName) {
        addWhere(fieldName, SQL_IN_VALUE);
        return this;
    }


    public QueryBuilder andIn(final String fieldName) {
        addAnd(fieldName, SQL_IN_VALUE);
        return this;
    }


    public QueryBuilder whereIn(final String fieldName, final QueryBuilder subQuery) {
        addWhere(fieldName, addIn(getAsSubSelect(subQuery)));
        return this;
    }


    public QueryBuilder andIn(final String fieldName, final QueryBuilder subQuery) {
        addAnd(fieldName, addIn(getAsSubSelect(subQuery)));
        return this;
    }


    /**
     * GROUP BY operations
     */

    public QueryBuilder groupBy(final String... fieldNames) {
        this.queryString += SQL_GROUP_BY + getFieldList(fieldNames);
        return this;
    }

    /**
     * HAVING > operation
     */
    public QueryBuilder havingGt(final String fieldName) {
        this.queryString += SQL_HAVING + fieldName + SQL_GT_COMPARATOR;
        return this;
    }

    /**
     * ORDER BY operations
     */

    public QueryBuilder orderBy(final String fieldName) {
        // default is ASC i guess ...
        addOrdering(fieldName, "");
        return this;
    }


    public QueryBuilder orderByAsc(final String fieldName) {
        addOrdering(fieldName, SQL_ORDER_ASC);
        return this;
    }


    public QueryBuilder orderByDesc(final String fieldName) {
        addOrdering(fieldName, SQL_ORDER_DESC);
        return this;
    }


    public QueryBuilder orderBy(final String tableAlias, final String fieldName) {
        this.queryValidator.checkHasAlias(tableAlias);

        addOrdering(withAlias(tableAlias, fieldName), "");
        return this;
    }


    public QueryBuilder orderByAsc(final String tableAlias, final String fieldName) {
        this.queryValidator.checkHasAlias(tableAlias);

        addOrdering(withAlias(tableAlias, fieldName), SQL_ORDER_ASC);
        return this;
    }


    public QueryBuilder orderByDesc(final String tableAlias, final String fieldName) {
        this.queryValidator.checkHasAlias(tableAlias);

        addOrdering(withAlias(tableAlias, fieldName), SQL_ORDER_DESC);
        return this;
    }


    private void addOrdering(final String fieldName, final String ordering) {
        this.queryValidator.validateOrdering(fieldName);

        this.queryString += SQL_ORDER_BY + fieldName + ordering;
    }


    /**
     * Query composition
     */

    public QueryBuilder compose() {
        this.queryValidator.isComposable();

        this.terminateQuery();
        this.formatQueryString();
        return this;
    }


    protected void terminateQuery() {
        this.queryString += SQL_QUERY_TERMINATOR;
    }


    protected void formatQueryString() {
        // replace double spaces with single
        this.queryString = this.queryString.replace("  ", " ");
        this.queryString = this.queryString.replace("  ", " ");
    }


    public String toString() {
        this.queryValidator.ensureComposed();

        return this.queryString;
    }


    /**
     * Getters for the validator
     */

    protected String getQueryString() {
        return queryString;
    }


    protected HashMap<String, String> getAliases() {
        return aliases;
    }


    /**
     * Utilities
     */

    protected String getFieldList(String[] fieldNames) {
        StringBuilder builder = new StringBuilder();
        int maxIdx = fieldNames.length - 1;
        String lastItem = fieldNames[maxIdx];
        for (String fieldName : fieldNames) {
            builder.append(fieldName);
            if (!fieldName.equals(lastItem)) {
                builder.append(SQL_FIELD_SEPARATOR);
            } else {
                builder.append(" ");
            }
        }
        return builder.toString();
    }


    protected String asSubSelect(String subQuery) {
        return String.format(SQL_SUB_SELECT_TEMPLATE, subQuery);
    }


    protected String addIn(String subSelect) {
        return SQL_IN + subSelect;
    }


    protected String getAsSubSelect(final QueryBuilder subSelect) {
        return spaceAround(asSubSelect(subSelect.compose().toString()));
    }


    protected void addSubSelect(final QueryBuilder subSelect) {
        this.queryString += getAsSubSelect(subSelect);
    }


    protected static String spaceAround(final String value) {
        return String.format(SQL_SPACED_VALUE_TEMPLATE, value);
    }


    protected static String quote(final String value) {
        return String.format(SQL_VALUE_QUOTE_TEMPLATE, value);
    }


    public static String withAlias(final String alias, final String fieldName) {
        return String.format(SQL_FIELD_ALIAS_TEMPLATE, alias, fieldName);
    }


    protected void registerAlias(final String alias, final String tableName) {
        this.aliases.putIfAbsent(alias, tableName);
    }
}
