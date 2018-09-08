package online.bogenliga.application.common.component.dao;

import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;

/**
 * I contain the configuration for the business entity mapping between database table and java object.
 *
 * The configuration is used by the {@link BasicDAO} to perform the "object-relational-mapping".
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BusinessEntityConfiguration<T> {
    private final Class<T> businessEntity;
    private final String table;
    private final Map<String, String> columnToFieldMapping;

    private final Logger logger;


    /**
     * Constructor with mandatory parameter
     *
     * @param businessEntity       to configure
     * @param table                to map a custom table name
     * @param columnToFieldMapping to map custom column names
     * @param logger               to log the generated SQL query
     */
    public BusinessEntityConfiguration(final Class<T> businessEntity, final String table,
                                       final Map<String, String> columnToFieldMapping, final Logger logger) {
        this.businessEntity = businessEntity;
        this.table = table;
        this.columnToFieldMapping = columnToFieldMapping;
        this.logger = logger;
    }


    Class<T> getBusinessEntity() {
        return businessEntity;
    }


    String getTable() {
        return table;
    }


    Map<String, String> getColumnToFieldMapping() {
        return Collections.unmodifiableMap(columnToFieldMapping);
    }


    Logger getLogger() {
        return logger;
    }
}
