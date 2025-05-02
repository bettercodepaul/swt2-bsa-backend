package de.bogenliga.application.business.schusszettel.impl.business;

import java.util.Optional;

/**
 * DAO für die schusszettel_tablet_session Tabelle.
 * Verwaltet Authentifizierung und Spielstatus pro Tablet-Team.
 *
 * @author Marty Lauterbach, mklemmingen
 */
public class TabletSessionDAO {

    public Optional<TabletSessionEntity> findByToken(String token) {
        // TODO: SELECT * FROM schusszettel_tablet_session WHERE token = ?
        return Optional.empty();
    }

    public void updateStatus(Long id, String newStatus, Integer currentPasse, Long matchId, boolean finalized) {
        // TODO: UPDATE schusszettel_tablet_session SET ... WHERE id = ?
    }

    public void createSession(TabletSessionEntity entity) {
        // TODO: INSERT INTO schusszettel_tablet_session VALUES (...)
    }
}