package de.bogenliga.application.business.altsystem.schuetze.entity;

import java.sql.SQLException;
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
    private final AltsystemUebersetzung altsystemUebersetzung;


    @Autowired
    public AltsystemSchuetze(final AltsystemSchuetzeMapper altsystemSchuetzeMapper, final DsbMitgliedComponent dsbMitgliedComponent,
                             AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemSchuetzeMapper = altsystemSchuetzeMapper;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
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

        // Informationen zum Schützen im neuen System anhand des Identifiers abrufen
        AltsystemUebersetzungDO schuetzeUebersetzung = null;
        try {
            schuetzeUebersetzung = altsystemSchuetzeMapper.getSchuetzeByIdentifier(altsystemSchuetzeDO.getId());
        }catch(Exception e){
            e.printStackTrace();
        }
        Long dsbMitgliedId = null;
        // Wenn der Identifier noch nicht in der Übersetzungstabelle vorhanden ist
        if (schuetzeUebersetzung == null || schuetzeUebersetzung.getBogenligaId() == null) {
            // Daten des Schützen in das Objekt für das neue System übertragen
            dsbMitgliedDO = altsystemSchuetzeMapper.toDO(dsbMitgliedDO, altsystemSchuetzeDO);
            dsbMitgliedDO = altsystemSchuetzeMapper.addDefaultFields(dsbMitgliedDO, currentUserId);

            // Daten des Schützen in die Datenbank im neuen System einfügen und die ID erhalten
            dsbMitgliedDO = dsbMitgliedComponent.create(dsbMitgliedDO, currentUserId);
            dsbMitgliedId = dsbMitgliedDO.getId();
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
    }
}