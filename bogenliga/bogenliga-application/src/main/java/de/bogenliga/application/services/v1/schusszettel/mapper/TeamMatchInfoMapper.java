package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.TeamMatchInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.TeamMatchInfoDTO;

public class TeamMatchInfoMapper {

    public static TeamMatchInfoDTO toDTO(TeamMatchInfoDO doObj) {
        return new TeamMatchInfoDTO(doObj.getTeamId(), doObj.getTeamName(), doObj.getMatchpunkte());
    }

    public static TeamMatchInfoDO toDO(TeamMatchInfoDTO dto) {
        return new TeamMatchInfoDO(dto.getTeamId(), dto.getTeamName(), dto.getMatchpunkte());
    }
}
