package de.bogenliga.application.services.v1.mannschaft.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.mannschaft.api.types.MannschaftDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.mannschaft.model.MannschaftDTO;

/**

 * I map the {@link MannschaftDO} and {@link MannschaftDTO} objects
 *
 * @author Philip Dengler
 */
public class MannschaftDTOMapper implements DataTransferObjectMapper {

    public static final Function<MannschaftDO, MannschaftDTO> toDTO = dsbMannschaftDO -> {


        final Long dsbMannschaftId = dsbMannschaftDO.getId();
        final Long dsbMannschaftVereinId = dsbMannschaftDO.getVereinId();
        final Long dsbMannschaftNummer = dsbMannschaftDO.getNummer();
        final Long dsbMannschaftBenutzerId = dsbMannschaftDO.getBenutzerId();
        final Long dsbMannschaftVeranstalungId = dsbMannschaftDO.getVeranstaltungId();
        final Long dsbMannschaftSortierung = dsbMannschaftDO.getSortierung();
        final String dsbMannschaftName = dsbMannschaftDO.getName();

        return new MannschaftDTO(dsbMannschaftId,
                dsbMannschaftName,
                dsbMannschaftVereinId,
                dsbMannschaftNummer,
                dsbMannschaftBenutzerId,
                dsbMannschaftVeranstalungId,
                dsbMannschaftSortierung);



    };

    /**
     * I map the {@link MannschaftDTO} object to the {@link MannschaftDO} object
     */


    public static final Function<MannschaftDTO, MannschaftDO> toDO = dto -> {


        final Long dsbMannschaftId = dto.getId();
        final Long dsbMannschaftVereinId = dto.getVereinId();
        final Long dsbMannschaftNummer = dto.getNummer();
        final Long dsbMannschaftBenutzerId = dto.getBenutzerId();
        final Long dsbMannschaftVeranstalungId = dto.getVeranstaltungId();
        final Long dsbMannschaftSoriterung = dto.getSortierung();
        final String dsbMannschaftName = dto.getName();

        return new MannschaftDO(dsbMannschaftId,
                dsbMannschaftName,
                dsbMannschaftVereinId,
                dsbMannschaftNummer,
                dsbMannschaftBenutzerId,
                dsbMannschaftVeranstalungId,
                dsbMannschaftSoriterung);

    };



    /**
     * Constructor
     */

    private MannschaftDTOMapper() {
        // empty private constructor
    }

}
