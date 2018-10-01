package de.bogenliga.application.common.component.entity;

import java.io.Serializable;

/**
 * Helper class for versioned database entity representations.
 *
 * Versioned business entities are used to detect concurrent modifications.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public abstract class VersionedBusinessEntity implements BusinessEntity {

    private int version;


    public int getVersion() {
        return version;
    }
}
