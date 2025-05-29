package de.bogenliga.application.services.v1.schusszettel.mapper.inside;

import de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO;
import de.bogenliga.application.services.v1.schusszettel.model.inside.TabletSessionSingDTO;

/**
 * Mapper for the TabletSessionSingDTO to TabletSessionSingDO and vice versa.
 *
 *
 * @author Marty Lauterbach
 */
public class TabletSessionSingMapper {
    /**
     * Converts a TabletSessionSingDO to a TabletSessionSingDTO.
     *
     * @param tabletSessionSingDO the TabletSessionSingDO to convert
     * @return the converted TabletSessionSingDTO
     */
    public static TabletSessionSingDTO mapToTabletSessionSingDTO(TabletSessionSingDO tabletSessionSingDO) {
        return new TabletSessionSingDTO(tabletSessionSingDO.getTeamId(), tabletSessionSingDO.getTeamName(),
                tabletSessionSingDO.getStatus(), tabletSessionSingDO.getToken(), tabletSessionSingDO.getCurrentPasse(),
                tabletSessionSingDO.getNaechsterGegnerName());
    }

    /**
     * Converts a TabletSessionSingDTO to a TabletSessionSingDO.
     *
     * @param tabletSessionSingDTO the TabletSessionSingDTO to convert
     * @return the converted TabletSessionSingDO
     */
    public static TabletSessionSingDO mapToTabletSessionSingDO(TabletSessionSingDTO tabletSessionSingDTO) {
        return new TabletSessionSingDO(tabletSessionSingDTO.getTeamId(), tabletSessionSingDTO.getTeamName(),
                tabletSessionSingDTO.getStatus(), tabletSessionSingDTO.getToken(),
                tabletSessionSingDTO.getCurrentPasse(), tabletSessionSingDTO.getNaechsterGegner());
    }
}
