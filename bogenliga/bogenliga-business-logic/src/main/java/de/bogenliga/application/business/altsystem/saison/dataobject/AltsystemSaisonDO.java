package de.bogenliga.application.business.altsystem.saison.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;
/**
 * Bean to sotre data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemSaisonDO extends AltsystemDO {

    private String name = null;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}
