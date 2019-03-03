package de.bogenliga.application.services.v1.kampfrichter.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterDTO;

/**
 * I map the {@link KampfrichterDO} and {@link KampfrichterDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class KampfrichterDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link KampfrichterDO} object to the {@link KampfrichterDTO} object
     */
    public static final Function<KampfrichterDO, KampfrichterDTO> toDTO = kampfrichterDO -> {

        final Long kampfrichterUserId = kampfrichterDO.getUserId();

        final long kampfrichterWettkampfId = kampfrichterDO.getWettkampfId();
        final boolean kampfrichterLeitend = kampfrichterDO.isLeitend();

        return new KampfrichterDTO(kampfrichterWettkampfId,
                kampfrichterUserId,
                kampfrichterLeitend
                );
    };


    /**
     * I map the {@link KampfrichterDTO} object to the {@link KampfrichterDO} object
     */
    public static final Function<KampfrichterDTO, KampfrichterDO> toDO = dto -> {

        final Long kampfrichterWetterkampfId = dto.getWettkampfId();
        final boolean kampfrichterLeitend = dto.getLeitend();
        final Long kampfrichterUserId = dto.getUserId();

        return new KampfrichterDO(
                kampfrichterUserId);
    };


    /**
     * Constructor
     */
    private KampfrichterDTOMapper() {
        // empty private constructor
    }
}
