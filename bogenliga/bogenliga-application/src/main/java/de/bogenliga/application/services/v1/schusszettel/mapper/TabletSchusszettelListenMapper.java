package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.inside.SatzErgebnisDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeMatchPunkteDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.SchuetzeStammdatenDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TeamMatchInfoDO;

import de.bogenliga.application.services.v1.schusszettel.model.inside.SatzErgebnisDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.SchuetzeMatchPunkteDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.SchuetzeStammdatenDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TeamMatchInfoDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.VerfuegbarerSchuetzeDTO;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hilfsklasse zur Konvertierung von Listen aus dem Business-Layer (DOs) in API-gerechte DTOs.
 * Wird vom TabletSchusszettelDTOMapper verwendet.
 *
 * @author Marty Lauterbach
 */
public class TabletSchusszettelListenMapper {

    public static List<SchuetzeStammdatenDTO> toSchuetzenInfoDTOList(List<SchuetzeStammdatenDO> doList) {
        if (doList == null) return null;

        return doList.stream()
                .map(doObj -> new SchuetzeStammdatenDTO(
                        doObj.getSchuetzenId(),
                        doObj.getRueckennummer(),
                        doObj.getVorname(),
                        doObj.getNachname()
                ))
                .collect(Collectors.toList());
    }

    public static List<SatzErgebnisDTO> toSatzErgebnisDTOList(List<SatzErgebnisDO> doList) {
        if (doList == null) return null;

        return doList.stream()
                .map(doObj -> new SatzErgebnisDTO(
                        doObj.getSatzNr(),
                        doObj.getTeam1Punkte(),
                        doObj.getTeam2Punkte()
                ))
                .collect(Collectors.toList());
    }

    public static List<TeamMatchInfoDTO> toTeamMatchInfoDTOList(List<TeamMatchInfoDO> doList) {
        if (doList == null) return null;

        return doList.stream()
                .map(doObj -> new TeamMatchInfoDTO(
                        doObj.getTeamId(),
                        doObj.getTeamName(),
                        doObj.getMatchpunkte()))
                .collect(Collectors.toList());
    }

    public static List<VerfuegbarerSchuetzeDTO> toVerfuegbarerSchuetzeDTOList(List<SchuetzeStammdatenDO> doList) {
        if (doList == null) return null;

        return doList.stream()
                .map(doObj -> new VerfuegbarerSchuetzeDTO(
                        doObj.getSchuetzenId(),
                        doObj.getVorname() + " " + doObj.getNachname())) // !NICHT GETRENNT, da in Visualisierung nicht benötigt!
                .collect(Collectors.toList());
    }

    public static List<SchuetzeMatchPunkteDTO> toSchuetzeMatchPunkteDTOList(List<SchuetzeMatchPunkteDO> doList) {
        if (doList == null) return null;

        return doList.stream()
                .map(doObj -> new SchuetzeMatchPunkteDTO(
                        doObj.getSchuetzenId(),
                        doObj.getPunkteBisher()
                ))
                .collect(Collectors.toList());
    }

    public static List<SchuetzeStammdatenDTO> toSchuetzeStammdatenDTOList(List<SchuetzeStammdatenDO> doList) {
        if (doList == null) return null;

        return doList.stream()
                .map(doObj -> new SchuetzeStammdatenDTO(
                        doObj.getSchuetzenId(),
                        doObj.getRueckennummer(),
                        doObj.getVorname(),
                        doObj.getNachname()
                ))
                .collect(Collectors.toList());
    }

    // Optional für die Zukunft, und für wer das hier jemals lesen sollte: Hier können weitere Mapper eingefügt werden,
    // um für die JSON weitere Felder hinzufügen
}