package de.bogenliga.application.common.altsystem;

/**
 * Parent class to store the imported data of entities of Bogenliga.de
 * Entity-specific fields have to be added in the concrete class
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public abstract class AltsystemDO {
    long id;

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return this.id;
    }
}
