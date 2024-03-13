package de.bogenliga.application.business.altsystem.wettkampfdaten.mapper;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;

/**
 * Erstellt Wettkampftage für eine Veranstaltung, falls es noch keine gibt
 * Gibt es bereits Wettkampftage, werden diese zurückgegeben
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemWettkampftagMapper {

    private final WettkampfComponent wettkampfComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final LigaComponent ligaComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    @Autowired
    public AltsystemWettkampftagMapper(final WettkampfComponent wettkampfComponent,
                                       VeranstaltungComponent veranstaltungComponent, LigaComponent ligaComponent,
                                       AltsystemUebersetzung altsystemUebersetzung){
        this.wettkampfComponent = wettkampfComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.ligaComponent = ligaComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     Creates 4 Wettkampf Datasets for a Veranstaltung or immediately returns the datasets if they have already been previously created*
     @param altsystemWettkampfdatenDO legacy data to a match
     @param currentUserId user id of the user that should be set as Ausrichter of the Wettkampf
     @return List of WettkampfDO (size 4)
     */
    public List<WettkampfDO> getOrCreateWettkampftage(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, long currentUserId){
        List<WettkampfDO> wettkampfTage;
        long mannschaftID = altsystemWettkampfdatenDO.getMannschaft();
        // Aus Übersetzungstabelle Veranstaltung für Mannschaft auslesen
        long veranstaltungID = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung, mannschaftID).getBogenligaId();

        // Schauen ob es bereits Wettkämpfe für die Veranstaltung gibt
        wettkampfTage = wettkampfComponent.findAllByVeranstaltungId(veranstaltungID);

        // Wettkämpfe erstellen, falls noch keine vorhanden sind
        if (wettkampfTage.isEmpty()){
            wettkampfTage = createWettkampftage(veranstaltungID, currentUserId);
        }

        return wettkampfTage;
    }

    /**
     Creates 4 Wettkampf Datasets for a Veranstaltung (is called from method getOrCreateWettkampftage, if no WettkampfObjects have been previously found)*
     @param veranstaltungId id of the veranstaltung
     @param currentUserId user id of the user that should be set as Ausrichter of the Wettkampf
     @return List of WettkampfDO (size 4)
     */
    public List<WettkampfDO> createWettkampftage(long veranstaltungId, long currentUserId){
        List<WettkampfDO> wettkampfTage = new LinkedList<>();
        // VeranstaltungsDO auslesen
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(veranstaltungId);
        long sportjahr = veranstaltungDO.getVeranstaltungSportJahr();
        // Wettkampftermine bestimmen
        Date[] wettkampfTermine = getWettkampfTermine(sportjahr);
        LigaDO ligaDO = ligaComponent.findById(veranstaltungDO.getVeranstaltungLigaID());

        for(long i = 1; i <= 4; i++) {
            WettkampfDO wettkampfDO = new WettkampfDO();
            // Werte in DO setzen
            wettkampfDO.setWettkampfTag(i);
            wettkampfDO.setWettkampfDatum(wettkampfTermine[(int) i - 1]);
            wettkampfDO.setWettkampfBeginn("9:00");
            wettkampfDO.setWettkampfVeranstaltungsId(veranstaltungId);
            wettkampfDO.setWettkampfDisziplinId(ligaDO.getDisziplinId());
            wettkampfDO.setWettkampfTypId(veranstaltungDO.getVeranstaltungWettkampftypID());
            wettkampfDO.setWettkampfAusrichter(currentUserId);
            // Datensatz in DB Tabelle erstellen
            wettkampfDO = wettkampfComponent.create(wettkampfDO, currentUserId);
            // DO in Liste schreiben
            wettkampfTage.add(wettkampfDO);
        }
        return wettkampfTage;
    }

    /**
     Returns all dates for Competitions.
     Dates are 01-11, 01-12, 01-01 and 01-02*
     @param sportjahr year of the Veranstaltung
     @return Array of dates
     */
    public Date[] getWettkampfTermine(long sportjahr){
        final int anzahlTermine = 4;
        Date[] termine = new Date[anzahlTermine];
        long vorjahr = sportjahr - 1;
        String[] terminString = new String[anzahlTermine];
        // 1. Termin: 01.11
        terminString[0] = vorjahr + "-11-01";
        // 2. Termin: 01.12
        terminString[1] = vorjahr + "-12-01";
        // 3. Termin: 01.01
        terminString[2] = sportjahr + "-01-01";
        // letzter Termin: 01.02
        terminString[3] = sportjahr + "-02-01";

        for(int i = 0; i < anzahlTermine; i++){
            termine[i] = Date.valueOf(terminString[i]);
        }

        return termine;

    }

}
