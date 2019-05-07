package de.bogenliga.application.services.v1.dsbmannschaft.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.dsbmannschaft.model.DsbMannschaftDTO;

/**

 * I map the {@link DsbMannschaftDO} and {@link DsbMannschaftDTO} objects
 *
 * @author Philip Dengler
 */
public class DsbMannschaftDTOMapper implements DataTransferObjectMapper {

    public static final Function<DsbMannschaftDO, DsbMannschaftDTO> toDTO = dsbMannschaftDO -> {


        final Long dsbMannschaftId = dsbMannschaftDO.getId();
        final Long dsbMannschaftVereinId = dsbMannschaftDO.getVereinId();
        final Long dsbMannschaftNummer = dsbMannschaftDO.getNummer();
        final Long dsbMannschaftBenutzerId = dsbMannschaftDO.getBenutzerId();
        final Long dsbMannschaftVeranstalungId = dsbMannschaftDO.getVeranstaltungId();

        return new DsbMannschaftDTO(dsbMannschaftId,
                dsbMannschaftVereinId,
                dsbMannschaftNummer,
                dsbMannschaftBenutzerId,
                dsbMannschaftVeranstalungId);



    };

    /**
     * I map the {@link DsbMannschaftDTO} object to the {@link DsbMannschaftDO} object
     */


    public static final Function<DsbMannschaftDTO, DsbMannschaftDO> toDO = dto -> {


        final Long dsbMannschaftId = dto.getId();
        final Long dsbMannschaftVereinId = dto.getVereinId();
        final Long dsbMannschaftNummer = dto.getNummer();
        final Long dsbMannschaftBenutzerId = dto.getBenutzerId();
        final Long dsbMannschaftVeranstalungId = dto.getVeranstaltungId();

        return new DsbMannschaftDO(dsbMannschaftId,
                dsbMannschaftVereinId,
                dsbMannschaftNummer,
                dsbMannschaftBenutzerId,
                dsbMannschaftVeranstalungId);

    };



    /**
     * Constructor
     */

    private DsbMannschaftDTOMapper() {
        // empty private constructor
    }

}