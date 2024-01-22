package de.bogenliga.application.business.altsystem.mannschaft.mapper;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
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
    private final LigaComponent ligaComponent;
    private final WettkampfTypComponent wettkampfTypComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    @Autowired
    public AltsystemVeranstaltungMapper(final VeranstaltungComponent veranstaltungComponent,
                                        LigaComponent ligaComponent, WettkampfTypComponent wettkampfTypComponent,
                                        DsbMannschaftComponent dsbMannschaftComponent, AltsystemUebersetzung altsystemUebersetzung) {
        this.veranstaltungComponent = veranstaltungComponent;
        this.ligaComponent = ligaComponent;
        this.wettkampfTypComponent = wettkampfTypComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    public VeranstaltungDO getOrCreateVeranstaltung(AltsystemMannschaftDO mannschaftDO, long currentUserId){
        VeranstaltungDO veranstaltungDO;
        long ligaId;
        long sportjahr;

        AltsystemUebersetzungDO uebersetzungLigaDO = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Liga_Liga, (long)mannschaftDO.getLiga_id());
        ligaId = uebersetzungLigaDO.getBogenliga_id();

        AltsystemUebersetzungDO uebersetzungSaisonDO = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Saison_Sportjahr, (long)mannschaftDO.getSaison_id());
        sportjahr = Long.parseLong(uebersetzungSaisonDO.getValue());

        try {
            //Get veranstaltungDO if already exists
            veranstaltungDO = veranstaltungComponent.findByLigaIDAndSportjahr(ligaId, sportjahr);

            // Check how many dsbMannschaften exist in this Veranstaltung and set veranstaltungGroesse accordingly
            List<DsbMannschaftDO> dsbMannschaften = dsbMannschaftComponent.findAllByVeranstaltungsId(veranstaltungDO.getVeranstaltungID());
            if(dsbMannschaften.size() >= 4 && dsbMannschaften.size() < 6) {
                veranstaltungDO.setVeranstaltungGroesse(6);
            } else if(dsbMannschaften.size() >= 6) {
                veranstaltungDO.setVeranstaltungGroesse(8);
            }

        } catch (Exception e) {
            veranstaltungDO = createVeranstaltung(ligaId, sportjahr, currentUserId);
        }

        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung, mannschaftDO.getId(),
                veranstaltungDO.getVeranstaltungID(), "");

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
        String deadline = vorherigesJahr + "-10-01";
        Date meldeDeadline = Date.valueOf(deadline);
        veranstaltungDO.setVeranstaltungMeldeDeadline(meldeDeadline);

        // groesse: default als Mindestgroesse auf 4 gesetzt, später in getOrCreateVeranstaltung updated
        veranstaltungDO.setVeranstaltungGroesse(4);



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

