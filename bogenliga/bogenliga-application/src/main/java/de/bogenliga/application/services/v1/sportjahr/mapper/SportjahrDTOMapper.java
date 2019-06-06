package de.bogenliga.application.services.v1.sportjahr.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.sportjahr.model.SportjahrDTO;


/**
 * I map the {@link SportjahrDO} and {@link SportjahrDTO} objects
 *
 * @author Marcel Schneider
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class SportjahrDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link SportjahrDO} to the {@link SportjahrDTO} object
     */

    public static final Function<SportjahrDO, SportjahrDTO> toDTO = sportjahrDO -> {
        final Long sportjahrId = sportjahrDO.getId();
        final String name = sportjahrDO.getName();

        final Long createdByUserId = sportjahrDO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = sportjahrDO.getCreatedAtUtc();
        final Long version = sportjahrDO.getVersion();


        return new SportjahrDTO(sportjahrId, name, createdByUserId, createdAtUtc, version);
    };


    /**
     * maps the {@link SportjahrDTO} to the {@link SportjahrDO} object
     */
    public static final Function<SportjahrDTO, SportjahrDO> toDO = sportjahrDTO -> {

        final Long sportjahrID = sportjahrDTO.getId();
        final String name = sportjahrDTO.getName();

        final Long createdByUserId = sportjahrDTO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = sportjahrDTO.getCreatedAtUtc();
        final Long version = sportjahrDTO.getVersion();

        return new SportjahrDO(sportjahrID, name, createdAtUtc, createdByUserId, version);
    };


    /**
     * Constructor
     */
    private SportjahrDTOMapper() {
        //empty
    }
}
