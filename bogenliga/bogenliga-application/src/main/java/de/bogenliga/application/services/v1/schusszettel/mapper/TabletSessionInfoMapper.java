package de.bogenliga.application.services.v1.schusszettel.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO;
import de.bogenliga.application.business.schusszettel.api.types.inside.WettkampfInfoDO;
import de.bogenliga.application.services.v1.schusszettel.model.TabletSessionInfoDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TabletSessionSingDTO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.WettkampfInfoDTO;

/**
 * Mapper to convert between TabletSessionInfoDO and TabletSessionInfoDTO
 * Updated to include wettkampf information.
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
                singDO.getNaechsterGegnerName(),
                mapWettkampfInfoToDTO(singDO.getWettkampfInfo())
        );
    }

    private static WettkampfInfoDTO mapWettkampfInfoToDTO(WettkampfInfoDO doObj) {
        if (doObj == null) return null;
        return new WettkampfInfoDTO(
                doObj.getWettkampfId(),
                doObj.getWettkampfTag(),
                doObj.getWettkampfDatum(),
                doObj.getWettkampfBeginn(),
                doObj.getWettkampfOrtsname(),
                doObj.getWettkampfOrtsinfo(),
                doObj.getWettkampfStrasse(),
                doObj.getWettkampfPlz(),
                doObj.getVeranstaltungId(),
                doObj.getVeranstaltungName(),
                doObj.getVeranstaltungSportjahr(),
                doObj.getLigaName(),
                doObj.getWettkampftypName()
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
                singDTO.getNaechsterGegner(),
                mapWettkampfInfoToDO(singDTO.getWettkampfInfo())
        );
    }

    private static WettkampfInfoDO mapWettkampfInfoToDO(WettkampfInfoDTO dto) {
        if (dto == null) return null;
        return new WettkampfInfoDO(
                dto.getWettkampfId(),
                dto.getWettkampfTag(),
                dto.getWettkampfDatum(),
                dto.getWettkampfBeginn(),
                dto.getWettkampfOrtsname(),
                dto.getWettkampfOrtsinfo(),
                dto.getWettkampfStrasse(),
                dto.getWettkampfPlz(),
                dto.getVeranstaltungId(),
                dto.getVeranstaltungName(),
                dto.getVeranstaltungSportjahr(),
                dto.getLigaName(),
                dto.getWettkampftypName()
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