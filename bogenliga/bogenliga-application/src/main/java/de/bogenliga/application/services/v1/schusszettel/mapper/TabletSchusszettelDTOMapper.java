package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeMatchPunkteDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.*;

import de.bogenliga.application.services.v1.schusszettel.model.inside.SatzErgebnisDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.SchuetzeStammdatenDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TeamInfoDTO;
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
            List<SchuetzeMatchPunkteDO> schuetzenMatchPunkte,
            List<SchuetzeStammdatenDO> eingesetzteSchuetzen,
            List<SatzErgebnisDO> satzErgebnisse,
            List<TeamMatchInfoDO> matchErgebnis,
            List<SchuetzeStammdatenDO> verfuegbareSchuetzen
    ) {
        TabletSchusszettelDTO dto = new TabletSchusszettelDTO();

        dto.setStatus(status);
        dto.setEigenesTeam(eigenesTeam);
        dto.setGegnerischesTeam(gegnerischesTeam);
        dto.setSchuetzenMatchPunkte(TabletSchusszettelListenMapper.toSchuetzeMatchPunkteDTOList(schuetzenMatchPunkte));
        dto.setSchuetzeStammDaten(TabletSchusszettelListenMapper.toSchuetzeStammdatenDTOList(eingesetzteSchuetzen));
        dto.setSatzErgebnisse(TabletSchusszettelListenMapper.toSatzErgebnisDTOList(satzErgebnisse));
        dto.setMatchErgebnis(TabletSchusszettelListenMapper.toTeamMatchInfoDTOList(matchErgebnis));
        dto.setVerfuegbareSchuetzen(TabletSchusszettelListenMapper.toVerfuegbarerSchuetzeDTOList(verfuegbareSchuetzen));

        return dto;
    }

    /**
     * Baut aus rohen Schützendaten eine Liste von SchuetzeStammdatenDTOs.
     */
    public static List<SchuetzeStammdatenDTO> fromRawSchuetzen(List<Object[]> rawData) {
        return rawData.stream()
                .map(row -> new SchuetzeStammdatenDTO(
                        (Long) row[0],     // schuetzenId
                        (Integer) row[1],  // rueckennummer
                        (String) row[2],   // vorname
                        (String) row[3]    // nachname
                ))
                .toList();
    }

    /**
     * Konvertiert rohe Satzdaten (z.B. aus SQL JOIN) zu SatzErgebnisDTO.
     */
    public static List<SatzErgebnisDTO> fromRawSatzdaten(List<Object[]> rawData) {
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
     * TODOs:
     * - Eigenes Team und Gegner-Team laden (z.B. über TeamDAO)
     * - Eingesetzte Schützen laden (z. B. über MitgliedZuordnungDAO)
     * - Satzdaten und Match-Ergebnis aufbereiten (z. B. über PasseDAO oder MatchDAO)
     * - Verfügbare Schützen aus MannschaftsmitgliedDAO holen
     * - Statuslogik klar kapseln und an Mapper übergeben
     */
}
