package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SatzErgebnisDO;
import de.bogenliga.application.services.v1.schusszettel.model.SatzErgebnisDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper zur Konvertierung zwischen SatzErgebnisDTO und SatzErgebnisDO.
 * @author Marty Lauterbach
 */
public class TabletSatzErgebnisMapper {

    public static SatzErgebnisDTO toDTO(SatzErgebnisDO doObj) {
        SatzErgebnisDTO dto = new SatzErgebnisDTO();
        dto.setSatzNr(doObj.getSatzNr());
        dto.setTeam1Punkte(doObj.getTeam1Punkte());
        dto.setTeam2Punkte(doObj.getTeam2Punkte());
        return dto;
    }

    public static SatzErgebnisDO toDO(SatzErgebnisDTO dto) {
        SatzErgebnisDO doObj = new SatzErgebnisDO();
        doObj.setSatzNr(dto.getSatzNr());
        doObj.setTeam1Punkte(dto.getTeam1Punkte());
        doObj.setTeam2Punkte(dto.getTeam2Punkte());
        return doObj;
    }

    public static List<SatzErgebnisDTO> toDTOList(List<SatzErgebnisDO> doList) {
        return doList.stream().map(TabletSatzErgebnisMapper::toDTO).collect(Collectors.toList());
    }

    public static List<SatzErgebnisDO> toDOList(List<SatzErgebnisDTO> dtoList) {
        return dtoList.stream().map(TabletSatzErgebnisMapper::toDO).collect(Collectors.toList());
    }
}
