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
        altsystemUebersetzungDAO.updateOrInsertUebersetzung(kategorie, altsystemID, bogenligaID, wert);
    }


    public AltsystemUebersetzungDO findByAltsystemID(AltsystemUebersetzungKategorie kategorie, Long altsystemID) {
        return altsystemUebersetzungDAO.findByAltsystemID(kategorie, altsystemID);
    }

    public AltsystemUebersetzungDO findByValue(AltsystemUebersetzungKategorie kategorie, String value) {
        return altsystemUebersetzungDAO.findByValue(kategorie, value);
    }
}