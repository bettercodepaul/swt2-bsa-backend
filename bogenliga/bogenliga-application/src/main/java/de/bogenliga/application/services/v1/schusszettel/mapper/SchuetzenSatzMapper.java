package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SchuetzenSatzDO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenSatzDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper zur Konvertierung zwischen SchuetzenSatzDTO und SchuetzenSatzDO.
 * Wird bei der SatzEingabe verwendet.
 *
 * @author Marty Lauterbach
 */
public class SchuetzenSatzMapper {

    public static SchuetzenSatzDO toDO(SchuetzenSatzDTO dto) {
        return new SchuetzenSatzDO(
                dto.getSchuetzenId(),
                dto.getSchuss1(),
                dto.getSchuss2(),
                dto.getSchuss3()
        );
    }

    public static SchuetzenSatzDTO toDTO(SchuetzenSatzDO doObj) {
        SchuetzenSatzDTO dto = new SchuetzenSatzDTO();
        dto.setSchuetzenId(doObj.getSchuetzenId());
        dto.setSchuss1(doObj.getSchuss1());
        dto.setSchuss2(doObj.getSchuss2());
        dto.setSchuss3(doObj.getSchuss3());
        return dto;
    }

    public static List<SchuetzenSatzDO> toDOList(List<SchuetzenSatzDTO> dtoList) {
        return dtoList.stream().map(SchuetzenSatzMapper::toDO).collect(Collectors.toList());
    }

    public static List<SchuetzenSatzDTO> toDTOList(List<SchuetzenSatzDO> doList) {
        return doList.stream().map(SchuetzenSatzMapper::toDTO).collect(Collectors.toList());
    }
}
