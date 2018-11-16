package de.bogenliga.application.services.v1.competitionclass.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.common.time.DateProvider;
import de.bogenliga.application.services.v1.competitionclass.model.CompetitionClassDTO;

/**
 * I map the {@link CompetitionClassDO} and {@link CompetitionClassDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassDTOMapper implements DataTransferObjectMapper {

    public CompetitionClassDTOMapper() {
        // Empty constructor
    }

    /**
     * I map the {@link CompetitionClassDO} data object to the {@link CompetitionClassDTO} data access object
     */
    public static final Function<CompetitionClassDO, CompetitionClassDTO> toDTO = competitionClassDO -> {

        final Long klasseId = competitionClassDO.getId();
        final String klasseName = competitionClassDO.getKlasseName();
        final Long klasseAlterMin = competitionClassDO.getKlasseAlterMin();
        final Long klasseAlterMax = competitionClassDO.getKlasseAlterMax();
        final Long klasseNr = competitionClassDO.getKlasseNr();

        return new CompetitionClassDTO(klasseId, klasseName, klasseAlterMin, klasseAlterMax, klasseNr);
    };

    /**
     * I map the {@link CompetitionClassDTO} data access object to the {@link CompetitionClassDO} data object
     */
    public static final Function<CompetitionClassDTO, CompetitionClassDO> toDO = competitionClassDTO -> {
        final Long klasseId = competitionClassDTO.getId();
        final String klasseName = competitionClassDTO.getKlasseName();
        final Long klasseAlterMin = competitionClassDTO.getKlasseAlterMin();
        final Long klasseAlterMax = competitionClassDTO.getKlasseAlterMax();
        final Long klasseNr = competitionClassDTO.getKlasseNr();

        return new CompetitionClassDO(klasseId, klasseName, klasseAlterMin, klasseAlterMax, klasseNr);
    };
}