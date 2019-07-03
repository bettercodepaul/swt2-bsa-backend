package de.bogenliga.application.services.v1.tabletsession.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.tabletsession.api.types.TabletSessionDO;
import de.bogenliga.application.services.v1.tabletsession.model.TabletSessionDTO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TabletSessionDTOMapper {
    private TabletSessionDTOMapper() {
    }


    /**
     * Map {@link TabletSessionDO} to {@link TabletSessionDTO}
     */
    public static final Function<TabletSessionDO, TabletSessionDTO> toDTO = tabDO -> new TabletSessionDTO(
            tabDO.getWettkampfId(), tabDO.getScheibennummer(), tabDO.getSatznummer(),
            tabDO.getMatchId(), tabDO.isActive());

    /**
     * Map {@Link TabletSessionDTO} to {@Link TabletSessionDO}
     */
    public static final Function<TabletSessionDTO, TabletSessionDO> toDO = tabDTO -> new TabletSessionDO(
            tabDTO.getWettkampfId(), tabDTO.getScheibennummer(), tabDTO.getSatznummer(),
            tabDTO.getMatchId(), tabDTO.isActive());
}
