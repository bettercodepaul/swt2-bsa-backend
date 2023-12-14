package de.bogenliga.application.business.altsystem.veranstaltung.mapper;

import de.bogenliga.application.business.altsystem.veranstaltung.dataobject.AltsystemVeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemVeranstaltungMapper implements ValueObjectMapper {

    public static VeranstaltungDO toDO(VeranstaltungDO veranstaltungDO, AltsystemVeranstaltungDO altsystemVeranstaltungDO) {

        // Name: Liga + Sportjahr (=saison_name)
        veranstaltungDO.setVeranstaltungName(altsystemVeranstaltungDO.getName());

        // Liga_id
        veranstaltungDO.setVeranstaltungLigaID();

        // sportjahr
        veranstaltungDO.setVeranstaltungSportJahr(altsystemSaisonDO.getName());

        // phase: aktuelles Jahr = aktiv; vergangenes Jahr: 'abgeschlossen(?)'
        veranstaltungDO.setVeranstaltungPhase();
        return veranstaltungDO;
    }

    public static VeranstaltungDO addDefaultFields(VeranstaltungDO veranstaltungDO) {

        // Wettkampftyp: Satzsystem
        veranstaltungDO.setVeranstaltungWettkampftypID(1L);
        // meldeDeadline: 1.10. des vorherigen Jahres
        veranstaltungDO.setVeranstaltungMeldeDeadline();
        // groesse: 8 bei meisten, abhängig davon wie viele Mannschaften im Sportjahr bei Liga waren
        veranstaltungDO.setVeranstaltungGroesse(8);


        veranstaltungDO.setVeranstaltungLigaleiterID(1L);

        return veranstaltungDO;
    }

    private AltsystemVeranstaltungMapper() {}
}
