package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SchuetzeMatchPunkteDO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzeMatchPunkteDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper zur Konvertierung zwischen SchuetzeInfoDTO und SchuetzeInfoDO.
 *
 * @author Marty Lauterbach
 */
public class SchuetzeMatchPunkteMapper {

    public static SchuetzeMatchPunkteDO toDO(SchuetzeMatchPunkteDTO dto) {
        return new SchuetzeMatchPunkteDO(dto.getSchuetzenId(), dto.getPunkteBisher());
    }

    public static SchuetzeMatchPunkteDTO toDTO(SchuetzeMatchPunkteDO doObj) {
        return new SchuetzeMatchPunkteDTO(doObj.getSchuetzenId(), doObj.getPunkteBisher());
    }

    public static List<SchuetzeMatchPunkteDTO> toDTOList(List<SchuetzeMatchPunkteDO> doList) {
        return doList.stream().map(SchuetzeMatchPunkteMapper::toDTO).collect(Collectors.toList());
    }

    public static List<SchuetzeMatchPunkteDO> toDOList(List<SchuetzeMatchPunkteDTO> dtoList) {
        return dtoList.stream().map(SchuetzeMatchPunkteMapper::toDO).collect(Collectors.toList());
    }
}
