package de.bogenliga.application.services.v1.trigger.model;



import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerDTO implements DataTransferObject {

    private long id;
    private Long version;
    private String timestamp;
    private String description;
    private String status;




    public TriggerDTO(long id, long version, String timestamp, String description, String status){
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


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
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
