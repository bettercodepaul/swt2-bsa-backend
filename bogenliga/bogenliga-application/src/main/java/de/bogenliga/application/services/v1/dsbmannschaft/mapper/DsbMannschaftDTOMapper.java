package de.bogenliga.application.services.v1.dsbmannschaft.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.dsbmitglied.model.DsbMitgliedDTO;

/**

 * I map the {@link DsbMannschaftDO} and {@link DsbMannschaftDTO} objects
 *
 * @author Philip Dengler
 */
public class DsbMannschaftDTOMapper implements DataTransferObjectMapper {

    public static final Function<DsbMannschaftDO, DsbMannschaftDTO> toDTO = dsbMannschaftdDO -> {


        final Long dsbMannschaftId = dsbMannschaftDo.getId();
        final Long dsbMannschaftVereinId = dsbMannschaftDo.getVereinId();
        final Long dsbMannschaftNummer = dsbMannschaftDo.getNummer();
        final Long dsbMannschaftBenutzerId = dsbMannschaftDo.getBenutzerId();
        final Long dsbMannschaftVeranstalungId = dsbMannschaftDO.getVeranstalungId();

        return new dsbMannschftDTO(dsbMannschaftId,
                dsbMannschaftVereinId,
                dsbMannschaftNummer,
                dsbMannschaftBenutzerId,
                dsbMannschaftVeranstalungId);



    };

    /**
     * I map the {@link DsbMannschaftDTO} object to the {@link DsbMannschaftDO} object
     */


    public static final Function<DsbMannschaftdDTO, DsbMannschaftDO> toDO = dto -> {



        final Long dsbMannschaftId = dto.getId();
        final Long dsbMannschaftVereinId = dto.getVereinId();
        final Long dsbMannschaftNummer = dto.getNummer();
        final Long dsbMannschaftBenutzerId = dto.getBenutzerId();
        final Long dsbMannschaftVeranstalungId = dto.getVeranstalungId();

        return new dsbMannschaftDO(dsbMannschaftId,
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