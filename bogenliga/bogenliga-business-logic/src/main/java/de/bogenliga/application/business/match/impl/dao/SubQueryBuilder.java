package de.bogenliga.application.business.match.impl.dao;

/**
 * This is a simple variation of the default QueryBuilder.
 * It's remove some composing/validation steps so the query can be used as subquery.
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class SubQueryBuilder extends QueryBuilder {

    public QueryBuilder compose() {
        this.queryValidator.isComposable();

        this.formatQueryString();
        return this;
    }


    protected void formatQueryString() {
        // replace double spaces with single
        this.queryString = this.queryString.replace("  ", " ");
        this.queryString = this.queryString.replace("  ", " ");
    }


    public String toString() {
        return this.queryString;
    }
}
