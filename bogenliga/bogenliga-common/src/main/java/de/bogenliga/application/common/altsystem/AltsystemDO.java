package de.bogenliga.application.common.altsystem;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import de.bogenliga.application.common.component.dao.BasicDAO;

/**
 * Parent class to store the imported data of entities of Bogenliga.de
 * Entity-specific fields have to be added in the concrete class
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public abstract class AltsystemDO {
    Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    private static final String ALTSYSTEM_TABLE_CREATED_AT = "created_at";
    private static final String ALTSYSTEM_TABLE_UPDATED_AT = "updated_at";
    private static final String ALTSYSTEM_DO_CREATED_AT = "createdAt";
    private static final String ALTSYSTEM_DO_UPDATED_AT = "updatedAt";

    public static final Map<String, String> technicalColumnsToFieldsMap = getColumsToFieldsMap();

    private static Map<String, String> getColumsToFieldsMap() {
        final Map<String, String> columnsToFieldMap = new HashMap<>();

        columnsToFieldMap.put(ALTSYSTEM_TABLE_CREATED_AT, ALTSYSTEM_DO_CREATED_AT);
        columnsToFieldMap.put(ALTSYSTEM_TABLE_UPDATED_AT, ALTSYSTEM_DO_UPDATED_AT);

        columnsToFieldMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldMap;

    }
}
