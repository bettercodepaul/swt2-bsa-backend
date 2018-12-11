package de.bogenliga.application.common.component.dao;

import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class VersionedTestBE extends CommonBusinessEntity {

    private static final long serialVersionUID = 5078982285222080484L;
    private long id;
    private String name;


    public long getId() {
        return id;
    }


    public void setId(final long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(final String name) {
        this.name = name;
    }
}
