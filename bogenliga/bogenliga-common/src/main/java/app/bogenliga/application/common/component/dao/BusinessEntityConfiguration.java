package app.bogenliga.application.common.component.dao;

import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class BusinessEntityConfiguration<T> {
    private final Class<T> businessEntity;
    private final String table;
    private final Map<String, String> columnToFieldMapping;

    private final Logger logger;


    public BusinessEntityConfiguration(final Class<T> businessEntity, final String table,
                                       final Map<String, String> columnToFieldMapping, final Logger logger) {
        this.businessEntity = businessEntity;
        this.table = table;
        this.columnToFieldMapping = columnToFieldMapping;
        this.logger = logger;
    }


    public Class<T> getBusinessEntity() {
        return businessEntity;
    }


    public String getTable() {
        return table;
    }


    public Map<String, String> getColumnToFieldMapping() {
        return Collections.unmodifiableMap(columnToFieldMapping);
    }


    public Logger getLogger() {
        return logger;
    }
}
