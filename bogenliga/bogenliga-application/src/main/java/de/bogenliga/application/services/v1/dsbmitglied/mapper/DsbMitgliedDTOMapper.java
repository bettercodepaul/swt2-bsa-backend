package de.bogenliga.application.services.v1.dsbmitglied.mapper;

import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.dsbmitglied.model.DsbMitgliedDTO;

import java.util.function.Function;

/**
 * I map the {@link DsbMitgliedDO} and {@link DsbMitgliedDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class DsbMitgliedDTOMapper implements DataTransferObjectMapper {

    /**
     * Constructor
     */
    private DsbMitgliedDTOMapper() {
        // empty private constructor
    }


    /**
     * I map the {@link DsbMitgliedDO} object to the {@link DsbMitgliedDTO} object
     */
    public static final Function<DsbMitgliedDO, DsbMitgliedDTO> toDTO = vo -> {

        final long dsbMitgliedId = vo.getId();
        final String dsbMitgliedVorname = vo.getVorname();
        final String dsbMitgliedNachname = vo.getNachname();
        final String dsbMitgliedGeburtsdatum = vo.getGeburtsdatum();
        final String dsbMitgliedNationalitaet = vo.getNationalitaet();
        final String dsbMitgliedMitgliedsnummer = vo.getMitgliedsnummer();
        final long dsbMitgliedVereinsId = vo.getVereinsId();
        final long dsbMitgliedUserId = vo.getUserId();

        return new DsbMitgliedDTO(dsbMitgliedId,
                dsbMitgliedVorname,
                dsbMitgliedNachname,
                dsbMitgliedGeburtsdatum,
                dsbMitgliedNationalitaet,
                dsbMitgliedMitgliedsnummer,
                dsbMitgliedVereinsId,
                dsbMitgliedUserId);
    };

    /**
     * I map the {@link DsbMitgliedDTO} object to the {@link DsbMitgliedDO} object
     */
    public static final Function<DsbMitgliedDTO, DsbMitgliedDO> toVO = dto -> {

        final long dsbMitgliedId = dto.getId();
        final String dsbMitgliedVorname = dto.getVorname();
        final String dsbMitgliedNachname = dto.getNachname();
        final String dsbMitgliedGeburtsdatum = dto.getGeburtsdatum();
        final String dsbMitgliedNationalitaet = dto.getNationalitaet();
        final String dsbMitgliedMitgliedsnummer = dto.getMitgliedsnummer();
        final long dsbMitgliedVereinsId = dto.getVereinsId();
        final long dsbMitgliedUserId = dto.getUserId();

        return new DsbMitgliedDO(dsbMitgliedId,
                dsbMitgliedVorname,
                dsbMitgliedNachname,
                dsbMitgliedGeburtsdatum,
                dsbMitgliedNationalitaet,
                dsbMitgliedMitgliedsnummer,
                dsbMitgliedVereinsId,
                dsbMitgliedUserId);
    };
}
