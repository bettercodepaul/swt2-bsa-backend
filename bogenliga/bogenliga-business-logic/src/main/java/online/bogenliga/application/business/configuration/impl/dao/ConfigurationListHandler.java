package online.bogenliga.application.business.configuration.impl.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import online.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;

/**
 * I map the {@link ResultSet} to a {@link ConfigurationBE} list
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
class ConfigurationListHandler extends BeanListHandler<ConfigurationBE> {

    /**
     * ResultSet mapping with custom business entity parameter names
     */
    ConfigurationListHandler() {
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
}
