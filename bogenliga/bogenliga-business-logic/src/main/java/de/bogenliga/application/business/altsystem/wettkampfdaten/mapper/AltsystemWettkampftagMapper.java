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
 * TODO [AL] class documentation
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

    public List<WettkampfDO> getOrCreateWettkampftage(AltsystemWettkampfdatenDO altsystemWettkampfdatenDO, long currentUserId){
        List<WettkampfDO> wettkampfTage = new LinkedList<>();
        long mannschaftID = altsystemWettkampfdatenDO.getMannschaftId();
        // Aus Übersetzungstabelle Veranstaltung für Mannschaft auslesen
        long veranstaltungID = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung, mannschaftID).getBogenliga_id();

        wettkampfTage = wettkampfComponent.findAllByVeranstaltungId(veranstaltungID);

        if (wettkampfTage.isEmpty()){
            wettkampfTage = createWettkampftage(veranstaltungID, currentUserId);
        }
        return wettkampfTage;
    }

    public List<WettkampfDO> createWettkampftage(long veranstaltungId, long currentUserId){
        List<WettkampfDO> wettkampfTage = new LinkedList<>();
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(veranstaltungId);
        long sportjahr = veranstaltungDO.getVeranstaltungSportJahr();
        Date[] wettkampfTermine = getWettkampfTermine(sportjahr);
        LigaDO ligaDO = ligaComponent.findById(veranstaltungDO.getVeranstaltungLigaID());

        for(long i = 1; i <= 4; i++) {
            WettkampfDO wettkampfDO = new WettkampfDO();
            // Werte in DO setzen
            wettkampfDO.setWettkampfTag(i);
            wettkampfDO.setWettkampfDatum(wettkampfTermine[(int) i - 1]);
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

    public Date[] getWettkampfTermine(long sportjahr){
        final int anzahlTermine = 4;
        Date[] termine = new Date[anzahlTermine];
        long vorjahr = sportjahr - 1;
        String[] terminString = new String[anzahlTermine];
        terminString[0] = vorjahr + "-11-01";
        terminString[1] = vorjahr + "-12-01";
        terminString[2] = sportjahr + "-01-01";
        terminString[3] = sportjahr + "-02-01";

        for(int i = 0; i < anzahlTermine; i++){
            termine[i] = Date.valueOf(terminString[i]);
        }

        return termine;

    }

}
