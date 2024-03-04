package de.bogenliga.application.business.altsystem.saison.entity;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AltsystemSaison(AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     * Creates new entry in altsystemUebersetzung for sportjahr
     *
     * @param altsystemSaisonDO    altsystemSaison DO containing saison name
     * @param currentUserId        id of the currently logged in user that sent the create request
     *
     */
    @Override
    public void create(AltsystemSaisonDO altsystemSaisonDO, long currentUserId) {

        // extract sportjahr
        String sportjahr = getSportjahr(altsystemSaisonDO.getName());

        // Add to translation table
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Saison_Sportjahr, altsystemSaisonDO.getId(), 0L, sportjahr);
    }

    /**
     * Updates sportjahr in altsystemUebersetzung
     *
     * @param altsystemSaisonDO    altsystemSaison DO containing id and name
     * @param currentUserId        id of the currently logged in user that sent the update request
     *
     */
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
        if(!sportjahr.equalsIgnoreCase(saisonUebersetzung.getWert())) {
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Saison_Sportjahr, altsystemSaisonDO.getId(), 0L, sportjahr);
        }
    }

    /**
     * Extracts sportjahr from saisonName
     *
     * @param saisonName    name of saison from which to extract the sportjahr
     *
     * @return sportjahr as string
     */
    public String getSportjahr(String saisonName) {
        return saisonName.split("-")[1].trim();
    }

}
