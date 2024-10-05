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
        final Long dsbMannschaftSortierung = dsbMannschaftDO.getSortierung();
        final String dsbMannschaftName = dsbMannschaftDO.getName();

        return new DsbMannschaftDTO(dsbMannschaftId,
                dsbMannschaftName,
                dsbMannschaftVereinId,
                dsbMannschaftNummer,
                dsbMannschaftBenutzerId,
                dsbMannschaftVeranstalungId,
                dsbMannschaftSortierung);



    };
    public static final Function<DsbMannschaftDO, DsbMannschaftDTO> toVerUWettDTO = dsbMannschaftVerUWettDO -> {
        final Long id = dsbMannschaftVerUWettDO.getId();
        final String veranstaltungName = dsbMannschaftVerUWettDO.getVeranstaltung_name();
        final String wettkampfTag = dsbMannschaftVerUWettDO.getWettkampfTag();
        final String wettkampfOrtsname = dsbMannschaftVerUWettDO.getWettkampf_ortsname();
        final String vereinName = dsbMannschaftVerUWettDO.getVerein_name();
        final Long mannschaftNummer = dsbMannschaftVerUWettDO.getNummer();
        return new DsbMannschaftDTO(veranstaltungName, wettkampfTag, wettkampfOrtsname, vereinName,mannschaftNummer);
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
        final Long dsbMannschaftSoriterung = dto.getSortierung();
        final String dsbMannschaftName = dto.getName();

        return new DsbMannschaftDO(dsbMannschaftId,
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

    private DsbMannschaftDTOMapper() {
        // empty private constructor
    }

}
