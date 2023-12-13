package de.bogenliga.application.business.altsystem.saison.entity;

import de.bogenliga.application.business.altsystem.saison.dataobject.AltsystemSaisonDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemSaison implements AltsystemEntity<AltsystemSaisonDO>{

    @Override
    public void create(AltsystemSaisonDO altsystemSaisonDO) {

        // altsystemSaison_name -> set

        // Add data to table

        // Add to translation table
    }

    @Override
    public void update(AltsystemSaisonDO altsystemSaisonDO) {
        // get primary key from translation table

        // find data in table with id

        // Map data to new object, without adding default fields
    }

}
