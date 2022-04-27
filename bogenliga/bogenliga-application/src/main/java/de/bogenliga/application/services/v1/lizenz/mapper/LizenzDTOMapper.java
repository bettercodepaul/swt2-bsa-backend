package de.bogenliga.application.services.v1.lizenz.mapper;

import java.util.function.Function;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.lizenz.model.LizenzDTO;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;

/**
 * I map the {@link LizenzDO} and {@link LizenzDTO} objects
 *
 * @author Manuel Baisch
 */
public class LizenzDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link LizenzDO} object to the {@link LizenzDTO} object
     */
    public static final Function<LizenzDO, LizenzDTO> toDTO = lizenzDO -> {

        final Long lizenzId = lizenzDO.getLizenzId();
        final String lizenznummer = lizenzDO.getLizenznummer();
        final Long lizenzRegionId = lizenzDO.getLizenzRegionId();
        final Long lizenzDsbMitgliedId = lizenzDO.getLizenzDsbMitgliedId();
        final String lizenztyp = lizenzDO.getLizenztyp();
        final Long lizenzDisziplinId = lizenzDO.getLizenzDisziplinId();

        return new LizenzDTO(lizenzId, lizenznummer, lizenzRegionId, lizenzDsbMitgliedId, lizenztyp,lizenzDisziplinId);

    };
    /**
     * I map the {@link LizenzDTO} object to the {@link LizenzDO} object
     */
    public static final Function<LizenzDTO, LizenzDO> toDO = lizenzDTO -> {

        final Long lizenzId = lizenzDTO.getLizenzId();
        final String lizenznummer = lizenzDTO.getLizenznummer();
        final Long lizenzRegionId = lizenzDTO.getLizenzRegionId();
        final Long lizenzDsbMitgliedId = lizenzDTO.getLizenzDsbMitgliedId();
        final String lizenztyp = lizenzDTO.getLizenztyp();
        final Long lizenzDisziplinId = lizenzDTO.getLizenzDisziplinId();

        return new LizenzDO(lizenzId, lizenznummer, lizenzRegionId, lizenzDsbMitgliedId, lizenztyp,lizenzDisziplinId);

    };


    /**
     * Constructor
     */
    private LizenzDTOMapper() {
        // empty private constructor
    }


}
