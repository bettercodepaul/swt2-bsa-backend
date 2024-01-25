package de.bogenliga.application.business.altsystem.liga.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * Bean to store the data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemLigaDO extends AltsystemDO {
    private String name;
    private int idNextLiga;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setIdNextLiga(int idNextLiga) { this.idNextLiga = idNextLiga; }

    public int getIdNextLiga(){ return this.idNextLiga; }
}