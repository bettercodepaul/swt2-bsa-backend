package de.bogenliga.application.services.v1.schusszettel.mapper.inside;

import de.bogenliga.application.business.schusszettel.api.types.inside.TeamInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TeamInfoDTO;

/**
 * Mapper zur Konvertierung zwischen TeamInfoDTO (Service Layer)
 * und TeamInfoDO (Business Layer).
 *
 * @author Marty Lauterbach
 */
public class TeamInfoMapper {

    public static TeamInfoDO toDO(TeamInfoDTO dto) {
        return new TeamInfoDO(
                dto.getTeamId(),
                dto.getTeamName()
        );
    }

    public static TeamInfoDTO toDTO(TeamInfoDO doObj) {
        TeamInfoDTO dto = new TeamInfoDTO();
        dto.setTeamId(doObj.getTeamId());
        dto.setTeamName(doObj.getTeamName());
        return dto;
    }
}
