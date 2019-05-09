package de.bogenliga.application.common.database.queries;


import de.bogenliga.application.common.validation.Preconditions;

/**
 * Rough validation for the querystring and the sequence the query methods are called.
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class QueryValidator {

    private static final String SQL_ERROR_VALUE_EMPTY = "Given query value must not be empty!";
    private static final String SQL_ERROR_NO_SELECT = "QueryString has no SELECT clause!";
    private static final String SQL_ERROR_NOT_COMPOSED = "QueryString must be composed before usage!";
    private static final String SQL_ERROR_ALREADY_COMPOSED = "QueryString cannot be modified after composing!";
    private static final String SQL_ERROR_NO_FROM_SELECTION = "QueryString must have a table to select from!";
    private static final String SQL_ERROR_DUPLICATE_WHERE = "QueryString must not already contain a WHERE clause!";
    private static final String SQL_ERROR_AND_WITHOUT_WHERE = "QueryString must have a WHERE clause in order to have an AND clause!";
    private static final String SQL_ERROR_DUPLICATE_FROM = "QueryString must not already contain a FROM clause!";
    private static final String SQL_ERROR_MISSING_TABLE_ALIAS = "Missing table alias in querystring!";
    private static final String SQL_ERROR_NO_JOIN = "Missing JOIN clause for used ON!";

    private QueryBuilder queryBuilder;


    public QueryValidator(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }


    public void validateFrom(final String tableName) {
        ensureSelect();
        ensureNotComposed();
        ensureValidString(tableName);
    }


    public void validateJoin(final String tableName) {
        ensureSelect();
        ensureFrom();
        ensureNotComposed();
        ensureValidString(tableName);
    }


    public void validateOn(String fieldName, String otherFieldName) {
        ensureJoin();
        ensureValidString(fieldName);
        ensureValidString(otherFieldName);
    }


    public void validateWhere(String fieldName) {
        ensureSelect();
        ensureFrom();
        ensureNotComposed();
        ensureNoWhere();
        ensureValidString(fieldName);
    }


    public void validateAnd(String fieldName) {
        ensureSelect();
        ensureFrom();
        ensureNotComposed();
        ensureValidString(fieldName);
        ensureWhere();
    }


    public void validateOrdering(String fieldName) {
        ensureSelect();
        ensureFrom();
        ensureNotComposed();
        ensureValidString(fieldName);
    }


    public void isComposable() {
        ensureNotComposed();
        ensureSelect();
        ensureFrom();
        if (hasJoin()) {
            ensureOn();
        }
    }


    public void checkDuplicateFrom() {
        Preconditions.checkArgument(!hasFrom(), SQL_ERROR_DUPLICATE_FROM);
    }


    public void checkHasAlias(String alias) {
        Preconditions.checkArgument(hasAlias(alias), SQL_ERROR_MISSING_TABLE_ALIAS);
    }


    public void ensureSelect() {
        Preconditions.checkArgument(hasSelect(), SQL_ERROR_NO_SELECT);
    }


    public void ensureFrom() {
        Preconditions.checkArgument(hasFrom(), SQL_ERROR_NO_FROM_SELECTION);
    }


    public void ensureJoin() {
        Preconditions.checkArgument(hasJoin(), SQL_ERROR_NO_JOIN);
    }


    public void ensureWhere() {
        Preconditions.checkArgument(hasWhere(), SQL_ERROR_AND_WITHOUT_WHERE);
    }


    public void ensureNoWhere() {
        Preconditions.checkArgument(!hasWhere(), SQL_ERROR_DUPLICATE_WHERE);
    }


    public void ensureNotComposed() {
        Preconditions.checkArgument(!hasTerminator(), SQL_ERROR_ALREADY_COMPOSED);
    }


    public void ensureComposed() {
        Preconditions.checkArgument(hasTerminator(), SQL_ERROR_NOT_COMPOSED);
    }


    public void ensureOn() {
        Preconditions.checkArgument(hasOn(), SQL_ERROR_NOT_COMPOSED);
    }


    public void ensureValidString(String value) {
        Preconditions.checkArgument(value != null, SQL_ERROR_VALUE_EMPTY);
        Preconditions.checkArgument(value.length() > 0, SQL_ERROR_VALUE_EMPTY);
    }


    private boolean hasAlias(String alias) {
        return this.queryBuilder.getAliases().containsKey(alias);
    }


    private boolean hasSelect() {
        return getQueryString().contains(QueryBuilder.SQL_SELECT);
    }


    private boolean hasFrom() {
        return getQueryString().contains(QueryBuilder.SQL_FROM);
    }


    private boolean hasJoin() {
        return getQueryString().contains(QueryBuilder.SQL_JOIN);
    }


    private boolean hasOn() {
        return getQueryString().contains(QueryBuilder.SQL_ON);
    }


    private boolean hasWhere() {
        return getQueryString().contains(QueryBuilder.SQL_WHERE);
    }


    private boolean hasTerminator() {
        return getQueryString().contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    private String getQueryString() {
        return this.queryBuilder.getQueryString();
    }

}
