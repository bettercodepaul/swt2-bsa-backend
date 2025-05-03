package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.*;
import de.bogenliga.application.services.v1.schusszettel.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper zur Konvertierung zwischen TabletSchusszettelDO und TabletSchusszettelDTO.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelMapper {

    public static TabletSchusszettelDTO toDTO(TabletSchusszettelDO doObj) {
        if (doObj == null) {
            return null;
        }

        TabletSchusszettelDTO dto = new TabletSchusszettelDTO();
        dto.setStatus(mapStatus(doObj.getStatus()));
        dto.setEigenesTeam(mapTeamInfo(doObj.getEigenesTeam()));
        dto.setGegnerischesTeam(mapTeamInfo(doObj.getGegnerischesTeam()));
        dto.setSatzErgebnisse(mapSatzErgebnisse(doObj.getSatzErgebnisse()));
        dto.setSchuetzen(mapSchuetzenInfo(doObj.getSchuetzen()));
        dto.setMatchErgebnis(mapMatchErgebnisse(doObj.getMatchErgebnis()));
        dto.setVerfuegbareSchuetzen(mapVerfuegbareSchuetzen(doObj.getVerfuegbareSchuetzen()));
        return dto;
    }

    private static TabletSchusszettelDTO.TabletSchusszettelStatus mapStatus(TabletSchusszettelDO.TabletSchusszettelStatus status) {
        return status != null
                ? TabletSchusszettelDTO.TabletSchusszettelStatus.valueOf(status.name())
                : TabletSchusszettelDTO.TabletSchusszettelStatus.NOT_ALLOWED;
    }

    private static TeamInfoDTO mapTeamInfo(TeamInfoDO doObj) {
        if (doObj == null) return null;
        TeamInfoDTO dto = new TeamInfoDTO();
        dto.setTeamId(doObj.getTeamId());
        dto.setTeamName(doObj.getTeamName());
        return dto;
    }

    private static List<SatzErgebnisDTO> mapSatzErgebnisse(List<SatzErgebnisDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> {
                    SatzErgebnisDTO dto = new SatzErgebnisDTO();
                    dto.setSatzNr(s.getSatzNr());
                    dto.setTeam1Punkte(s.getTeam1Punkte());
                    dto.setTeam2Punkte(s.getTeam2Punkte());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private static List<SchuetzenInfoDTO> mapSchuetzenInfo(List<SchuetzenInfoDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> {
                    SchuetzenInfoDTO dto = new SchuetzenInfoDTO();
                    dto.setSchuetzenId(s.getSchuetzenId());
                    dto.setRueckennummer(s.getRueckennummer());
                    dto.setVorname(s.getVorname());
                    dto.setNachname(s.getNachname());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private static List<TeamMatchInfoDTO> mapMatchErgebnisse(List<TeamMatchInfoDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> {
                    TeamMatchInfoDTO dto = new TeamMatchInfoDTO();
                    dto.setTeamId(s.getTeamId());
                    dto.setTeamName(s.getTeamName());
                    dto.setMatchpunkte(s.getMatchpunkte());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private static List<VerfuegbarerSchuetzeDTO> mapVerfuegbareSchuetzen(List<VerfuegbarerSchuetzeDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> {
                    VerfuegbarerSchuetzeDTO dto = new VerfuegbarerSchuetzeDTO();
                    dto.setSchuetzenId(s.getSchuetzenId());
                    dto.setName(s.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
