package de.bogenliga.application.common.database.queries;

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
    private static final String TABLE_ALIAS = "t1";
    private static final String DEFAULT_FIELD = "field";

    private static final String OTHER_TABLE_NAME = "table";
    private static final String OTHER_TABLE_ALIAS = "t2";
    private static final String OTHER_DEFAULT_FIELD = "field";

    private static final String SQL_FUNCTION = "function";


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
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
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
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
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
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
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
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
        assertThat(builtQuery).contains(TABLE_NAME.trim());
    }


    @Test
    public void testDuplicateFromClause() {
        this.queryBuilder.selectAll().from(TABLE_NAME);
        assertThatThrownBy(() -> {
            this.queryBuilder.from(TABLE_NAME);
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
        assertThat(builtQuery).contains(TABLE_NAME.trim());
    }


    @Test
    public void testToStringBeforeCompose() {
        this.queryBuilder.selectAll().from(TABLE_NAME);
        assertThatThrownBy(() -> {
            this.queryBuilder.toString();
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
        assertThat(builtQuery).contains(TABLE_NAME.trim());
    }


    @Test
    public void testComposeWithoutFrom() {
        this.queryBuilder.selectAll();
        assertThatThrownBy(() -> {
            this.queryBuilder.compose();
        }).isInstanceOf(BusinessException.class);
        this.queryBuilder.from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
        assertThat(builtQuery).contains(TABLE_NAME.trim());
    }


    @Test
    public void selectAll() {
        this.queryBuilder.selectAll().from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void selectFields() {
        String[] fields = {"field_1", "field_2", "field_3"};
        this.queryBuilder.selectFields(fields).from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_FROM.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
        for (String field : fields) {
            assertThat(builtQuery).contains(field);
        }
    }


    @Test
    public void from() {
        this.queryBuilder.selectAll().from(TABLE_NAME).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereEquals() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME.trim());
        assertThat(builtQuery).contains(DEFAULT_FIELD.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_EQUAL_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereGt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereGt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GT_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereGte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereGte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GTE_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereLt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereLt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LT_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereLte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereLte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LTE_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andEquals() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andEquals(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_EQUAL_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andGt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andGt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GT_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andGte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andGte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_GTE_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andLt() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andLt(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LT_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andLte() {
        this.queryBuilder.selectAll().from(TABLE_NAME).whereEquals(DEFAULT_FIELD).andLte(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_LTE_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
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
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void join() {
        this.queryBuilder.selectAll().from(TABLE_NAME).join(OTHER_TABLE_NAME).on(DEFAULT_FIELD,
                OTHER_DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_JOIN.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_ON.trim());
    }


    @Test
    public void joinAlias() {
        this.queryBuilder.selectAll().from(TABLE_NAME, TABLE_ALIAS)
                .join(OTHER_TABLE_NAME, OTHER_TABLE_ALIAS)
                .on(TABLE_ALIAS, DEFAULT_FIELD, OTHER_TABLE_ALIAS, OTHER_DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(TABLE_ALIAS);
        assertThat(builtQuery).contains(OTHER_TABLE_NAME);
        assertThat(builtQuery).contains(OTHER_TABLE_ALIAS);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(OTHER_DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_JOIN.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_ON.trim());
    }


    @Test
    public void selectField() {
        this.queryBuilder.selectAll().from(TABLE_NAME).orderByDesc(DEFAULT_FIELD).compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_ORDER_BY.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_ORDER_DESC.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void applyFunction() {
        this.queryBuilder.selectAll().from(TABLE_NAME)
                .whereEquals(DEFAULT_FIELD, QueryBuilder.applyFunction(OTHER_DEFAULT_FIELD, SQL_FUNCTION))
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(SQL_FUNCTION);
        assertThat(builtQuery).contains(OTHER_DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereIn() {
        this.queryBuilder.selectAll().from(TABLE_NAME)
                .whereIn(DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereInSubQuery() {
        this.queryBuilder.selectAll().from(TABLE_NAME)
                .whereIn(
                        DEFAULT_FIELD,
                        new SubQueryBuilder()
                                .selectField(OTHER_DEFAULT_FIELD)
                                .from(OTHER_TABLE_NAME)
                                .whereEquals(OTHER_DEFAULT_FIELD)
                )
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andFrom() {
        this.queryBuilder.selectAll().from(TABLE_NAME).andFrom(OTHER_TABLE_NAME)
                .whereEquals(DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andFromAliases() {
        this.queryBuilder.selectAll().from(TABLE_NAME, TABLE_ALIAS).andFrom(OTHER_TABLE_NAME, OTHER_TABLE_ALIAS)
                .whereEquals(TABLE_ALIAS, DEFAULT_FIELD, OTHER_TABLE_ALIAS, OTHER_DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereEqualsWithSubQuery() {
        this.queryBuilder.selectAll()
                .from(TABLE_NAME)
                .whereEquals(
                        DEFAULT_FIELD,
                        new SubQueryBuilder()
                                .selectField(OTHER_DEFAULT_FIELD)
                                .from(OTHER_TABLE_NAME)
                                .whereEquals(OTHER_DEFAULT_FIELD)
                )
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(OTHER_TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereEqualsWithExplicitTable() {
        this.queryBuilder.selectAll()
                .from(TABLE_NAME)
                .whereEquals(TABLE_NAME, DEFAULT_FIELD, OTHER_TABLE_NAME, OTHER_DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(OTHER_TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void whereEqualsWithAliases() {
        this.queryBuilder.selectAll()
                .from(TABLE_NAME, TABLE_ALIAS)
                .andFrom(OTHER_TABLE_NAME, OTHER_TABLE_ALIAS)
                .whereEquals(TABLE_ALIAS, DEFAULT_FIELD, OTHER_TABLE_ALIAS, OTHER_DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(TABLE_ALIAS);
        assertThat(builtQuery).contains(OTHER_TABLE_ALIAS);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andEqualsWithAliases() {
        this.queryBuilder.selectAll()
                .from(TABLE_NAME, TABLE_ALIAS)
                .andFrom(OTHER_TABLE_NAME, OTHER_TABLE_ALIAS)
                .whereEquals(DEFAULT_FIELD)
                .andEquals(TABLE_ALIAS, DEFAULT_FIELD, OTHER_TABLE_ALIAS, OTHER_DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(TABLE_ALIAS);
        assertThat(builtQuery).contains(OTHER_TABLE_ALIAS);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andTrue() {
        this.queryBuilder.selectAll()
                .from(TABLE_NAME)
                .whereTrue(DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_TRUE_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }


    @Test
    public void andFalse() {
        this.queryBuilder.selectAll()
                .from(TABLE_NAME)
                .whereFalse(DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();
        assertThat(builtQuery).contains(TABLE_NAME);
        assertThat(builtQuery).contains(DEFAULT_FIELD);
        assertThat(builtQuery).contains(QueryBuilder.SQL_SELECT_ALL.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_FALSE_COMPARATOR.trim());
        assertThat(builtQuery).contains(QueryBuilder.SQL_QUERY_TERMINATOR.trim());
    }
    @Test
    public void havingGt() {
        this.queryBuilder.selectAll()
                .from(TABLE_NAME)
                .groupBy(DEFAULT_FIELD)
                .havingGt(DEFAULT_FIELD)
                .compose();
        String builtQuery = this.queryBuilder.toString();

        assertThat(builtQuery).contains(" HAVING field >?");
    }
}