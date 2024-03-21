package de.bogenliga.application.business.altsystem.ergebnisse.entity;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.ergebnisse.mapper.AltsystemPasseMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;


/**
 * Komponente um Datensätze der Entität Ergebnis aus dem Altsystem einzulesen
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemErgebnisse implements AltsystemEntity<AltsystemErgebnisseDO> {

    private final AltsystemPasseMapper altsystemPasseMapper;

    // Components
    private final PasseComponent passeComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    @Autowired
    public AltsystemErgebnisse(final AltsystemPasseMapper altsystemPasseMapper, final PasseComponent passeComponent,
                               AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemPasseMapper = altsystemPasseMapper;
        this.passeComponent = passeComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     Create Passe-Objects with given data of the legacy system.*
     @param altsystemErgebnisseDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
    @Override
    public void create(AltsystemErgebnisseDO altsystemErgebnisseDO, long currentUserId){

        // Passen aus dem Ergebnis erstellen
        List<PasseDO> passen = altsystemPasseMapper.toDO(new ArrayList<>(), altsystemErgebnisseDO);

        //wenn wir nichts zurückbekommen, soll nichts angelegt werden....
        if (passen.isEmpty() ){
            return;
        }

        StringBuilder bld = new StringBuilder();
        for(PasseDO passe : passen){
            // Passe ins neue System schreiben
            passe = passeComponent.create(passe, currentUserId);
            // Ids der Passen merken für die Übersetzungstabelle
            bld.append(passe.getId());
            bld.append(";");
        }
        // Ids der Passen in die Übersetzungstabelle schreiben
        String passeIdString = bld.toString();
        Long altsystemSchutzeId = altsystemErgebnisseDO.getSchuetze_Id();
        Long altsystemMatchId = Long.valueOf(altsystemErgebnisseDO.getMatch());

        // die Tabelle ergebniss hat keine ID - der Primary Key besteht aus SchutzenID + Match
        StringBuilder bldAltsystemId = new StringBuilder();
        bldAltsystemId.append(altsystemSchutzeId.toString());
        bldAltsystemId.append(altsystemMatchId.toString());

        Long ergebnissIdUebersetzung = Long.valueOf(bldAltsystemId.toString());

        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Ergebnis_Passen,ergebnissIdUebersetzung , 0L, passeIdString);

    }

    /**
     Update existing Passe-Objects with given data of the legacy system.*
     @param altsystemErgebnisseDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
    @Override
    public void update(AltsystemErgebnisseDO altsystemErgebnisseDO, long currentUserId){

        Long altsystemSchutzeId = altsystemErgebnisseDO.getSchuetze_Id();
        Long altsystemMatchId = Long.valueOf(altsystemErgebnisseDO.getMatch());
        // die Tabelle ergebniss hat keine ID - der Primary Key besteht aus SchutzenID + Match
        StringBuilder bldAltsystemId = new StringBuilder();
        bldAltsystemId.append(altsystemSchutzeId.toString());
        bldAltsystemId.append(altsystemMatchId.toString());
        Long ergebnissId_uebersetzung = Long.valueOf(bldAltsystemId.toString());

        // Ids der zugehörigen Passen extrahieren
        AltsystemUebersetzungDO uebersetzungDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Ergebnis_Passen, ergebnissId_uebersetzung);
        String passeIdString = uebersetzungDO.getWert().trim();
        String[] passeIds = passeIdString.split(";");

        // Bestehende Data Objects finden
        List<PasseDO> passen = new LinkedList<>();
        for(int i = 0; i < passeIds.length; i++){
            passen.add(passeComponent.findById(Long.parseLong(passeIds[i])));
        }

        // Änderungen in die Data Objects übernehmen
        passen = altsystemPasseMapper.recalculatePassen(passen, altsystemErgebnisseDO);

        // Neues Data Object in die Tabelle schreiben
        for(PasseDO passe : passen){
            passeComponent.update(passe, currentUserId);
        }
    }
}