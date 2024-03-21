package de.bogenliga.application.business.altsystem.schuetze.entity;

import java.sql.SQLException;
import java.util.List;

import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.user.impl.business.UserRoleComponentImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSchuetze implements AltsystemEntity<AltsystemSchuetzeDO> {

    private final AltsystemSchuetzeMapper altsystemSchuetzeMapper;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final MannschaftsmitgliedComponent mannschaftsmitgliedComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;

    private static final Logger LOGGER = LoggerFactory.getLogger(AltsystemSchuetze.class);


    @Autowired
    public AltsystemSchuetze(final AltsystemSchuetzeMapper altsystemSchuetzeMapper, final DsbMitgliedComponent dsbMitgliedComponent,
                             MannschaftsmitgliedComponent mannschaftsmitgliedComponent, AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemSchuetzeMapper = altsystemSchuetzeMapper;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }


    /**
     * Erstellt einen neuen Datensatz für einen Schützen im neuen System basierend auf den Daten aus dem Altsystem.
     *
     * @param altsystemSchuetzeDO Das Objekt, das die Daten des Schützen im Altsystem enthält.
     * @param currentUserId Die ID des aktuellen Benutzers, der die Erstellung durchführt.
     * @throws SQLException Falls ein Fehler bei der Verarbeitung der Daten in der Datenbank auftritt.
     */
    @Override
    public void create(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId) throws SQLException {
        // Leeres Objekt für das neue System erstellen
        DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO();

        // Identifier für den zu vegleichenden Schützen generieren
        String parsedIdentifier = altsystemSchuetzeMapper.getIdentifier(altsystemSchuetzeDO);

        // Informationen zum Schützen im neuen System anhand der Altsystem-ID abrufen
        AltsystemUebersetzungDO schuetzeUebersetzung = null;
        try {
            schuetzeUebersetzung = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied, altsystemSchuetzeDO.getId());
        }catch(Exception e){
            LOGGER.debug(String.valueOf(e));
        }
        // für den Fall, dass wir zur altsystem_id keine Übersetzung finden, suchen wir noch nach der küsntlich erzeugten
        // die DSB-mitlgiedsID - auch die muss ja eindeutig sein...
        if (schuetzeUebersetzung == null) {
            try {
                schuetzeUebersetzung = altsystemUebersetzung.findByWert(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied, parsedIdentifier);
            } catch (Exception e) {
                LOGGER.debug(String.valueOf(e));
            }
        }
        Long dsbMitgliedId = null;
        // Wenn der Identifier noch nicht in der Übersetzungstabelle vorhanden ist
        if (schuetzeUebersetzung == null || schuetzeUebersetzung.getBogenligaId() == null) {
            // Daten des Schützen in das Objekt für das neue System übertragen
            dsbMitgliedDO = altsystemSchuetzeMapper.toDO(dsbMitgliedDO, altsystemSchuetzeDO);
            dsbMitgliedDO = altsystemSchuetzeMapper.addDefaultFields(dsbMitgliedDO, currentUserId);

            List<DsbMitgliedDO> dsbMitgliedexist = dsbMitgliedComponent.findBySearch(dsbMitgliedDO.getMitgliedsnummer());

            if (dsbMitgliedexist.isEmpty()) {
                // Daten des Schützen in die Datenbank im neuen System einfügen und die ID erhalten
                dsbMitgliedDO = dsbMitgliedComponent.create(dsbMitgliedDO, currentUserId);
            }
            else{
                dsbMitgliedDO = dsbMitgliedexist.get(0);
            }
            dsbMitgliedId = dsbMitgliedDO.getId();
            //Schuetze als Mannschaftsmitglied eintragen
            MannschaftsmitgliedDO mannschaftsmitgliedDO = altsystemSchuetzeMapper.buildMannschaftsMitglied( Long.valueOf(altsystemSchuetzeDO.getMannschaft_id()), Long.valueOf(altsystemSchuetzeDO.getRuecknr()), dsbMitgliedDO);
            mannschaftsmitgliedComponent.create(mannschaftsmitgliedDO, currentUserId);
            // wir erzeugen keinen Eintrag in der Übersetzungstabelle, da die Ergebnisse mit IDs von Mannschaft und Schutze abgelegt werden
            // wir benötigen die ID im weiteren Verlauf nicht
        }
        else {
            // Wenn der Identifier bereits vorhanden ist, die entsprechende ID aus der Übersetzungstabelle verwenden
            dsbMitgliedId = schuetzeUebersetzung.getBogenligaId();
        }

        // Übersetzung des Identifiers und Speichern in der Übersetzungstabelle
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied, altsystemSchuetzeDO.getId(),
                dsbMitgliedId, parsedIdentifier);

        // Informationen zur Mannschaft des Schützen aus dem Altsystem abrufen
        AltsystemUebersetzungDO altsystemUebersetzungDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft,
                Long.valueOf(altsystemSchuetzeDO.getMannschaft_id()));

        // Übersetzung der Mannschaftsdaten und Speichern in der Übersetzungstabelle
        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Schuetze_Mannschaft, altsystemSchuetzeDO.getId(),
                altsystemUebersetzungDO.getBogenligaId(), "");
    }

    /**
     * Aktualisiert die Informationen eines Schützen im neuen System basierend auf den Daten aus dem Altsystem.
     *
     * @param altsystemSchuetzeDO Das Objekt, das die Daten des Schützen im Altsystem enthält.
     * @param currentUserId Die ID des aktuellen Benutzers, der die Aktualisierung durchführt.
     */
    @Override
    public void update(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId) {
        // Das Objekt aus der Uebersetzungs-ID auslesen
        AltsystemUebersetzungDO altsystemUebersetzungDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Schuetze_DSBMitglied,
                altsystemSchuetzeDO.getId());

        // Das zu aktualisierende Objekt im neuen System finden
        DsbMitgliedDO dsbMitgliedDO = dsbMitgliedComponent.findById(altsystemUebersetzungDO.getBogenligaId());

        // Namen des Schützen parsen
        String namen [] = altsystemSchuetzeMapper.parseName(altsystemSchuetzeDO);

        // Vor- und Nachnamen sowie Vereins-ID des Schützen aktualisieren
        dsbMitgliedDO.setVorname(namen[1]);
        dsbMitgliedDO.setNachname(namen[0]);
        dsbMitgliedDO.setVereinsId(altsystemUebersetzungDO.getBogenligaId());

        // Aktualisierung des Schützen im neuen System durchführen
        dsbMitgliedComponent.update(dsbMitgliedDO, currentUserId);

        //Mannschaftsmitglied aktualisieren
        AltsystemUebersetzungDO uebersetzungMannschaftDO = altsystemUebersetzung.findByAltsystemID(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, Long.valueOf(altsystemSchuetzeDO.getMannschaft_id()));
        // bisherigen Datensatz lesen
        MannschaftsmitgliedDO mannschaftsmitgliedDO = mannschaftsmitgliedComponent.findByMemberAndTeamId(uebersetzungMannschaftDO.getBogenligaId(), dsbMitgliedDO.getId());
        //Daten mit neuen Inhalten überschreiben
        mannschaftsmitgliedDO.setDsbMitgliedVorname(namen[1]);
        mannschaftsmitgliedDO.setDsbMitgliedNachname(namen[0]);
        mannschaftsmitgliedDO.setRueckennummer(Long.valueOf(altsystemSchuetzeDO.getRuecknr()));

        // Aktualisierung des Eintrags als Mannschaftsmitglied
        mannschaftsmitgliedDO = mannschaftsmitgliedComponent.update(mannschaftsmitgliedDO, currentUserId);

    }
}