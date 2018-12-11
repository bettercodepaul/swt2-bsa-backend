package de.bogenliga.application.services.v1.wettkampf.mapper;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;


/**
 * I map the {@link WettkampfDO} and {@link WettkampfDTO} objects
 *
 * @author Marvin Holm, Daniel Schott
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class WettkampfDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link WettkampfDO} to the {@link WettkampfDTO} object
     */

    public static final Function<WettkampfDO, WettkampfDTO> toDTO = wettkampfDO -> {
        final Long wettkampfId = wettkampfDO.getId();
        final Long veranstaltungsId = wettkampfDO.getVeranstaltungsId();
        final String datum = wettkampfDO.getDatum();
        final String wettkampfOrt = wettkampfDO.getWettkampfOrt();
        final String wettkampfBeginn = wettkampfDO.getWettkampfBeginn();
        final Long wettkampfTag = wettkampfDO.getWettkampfTag();
        final Long wettkampfDisziplinId = wettkampfDO.getWettkampfDisziplinId();
        final Long wettkampfTypId = wettkampfDO.getWettkampfTypId();
        final Long createdByUserId = wettkampfDO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = wettkampfDO.getCreatedAtUtc();
        final Long version = wettkampfDO.getVersion();


        return new WettkampfDTO(wettkampfId, veranstaltungsId, datum, wettkampfOrt, wettkampfBeginn, wettkampfTag,
                wettkampfDisziplinId, wettkampfTypId, createdByUserId, createdAtUtc, version);
    };


    /**
     * maps the {@link WettkampfDTO} to the {@link WettkampfDO} object
     */
    public static final Function<WettkampfDTO, WettkampfDO> toDO = wettkampfDTO -> {

        final Long wettkampfId = wettkampfDTO.getId();
        final Long veranstaltungsId = wettkampfDTO.getVeranstaltungsId();
        final String datum = wettkampfDTO.getDatum();
        final String wettkampfOrt = wettkampfDTO.getWettkampfOrt();
        final String wettkampfBeginn = wettkampfDTO.getWettkampfBeginn();
        final Long wettkampfTag = wettkampfDTO.getWettkampfTag();
        final Long wettkampfDisziplinId = wettkampfDTO.getWettkampfDisziplinId();
        final Long wettkampfTypId = wettkampfDTO.getWettkampfTypId();
        final Long createdByUserId = wettkampfDTO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = wettkampfDTO.getCreatedAtUtc();
        final Long version = wettkampfDTO.getVersion();

        return new WettkampfDO(wettkampfId, veranstaltungsId, datum, wettkampfOrt, wettkampfBeginn, wettkampfTag,
                wettkampfDisziplinId, wettkampfTypId, createdAtUtc,createdByUserId, version);
    };


    /**
     * Constructor
     */
    private WettkampfDTOMapper(){
        //empty
    }
}
