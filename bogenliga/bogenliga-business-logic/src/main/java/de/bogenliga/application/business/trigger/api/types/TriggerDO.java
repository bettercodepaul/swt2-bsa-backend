package de.bogenliga.application.business.trigger.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerDO extends CommonDataObject implements DataObject {
    private long id;
    private String timestamp;
    private String description;
    private String status;
    public TriggerDO(long id, Long version, String timestamp, String description, String status){
        this.id = id;
        this.version = version;
        this.timestamp = timestamp;
        this.description = description;
        this.status = status;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }
    public String getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }



}
