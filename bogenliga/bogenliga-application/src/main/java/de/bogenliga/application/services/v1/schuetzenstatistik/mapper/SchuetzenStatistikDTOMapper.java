package de.bogenliga.application.services.v1.schuetzenstatistik.mapper;

import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.schuetzenstatistik.model.SchuetzenstatistikDTO;

import java.util.function.Function;

/**
 * I map the {@link SchuetzenstatistikDO} and {@link SchuetzenstatistikDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class SchuetzenstatistikDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link SchuetzenstatistikDO} object to the {@link SchuetzenstatistikDTO} object
     */
    public static final Function<SchuetzenstatistikDO, SchuetzenstatistikDTO> toDTO = schuetzenstatistikDO -> {

        final Long veranstaltungId = schuetzenstatistikDO.getveranstaltungId();
        final String veranstaltungName = schuetzenstatistikDO.getveranstaltungName();
        final Long wettkampfId = schuetzenstatistikDO.getwettkampfId();
        final int wettkampfTag = schuetzenstatistikDO.getwettkampfTag();
        final Long mannschaftId = schuetzenstatistikDO.getmannschaftId();
        final int mannschaftNummer = schuetzenstatistikDO.getmannschaftNummer();
        final Long vereinId = schuetzenstatistikDO.getvereinId();
        final String vereinName = schuetzenstatistikDO.getvereinName();



        return new SchuetzenstatistikDTO(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName);
    };
    /**
     * I map the {@link SchuetzenstatistikDTO} object to the {@link SchuetzenstatistikDO} object
     */
    public static final Function<SchuetzenstatistikDTO, SchuetzenstatistikDO> toDO = dto -> {

        SchuetzenstatistikDO schuetzenstatistikDO = new SchuetzenstatistikDO();

        schuetzenstatistikDO.setveranstaltungId(dto.getVeranstaltungId());
        schuetzenstatistikDO.setveranstaltungName(dto.getVeranstaltungName());
        schuetzenstatistikDO.setwettkampfId(dto.getWettkampfId());
        schuetzenstatistikDO.setwettkampfTag(dto.getWettkampfTag());
        schuetzenstatistikDO.setmannschaftId(dto.getMannschaftId());
        schuetzenstatistikDO.setmannschaftNummer(dto.getMannschaftNummer());
        schuetzenstatistikDO.setvereinId(dto.getVereinId());
        schuetzenstatistikDO.setvereinName(dto.getVereinName());


        return schuetzenstatistikDO;
    };


    /**
     * Constructor
     */
    private SchuetzenstatistikDTOMapper() {
        // empty private constructor
    }
}
