package de.bogenliga.application.business.match.impl.dao;

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
    private static final String SQL_VALUE_PLACEHOLDER = "=? ";

    private String queryString;


    public QueryBuilder() {
        this.queryString = "";
    }


    public QueryBuilder selectAll() {
        this.queryString += SQL_SELECT_ALL;
        return this;
    }


    public QueryBuilder from(String tableName) {
        this.queryString += SQL_FROM + tableName;
        return this;
    }


    public QueryBuilder where(String fieldName) {
        this.queryString += SQL_WHERE + fieldName + SQL_VALUE_PLACEHOLDER;
        return this;
    }


    public QueryBuilder and(String fieldName) {
        this.queryString += SQL_AND + fieldName + SQL_VALUE_PLACEHOLDER;
        return this;
    }


    public QueryBuilder orderBy(String fieldName) {
        this.queryString += SQL_ORDER_BY + fieldName;
        return this;
    }


    public QueryBuilder orderByAsc(String fieldName) {
        this.queryString += this.orderBy(fieldName) + SQL_ORDER_ASC;
        return this;
    }


    public QueryBuilder orderByDesc(String fieldName) {
        this.queryString += this.orderBy(fieldName) + SQL_ORDER_DESC;
        return this;
    }


    private void formatQueryString() {
        // replace double spaces with single
        this.queryString = this.queryString.replace("  ", " ");
    }


    private void terminateQuery() {
        this.queryString += ";";
    }


    public QueryBuilder compose() {
        this.terminateQuery();
        this.formatQueryString();
        return this;
    }


    public String toString() {
        return this.queryString;
    }

}
