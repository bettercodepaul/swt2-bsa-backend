package de.bogenliga.application.business.altsystem.saison.entity;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.saison.dataobject.AltsystemSaisonDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Component to handle the import of a "Saison" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSaison implements AltsystemEntity<AltsystemSaisonDO>{

    private final AltsystemUebersetzung altsystemUebersetzung;


    public AltsystemSaison(AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemUebersetzung = altsystemUebersetzung;
    }


    @Override
    public void create(AltsystemSaisonDO altsystemSaisonDO, long currentUserId) {

        // altsystemSaison_name -> set
        String sportjahr = getSportjahr(altsystemSaisonDO.getName());
        altsystemSaisonDO.setName(sportjahr);

        // Add to translation table
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Saison_Sportjahr, altsystemSaisonDO.getId(), 0, sportjahr);
    }

    @Override
    public void update(AltsystemSaisonDO altsystemSaisonDO, long currentUserId) {
        // get primary key from translation table
        AltsystemUebersetzungDO saisonUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Saison_Sportjahr, altsystemSaisonDO.getId());

        // Check if the translation data has been found
        if(saisonUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemSaisonDO.getId()));
        }

        // extract sportjahr from name
        String sportjahr = getSportjahr(altsystemSaisonDO.getName());

        // Check if new sportjahr is different from already existing one
        if(!sportjahr.equalsIgnoreCase(saisonUebersetzung.getValue())) {
            altsystemSaisonDO.setName(sportjahr);
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Saison_Sportjahr, altsystemSaisonDO.getId(), 0, sportjahr);
        }
    }

    private String getSportjahr(String saisonName) {
        return saisonName.split("-")[1];
    }

}
