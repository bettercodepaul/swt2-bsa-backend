package online.bogenliga.application.common.component.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;

/**
 * I´m a generic {@link BeanHandler} class to map the {@link ResultSet} to a java bean.
 *
 * I handle the mapping between table column names and the bean parameter names.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see BeanHandler
 * @see ResultSet
 */
public class BasicBeanHandler<T> extends BeanHandler<T> {

    /**
     * ResultSet mapping with custom business entity parameter names
     */
    BasicBeanHandler(final Class<T> businessEntityClass, final Map<String, String> columnToFieldMapping) {
        super(businessEntityClass, new BasicRowProcessor(new BeanProcessor(columnToFieldMapping)));
    }


    @Override
    public T handle(final ResultSet rs) throws SQLException {
        return super.handle(rs);
    }

}
