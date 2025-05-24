package de.bogenliga.application.services.v1.schusszettel.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSessionInfoDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TabletSessionSingDTO;

/**
 * Mapper to convert between TabletSessionInfoDO and TabletSessionInfoDTO
 */
public class TabletSessionInfoMapper {

    /** convenience: full mapping to DTO */
    public static TabletSessionInfoDTO toDTO(final TabletSessionInfoDO businessDO) {
        TabletSessionInfoDTO dto = new TabletSessionInfoDTO();
        dto.setWettkampfId(businessDO.getWettkampfId());
        dto.setTabletSessionSingDTOs(
                toDTOList(businessDO)
                        .toArray(new TabletSessionSingDTO[0])
        );
        return dto;
    }

    /** map the array from DO → List<DTO> */
    public static List<TabletSessionSingDTO> toDTOList(final TabletSessionInfoDO businessDO) {
        return Arrays.stream(businessDO.getTabletSessionSingDOs())
                .map(TabletSessionInfoMapper::toSingDTO)
                .collect(Collectors.toList());
    }

    private static TabletSessionSingDTO toSingDTO(final TabletSessionSingDO singDO) {
        return new TabletSessionSingDTO(
                singDO.getTeamId(),
                singDO.getTeamName(),
                singDO.getStatus(),
                singDO.getToken(),
                singDO.getCurrentPasse(),
                singDO.getNaechsterGegnerName()
        );
    }

    public static TabletSessionInfoDO toDO(final TabletSessionInfoDTO dto) {
        TabletSessionInfoDO do_ = new TabletSessionInfoDO();
        do_.setWettkampfId(dto.getWettkampfId());
        do_.setTabletSessionSingDOs(toDOs(dto));
        return do_;
    }

    private static TabletSessionSingDO toSingDO(final TabletSessionSingDTO singDTO) {
        return new TabletSessionSingDO(
                singDTO.getTeamId(),
                singDTO.getTeamName(),
                singDTO.getStatus(),
                singDTO.getToken(),
                singDTO.getCurrentPasse(),
                singDTO.getNaechsterGegner()
        );
    }

    /**
     * array-based helper for DTO
     */
    public static TabletSessionSingDTO[] toDTOs(final TabletSessionInfoDO businessDO) {
        return Arrays.stream(businessDO.getTabletSessionSingDOs())
                .map(TabletSessionInfoMapper::toSingDTO)
                .toArray(TabletSessionSingDTO[]::new);
    }

    /**
     * array-based helper for DO
     */
    public static TabletSessionSingDO[] toDOs(final TabletSessionInfoDTO dto) {
        return Arrays.stream(dto.getTabletSessionSingDTOs())
                .map(TabletSessionInfoMapper::toSingDO)
                .toArray(TabletSessionSingDO[]::new);
    }
}
