package de.bogenliga.application.services.v1.dsbmitglied.mapper;

import java.sql.Date;
import java.util.function.Function;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.dsbmitglied.model.DsbMitgliedDTO;

/**
 * I map the {@link DsbMitgliedDO} and {@link DsbMitgliedDTO} objects
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 * test123
 */
public final class DsbMitgliedDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link DsbMitgliedDO} object to the {@link DsbMitgliedDTO} object
     */
    public static final Function<DsbMitgliedDO, DsbMitgliedDTO> toDTO = dsbMitgliedDO -> {

        final Long dsbMitgliedId = dsbMitgliedDO.getId();
        final String dsbMitgliedVorname = dsbMitgliedDO.getVorname();
        final String dsbMitgliedNachname = dsbMitgliedDO.getNachname();

        final Date dsbMitgliedGeburtsdatumDate = dsbMitgliedDO.getGeburtsdatum();
        final String dsbMitgliedGeburtsdatum = dsbMitgliedGeburtsdatumDate.toString();

        final String dsbMitgliedNationalitaet = dsbMitgliedDO.getNationalitaet();
        final String dsbMitgliedMitgliedsnummer = dsbMitgliedDO.getMitgliedsnummer();
        final Long dsbMitgliedVereinsId = dsbMitgliedDO.getVereinsId();
        final String dsbMitgliedVereinName = dsbMitgliedDO.getVereinName();
        final Long dsbMitgliedUserId = dsbMitgliedDO.getUserId();
        final Boolean dsbMitgliedKampfrichterlizenz = dsbMitgliedDO.isKampfrichter();

        final Date dsbMitgliedBeitrittsdatumDate = dsbMitgliedDO.getBeitrittsdatum();
        final String dsbMitgliedBeitrittsdatum = dsbMitgliedBeitrittsdatumDate.toString();

        return new DsbMitgliedDTO(dsbMitgliedId,
                dsbMitgliedVorname,
                dsbMitgliedNachname,
                dsbMitgliedGeburtsdatum,
                dsbMitgliedNationalitaet,
                dsbMitgliedMitgliedsnummer,
                dsbMitgliedVereinsId,
                dsbMitgliedVereinName,
                dsbMitgliedUserId,
                dsbMitgliedKampfrichterlizenz,
                dsbMitgliedBeitrittsdatum
        );
    };

    /**
     * I map the {@link DsbMitgliedDTO} object to the {@link DsbMitgliedDO} object
     */
    public static final Function<DsbMitgliedDTO, DsbMitgliedDO> toDO = dto -> {

        final Long dsbMitgliedId = dto.getId();
        final String dsbMitgliedVorname = dto.getVorname();
        final String dsbMitgliedNachname = dto.getNachname();

        final String dsbMitgliedGeburtsdatum = dto.getGeburtsdatum();
        Date dsbMitgliedGeburtsdatumDate = Date.valueOf(dsbMitgliedGeburtsdatum);

        final String dsbMitgliedNationalitaet = dto.getNationalitaet();
        final String dsbMitgliedMitgliedsnummer = dto.getMitgliedsnummer();
        final Long dsbMitgliedVereinsId = dto.getVereinsId();
        final String dsbMitgliedVereinName = dto.getVereinsName();
        final Long dsbMitgliedUserId = dto.getUserId();
        final Boolean dsbMitgliedKampfrichter = dto.isKampfrichter();

        final String dsbMitgliedBeitrittsdatum = dto.getBeitrittsdatum();
        Date dsbMitgliedBeitrittsdatumDate = Date.valueOf(dsbMitgliedBeitrittsdatum);

        return new DsbMitgliedDO(dsbMitgliedId,
                dsbMitgliedVorname,
                dsbMitgliedNachname,
                dsbMitgliedGeburtsdatumDate,
                dsbMitgliedNationalitaet,
                dsbMitgliedMitgliedsnummer,
                dsbMitgliedVereinsId,
                dsbMitgliedVereinName,
                dsbMitgliedUserId,
                dsbMitgliedKampfrichter,
                dsbMitgliedBeitrittsdatumDate
        );
    };


    /**
     * Constructor
     */
    private DsbMitgliedDTOMapper() {
        // empty private constructor
    }
}
