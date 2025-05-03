package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SchuetzeInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzeInfoDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper zur Konvertierung zwischen SchuetzeInfoDTO und SchuetzeInfoDO.
 *
 * @author Marty Lauterbach
 */
public class SchuetzeInfoMapper {

    public static SchuetzeInfoDO toDO(SchuetzeInfoDTO dto) {
        return new SchuetzeInfoDO(dto.getSchuetzenId(), dto.getPunkteBisher());
    }

    public static SchuetzeInfoDTO toDTO(SchuetzeInfoDO doObj) {
        return new SchuetzeInfoDTO(doObj.getSchuetzenId(), doObj.getPunkteBisher());
    }

    public static List<SchuetzeInfoDTO> toDTOList(List<SchuetzeInfoDO> doList) {
        return doList.stream().map(SchuetzeInfoMapper::toDTO).collect(Collectors.toList());
    }

    public static List<SchuetzeInfoDO> toDOList(List<SchuetzeInfoDTO> dtoList) {
        return dtoList.stream().map(SchuetzeInfoMapper::toDO).collect(Collectors.toList());
    }
}
