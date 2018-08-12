package app.bogenliga.application.business.configuration.impl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import app.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;

/**
 * I map the {@link ResultSet} to a {@link ConfigurationBE} list
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
class ConfigurationHandler extends BeanHandler<ConfigurationBE> {

    /**
     * ResultSet mapping with custom business entity parameter names
     */
    ConfigurationHandler() {
        super(ConfigurationBE.class,
                new BasicRowProcessor(new BeanProcessor(getColumnsToFieldsMap())));
    }


    /**
     * Mapping between database column label and business entity class parameter name
     */
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();
        columnsToFieldsMap.put("configuration_key", "configurationKey");
        columnsToFieldsMap.put("configuration_value", "configurationValue");
        return columnsToFieldsMap;
    }


    @Override
    public ConfigurationBE handle(final ResultSet rs) throws SQLException {
        return super.handle(rs);
    }
}
