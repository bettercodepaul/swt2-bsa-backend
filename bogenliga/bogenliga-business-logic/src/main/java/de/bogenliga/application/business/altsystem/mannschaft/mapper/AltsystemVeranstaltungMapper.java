package de.bogenliga.application.business.altsystem.mannschaft.mapper;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.Uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.Uebersetzung.Kategorien;
import de.bogenliga.application.business.altsystem.Uebersetzung.Uebersetzung;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;

/**
 * Component to handle the import of a "Veranstaltung" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

@Component
public class AltsystemVeranstaltungMapper {

    private final VeranstaltungComponent veranstaltungComponent;
    private final AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;
    private final LigaComponent ligaComponent;
    private final WettkampfTypComponent wettkampfTypComponent;

    @Autowired
    public AltsystemVeranstaltungMapper(final AltsystemVeranstaltungMapper altsystemVeranstaltungMapper, final VeranstaltungComponent veranstaltungComponent,
                                        LigaComponent ligaComponent, WettkampfTypComponent wettkampfTypComponent) {
        this.altsystemVeranstaltungMapper = altsystemVeranstaltungMapper;
        this.veranstaltungComponent = veranstaltungComponent;
        this.ligaComponent = ligaComponent;
        this.wettkampfTypComponent = wettkampfTypComponent;
    }

    public VeranstaltungDO getOrCreateVeranstaltung(AltsystemMannschaftDO mannschaftDO, long currentUserId){
        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        long ligaId;
        long sportjahr;

        AltsystemUebersetzungDO uebersetzungLigaDO = Uebersetzung.findByAltsystemID(Kategorien.Liga_Liga, (long)mannschaftDO.getLiga_id());
        ligaId = uebersetzungLigaDO.getBogenliga_id();

        AltsystemUebersetzungDO uebersetzungSaisonDO = Uebersetzung.findByAltsystemID(Kategorien.Saison_Sportjahr, (long)mannschaftDO.getSaison_id());
        sportjahr = Long.parseLong(uebersetzungSaisonDO.getValue());

        try {
            veranstaltungDO = veranstaltungComponent.findByLigaIDAndSportjahr(ligaId, sportjahr);
        } catch (Exception e) {
            veranstaltungDO = createVeranstaltung(ligaId, sportjahr, currentUserId);
        }


        return veranstaltungDO;
    }


    public VeranstaltungDO createVeranstaltung(long ligaId, long sportjahr, long currentUserId){

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();

        // Name: Liga + Sportjahr (=saison_name)
        LigaDO ligaDO = ligaComponent.findById(ligaId);
        String veranstaltungName = ligaDO.getName() + " " + sportjahr;
        veranstaltungDO.setVeranstaltungName(veranstaltungName);

        // Liga_id
        veranstaltungDO.setVeranstaltungLigaID(ligaId);

        // sportjahr
        veranstaltungDO.setVeranstaltungSportJahr(sportjahr);

        // meldeDeadline: 1.10. des vorherigen Jahres
        long vorherigesJahr = sportjahr - 1;
        String deadline = Long.toString(vorherigesJahr) + "-10-01";
        Date meldeDeadline = Date.valueOf(deadline);
        veranstaltungDO.setVeranstaltungMeldeDeadline(meldeDeadline);

        // groesse: 8 bei meisten, abhängig davon wie viele Mannschaften im Sportjahr bei Liga waren
        veranstaltungDO.setVeranstaltungGroesse(8);



        // phase: "geplant" setzen
        veranstaltungDO.setVeranstaltungPhase("geplant");

        // Ligaleiter_id:
        veranstaltungDO.setVeranstaltungLigaleiterID(currentUserId);

        // Wettkampftyp: Satzsystem
        WettkampfTypDO satzSystemDO = getSatzSystemDO();
        veranstaltungDO.setVeranstaltungWettkampftypID(satzSystemDO.getId());

        veranstaltungDO = veranstaltungComponent.create(veranstaltungDO, currentUserId);

        return veranstaltungDO;
    }

    public WettkampfTypDO getSatzSystemDO() {
        WettkampfTypDO satzSystemDO = null;
        List<WettkampfTypDO> wettkampfTypDOS = wettkampfTypComponent.findAll();
        for (WettkampfTypDO wettkampfTypDO : wettkampfTypDOS) {
            String wettkampfTypName = wettkampfTypDO.getName().toLowerCase();
            if (wettkampfTypName.equals("liga satzsystem")) {
                satzSystemDO = wettkampfTypDO;
                break;
            }
        }
        return satzSystemDO;
    }


}

