package de.bogenliga.application.business.altsystem.saison.entity;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.saison.dataobject.AltsystemSaisonDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
/**
 * Component to handle the import of a "Saison" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSaison implements AltsystemEntity<AltsystemSaisonDO>{

    @Override
    public void create(AltsystemSaisonDO altsystemSaisonDO, long currentUserId) {

        // altsystemSaison_name -> set
        String saison = altsystemSaisonDO.getName();
        String[] sportjahr = saison.split("-");
        altsystemSaisonDO.setName(sportjahr[1]);

        // Add to translation table
    }

    @Override
    public void update(AltsystemSaisonDO altsystemSaisonDO, long currentUserId) {
        // get primary key from translation table

        // find data in table with id

        // Map data to new object, without adding default fields


        // sportjahr & saison aus Tabelle holen
        // wenn sportjahr anderst -> wert verändern
    }

}
