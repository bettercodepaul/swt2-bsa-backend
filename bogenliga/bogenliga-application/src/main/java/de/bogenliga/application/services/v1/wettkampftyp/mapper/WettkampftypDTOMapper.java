package de.bogenliga.application.services.v1.wettkampftyp.mapper;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampftypDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.wettkampftyp.model.WettkampftypDTO;


/**
 * I map the {@link WettkampftypDO} and {@link WettkampftypDTO} objects
 *
 * @author Marvin Holm, Daniel Schott
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class WettkampftypDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link WettkampftypDO} to the {@link WettkampftypDTO} object
     */

    public static final Function<WettkampftypDO, WettkampftypDTO> toDTO = wettkampftypDO -> {
        final Long wettkampftypId = wettkampftypDO.getId();
        final String name = wettkampftypDO.getName();

        final Long createdByUserId = wettkampftypDO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = wettkampftypDO.getCreatedAtUtc();
        final Long version = wettkampftypDO.getVersion();


        return new WettkampftypDTO(wettkampftypId, name, createdByUserId, createdAtUtc, version);
    };


    /**
     * maps the {@link WettkampftypDTO} to the {@link WettkampftypDO} object
     */
    public static final Function<WettkampftypDTO, WettkampftypDO> toDO = wettkampftypDTO -> {

        final Long wettkampftypId = wettkampftypDTO.getId();
        final String name = wettkampftypDTO.getName();

        final Long createdByUserId = wettkampftypDTO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = wettkampftypDTO.getCreatedAtUtc();
        final Long version = wettkampftypDTO.getVersion();

        return new WettkampftypDO(wettkampftypId, name, createdAtUtc,createdByUserId, version);
    };


    /**
     * Constructor
     */
    private WettkampftypDTOMapper(){
        //empty
    }
}
