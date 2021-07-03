package de.bogenliga.application.services.v1.kampfrichter.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterDTO;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterExtendedDTO;

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

        return new KampfrichterDTO(kampfrichterUserId, kampfrichterWettkampfId, kampfrichterLeitend);
    };


    /**
     * I map the {@link KampfrichterDTO} object to the {@link KampfrichterDO} object
     */
    public static final Function<KampfrichterDTO, KampfrichterDO> toDO = dto -> {

        final Long kampfrichterUserId = dto.getUserID();
        final Long kampfrichterWettkampfId = dto.getWettkampfID();
        final boolean kampfrichterLeitend = dto.getLeitend();

        return new KampfrichterDO(
                kampfrichterUserId,
                kampfrichterWettkampfId,
                kampfrichterLeitend);
    };

    /**
     * I map the {@link KampfrichterDO} object to the {@link KampfrichterExtendedDTO} object
     */
    public static final Function<KampfrichterDO, KampfrichterExtendedDTO> toDTOExtended = kampfrichterDO -> {

        final Long kampfrichterUserId = kampfrichterDO.getUserId();
        final String kampfrichterVorname = kampfrichterDO.getVorname();
        final String kampfritcherNachname= kampfrichterDO.getNachname();
        final String kampfrichterEmail= kampfrichterDO.getKampfrichterEmail();
        long kampfrichterWettkampfId = 0;
        if(kampfrichterDO.getWettkampfId() != null){
            kampfrichterWettkampfId = kampfrichterDO.getWettkampfId();
        }
        final boolean kampfrichterLeitend = kampfrichterDO.isLeitend();

        return new KampfrichterExtendedDTO(kampfrichterUserId, kampfrichterVorname, kampfritcherNachname, kampfrichterEmail, kampfrichterWettkampfId, kampfrichterLeitend);
    };

    /**
     * I map the {@link KampfrichterExtendedDTO} object to the {@link KampfrichterDO} object
     */
    public static final Function<KampfrichterExtendedDTO, KampfrichterDO> toDOExtended = dto -> {

        final Long kampfrichterUserId = dto.getUserID();
        final String kampfrichterVorname = dto.getKampfrichterVorname();
        final String kampfritcherNachname= dto.getKampfrichterNachname();
        final String kampfrichterEmail= dto.getEmail();
        final Long kampfrichterWettkampfId = dto.getWettkampfID();
        final boolean kampfrichterLeitend = dto.getLeitend();

        return new KampfrichterDO(
                kampfrichterUserId,
                kampfrichterVorname,
                kampfritcherNachname,
                kampfrichterEmail,
                kampfrichterWettkampfId,
                kampfrichterLeitend);
    };


    /**
     * Constructor
     */
    private KampfrichterDTOMapper() {
        // empty private constructor
    }
}
