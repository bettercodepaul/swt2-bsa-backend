package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SchuetzenInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenInfoDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper zur Umwandlung zwischen SchuetzenInfoDO (Business) und SchuetzenInfoDTO (REST).
 * @author Marty Lauterbach
 */
public class SchuetzenInfoMapper {

    public static SchuetzenInfoDTO toDTO(SchuetzenInfoDO doObj) {
        if (doObj == null) {
            return null;
        }

        return new SchuetzenInfoDTO(
                doObj.getSchuetzenId(),
                doObj.getRueckennummer(),
                doObj.getVorname(),
                doObj.getNachname()
        );
    }

    public static SchuetzenInfoDO toDO(SchuetzenInfoDTO dto) {
        if (dto == null) {
            return null;
        }

        return new SchuetzenInfoDO(
                dto.getSchuetzenId(),
                dto.getRueckennummer(),
                dto.getVorname(),
                dto.getNachname()
        );
    }

    public static List<SchuetzenInfoDTO> toDTOList(List<SchuetzenInfoDO> doList) {
        return doList == null ? null : doList.stream()
                .map(SchuetzenInfoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<SchuetzenInfoDO> toDOList(List<SchuetzenInfoDTO> dtoList) {
        return dtoList == null ? null : dtoList.stream()
                .map(SchuetzenInfoMapper::toDO)
                .collect(Collectors.toList());
    }
}
