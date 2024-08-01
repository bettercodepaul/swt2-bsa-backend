package de.bogenliga.application.services.v1.match.mapper;

import java.util.ArrayList;
import java.util.function.Function;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import de.bogenliga.application.services.v1.match.model.MatchDTO;


/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchDTOMapper implements DataTransferObjectMapper {

    /**
     * Empty hidden constructor to prevent instantiation
     */
    private MatchDTOMapper () {}

    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<MatchDO, MatchDTO> toDTO = matchDO -> new MatchDTO(
            matchDO.getId(), matchDO.getNr(), matchDO.getVersion(), matchDO.getWettkampfId(),
            matchDO.getMannschaftId(), matchDO.getBegegnung(), matchDO.getMatchScheibennummer(),
            matchDO.getMatchpunkte(), matchDO.getSatzpunkte(),
            new ArrayList<>(), // init empty, filled when required
            matchDO.getStrafPunkteSatz1(), matchDO.getStrafPunkteSatz2(),
            matchDO.getStrafPunkteSatz3(), matchDO.getStrafPunkteSatz4(), matchDO.getStrafPunkteSatz5()
    );



    /**
     * I map the {@link ConfigurationDTO} object to the {@link ConfigurationDO} object
     */
    public static final Function<MatchDTO, MatchDO> toDO = matchDTO -> new MatchDO(
            matchDTO.getId(), matchDTO.getMatchNr(), matchDTO.getWettkampfId(), matchDTO.getMannschaftId(),
            matchDTO.getBegegnung(), matchDTO.getMatchScheibennummer(),
            matchDTO.getMatchpunkte(), matchDTO.getSatzpunkte(), matchDTO.getStrafPunkteSatz1(),
            matchDTO.getStrafPunkteSatz2(), matchDTO.getStrafPunkteSatz3(),
            matchDTO.getStrafPunkteSatz4(), matchDTO.getStrafPunkteSatz5()
    );
}
