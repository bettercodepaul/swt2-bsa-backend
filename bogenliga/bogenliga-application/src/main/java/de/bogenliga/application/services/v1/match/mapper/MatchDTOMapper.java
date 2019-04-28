package de.bogenliga.application.services.v1.match.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;

/**
 * TODO [AL] class documentation
 *
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<MatchDO, MatchDTO> toMatchDTO = matchDO -> new MatchDTO(matchDO.getNr(),
            matchDO.getVersion(), matchDO.getWettkampfId(),
            matchDO.getMannschaftId(), matchDO.getScheibenNummer(), matchDO.getMatchpunkte(), matchDO.getSatzpunkte());

    /**
     * I map the {@link ConfigurationDTO} object to the {@link ConfigurationDO} object
     */
    public static final Function<MatchDTO, MatchDO> toDO = dto -> new MatchDO(
            dto.getNr(), dto.getWettkampfId(), dto.getMannschaftId(), dto.getScheibenNummer(), dto.getMatchpunkte(),
            dto.getSatzpunkte()
    );
}
