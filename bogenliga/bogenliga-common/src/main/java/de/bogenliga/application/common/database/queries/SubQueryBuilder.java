package de.bogenliga.application.common.database.queries;

/**
 * This is a simple variation of the default QueryBuilder.
 * It removes some composing/validation steps so the querystring can be used as subquery.
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class SubQueryBuilder extends QueryBuilder {

    @Override
    public QueryBuilder compose() {
        this.queryValidator.isComposable();

        this.formatQueryString();
        return this;
    }


    @Override
    protected void formatQueryString() {
        // replace double spaces with single
        this.queryString = this.queryString.replace("  ", " ");
        this.queryString = this.queryString.replace("  ", " ");
    }


    @Override
    public String toString() {
        return this.queryString;
    }
}
