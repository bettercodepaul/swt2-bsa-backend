package de.bogenliga.application.business.schusszettel.impl.dao;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import java.util.List;

/**
 * DAO für den Tablet-Schusszettel.
 * Verwaltet Zugriff auf die Datenbanktabelle mit Satz-Eingaben.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class TabletSchusszettelDAO {

    public List<TabletSchusszettelEntity> findByWettkampfUndTeam(Long wettkampfId, Long teamId) {
        // TODO: Implementiere Datenbankabfrage
        return List.of();
    }

    public void saveSatzEingabe(TabletSchusszettelEntity entity) {
        // TODO: INSERT in DB
    }

    public void deleteAllForWettkampfTeam(Long wettkampfId, Long teamId) {
        // TODO: Optional – zum Zurücksetzen
    }
}