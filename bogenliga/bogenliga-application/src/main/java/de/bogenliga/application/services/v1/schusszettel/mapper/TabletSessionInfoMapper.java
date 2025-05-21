package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSessionInfoDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TabletSessionSingDTO;

/**
 * Mapper to convert between TabletSessionInfoDO and TabletSessionInfoDTO
 *
 * @author Marty Lauterbach
 */
public class TabletSessionInfoMapper {

    /**
     * Maps a TabletSessionInfoDO to a TabletSessionInfoDTO
     *
     * @param do_ the data object to convert
     * @return the converted DTO
     */
    public static TabletSessionInfoDTO toDTO(TabletSessionInfoDO do_) {
        TabletSessionInfoDTO dto = new TabletSessionInfoDTO();
        dto.setWettkampfId(do_.getWettkampfId());

        TabletSessionSingDO[] singDOs = do_.getTabletSessionSingDOs();
        TabletSessionSingDTO[] singDTOs = new TabletSessionSingDTO[singDOs.length];

        for (int i = 0; i < singDOs.length; i++) {
            singDTOs[i] = toSingDTO(singDOs[i]);
        }

        dto.setTabletSessionSingDTOs(singDTOs);
        return dto;
    }

    /**
     * Maps a TabletSessionSingDO to a TabletSessionSingDTO
     *
     * @param singDO the data object to convert
     * @return the converted DTO
     */
    private static TabletSessionSingDTO toSingDTO(TabletSessionSingDO singDO) {
        return new TabletSessionSingDTO(
                singDO.getTeamId(),
                singDO.getTeamName(),
                singDO.getStatus(),
                singDO.getToken(),
                singDO.getCurrentPasse(),
                singDO.getNaechsterGegnerName()
        );
    }

    /**
     * Maps a TabletSessionInfoDTO to a TabletSessionInfoDO
     *
     * @param dto the DTO to convert
     * @return the converted data object
     */
    public static TabletSessionInfoDO toDO(TabletSessionInfoDTO dto) {
        TabletSessionInfoDO do_ = new TabletSessionInfoDO();
        do_.setWettkampfId(dto.getWettkampfId());

        TabletSessionSingDTO[] singDTOs = dto.getTabletSessionSingDTOs();
        TabletSessionSingDO[] singDOs = new TabletSessionSingDO[singDTOs.length];

        for (int i = 0; i < singDTOs.length; i++) {
            singDOs[i] = toSingDO(singDTOs[i]);
        }

        do_.setTabletSessionSingDOs(singDOs);
        return do_;
    }

    /**
     * Maps a TabletSessionSingDTO to a TabletSessionSingDO
     *
     * @param singDTO the DTO to convert
     * @return the converted data object
     */
    private static TabletSessionSingDO toSingDO(TabletSessionSingDTO singDTO) {
        return new TabletSessionSingDO(
                singDTO.getTeamId(),
                singDTO.getTeamName(),
                singDTO.getStatus(),
                singDTO.getToken(),
                singDTO.getCurrentPasse(),
                singDTO.getNaechsterGegner()
        );
    }
}
