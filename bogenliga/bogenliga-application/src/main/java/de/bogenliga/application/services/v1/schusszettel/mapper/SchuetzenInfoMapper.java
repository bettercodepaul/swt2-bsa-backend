package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SchuetzeStammdatenDO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzeStammdatenDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper zur Umwandlung zwischen SchuetzenInfoDO (Business) und SchuetzenInfoDTO (REST).
 * @author Marty Lauterbach
 */
public class SchuetzenInfoMapper {

    public static SchuetzeStammdatenDTO toDTO(SchuetzeStammdatenDO doObj) {
        if (doObj == null) {
            return null;
        }

        return new SchuetzeStammdatenDTO(
                doObj.getSchuetzenId(),
                doObj.getRueckennummer(),
                doObj.getVorname(),
                doObj.getNachname()
        );
    }

    public static SchuetzeStammdatenDO toDO(SchuetzeStammdatenDTO dto) {
        if (dto == null) {
            return null;
        }

        return new SchuetzeStammdatenDO(
                dto.getSchuetzenId(),
                dto.getRueckennummer(),
                dto.getVorname(),
                dto.getNachname()
        );
    }

    public static List<SchuetzeStammdatenDTO> toDTOList(List<SchuetzeStammdatenDO> doList) {
        return doList == null ? null : doList.stream()
                .map(SchuetzenInfoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<SchuetzeStammdatenDO> toDOList(List<SchuetzeStammdatenDTO> dtoList) {
        return dtoList == null ? null : dtoList.stream()
                .map(SchuetzenInfoMapper::toDO)
                .collect(Collectors.toList());
    }
}
