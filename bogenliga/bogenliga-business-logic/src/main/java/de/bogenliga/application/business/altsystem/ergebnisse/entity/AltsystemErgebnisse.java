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

    @Override
    public void create(AltsystemErgebnisseDO altsystemDataObject, long currentUserId){
    /**
     Create Passe-Objects with given data of the legacy system.*
     @param AltsystemErgebnisseDO data of the legacy system
     @param currentUserId id of the user starting the synchronization
     */
        // Passen aus dem Ergebnis erstellen
        List<PasseDO> passen = altsystemPasseMapper.toDO(new ArrayList<>(), altsystemDataObject);

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
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Ergebnis_Passen, altsystemDataObject.getId(), 0L, passeIdString);

    }
    @Override
    public void update(AltsystemErgebnisseDO altsystemDataObject, long currentUserId){
        /**
         Update existing Passe-Objects with given data of the legacy system.*
         @param AltsystemErgebnisseDO data of the legacy system
         @param currentUserId id of the user starting the synchronization
         */
        // Ids der zugehörigen Passen extrahieren
        AltsystemUebersetzungDO uebersetzungDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Ergebnis_Passen, altsystemDataObject.getId());
        String passeIdString = uebersetzungDO.getWert().trim();
        String[] passeIds = passeIdString.split(";");

        // Bestehende Data Objects finden
        List<PasseDO> passen = new LinkedList<>();
        for(int i = 0; i < passeIds.length; i++){
            passen.add(passeComponent.findById(Long.parseLong(passeIds[i])));
        }

        // Änderugnen in die Data Objects übernehmen
        passen = altsystemPasseMapper.toDO(passen, altsystemDataObject);

        // Neues Data Object in die Tabelle schreiben
        for(PasseDO passe : passen){
            passeComponent.update(passe, currentUserId);
        }
    }
}