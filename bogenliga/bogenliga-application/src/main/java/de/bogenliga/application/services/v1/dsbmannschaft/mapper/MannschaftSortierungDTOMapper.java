package de.bogenliga.application.services.v1.dsbmannschaft.mapper;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.dsbmannschaft.model.MannschaftSortierungDTO;

import java.util.function.Function;

public class MannschaftSortierungDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link MannschaftSortierungDTO} object to the {@link DsbMannschaftDO} object
     */
    public static final Function<MannschaftSortierungDTO, DsbMannschaftDO> toDO = dto -> {


        final Long dsbMannschaftId = dto.getId();
        final Long dsbMannschaftSoriterung = dto.getSortierung();
        /*
         * empty attributes! They will be filled with the values from the Database in the
         * DsbMannschaftComponentImpl.updateSortierung().
         */
        final String dsbMannschaftsName = null;
        final Long dsbMannschaftVereinId = 0L;
        final Long dsbMannschaftNummer = 0L;
        final Long dsbMannschaftBenutzerId = 0L;
        final Long dsbMannschaftVeranstalungId = 0L;
        final Long dsbMannschaftSportjahr = 0L;

        return new DsbMannschaftDO(dsbMannschaftId,
                dsbMannschaftsName,
                dsbMannschaftVereinId,
                dsbMannschaftNummer,
                dsbMannschaftBenutzerId,
                dsbMannschaftVeranstalungId,
                dsbMannschaftSoriterung,
                dsbMannschaftSportjahr);

    };

    /**
     * I map the {@link DsbMannschaftDO} object to the {@link MannschaftSortierungDTO} object
     */
    public static final Function<DsbMannschaftDO, MannschaftSortierungDTO> toDTO = dsbMannschaftDO -> {

        final Long dsbMannschaftId = dsbMannschaftDO.getId();
        final Long dsbMannschaftSortierung = dsbMannschaftDO.getSortierung();

        return new MannschaftSortierungDTO(dsbMannschaftId, dsbMannschaftSortierung);
    };


    /**
     * Constructor
     */
    private MannschaftSortierungDTOMapper() {
        // empty private constructor
    }
}
