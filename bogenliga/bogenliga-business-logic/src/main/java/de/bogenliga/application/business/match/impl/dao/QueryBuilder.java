package de.bogenliga.application.business.match.impl.dao;

import java.util.HashMap;

/**
 * <p>
 * "Not so simple anymore" query builder utility class. Use it in case you:
 * <br>
 * - don't want those unhandy raw SQL strings inside your DAO
 * <br>
 * - prefer a readable and easy-to-use way to build your queries
 * <br>
 * Does support:
 * Non-nested queries, simple joins with equality, aliases on table names and for field reference.
 *
 * Best practice:
 * Define constants for your table column names and required aliases and use them in the query builder api. -> DRY!
 *
 * <br>
 * Usage e.g.: new QueryBuilder()
 *                      .selectAll()
 *                      .from(TABLE)
 *                      .whereEquals(FIELD)
 *                      .orderByAsc(ANOTHER_FIELD)
 *                      .compose().toString();
 * Advanced usage (but without constants):
 *      new QueryBuilder()
 *                 .selectFields("mannschaftsmitglied_mannschaft_id", "mannschaftsmitglied_dsb_mitglied_id", "mannschaftsmitglied_dsb_mitglied_eingesetzt", "dsb_mitglied_vorname")
 *                 .from(TABLE, "m").join("sdb_mitglied", "d")
 *                 .on("m", "mannschaftsmitglied_dsb_mitglied_id", "d", "dsb_mitglied_id")
 *                 .whereEquals("mannschaftsmitglied_mannschaft_id")
 *                 .andTrue("mannschaftsmitglied_dsb_mitglied_eingesetzt")
 *                 .compose().toString()
 *
 * <br> <p>TODO: add support for:</p>
 *     -    single field select (new QueryBuilder().selectField(String field).from[...])                                   Y
 *     -    IN e.g. [...].whereIn(field).andIn(field)                                                                      X
 *     -    nested Selects e.g.                                                                                            X
 *     -    *   [...].whereIn(field, new QueryBuilder().selectField().[...])                                               X
 *     -    *   [...].whereEquals(field, new QueryBuilder().selectField().[...])                                           X
 *     -    joins                                                                                                          Y
 *     -    functions (e.g. static QueryBuilder.applyFunction(String, function, String field) -> String)                   Y
 *     -    table aliases                                                                                                  Y
 *     -    multi table selection (SELECT [...] FROM tablenameA, tablenameB WHERE [...])                                   Y
 *     -    explicit table names in fields (SELECT tablenameA.field, tablenameB.field FROM [...]) for each method          X
 *     -    comparators etc. for fields of diff tables (WHERE tablenameA.field=tablenameB.field)                           X
 *     -    whereEquals with fixed value (e.g. [...].whereEquals(field, value) -> "[...]WHERE field='Fixed value'[...]")   Y
 *     -    whereTrue (e.g. whereTrue(field) -> "[...]WHERE field=true[...]")                                              Y
 * </p>
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class QueryBuilder {

    // Statements/Clauses
    public static final String SQL_SELECT = "SELECT ";
    public static final String SQL_SELECT_ALL = SQL_SELECT + "* ";
    public static final String SQL_FROM = " FROM ";
    public static final String SQL_JOIN = " JOIN ";
    public static final String SQL_ON = " ON ";
    public static final String SQL_WHERE = " WHERE ";
    public static final String SQL_AND = " AND ";
    public static final String SQL_ORDER_BY = " ORDER BY ";
    public static final String SQL_ORDER_ASC = " ASC ";
    public static final String SQL_ORDER_DESC = " DESC ";

    // Comparators
    public static final String SQL_EQUALS = " = ";
    public static final String SQL_EQUAL_COMPARATOR = SQL_EQUALS + "? ";
    public static final String SQL_GT_COMPARATOR = ">? ";
    public static final String SQL_GTE_COMPARATOR = ">=? ";
    public static final String SQL_LT_COMPARATOR = "<? ";
    public static final String SQL_LTE_COMPARATOR = "<=? ";
    public static final String SQL_TRUE_COMPARATOR = SQL_EQUALS + "true ";
    public static final String SQL_FALSE_COMPARATOR = SQL_EQUALS + "false ";

    public static final String SQL_QUERY_TERMINATOR = ";";
    public static final String SQL_FIELD_SEPARATOR = ", ";

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

    private String queryString;
    private HashMap<String, String> aliases;
    private QueryValidator queryValidator;


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
        StringBuilder builder = new StringBuilder(this.queryString);
        builder.append(SQL_SELECT);
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
        this.queryString = builder.toString();
        return this;
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
        this.addJoin(otherTableName);
        return this;
    }


    public QueryBuilder join(String otherTableName, String otherTableAlias) {
        this.registerAlias(otherTableAlias, otherTableName);

        this.addJoin(otherTableName);
        this.queryString += spaceAround(otherTableAlias);
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


    private void addJoin(String otherTableName) {
        this.queryValidator.validateJoin(otherTableName);

        this.queryString += SQL_JOIN + spaceAround(otherTableName);
    }


    private void addOn(String fieldName, String otherFieldName) {
        this.queryValidator.validateOn(fieldName, otherFieldName);

        this.queryString += String.format(SQL_JOIN_ON_TEMPLATE, spaceAround(fieldName), spaceAround(otherFieldName));
    }


    /**
     * Functions
     */

    /**
     * Usage: [...].whereEquals(fieldName).addFunction(functionName)) In case a field value should be equal (or gt, lt,
     * ...) to the functions result.
     *
     * @param functionName
     *
     * @return
     */
    public QueryBuilder addFunction(String functionName) {
        this.queryString += spaceAround(String.format(SQL_FUNCTION_TEMPLATE, functionName, ""));
        return this;
    }


    /**
     * Usage: [...].whereEquals(QueryBuilder.applyFunction(fieldName, functionName)) In case a a function must be
     * applied to a fields value
     *
     * @param fieldName
     * @param functionName
     *
     * @return
     */
    public static String applyFunction(String fieldName, String functionName) {
        return String.format(SQL_FUNCTION_TEMPLATE, functionName, fieldName);
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
     * Equality with fixed given value
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
        this.queryString += spaceAround(quote(withAlias(otherTableName, otherFieldName)));
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
        this.queryString += spaceAround(quote(withAlias(otherTableAlias, otherFieldName)));
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


    private void addWhere(String fieldName, String comparator) {
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
        this.addAnd(fieldName, SQL_EQUALS);
        this.queryString += spaceAround(quote(value));
        return this;
    }


    public QueryBuilder andEquals(final String tableName, final String fieldName, final String otherTableName,
                                  final String otherFieldName) {
        this.addAnd(withAlias(tableName, fieldName), SQL_EQUALS);
        this.queryString += spaceAround(quote(withAlias(otherTableName, otherFieldName)));
        return this;
    }


    public QueryBuilder andEqualsWithAliases(final String tableAlias, final String fieldName,
                                             final String otherTableAlias,
                                             final String otherFieldName) {
        this.queryValidator.checkHasAlias(tableAlias);
        this.queryValidator.checkHasAlias(otherTableAlias);

        this.addAnd(withAlias(tableAlias, fieldName), SQL_EQUALS);
        this.queryString += spaceAround(quote(withAlias(otherTableAlias, otherFieldName)));
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


    private void addAnd(String fieldName, String comparator) {
        this.queryValidator.validateAnd(fieldName);

        this.queryString += SQL_AND + fieldName + comparator;
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


    private void terminateQuery() {
        this.queryString += SQL_QUERY_TERMINATOR;
    }


    private void formatQueryString() {
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

    private static String spaceAround(String value) {
        return String.format(SQL_SPACED_VALUE_TEMPLATE, value);
    }


    private static String quote(String value) {
        return String.format(SQL_VALUE_QUOTE_TEMPLATE, value);
    }


    private static String withAlias(String alias, String fieldName) {
        return String.format(SQL_FIELD_ALIAS_TEMPLATE, alias, fieldName);
    }


    private void registerAlias(String alias, String tableName) {
        this.aliases.putIfAbsent(alias, tableName);
    }
}
