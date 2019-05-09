package de.bogenliga.application.business.match.impl.dao;

import org.junit.Before;
import org.junit.Test;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class QueryBuilderTest {

    private QueryBuilder queryBuilder;
    private static final String TABLE_NAME = "table";
    private static final String DEFAULT_FIELD = "field";


    @Before
    public void setUp() {
        this.queryBuilder = new QueryBuilder();
    }


    @Test
    public void testEmptyStrings() {
        this.queryBuilder.selectAll();
        assertThatThrownBy(() -> {
            this.queryBuilder.from("");
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.from(TABLE_NAME);
        assertThatThrownBy(() -> {
            this.queryBuilder.whereEquals("");
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.whereEquals(DEFAULT_FIELD);
        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
    }


    @Test
    public void testQueryDependencies() {
        assertThatThrownBy(() -> {
            this.queryBuilder.from(TABLE_NAME);
        }).isInstanceOf(BusinessException.class);

        this.queryBuilder.selectAll();

        assertThatThrownBy(() -> {
            this.queryBuilder.whereEquals(DEFAULT_FIELD);
        }).isInstanceOf(BusinessException.class);

        this.queryBuilder.from(TABLE_NAME);

        assertThatThrownBy(() -> {
            this.queryBuilder.andEquals(DEFAULT_FIELD);
        }).isInstanceOf(BusinessException.class);

        this.queryBuilder.whereEquals(DEFAULT_FIELD);

        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
    }

    @Test
    public void testDuplicateWhereClause() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD);
        assertThatThrownBy(() -> {
            this.queryBuilder.whereEquals(DEFAULT_FIELD);
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
    }


    @Test
    public void testAndWithoutWhere() {
        this.queryBuilder.selectAll().from(TABLE_NAME);
        assertThatThrownBy(() -> {
            this.queryBuilder.andEquals(DEFAULT_FIELD);
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        assertThat(builtQuery).contains(TABLE_NAME);
    }


    @Test
    public void testDuplicateFromClause() {
        this.queryBuilder.selectAll().from(TABLE_NAME);
        assertThatThrownBy(() -> {
            this.queryBuilder.from(TABLE_NAME);
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        assertThat(builtQuery).contains(TABLE_NAME);
    }


    @Test
    public void testToStringBeforeCompose() {
        this.queryBuilder.selectAll().from(TABLE_NAME);
        assertThatThrownBy(() -> {
            this.queryBuilder.toString();
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        assertThat(builtQuery).contains(TABLE_NAME);
    }


    @Test
    public void testComposeWithoutFrom() {
        this.queryBuilder.selectAll();
        assertThatThrownBy(() -> {
            this.queryBuilder.compose();
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        assertThat(builtQuery).contains(TABLE_NAME);
    }


    @Test
    public void selectAll() {
        this.queryBuilder.selectAll().from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void selectFields() {
        String[] fields = {"field_1", "field_2", "field_3"};
        this.queryBuilder.selectFields(fields).from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT);
        assertThat(builtQuery).contains(QueryBuilder.SQL_FROM);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
        for (String field : fields) {
            assertThat(builtQuery).contains(field);
        }
    }


    @Test
    public void from() {
        this.queryBuilder.selectAll().from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void whereEquals() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_EQUAL_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void whereGt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereGt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GT_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void whereGte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereGte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GTE_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void whereLt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereLt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LT_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void whereLte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereLte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LTE_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void andEquals() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andEquals(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_EQUAL_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void andGt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andGt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GT_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void andGte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andGte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GTE_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void andLt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andLt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LT_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void andLte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andLte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LTE_COMPARATOR);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void orderBy() {
        this.queryBuilder.selectAll().from(TABLE_NAME).orderBy(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_ORDER_BY);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void orderByAsc() {
        this.queryBuilder.selectAll().from(TABLE_NAME).orderByAsc(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_ORDER_BY.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_ORDER_ASC.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }


    @Test
    public void orderByDesc() {
        this.queryBuilder.selectAll().from(TABLE_NAME).orderByDesc(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_ORDER_BY.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_ORDER_DESC.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL);
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR);
    }
}