package de.bogenliga.application.business.altsystem.uebersetzung;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Interface um auf die Übersetzungstabelle für die DB-Migration zuzugreifen
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemUebersetzung {
    private final AltsystemUebersetzungDAO altsystemUebersetzungDAO;

    @Autowired
    public AltsystemUebersetzung(AltsystemUebersetzungDAO altsystemUebersetzungDAO) {
        this.altsystemUebersetzungDAO = altsystemUebersetzungDAO;
    }


    public void updateOrInsertUebersetzung(AltsystemUebersetzungKategorie kategorie, long altsystemID, long bogenligaID, String wert) {
        /**
         Inserts a row into the translation table (or updates it, if a row with the the same category and legacyID already exists) *
         @param kategorie category of the translation
         @param altsystem_id id in the legacy system
         @param bogenliga_id id in the new system
         @param wert any kind of information that should be stored with this data
         */
        altsystemUebersetzungDAO.updateOrInsertUebersetzung(kategorie, altsystemID, bogenligaID, wert);
    }


    public AltsystemUebersetzungDO findByAltsystemID(AltsystemUebersetzungKategorie kategorie, Long altsystemID) {
        /**
         Selects a row from the translation table with given category and legacyID
         @param kategorie category of the translation
         @param altsystem_id id in the legacy system
         @return AltsystemUebersetzungDO result
         */
        return altsystemUebersetzungDAO.findByAltsystemID(kategorie, altsystemID);
    }

    public AltsystemUebersetzungDO findByWert(AltsystemUebersetzungKategorie kategorie, String wert) {
        /**
         Selects a row from the translation table with given category and value
         @param kategorie category of the translation
         @param wert value that is associated with
         @return AltsystemUebersetzungDO result
         */
        return altsystemUebersetzungDAO.findByWert(kategorie, wert);
    }
}