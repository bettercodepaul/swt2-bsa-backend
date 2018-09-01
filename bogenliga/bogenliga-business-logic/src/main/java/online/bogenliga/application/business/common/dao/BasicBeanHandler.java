package online.bogenliga.application.business.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
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
