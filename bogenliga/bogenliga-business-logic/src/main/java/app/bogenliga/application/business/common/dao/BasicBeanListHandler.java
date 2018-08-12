package app.bogenliga.application.business.common.dao;

import java.sql.ResultSet;
import java.util.Map;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import app.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;

/**
 * I map the {@link ResultSet} to a {@link ConfigurationBE} list
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
class BasicBeanListHandler<T> extends BeanListHandler<T> {

    /**
     * ResultSet mapping with custom business entity parameter names
     */
    BasicBeanListHandler(final Class<T> businessEntityClass, final Map<String, String> columnToFieldMapping) {
        super(businessEntityClass, new BasicRowProcessor(new BeanProcessor(columnToFieldMapping)));
    }
}
