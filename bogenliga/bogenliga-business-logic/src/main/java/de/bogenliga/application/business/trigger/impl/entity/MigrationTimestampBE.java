package de.bogenliga.application.business.trigger.impl.entity;

import java.sql.Timestamp;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Bennedikt Holst
 */
public class MigrationTimestampBE implements BusinessEntity {
    private Timestamp syncTimestamp;

    private Long id;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Timestamp getSyncTimestamp() {return syncTimestamp;}

    public void setSyncTimestamp(Timestamp syncTimestamp) {this.syncTimestamp = syncTimestamp;}

    public String toString() {
        return "SyncDataTriggerBE{" +
                "syncTimestamp" + syncTimestamp +
                '}';
    }


}