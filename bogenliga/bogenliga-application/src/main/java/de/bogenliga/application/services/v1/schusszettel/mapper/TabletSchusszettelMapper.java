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
        dto.setSchuetzenMatchPunkte(mapMatchPunkte(doObj.getSchuetzenMatchPunkte()));
        dto.setSchuetzeStammDaten(mapSchuetzeStammdaten(doObj.getSchuetzeStammDaten()));
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
        return new TeamInfoDTO(doObj.getTeamId(), doObj.getTeamName());
    }

    private static List<SatzErgebnisDTO> mapSatzErgebnisse(List<SatzErgebnisDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> new SatzErgebnisDTO(s.getSatzNr(), s.getTeam1Punkte(), s.getTeam2Punkte()))
                .collect(Collectors.toList());
    }

    private static List<SchuetzeMatchPunkteDTO> mapMatchPunkte(List<SchuetzeMatchPunkteDO> doList) {
        if (doList == null) return null;
        return doList.stream()
                .map(s -> new SchuetzeMatchPunkteDTO(s.getSchuetzenId(), s.getPunkteBisher()))
                .collect(Collectors.toList());
    }

    private static List<SchuetzeStammdatenDTO> mapSchuetzeStammdaten(List<SchuetzeStammdatenDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> new SchuetzeStammdatenDTO(s.getSchuetzenId(), s.getRueckennummer(), s.getVorname(), s.getNachname()))
                .collect(Collectors.toList());
    }

    private static List<TeamMatchInfoDTO> mapMatchErgebnisse(List<TeamMatchInfoDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> new TeamMatchInfoDTO(s.getTeamId(), s.getTeamName(), s.getMatchpunkte()))
                .collect(Collectors.toList());
    }

    private static List<VerfuegbarerSchuetzeDTO> mapVerfuegbareSchuetzen(List<VerfuegbarerSchuetzeDO> doList) {
        return doList == null ? null : doList.stream()
                .map(s -> new VerfuegbarerSchuetzeDTO(s.getSchuetzenId(), s.getName()))
                .collect(Collectors.toList());
    }

    // --------------------------------------------------------------------

    public static TabletSchusszettelDO fromDTO(TabletSchusszettelDTO dto) {
        if (dto == null) {
            return null;
        }

        TabletSchusszettelDO doObj = new TabletSchusszettelDO();

        doObj.setStatus(dto.getStatus() != null
                ? TabletSchusszettelDO.TabletSchusszettelStatus.valueOf(dto.getStatus().name())
                : TabletSchusszettelDO.TabletSchusszettelStatus.NOT_ALLOWED);

        doObj.setEigenesTeam(fromTeamInfo(dto.getEigenesTeam()));
        doObj.setGegnerischesTeam(fromTeamInfo(dto.getGegnerischesTeam()));
        doObj.setSatzErgebnisse(fromSatzErgebnisse(dto.getSatzErgebnisse()));
        doObj.setSchuetzenMatchPunkte(fromMatchPunkte(dto.getSchuetzenMatchPunkte()));
        doObj.setSchuetzeStammDaten(fromSchuetzeStammdaten(dto.getSchuetzeStammDaten()));
        doObj.setMatchErgebnis(fromMatchErgebnisse(dto.getMatchErgebnis()));
        doObj.setVerfuegbareSchuetzen(fromVerfuegbareSchuetzen(dto.getVerfuegbareSchuetzen()));

        return doObj;
    }


    private static TeamInfoDO fromTeamInfo(TeamInfoDTO dto) {
        return dto == null ? null : new TeamInfoDO(dto.getTeamId(), dto.getTeamName());
    }

    private static List<SatzErgebnisDO> fromSatzErgebnisse(List<SatzErgebnisDTO> dtoList) {
        return dtoList == null ? null : dtoList.stream()
                .map(dto -> new SatzErgebnisDO(dto.getSatzNr(), dto.getTeam1Punkte(), dto.getTeam2Punkte()))
                .collect(Collectors.toList());
    }

    private static List<SchuetzeMatchPunkteDO> fromMatchPunkte(List<SchuetzeMatchPunkteDTO> dtoList) {
        return dtoList == null ? null : dtoList.stream()
                .map(dto -> new SchuetzeMatchPunkteDO(dto.getSchuetzenId(), dto.getPunkteBisher()))
                .collect(Collectors.toList());
    }

    private static List<SchuetzeStammdatenDO> fromSchuetzeStammdaten(List<SchuetzeStammdatenDTO> dtoList) {
        return dtoList == null ? null : dtoList.stream()
                .map(dto -> new SchuetzeStammdatenDO(dto.getSchuetzenId(), dto.getRueckennummer(), dto.getVorname(), dto.getNachname()))
                .collect(Collectors.toList());
    }

    private static List<TeamMatchInfoDO> fromMatchErgebnisse(List<TeamMatchInfoDTO> dtoList) {
        return dtoList == null ? null : dtoList.stream()
                .map(dto -> new TeamMatchInfoDO(dto.getTeamId(), dto.getTeamName(), dto.getMatchpunkte()))
                .collect(Collectors.toList());
    }

    private static List<VerfuegbarerSchuetzeDO> fromVerfuegbareSchuetzen(List<VerfuegbarerSchuetzeDTO> dtoList) {
        return dtoList == null ? null : dtoList.stream()
                .map(dto -> new VerfuegbarerSchuetzeDO(dto.getSchuetzenId(), dto.getName()))
                .collect(Collectors.toList());
    }
}
