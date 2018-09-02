package online.bogenliga.application.common.component.dao;

import java.sql.ResultSet;
import java.util.Map;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * I map the {@link ResultSet} to a business entity list
 *
 * I handle the mapping between table column names and the bean parameter names.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see BeanListHandler
 * @see ResultSet
 */
public class BasicBeanListHandler<T> extends BeanListHandler<T> {

    /**
     * ResultSet mapping with custom business entity parameter names
     */
    BasicBeanListHandler(final Class<T> businessEntityClass, final Map<String, String> columnToFieldMapping) {
        super(businessEntityClass, new BasicRowProcessor(new BeanProcessor(columnToFieldMapping)));
    }
}
