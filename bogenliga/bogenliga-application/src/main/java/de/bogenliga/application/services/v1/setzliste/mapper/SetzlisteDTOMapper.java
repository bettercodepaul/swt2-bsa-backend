package de.bogenliga.application.services.v1.setzliste.mapper;

import de.bogenliga.application.business.Setzliste.api.types.SetzlisteDO;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.setzliste.model.SetzlisteDTO;

import java.sql.Date;
import java.util.function.Function;

/**
 * I map the {@link DsbMitgliedDO} and {@link SetzlisteDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class SetzlisteDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link DsbMitgliedDO} object to the {@link SetzlisteDTO} object
     */
    public static final Function<SetzlisteDO, SetzlisteDTO> toDTO = setzlisteDO -> {
        return null;
    };
    /**
     * I map the {@link SetzlisteDTO} object to the {@link DsbMitgliedDO} object
     */
    public static final Function<SetzlisteDTO, DsbMitgliedDO> toDO = dto -> {

    return null;
    };


    /**
     * Constructor
     */
    private SetzlisteDTOMapper() {
        // empty private constructor
    }
}
