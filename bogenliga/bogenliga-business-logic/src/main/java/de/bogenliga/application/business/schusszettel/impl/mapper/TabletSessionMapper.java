package de.bogenliga.application.business.schusszettel.impl.mapper;

import de.bogenliga.application.services.v1.schusszettel.model.TabletSchusszettelDTO;

/**
 * Mapper zum Umwandeln von TabletSessionEntity in TabletSchusszettelDTO.
 */
public class TabletSessionMapper {

    public static TabletSchusszettelDTO.TabletSchusszettelStatus mapStatus(String dbStatus) {
        return switch (dbStatus) {
            case "SATZEINGABE" -> TabletSchusszettelDTO.TabletSchusszettelStatus.SATZEINGABE;
            case "SCHUETZENMELDUNG" -> TabletSchusszettelDTO.TabletSchusszettelStatus.SCHUETZENMELDUNG;
            case "WARTE" -> TabletSchusszettelDTO.TabletSchusszettelStatus.WARTE;
            case "WETTKAMPF_ENDE" -> TabletSchusszettelDTO.TabletSchusszettelStatus.WETTKAMPF_ENDE;
            default -> TabletSchusszettelDTO.TabletSchusszettelStatus.NOT_ALLOWED;
        };
    }
}