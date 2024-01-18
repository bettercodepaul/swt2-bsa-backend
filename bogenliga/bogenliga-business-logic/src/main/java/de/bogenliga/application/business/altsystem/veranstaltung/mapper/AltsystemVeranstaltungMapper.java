package de.bogenliga.application.business.altsystem.veranstaltung.mapper;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.veranstaltung.dataobject.AltsystemVeranstaltungDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import static de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungPhase.Phase.GEPLANT;

/**
 * Provides functionality to map the bean of Bogenliga.de to a
 * Business Entity (BE) of the new application
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemVeranstaltungMapper implements ValueObjectMapper {

    private final UserComponent userComponent;

    private final WettkampfTypComponent wettkampfTypComponent;

    @Autowired
    public AltsystemVeranstaltungMapper(final WettkampfTypComponent wettkampfTypComponent, final UserComponent userComponent) {
        this.userComponent = userComponent;
        this.wettkampfTypComponent = wettkampfTypComponent;
    }

    public VeranstaltungDO toDO(VeranstaltungDO veranstaltungDO, AltsystemVeranstaltungDO altsystemVeranstaltungDO) {
        /*

        // Name: Liga + Sportjahr (=saison_name)
        String veranstaltungName = LigaDO.getName() + Sportjahr
        veranstaltungDO.setVeranstaltungName(veranstaltungName);

        // Liga_id
        veranstaltungDO.setVeranstaltungLigaID(ligaDO.getId());

        // sportjahr
        int sportjahr = getSportjahr();
        veranstaltungDO.setVeranstaltungSportJahr();

        // meldeDeadline: 1.10. des vorherigen Jahres
        int vorherigesJahr = sportjahr - 1;
        String deadline = Integer.toString(vorherigesJahr) + "10" + "01";
        Date meldeDeadline = Date.valueOf(deadline);
        veranstaltungDO.setVeranstaltungMeldeDeadline(meldeDeadline);

        // groesse: 8 bei meisten, abhängig davon wie viele Mannschaften im Sportjahr bei Liga waren
        veranstaltungDO.setVeranstaltungGroesse(8);
*/
        return veranstaltungDO;
    }

    public VeranstaltungDO addDefaultFields(VeranstaltungDO veranstaltungDO) {


        // phase: "geplant" setzen
        veranstaltungDO.setVeranstaltungPhase("geplant");

        // Ligaleiter_id:
        UserDO adminDO = userComponent.findByEmail("admin@bogenliga.de");
        veranstaltungDO.setVeranstaltungLigaleiterID(adminDO.getId());

        // Wettkampftyp: Satzsystem
        WettkampfTypDO satzSystemDO = getSatzSystemDO();
        veranstaltungDO.setVeranstaltungWettkampftypID(satzSystemDO.getId());

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

