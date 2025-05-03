package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.*;

import java.util.List;

/**
 * Mapper für den Tablet-Schusszettel: Baut vollständige DTO-Antwort aus DOs und BEs.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelDTOMapper {

    public static TabletSchusszettelDTO buildDTOFromData(
            TabletSchusszettelDTO.TabletSchusszettelStatus status,
            TeamInfoDTO eigenesTeam,
            TeamInfoDTO gegnerischesTeam,
            List<SchuetzenInfoDO> eingesetzteSchuetzen,
            List<SatzErgebnisDO> satzErgebnisse,
            List<SatzErgebnisDO> matchErgebnis,
            List<SchuetzenInfoDO> verfuegbareSchuetzen
    ) {
        TabletSchusszettelDTO dto = new TabletSchusszettelDTO();

        dto.setStatus(status);
        dto.setEigenesTeam(eigenesTeam);
        dto.setGegnerischesTeam(gegnerischesTeam);
        dto.setSchuetzen(TabletSchusszettelListenMapper.toSchuetzenInfoDTOList(eingesetzteSchuetzen));
        dto.setSatzErgebnisse(TabletSchusszettelListenMapper.toSatzErgebnisDTOList(satzErgebnisse));
        dto.setMatchErgebnis(TabletSchusszettelListenMapper.toSatzErgebnisDTOList(matchErgebnis));
        dto.setVerfuegbareSchuetzen(TabletSchusszettelListenMapper.toSchuetzenInfoDTOList(verfuegbareSchuetzen));

        return dto;
    }

    /**
     * Baut aus rohen Schützendaten eine Liste von SchuetzenInfoDTOs.
     */
    public static List<SchuetzenInfoDTO> fromRawSchuetzen(List<Object[]> rawData) {
        return rawData.stream()
                .map(row -> new SchuetzenInfoDTO(
                        (Long) row[0],     // schuetzenId
                        (Integer) row[1],  // rueckennummer
                        (String) row[2],   // vorname
                        (String) row[3]    // nachname
                ))
                .toList();
    }

    /**
     * TODO: Wenn du Sätze als rawData bekommst, hier konvertieren.
     */
    public static List<SatzErgebnisDTO> fromRawSatzdaten(List<Object[]> rawData) {
        // Beispiel: muss angepasst werden, falls SatzErgebnis nicht aus Schüssen kommt
        return rawData.stream()
                .map(row -> {
                    SatzErgebnisDTO dto = new SatzErgebnisDTO();
                    dto.setSatzNr(((Number) row[0]).intValue());
                    dto.setTeam1Punkte(((Number) row[1]).intValue());
                    dto.setTeam2Punkte(((Number) row[2]).intValue());
                    return dto;
                })
                .toList();
    }

    /*
     * TODOs, die du ergänzen oder vorbereiten musst:
     * - Eigenes Team und Gegner-Team laden (TeamInfoDTO) TeamInfoDTO eigenesTeam = new TeamInfoDTO(team.getId(), team.getName());
TeamInfoDTO gegnerTeam = new TeamInfoDTO(opponent.getId(), opponent.getName());

     * - Eingesetzte Schützen laden (z. B. über MitgliedZuordnungDAO)
     * - Satzdaten und Match-Ergebnis aufbereiten (ggf. aus PasseDAO oder MatchDAO)
     * - Verfügbare Schützen aus MannschaftsmitgliedDAO holen
     * - Optional: Statuslogik in ComponentImpl kapseln und an Mapper übergeben
     */
}
