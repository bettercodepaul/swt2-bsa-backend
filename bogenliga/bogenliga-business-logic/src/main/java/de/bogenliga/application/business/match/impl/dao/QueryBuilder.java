package de.bogenliga.application.business.match.impl.dao;

import de.bogenliga.application.common.validation.Preconditions;

/**
 * Simple query builder utility class. Use it in case you: - don't want those ugly raw SQL strings inside your DAO -
 * prefer a readable and easy-to-use way to build your queries
 * <p>
 * Does support: Simple, non-nested queries for a single table with currently no individual field selection.
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class QueryBuilder {

    private static final String SQL_SELECT = "SELECT ";
    private static final String SQL_SELECT_ALL = SQL_SELECT + "* ";
    private static final String SQL_FROM = " FROM ";
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_ORDER_BY = " ORDER BY ";
    private static final String SQL_ORDER_ASC = " ASC ";
    private static final String SQL_ORDER_DESC = " DESC ";
    private static final String SQL_VALUE_EQUAL_PLACEHOLDER = "=? ";
    private static final String SQL_VALUE_GT_PLACEHOLDER = ">? ";
    private static final String SQL_VALUE_GTE_PLACEHOLDER = ">=? ";
    private static final String SQL_VALUE_LT_PLACEHOLDER = "<? ";
    private static final String SQL_VALUE_LTE_PLACEHOLDER = "<=? ";
    private static final String SQL_QUERY_TERMINATOR = ";";

    private String queryString;


    public QueryBuilder() {
        this.queryString = "";
    }


    public QueryBuilder selectAll() {
        this.queryString += SQL_SELECT_ALL;
        return this;
    }


    public QueryBuilder selectFields(String ... fieldNames) {
        StringBuilder builder = new StringBuilder(this.queryString);
        builder.append(SQL_SELECT);
        int maxIdx = fieldNames.length - 1;
        String lastItem = fieldNames[maxIdx];
        for (String fieldName: fieldNames) {
            builder.append(fieldName);
            if (!fieldName.equals(lastItem)) {
                builder.append(", ");
            } else {
                builder.append(" ");
            }
        }
        this.queryString = builder.toString();
        return this;
    }


    public QueryBuilder from(final String tableName) {
        this.queryString += SQL_FROM + tableName;
        return this;
    }


    public QueryBuilder whereEquals(final String fieldName) {
        this.queryString += SQL_WHERE + fieldName + SQL_VALUE_EQUAL_PLACEHOLDER;
        return this;
    }


    public QueryBuilder whereGt(final String fieldName) {
        this.queryString += SQL_WHERE + fieldName + SQL_VALUE_GT_PLACEHOLDER;
        return this;
    }


    public QueryBuilder whereGte(final String fieldName) {
        this.queryString += SQL_WHERE + fieldName + SQL_VALUE_GTE_PLACEHOLDER;
        return this;
    }


    public QueryBuilder whereLt(final String fieldName) {
        this.queryString += SQL_WHERE + fieldName + SQL_VALUE_LT_PLACEHOLDER;
        return this;
    }


    public QueryBuilder whereLte(final String fieldName) {
        this.queryString += SQL_WHERE + fieldName + SQL_VALUE_LTE_PLACEHOLDER;
        return this;
    }


    public QueryBuilder and(final String fieldName) {
        this.queryString += SQL_AND + fieldName + SQL_VALUE_EQUAL_PLACEHOLDER;
        return this;
    }


    public QueryBuilder orderBy(final String fieldName) {
        this.queryString += SQL_ORDER_BY + fieldName;
        return this;
    }


    public QueryBuilder orderByAsc(final String fieldName) {
        this.queryString += this.orderBy(fieldName) + SQL_ORDER_ASC;
        return this;
    }


    public QueryBuilder orderByDesc(final String fieldName) {
        this.queryString += this.orderBy(fieldName) + SQL_ORDER_DESC;
        return this;
    }


    private void formatQueryString() {
        // replace double spaces with single
        this.queryString = this.queryString.replace("  ", " ");
    }


    private void terminateQuery() {
        this.queryString += SQL_QUERY_TERMINATOR;
    }


    public QueryBuilder compose() {
        this.terminateQuery();
        this.formatQueryString();
        return this;
    }


    public String toString() {
        Preconditions.checkArgument(
                this.queryString.contains(SQL_QUERY_TERMINATOR),
                "QueryString must be composed before usage!"
        );
        return this.queryString;
    }

}
