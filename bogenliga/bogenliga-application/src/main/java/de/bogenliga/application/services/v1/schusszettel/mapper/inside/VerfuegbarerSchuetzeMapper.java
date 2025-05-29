package de.bogenliga.application.services.v1.schusszettel.mapper.inside;

import de.bogenliga.application.business.schusszettel.api.types.inside.VerfuegbarerSchuetzeDO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.VerfuegbarerSchuetzeDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper für verfügbare Schützen: DTO ↔ DO
 * Trennung zwischen REST (DTO) und Business-Logik (DO).
 *
 * @author Marty Lauterbach
 */
public class VerfuegbarerSchuetzeMapper {

    public static VerfuegbarerSchuetzeDTO toDTO(VerfuegbarerSchuetzeDO doObj) {
        return new VerfuegbarerSchuetzeDTO(doObj.getSchuetzenId(), doObj.getName());
    }

    public static VerfuegbarerSchuetzeDO toDO(VerfuegbarerSchuetzeDTO dto) {
        return new VerfuegbarerSchuetzeDO(dto.getSchuetzenId(), dto.getName());
    }

    public static List<VerfuegbarerSchuetzeDTO> toDTOList(List<VerfuegbarerSchuetzeDO> doList) {
        return doList.stream().map(VerfuegbarerSchuetzeMapper::toDTO).collect(Collectors.toList());
    }

    public static List<VerfuegbarerSchuetzeDO> toDOList(List<VerfuegbarerSchuetzeDTO> dtoList) {
        return dtoList.stream().map(VerfuegbarerSchuetzeMapper::toDO).collect(Collectors.toList());
    }
}
