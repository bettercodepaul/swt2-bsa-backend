package de.bogenliga.application.services.v1.wettkampf.mapper;

import de.bogenliga.application.business.wettkampf.api.types.WettkampfOverviewDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfOverviewDTO;

import java.time.OffsetDateTime;
import java.util.function.Function;


/**
 * I map the {@link WettkampfOverviewDO} and {@link WettkampfDTO} objects
 *
 * @author Marvin Holm, Daniel Schott
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class WettkampfOverviewDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link WettkampfOverviewDO} to the {@link WettkampfOverviewDTO} object
     */

    public static final Function<WettkampfOverviewDO, WettkampfOverviewDTO> toDTO = wettkampfOverviewDO -> {
        final Long wettkampfId = wettkampfOverviewDO.getId();
        final String datum = wettkampfOverviewDO.getDatum();
        final String wettkampfOrt = wettkampfOverviewDO.getWettkampfOrt();
        final String wettkampfBeginn = wettkampfOverviewDO.getWettkampfBeginn();
        final Long wettkampfTag = wettkampfOverviewDO.getWettkampfTag();
        final Long wettkampfDisziplinId = wettkampfOverviewDO.getDisziplinID();
        final String disziplinName = wettkampfOverviewDO.getDisziplinName();

        return new WettkampfOverviewDTO(wettkampfId, datum, wettkampfOrt, wettkampfBeginn, wettkampfTag,
                wettkampfDisziplinId, disziplinName);
    };

    /**
     * Constructor
     */
    private WettkampfOverviewDTOMapper(){
        //empty
    }
}
