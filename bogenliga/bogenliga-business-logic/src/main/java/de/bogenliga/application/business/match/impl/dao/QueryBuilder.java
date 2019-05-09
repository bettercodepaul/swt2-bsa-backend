package de.bogenliga.application.business.match.impl.dao;

import de.bogenliga.application.common.validation.Preconditions;

/**
 * <p>
 * Simple query builder utility class. Use it in case you:
 * <br>
 * - don't want those unhandy raw SQL strings inside your DAO
 * <br>
 * - prefer a readable and easy-to-use way to build your queries
 * <br>
 * Does support:
 * Simple, non-nested queries for a single table.
 * Has some rough validation for the querystring and the sequence the query methods are called.
 * <br>
 * Usage e.g.: new QueryBuilder()
 *                      .selectAll()
 *                      .from(TABLE)
 *                      .whereEquals(FIELD)
 *                      .orderByAsc(ANOTHER_FIELD)
 *                      .compose().toString();
 * <br> TODO: add support for IN and nested Selects? e.g. ... .whereIn(new QueryBuilder().selectFields()....)
 * </p>
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class QueryBuilder {

    // Statements/Clauses
    public static final String SQL_SELECT = "SELECT ";
    public static final String SQL_SELECT_ALL = SQL_SELECT + "* ";
    public static final String SQL_FROM = " FROM ";
    public static final String SQL_WHERE = " WHERE ";
    public static final String SQL_AND = " AND ";
    public static final String SQL_ORDER_BY = " ORDER BY ";
    public static final String SQL_ORDER_ASC = " ASC ";
    public static final String SQL_ORDER_DESC = " DESC ";

    // Comparators
    public static final String SQL_EQUAL_COMPARATOR = "=? ";
    public static final String SQL_GT_COMPARATOR = ">? ";
    public static final String SQL_GTE_COMPARATOR = ">=? ";
    public static final String SQL_LT_COMPARATOR = "<? ";
    public static final String SQL_LTE_COMPARATOR = "<=? ";

    public static final String SQL_QUERY_TERMINATOR = ";";
    public static final String SQL_FIELD_SEPARATOR = ", ";

    // Validation/Error Messages
    private static final String SQL_ERROR_NO_SELECT = "QueryString has no SELECT clause!";
    private static final String SQL_ERROR_NOT_COMPOSED = "QueryString must be composed before usage!";
    private static final String SQL_ERROR_ALREADY_COMPOSED = "QueryString cannot be modified after composing!";
    private static final String SQL_ERROR_NO_FROM_SELECTION = "QueryString must have a table to select from!";
    private static final String SQL_ERROR_AND_WITHOUT_WHERE = "QueryString must have a WHERE clause in order to have an AND clause!";
    private static final String SQL_ERROR_VALUE_EMPTY = "Given query value must not be empty!";
    private static final String SQL_ERROR_DUPLICATE_WHERE = "QueryString must not already contain a WHERE clause!";
    private static final String SQL_ERROR_DUPLICATE_FROM = "QueryString must not already contain a FROM clause!";

    private String queryString;


    public QueryBuilder() {
        this.queryString = "";
    }


    public QueryBuilder selectAll() {
        this.queryString += SQL_SELECT_ALL;
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


    public QueryBuilder from(final String tableName) {
        this.ensureSelect();
        this.ensureNotComposed();
        Preconditions.checkArgument(!this.queryString.contains(SQL_FROM), SQL_ERROR_DUPLICATE_FROM);
        this.ensureValidString(tableName);

        this.queryString += SQL_FROM + tableName;
        return this;
    }


    /**
     * WHERE operations
     */

    private void addWhere(String fieldName, String comparator) {
        this.validateWhere(fieldName);

        this.queryString += SQL_WHERE + fieldName + comparator;
    }


    private void validateWhere(String fieldName) {
        this.ensureSelect();
        this.ensureFrom();
        this.ensureNotComposed();
        Preconditions.checkArgument(!this.queryString.contains(SQL_WHERE), SQL_ERROR_DUPLICATE_WHERE);
        this.ensureValidString(fieldName);
    }


    public QueryBuilder whereEquals(final String fieldName) {
        this.addWhere(fieldName, SQL_EQUAL_COMPARATOR);
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


    /**
     * AND operations
     * <p>
     * gt=greater than, gte=gt or equal, lt=lesser than, lte=lt or equal
     */

    private void addAnd(String fieldName, String comparator) {
        this.ensureSelect();
        this.ensureFrom();
        this.ensureNotComposed();
        this.ensureValidString(fieldName);
        Preconditions.checkArgument(this.queryString.contains(SQL_WHERE), SQL_ERROR_AND_WITHOUT_WHERE);

        this.queryString += SQL_AND + fieldName + comparator;
    }


    public QueryBuilder andEquals(final String fieldName) {
        addAnd(fieldName, SQL_EQUAL_COMPARATOR);
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


    /**
     * ORDER BY operations
     */

    private void addOrdering(final String fieldName, final String ordering) {
        this.ensureSelect();
        this.ensureFrom();
        this.ensureNotComposed();
        this.ensureValidString(fieldName);

        this.queryString += SQL_ORDER_BY + fieldName + ordering;
    }


    public QueryBuilder orderBy(final String fieldName) {
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


    /**
     * General validations
     */

    private void ensureSelect() {
        Preconditions.checkArgument(this.queryString.contains(SQL_SELECT), SQL_ERROR_NO_SELECT);
    }


    private void ensureFrom() {
        Preconditions.checkArgument(this.queryString.contains(SQL_FROM), SQL_ERROR_NO_FROM_SELECTION);
    }


    private void ensureNotComposed() {
        Preconditions.checkArgument(!this.queryString.contains(SQL_QUERY_TERMINATOR), SQL_ERROR_ALREADY_COMPOSED);
    }


    private void ensureComposed() {
        Preconditions.checkArgument(this.queryString.contains(SQL_QUERY_TERMINATOR), SQL_ERROR_ALREADY_COMPOSED);
    }


    private void ensureValidString(String value) {
        Preconditions.checkArgument(value != null, SQL_ERROR_VALUE_EMPTY);
        Preconditions.checkArgument(value.length() > 0, SQL_ERROR_VALUE_EMPTY);
    }


    /**
     * Query composition
     */

    public QueryBuilder compose() {
        this.ensureNotComposed();
        this.ensureSelect();
        this.ensureFrom();

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
    }


    public String toString() {
        this.ensureComposed();

        return this.queryString;
    }

}
